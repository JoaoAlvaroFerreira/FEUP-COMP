.source exemplo2.jmm
.class public Haha
.super java/lang/Object


.field public nuno Z

.method public static main([Ljava/lang/String;)V
.limit locals 2

.var 1 is l I from main_init to main_end

main_init:
 ; l = 2
bipush 2
astore 1
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
astore 1
main_end:

.end method

.method public callMe()I
.limit locals 0


callMe_init:
callMe_end:

.end method

.method public coisas()Z
.limit locals 1

.var 0 is i Z from coisas_init to coisas_end

coisas_init:
<<<<<<< Updated upstream
 ; i = null
bipush null
=======
 ; i = true
bipush 1
>>>>>>> Stashed changes
astore 0
coisas_end:

.end method
