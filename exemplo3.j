.source exemplo3.jmm
.class public Fac
.super java/lang/Object



.method public static main([Ljava/lang/String;)V
.limit locals 4

.var 2 is a I from main_init to main_end
.var 3 is c I from main_init to main_end

main_init:
main_end:

.end method

.method public f1(I)[I
.limit locals 3

.var 2 is b [I from f1_init to f1_end

f1_init:
 ; b = int[]
bipush int[]
astore 2
f1_end:

.end method


