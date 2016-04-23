package org.kropek.gojira.datagen.db

import org.kropek.gojira.datagen.ConfigReading
import org.kropek.gojira.datagen.db.DAO.DepthAndPoint

import scalaz.effect.IO
import scalaz.stream.Process
import net.ceedubs.ficus.Ficus._
import scalaz._, Scalaz._
import doobie.imports._


object DAO extends ConfigReading {

  case class DepthAndPoint(depth: Int, lat: Double, long: Double)

  val conf = getConfig.unsafePerformIO()

  val db = DriverManagerTransactor[IO](
      conf.as[String]("dataGen.db.driver"),
      conf.as[String]("dataGen.db.connection"),
      conf.as[String]("dataGen.db.user"),
      conf.as[String]("dataGen.db.password")
    )

  def insert(entities: List[DepthAndPoint]): IO[Disjunction[Throwable, String]] = {

    def inserts(input: List[DepthAndPoint]): ConnectionIO[String] = for {
      dps <- Update[DepthAndPoint](Queries.insertDepthAndPoint).updateMany(input)
    } yield "All done!"

    inserts(entities).transact(db).attempt
  }

  def getIds = IO {
    Queries.ids.process.list.transact(db).attempt.unsafePerformIO().getOrElse(List.empty[Int])
  }

  def getNearest(ids: List[Int]) = IO {

    ids.map { id =>

      val key = Queries.dp(id).unique.transact(db).attempt.unsafePerformIO().toOption
      val values = Queries.nearestFive(id).process.list.transact(db).attempt.unsafePerformIO().toOption

      (key, values) match {
        case (a, b) if a.isEmpty || b.isEmpty => throw new RuntimeException("someting wrong")
        case (c, d) =>
          (c.get, d.get)
      }

    }.toMap
  }


}


object Queries {

  val ids = sql"SELECT id FROM japan_points".query[Int]

  def dp(id: Int) = sql"SELECT depth, ST_X(location) AS lat, ST_Y(location) AS long FROM japan_points WHERE id=$id".query[DepthAndPoint]

  val insertDepthAndPoint =
    "INSERT INTO japan_points (depth, location) VALUES (?, ST_SetSRID(st_makepoint(? ,?), 4326))"

  def nearestFive(id: Int) = sql"""
      WITH locations AS (
        SELECT st_makepoint(ST_X(location), ST_Y(location)) AS point
        FROM japan_points
        WHERE id=$id
      )
      SELECT DISTINCT ON (depth) depth, ST_X(location) AS lat, ST_Y(location) AS long FROM locations, japan_points
      ORDER BY depth, location <-> ST_SetSRID(point, 4326)
      LIMIT 5
      """.query[DepthAndPoint]

}
