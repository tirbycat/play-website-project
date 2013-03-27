import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "elendolf-site"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    //"postgresql" % "postgresql" % "9.2-1002.jdbc4",
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
