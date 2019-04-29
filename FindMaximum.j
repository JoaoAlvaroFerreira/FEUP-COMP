.source FindMaximum.jmm
.class public FindMaximum
.super java/lang/Object


.field public test_arr [I

; default constructor
.method public <init>()V
aload_0
invokespecial java/lang/Object/<init>()V
return
.end method

.method public find_maximum([I)I
.limit stack 1
.limit locals 5

.var 2 is i I from find_maximum_init to find_maximum_end
.var 3 is maximum I from find_maximum_init to find_maximum_end
.var 4 is value I from find_maximum_init to find_maximum_end

find_maximum_init:
bipush 1
istore 2
istore 3
iload 3
ireturn
find_maximum_end:

.end method

.method public build_test_arr()I
.limit stack 6
.limit locals 1


build_test_arr_init:
new int[]
dup
invokespecial int[]/<init>()V
putfield FindMaximum/test_arr [I
bipush 14
bipush 28
bipush 0
bipush 0
bipush 5
isub
bipush 12
bipush 0
ireturn
build_test_arr_end:

.end method

.method public get_array()[I
.limit stack 1
.limit locals 1


get_array_init:
getfield FindMaximum/test_arr [I
ireturn
get_array_end:

.end method

.method public static main([Ljava/lang/String;)V
.limit stack 2
.limit locals 3

.var 2 is fm LFindMaximum; from main_init to main_end

main_init:
new FindMaximum
dup
invokespecial FindMaximum/<init>()V
astore 2
aload 2
invokevirtual FindMaximum/build_test_arr()Iaload 2
aload 2
invokevirtual FindMaximum/get_array()[I
invokevirtual FindMaximum/find_maximum([I)I
invokevirtual FindMaximum/printResult(return
main_end:

.end method


