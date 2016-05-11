object Main extends App {
  val file = Macro loadFile "src/main/scala/Main.scala"
  val properties = LoadProperties loadProperties "some.properties"
  println(properties)
  println("20/3=" + Macro.divC(20, 3))
}