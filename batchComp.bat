@echo off
echo jjtree - - - - - - - - - - - - - - - - - 
echo - - - -  - - - - - - - - - - - - - - - - -
CALL jjtree NewJava.jjt
echo javacc - - - - - - - - - - - - - - - - -
echo - - - -  - - - - - - - - - - - - - - - - -
CALL javacc NewJava.jj
echo javac - - - - -- - - - - - - -  - - - - -
echo - - - -  - - - - - - - - - - - - - - - - -
CALL javac *.java
echo execution - - - - - - - - - - - - - - - -
echo - - - -  - - - - - - - - - - - - - - - - -
CALL java NewJava