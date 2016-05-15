# scala-metaprograming-fun
## Struture
1. [Macro implementations] (macros/src/main/scala)
2. [Calling macros](src/main/scala/Main.scala)
3. [Powerpoint presentation](Scala_metaprogramming.pptx)

## Basic demo: Windows
1. Build and run the project with `cleanrun.bat`
2. See the macros have ouputed something
3. Run it it again with `run.bat`
4. Notice there is less output - it is already compiled so macros did not run
5. Look at the decoded bytecode in `Main.class.decoded.txt`
  1. The text file has been loaded in as a String constant [see here](Main.class.decoded.txt#L91)
  2. See the generated code for creating the map [see here](Main.class.decoded.txt#L515-L555)
  3. You can generate this file with `decode-main.bat`. Note: `<path-to-jdk>\bin` must be in PATH


## Basic demo: Linux
If you do not have sbt installed already you can use `java -jar sbt-launch.jar` in place of `sbt`.

1. Build and run the project with `sbt clean run`
2. See the macros have ouputed something
3. Run it it again with `sbt run`
4. Notice there is less output - it is already compiled so macros did not run
5. Look at the decoded bytecode in `Main.class.decoded.txt`
  1. The text file has been loaded in as a String constant [see here](Main.class.decoded.txt#L91)
  2. See the generated code for creating the map [see here](Main.class.decoded.txt#L515-L555)
  3. You can generate this file with `sh decode-main.sh` Note: requires a JDK installed.
