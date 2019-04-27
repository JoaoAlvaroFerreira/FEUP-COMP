.source FindMaximum.jmm
.class public FindMaximum
.super java/lang/Object


.field public test_arr [I

.method public find_maximum([I)I
.limit stack 1
.limit locals 5

.var 2 is i I from find_maximum_init to find_maximum_end
.var 3 is maximum I from find_maximum_init to find_maximum_end
.var 4 is value I from find_maximum_init to find_maximum_end

find_maximum_init:
 ; i = 1
bipush 1
istore 2
 ; maximum = arr
bipush arr
istore 3
bipush maximum
ireturn
find_maximum_end:

.end method

.method public build_test_arr()I
.limit stack 2
.limit locals 1


build_test_arr_init:
 ; test_arr = int[]
bipush int[]
putfield FindMaximum/test_arr [I
 ; test_arr = 14
bipush 14
putfield FindMaximum/test_arr [I
 ; test_arr = 28
bipush 28
putfield FindMaximum/test_arr [I
 ; test_arr = 0
bipush 0
putfield FindMaximum/test_arr [I
 ; test_arr = -
bipush 0
bipush 5
isub
putfield FindMaximum/test_arr [I
 ; test_arr = 12
bipush 12
putfield FindMaximum/test_arr [I
bipush 0
ireturn
build_test_arr_end:

.end method

.method public get_array()[I
.limit stack 1
.limit locals 1


get_array_init:
bipush test_arr
ireturn
get_array_end:

.end method

.method public static main([Ljava/lang/String;)V
.limit stack 1
.limit locals 3

.var 2 is fm  from main_init to main_end

main_init:
 ; fm = FindMaximum
bipush FindMaximum
istore 2
return
main_end:

.end method


