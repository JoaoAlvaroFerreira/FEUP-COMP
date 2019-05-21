jjtree NewJava.jjt
echo -----------------------------------
javacc NewJava.jj
echo -----------------------------------
javac *.java
echo -----------------------------------
java Main MonteCarloPi.jmm
echo -----------------------------------
java -jar jasmin-2.4/jasmin.jar MonteCarloPi.j
echo -----------------------------------
java MonteCarloPi
