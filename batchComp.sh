jjtree NewJava.jjt
echo -----------------------------------
javacc NewJava.jj
echo -----------------------------------
javac *.java
echo -----------------------------------
java Main Fac.jmm
echo -----------------------------------
java -jar jasmin-2.4/jasmin.jar Fac.j
