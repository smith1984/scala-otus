import sbt._

object Dependencies {
  lazy val KindProjectorVersion = "0.10.3"
  lazy val kindProjector = "org.typelevel" %% "kind-projector" % KindProjectorVersion

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.11"

  lazy val catsCore = "org.typelevel" %% "cats-core" % "2.3.0"

}
