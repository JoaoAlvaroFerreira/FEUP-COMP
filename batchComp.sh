jjtree NewJava.jjt
echo -----------------------------------
javacc NewJava.jj
echo -----------------------------------
javac *.java
echo -----------------------------------
java Main FindMaximum.jmm
echo -----------------------------------
java -jar jasmin-2.4/jasmin.jar FindMaximum.j
echo -----------------------------------
java FindMaximum

