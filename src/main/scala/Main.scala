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

  // multiply by a constant by only using bitshifts and adds
  println(s"20*3=${MultiplyByConst.multC(20, 3)}=${20*3}")
  val x = 20
  println(s"$x*3=${MultiplyByConst.multC(x, 3)}=${x*3}")
  println(s"49*12=${MultiplyByConst.multC(49, 12)}=${49*12}")

  // validate URLs
  // all the parsing errors will appear at compile time :-)
  val url = ValidWebsite.URL("http://example.com")
  println(s"host: ${url.getHost} port: ${url.getPort}")
  // uncomment these lines to see it fail
  //  ValidWebsite.URL("ftp://example.com")
  //  ValidWebsite.URL("something...something")

  // custom string interpolation
  {
    import ValidWebsite._
    website"https://github.com/valdisxp1/scala-metaprograming-fun"
    // uncomment these lines to see it fail
//    website"akka-tcp://192.168.0.11"
//    website"file:///var/www/index.html"
//    website"cow says moo"

//    val host = "aaa.com"
//    val port = 8080
//    website"http://$host:$port/ping"
  }
}