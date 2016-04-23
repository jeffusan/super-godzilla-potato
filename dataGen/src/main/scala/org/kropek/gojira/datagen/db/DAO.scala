package org.kropek.gojira.datagen.db

import com.typesafe.config.Config
import doobie.imports._
import scalaz._, Scalaz._
import scalaz.effect.IO
import net.ceedubs.ficus.Ficus._


object DAO {

  case class DepthAndPoint(depth: Int, lat: Double, long: Double)

  def insert(conf: Config, entities: List[DepthAndPoint]): IO[Disjunction[Throwable, String]] = {

    def inserts(input: List[DepthAndPoint]): ConnectionIO[String] = for {
      dps <- Update[DepthAndPoint](Queries.insertDepthAndPoint).updateMany(input)
    } yield "All done!"

    val db = DriverManagerTransactor[IO](
      conf.as[String]("dataGen.db.driver"),
      conf.as[String]("dataGen.db.connection"),
      conf.as[String]("dataGen.db.user"),
      conf.as[String]("dataGen.db.password")
    )
    inserts(entities).transact(db).attempt
  }


  object Queries {

    val insertDepthAndPoint =
      "INSERT INTO japan_points (depth, location) VALUES (?, ST_SetSRID(st_makepoint(? ,?), 4326))"

    def nearestFive(lat: Double, long: Double) = sql"""
      select depth, ST_X(location), ST_Y(location) from japan_points
      order by location  <-> ST_SetSRID(st_makepoint($lat ,$long), 4326) limit 5
      """.query[DepthAndPoint]

  }

}
