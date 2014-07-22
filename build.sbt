scalaVersion := "2.11.1"

lazy val root = project.in( file(".") ).dependsOn(macro)

lazy val macro = project