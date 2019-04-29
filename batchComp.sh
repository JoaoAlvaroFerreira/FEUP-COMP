jjtree NewJava.jjt
echo -----------------------------------
javacc NewJava.jj
echo -----------------------------------
javac *.java
echo -----------------------------------
java Main FindMaximum.jmm MonteCarloPi.jmm HelloWorld.jmm

