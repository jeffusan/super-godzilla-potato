import sbt._

object Version {

  val doobie = "0.2.1"
  val config = "1.3.0"
  val ficus = "1.1.2"
}

object Library {

  val doobieCore = "org.tpolecat" %% "doobie-core" % Version.doobie
  val doobiePostgres = "org.tpolecat" %% "doobie-contrib-postgresql" % Version.doobie
  val ficus = "net.ceedubs" %% "ficus" % Version.ficus
  val config = "com.typesafe" % "config" % Version.config
}

object Dependencies {

  import Library._

  val dataGen = List(
    doobieCore,
    doobiePostgres
  )
  val dataFeed = List()
  val sparkStream = List()
  val common = List(
    config,
    ficus
  )
}
