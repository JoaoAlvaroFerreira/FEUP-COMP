@echo off
echo jjtree - - - - - - - - - - - - - - - - - 
echo - - - -  - - - - - - - - - - - - - - - - -
CALL jjtree NewJava.jjt
echo - - - - - - - - - - - - - - - - - - - - - 
echo javacc - - - - - - - - - - - - - - - - - -
echo - - - -  - - - - - - - - - - - - - - - - -
CALL javacc NewJava.jj
echo javac - - - - -- - - - - - - -  - - - - -
echo - - - -  - - - - - - - - - - - - - - - - -
CALL javac *.java
CALL java Main FindMaximum.jmm
echo execution - - - - - - - - - - - - - - - -
echo - - - -  - - - - - - - - - - - - - - - - -