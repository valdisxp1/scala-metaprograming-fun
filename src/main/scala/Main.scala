object Main extends App{
val file = Macro loadFile "src\\main\\scala\\Main.Scala"
val properties = Macro loadProperties "some.properties"
}