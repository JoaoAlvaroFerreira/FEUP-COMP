.source exemplo2.jmm
.class public Haha
.super java/lang/Object


.field public nuno Z

.method public static main([Ljava/lang/String;)V
.limit stack 5
.limit locals 3

.var 2 is l I from main_init to main_end

main_init:
 ; l = 2
bipush 2
istore 2
 ; nuno = false
bipush 0
putfield Haha/nuno Z
 ; l = +
bipush 2
bipush 4
bipush 3
bipush 4
bipush 2
idiv
imul
iadd
iadd
istore 2
return
main_end:

.end method

.method public callMe(I)I
.limit stack 1
.limit locals 2


callMe_init:
bipush 2
ireturn
callMe_end:

.end method

.method public coisas()Z
.limit stack 1
.limit locals 3

.var 1 is a I from coisas_init to coisas_end
.var 2 is b I from coisas_init to coisas_end

coisas_init:
bipush 1
ireturn
coisas_end:

.end method


