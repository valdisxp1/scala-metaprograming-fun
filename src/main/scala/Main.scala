object Main extends App{
val file = Macro loadFile "src\\main\\scala\\Main.Scala"
val properties = Macro loadProperties "some.properties"
println(properties)
println("20/3="+ Macro.divC(20,3))
}