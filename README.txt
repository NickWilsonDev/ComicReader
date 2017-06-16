jar cvfm ComicReader.jar manifest.txt *.class commons-logging-1.2.jar com icons
chmod +x ComicReader.jar


Compile on linux terminal
javac -cp commons-logging-1.2.jar:junrar-0.7.jar *.java

Run on linux terminal
java -cp commons-logging-1.2.jar:junrar-0.7.jar ComicReader

