jjtree NewJava.jjt
echo -----------------------------------
javacc NewJava.jj
echo -----------------------------------
javac *.java
echo -----------------------------------
java Main HelloWorld.jmm
echo -----------------------------------
java -jar jasmin-2.4/jasmin.jar HelloWorld.j
