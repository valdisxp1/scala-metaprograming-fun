object Main extends App {
  // use a macro to copy contents of a text file as a constant in the code
  val file = LoadTextFile loadFile "src/main/scala/Main.scala"
  println(s"Source for this file is stored in the compiled code: \n$file\n---")

  // load properties into a map constant
  // the properties file is then no longer needed
  // using quasiquotes
  val properties = LoadProperties loadProperties "some.properties"
  println(s"Loaded properties $properties")
  // without quasiquotes
  val properties2 = LoadProperties loadPropertiesWithoutQ "some.properties"
  println(s"Loaded properties $properties2")

  // divide by a constant by only using bitshifts and adds
  println(s"20/3=${Macro.divC(20, 3)}")
  val x = 20
  println(s"20/3=${Macro.divC(x, 3)}")
}