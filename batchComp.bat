@echo off
echo jjtree - - - - - - - - - - - - - - - - - 
echo - - - -  - - - - - - - - - - - - - - - - -
CALL jjtree ExpressionParsing.jjt
echo javacc - - - - - - - - - - - - - - - - -
echo - - - -  - - - - - - - - - - - - - - - - -
CALL javacc ExpressionParsing.jj
echo javac - - - - -- - - - - - - -  - - - - -
echo - - - -  - - - - - - - - - - - - - - - - -
CALL javac *.java
echo execution - - - - - - - - - - - - - - - -
echo - - - -  - - - - - - - - - - - - - - - - -
CALL java ExpressionParsing