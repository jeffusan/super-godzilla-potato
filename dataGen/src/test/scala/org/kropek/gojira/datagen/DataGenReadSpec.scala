package org.kropek.gojira.datagen

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import org.specs2.mutable.Specification

import scalaz.effect.IO


class DataGenReadSpec extends Specification with ConfigReading {
  override def is =
    s2"""

 This is a specification to check the csv read functionality

 The read function should
   contain 10 lines                                         $e1
                                                                 """

  def e1 = read(getConfig.unsafePerformIO()).unsafePerformIO() must have size (10)

  override def getConfig: IO[Config] = IO {
    ConfigFactory.parseFile(new File("dataGen/src/test/resources/testreference.conf"))
  }
}
