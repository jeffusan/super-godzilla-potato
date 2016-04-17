import sbt.Keys._
import sbt.project

name := "ゴジラ Monitoring"

organization in ThisBuild := "org.kropek.gojira"

scalaVersion in ThisBuild := "2.11.7"

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "tpolecat" at "http://dl.bintray.com/tpolecat/maven",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
)

resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"


lazy val root = project.in(file("."))
  .dependsOn(dataGen, sparkStream)
  .aggregate(dataGen, sparkStream)

lazy val common = project
  .settings(libraryDependencies ++= Dependencies.common)

lazy val sparkStream = project
  .dependsOn(common)
  .aggregate(common)
  .settings(libraryDependencies ++= Dependencies.sparkStream)

lazy val dataGen = project
  .dependsOn(common)
  .aggregate(common)
  .settings(libraryDependencies ++= Dependencies.dataGen)

lazy val dataFeed = project
  .dependsOn(common)
  .aggregate(common)
  .settings(libraryDependencies ++= Dependencies.dataFeed)
