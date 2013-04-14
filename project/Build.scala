import sbt._
import Keys._
import play.Project._
import com.typesafe.sbteclipse.core.EclipsePlugin._

object ApplicationBuild extends Build {

  val appName         = "dojo-bootstrap-app"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    "com.typesafe.slick" %% "slick" % "1.0.0",
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
    "org.webjars" % "webjars-play" % "2.1.0",
    "org.webjars" % "bootstrap" % "2.3.1",
    "org.webjars" % "dojo" % "1.8.1",
    "org.webjars" % "dojo-bootstrap" % "1.3"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
	  EclipseKeys.withSource := true,
	  resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
	  resolvers += Resolver.url("sbt-plugin-snapshots", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots/"))(Resolver.ivyStylePatterns)
  )

}
