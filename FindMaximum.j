.source FindMaximum.jmm
.class public FindMaximum
.super java/lang/Object


.field public test_arr [I

.method public find_maximum([I)I
.limit locals 6

.var 2 is i I from find_maximum_init to find_maximum_end
.var 3 is maximum I from find_maximum_init to find_maximum_end
.var 4 is value I from find_maximum_init to find_maximum_end
.var 5 is arr [I from find_maximum_init to find_maximum_end

find_maximum_init:
 ; arr = int[]
bipush int[]
astore 5
 ; i = 1
bipush 1
astore 2
 ; maximum = arr
bipush arr
astore 3
find_maximum_end:

.end method

.method public build_test_arr()I
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
build_test_arr_end:

.end method

.method public get_array()[I
.limit locals 1


get_array_init:
get_array_end:

.end method

.method public static main([Ljava/lang/String;)V
.limit locals 3

.var 2 is fm  from main_init to main_end

main_init:
 ; fm = FindMaximum
bipush FindMaximum
astore 2
main_end:

.end method


