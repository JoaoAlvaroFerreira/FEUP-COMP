jjtree NewJava.jjt
echo -----------------------------------
javacc NewJava.jj
echo -----------------------------------
javac *.java
echo -----------------------------------
java Main $1

