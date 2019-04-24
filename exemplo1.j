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
main_end:

.end method

.method public f1(I;Z;I)I
.limit vars 3

.var 0 is counter I from f1_init to f1_end
.var 1 is item  from f1_init to f1_end
.var 2 is cenas Z from f1_init to f1_end

f1_init:
 ; counter = arg1
bipush arg1
astore 2
 ; cenas = arg2
bipush arg2
astore 4
f1_end:

.end method

.method public cenas()Z
.limit vars 0


cenas_init:
cenas_end:

.end method


