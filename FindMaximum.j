.source FindMaximum.jmm
.class public FindMaximum
.super java/lang/Object


.field public test_arr [I
.field public testes I

; default constructor
.method public <init>()V
aload_0
invokespecial java/lang/Object/<init>()V
return
.end method

.method public build_test_arr()I
.limit stack 1
.limit locals 1


build_test_arr_init:
bipush 0
ireturn
build_test_arr_end:

.end method

.method public static main([Ljava/lang/String;)V
.limit stack 6
.limit locals 5

.var 2 is fm LFindMaximum; from main_init to main_end
.var 3 is oi I from main_init to main_end
.var 4 is arr [I from main_init to main_end

main_init:
bipush 9
newarray int
astore 4
bipush 5
istore 3
aload 4
bipush 2
bipush 3
iastore
aload 4
bipush 2
iaload
istore 3
iload 3

invokestatic io/println(I)V
aload 4
bipush 2
iaload

invokestatic io/println(I)V
aload 4
arraylength

invokestatic io/println(I)V
return
main_end:

.end method


