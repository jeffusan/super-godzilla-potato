import sbt._

object Version {

  val doobie = "0.2.1"
  val config = "1.3.0"
  val ficus = "1.1.2"
  val specs = "3.7.2"
  val postgres = "9.4.1208.jre7"
}

object Library {

  val doobieCore = "org.tpolecat" %% "doobie-core" % Version.doobie
  val doobiePostgres = "org.tpolecat" %% "doobie-contrib-postgresql" % Version.doobie
  val doobieSpecs = "org.tpolecat" %% "doobie-contrib-specs2" % Version.doobie
  val ficus = "net.ceedubs" %% "ficus" % Version.ficus
  val config = "com.typesafe" % "config" % Version.config
  val specs = "org.specs2" %% "specs2-core" % Version.specs
  val specsME = "org.specs2" %% "specs2-matcher-extra" % Version.specs
  val specsSC = "org.specs2" %% "specs2-scalacheck" % Version.specs
  val postgres = "org.postgresql" % "postgresql" % Version.postgres

}

object Dependencies {

  import Library._

  val dataGen = List(
    doobieCore,
    doobiePostgres,
    doobieSpecs % "test"
  )
  val dataFeed = List()
  val sparkStream = List()
  val common = List(
    config,
    ficus,
    postgres,
    specs % "test",
    specsME % "test",
    specsSC % "test"
  )
}
