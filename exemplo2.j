.source exemplo2.jmm
.class public Haha
.method public static main([Ljava/lang/String;)V
.limit vars 1

.var 0 is l I from main_init to main_end

main_init:
 ; l = 2
bipush 2
astore -1
main_end:

.end method

.method public callMe()I
.limit vars 0


callMe_init:
callMe_end:

.end method

.method public coisas()Z
.limit vars 1

.var 0 is i Z from coisas_init to coisas_end

coisas_init:
 ; i = null
bipush null
astore 0
coisas_end:

.end method


