package org.kropek.gojira.datagen

import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import org.kropek.gojira.datagen.db.DAO
import org.kropek.gojira.datagen.db.DAO.DepthAndPoint

import scalaz.effect.IO._
import scalaz.effect.{IO, SafeApp}

object Main extends SafeApp with ConfigReading {

  override def runc = for {
    conf <- getConfig
    dp <- read(conf)
    _ <- putStrLn(s"Processing ${dp.size.toString} entries")
    results <- DAO.insert(dp)
    _ <- putStrLn(s"Results: $results inserted")
    id <- DAO.getIds
    nearest <- DAO.getNearest(id)
    _ <- putStrLn(s"Results: $nearest found")
  } yield ()

}

trait ConfigReading {

  def getConfig: IO[Config] = IO {
    ConfigFactory.load()
  }

  def read(conf: Config): IO[List[DepthAndPoint]] = IO {
    scala.io.Source.fromFile(conf.as[String]("dataGen.input_source")).getLines().drop(1).map { line =>
      line.split(",") match {
        case bits if bits.length == 3 && bits(0).nonEmpty =>

          Some(
            DepthAndPoint(
              bits(0).trim.toInt,
              bits(2).trim.toDouble,
              bits(1).trim.toDouble))

        case _ =>
          println(s"skipping invalid input line: $line")
          None
      }
    }.filter(p => p.isDefined).map(b => b.get).toList
  }
}
