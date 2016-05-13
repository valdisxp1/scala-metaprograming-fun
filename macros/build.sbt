scalaVersion := "2.11.1"

libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _)