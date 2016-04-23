package org.kropek.gojira.datagen

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import org.specs2.mutable.Specification
import org.kropek.gojira.datagen.Main.read

/**
 * Developer: jeffusan
 * Date: 4/23/16
 */
class DataGenReadSpec extends Specification with GenSpecFuncs {
  override def is =
    s2"""

 This is a specification to check the csv read functionality

 The read function should
   contain 10 lines                                         $e1
                                                                 """

  def e1 = read(config).unsafePerformIO() must have size (10)

}


trait GenSpecFuncs {

  def config: Config = {
    ConfigFactory.parseFile(new File("dataGen/src/test/resources/testreference.conf"))
  }
}