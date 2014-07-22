scalaVersion := "2.11.1"

lazy val root = project.in( file(".") ).dependsOn(macros)

lazy val macros = project