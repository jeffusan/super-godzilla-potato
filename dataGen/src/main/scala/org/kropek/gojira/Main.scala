package org.kropek.gojira

import scalaz.effect.{ SafeApp, IO }
import com.typesafe.config.{ ConfigFactory, Config }
import scalaz.effect.IO._

import net.ceedubs.ficus.Ficus._
import doobie.imports._
import scalaz._, Scalaz._

object Main extends SafeApp {

  case class DepthAndPoint(depth: Int, lat: Double, long: Double)

  def getConfig(): IO[Config] = IO {
    ConfigFactory.load()
  }

  override def runc = for {
    conf <- getConfig()
    dp <- read(conf)
    _ <- putStrLn(s"Processed ${dp.size.toString} entries")
  } yield ()

  def read(conf: Config): IO[Seq[DepthAndPoint]] = IO {
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
    }.filter(p => p.isDefined).map(b => b.get).toSeq
  }

}
