set SCRIPT_DIR=%~dp0
java -Xms512M -Xmx1000M -Xss1M -XX:+CMSClassUnloadingEnabled -Dinput.encoding=Cp1252 -XX:MaxPermSize=256M -jar "%SCRIPT_DIR%sbt-launch.jar" %*