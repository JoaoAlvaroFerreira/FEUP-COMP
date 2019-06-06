jjtree NewJava.jjt
echo -----------------------------------
javacc NewJava.jj
echo -----------------------------------
javac *.java
echo -----------------------------------
java Main $1.jmm
echo -----------------------------------
java -jar jasmin-2.4/jasmin.jar $1.j
echo -----------------------------------
java $1
