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
    "com.github.tototoshi" %% "slick-joda-mapper" % "0.2.1",
    "com.typesafe.play" %% "play-slick" % "0.3.2",
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
    "com.h2database" % "h2" % "1.3.171",
    "org.activiti" % "activiti-engine" % "5.12.1",
    "org.webjars" % "webjars-play" % "2.1.0-1",
    "org.webjars" % "bootstrap" % "2.3.1",
    "org.webjars" % "dojo" % "1.8.3",
    "org.webjars" % "dojo-bootstrap" % "1.3"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
    EclipseKeys.withSource := true,
    resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
    resolvers += "Alfresco Maven Repository" at "https://maven.alfresco.com/nexus/content/groups/public/",
    resolvers += Resolver.url("sbt-plugin-snapshots", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots/"))(Resolver.ivyStylePatterns)
  )

}
