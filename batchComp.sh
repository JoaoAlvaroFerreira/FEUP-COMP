jjtree NewJava.jjt
echo -----------------------------------
javacc NewJava.jj
echo -----------------------------------
javac *.java
echo -----------------------------------
java Main Fac.jmm exemplo3.jmm
echo -----------------------------------
java -jar jasmin-2.4/jasmin.jar Fac.j exemplo3.j
echo -----------------------------------
java Fac

