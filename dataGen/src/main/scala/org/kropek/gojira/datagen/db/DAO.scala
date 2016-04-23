package org.kropek.gojira.datagen.db

import com.typesafe.config.Config
import doobie.imports._
import scalaz._, Scalaz._
import scalaz.effect.IO
import net.ceedubs.ficus.Ficus._

/**
 * Developer: jeffusan
 * Date: 4/23/16
 */
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

    val trivial = sql"""
      select 42, 'foo'::varchar
    """.query[(Int, String)]
  }

}
