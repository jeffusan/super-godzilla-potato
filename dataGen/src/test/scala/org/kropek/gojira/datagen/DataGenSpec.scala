package org.kropek.gojira.datagen

import doobie.contrib.specs2.analysisspec.AnalysisSpec
import doobie.imports._
import org.kropek.gojira.datagen.db.DAO.Queries._
import org.specs2.mutable.Specification

import scalaz.concurrent.Task


object DataGenSpec extends Specification with AnalysisSpec {

  val transactor = DriverManagerTransactor[Task](
    "org.postgresql.Driver", "jdbc:postgresql://192.168.99.101:25432/gis", "docker", "docker"
  )

  check(nearestFive(24, 24))
}
