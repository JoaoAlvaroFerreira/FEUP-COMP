jjtree NewJava.jjt
echo -----------------------------------
javacc NewJava.jj
echo -----------------------------------
javac *.java
echo -----------------------------------
java NewJava $1
