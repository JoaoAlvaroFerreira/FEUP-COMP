.source exemplo1.jmm
.class public Item
.method public static main([Ljava/lang/String;)V
.limit vars 3

.var 0 is i I from main_init to main_end
.var 1 is j Z from main_init to main_end
.var 2 is item  from main_init to main_end

main_init:
 ; i = 2
bipush 2
astore -1
 ; k = 2
bipush 2
astore -2
main_end:

.end method

.method public f1(I;Z;I)I
.limit vars 1

.var 0 is i I from f1_init to f1_end

f1_init:
 ; i = 2
bipush 2
astore -1
f1_end:

.end method

.method public cenas()Z
.limit vars 2

.var 0 is oi Z from cenas_init to cenas_end
.var 1 is i I from cenas_init to cenas_end

cenas_init:
 ; oi = null
bipush null
astore 3
cenas_end:

.end method

.method public ardeu()
.limit vars 1

.var 0 is haha  from ardeu_init to ardeu_end

ardeu_init:
ardeu_end:

.end method


