.source exemplo3.jmm
.class public Fac
.super Nuno

.method public ComputeFac(I;I)V
.limit vars 1

.var 0 is num_aux I from ComputeFac_init to ComputeFac_end

ComputeFac_init:
ComputeFac_end:

.end method

.method public add()I
.limit vars 3

.var 0 is num1 I from add_init to add_end
.var 1 is num2 I from add_init to add_end
.var 2 is res I from add_init to add_end

add_init:
 ; num1 = 5
bipush 5
astore 0
 ; num2 = 4
bipush 4
astore 1
 ; res = +
bipush +
astore 2
add_end:

.end method

.method public static main([Ljava/lang/String;)V
.limit vars 2

.var 0 is countador I from main_init to main_end
.var 1 is teste Z from main_init to main_end

main_init:
 ; i = 4
bipush 4
astore -2
main_end:

.end method


