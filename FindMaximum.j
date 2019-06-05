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
.limit stack 8
.limit locals 6

.var 2 is i I from find_maximum_init to find_maximum_end
.var 3 is maximum I from find_maximum_init to find_maximum_end
.var 4 is value I from find_maximum_init to find_maximum_end
.var 5 is temp I from find_maximum_init to find_maximum_end

find_maximum_init:






sipush 1
istore 2

aload 1
sipush 0
iaload
istore 3

aload 1
arraylength
istore 5


while0: 
iload 2
iload 5
if_icmplt true0
bipush 0
goto endComp0
true0:
bipush 1
endComp0:
ifeq endWhile0

aload 1
iload 2
iaload
istore 4

if0: 
iload 3
iload 4
if_icmplt true1
bipush 0
goto endComp1
true1:
bipush 1
endComp1:
ifeq else0

iload 4
istore 3
goto endIf0

else0: 
endElse0:
goto endIf0
endIf0:
iload 2
sipush 1
iadd
istore 2
goto while0
endWhile0:

iload 3
invokestatic io/println(I)V

iload 3

ireturn
find_maximum_end:

.end method

.method public build_test_arr()I
.limit stack 17
.limit locals 1


build_test_arr_init:


aload 0
bipush 5
newarray int
putfield FindMaximum/test_arr [I

aload 0
getfield FindMaximum/test_arr [I
sipush 0
sipush 14
iastore

aload 0
getfield FindMaximum/test_arr [I
sipush 1
sipush 28
iastore

aload 0
getfield FindMaximum/test_arr [I
sipush 2
sipush 0
iastore

aload 0
getfield FindMaximum/test_arr [I
sipush 3
sipush 0
sipush 5
isub
iastore

aload 0
getfield FindMaximum/test_arr [I
sipush 4
sipush 12
iastore

sipush 0

ireturn
build_test_arr_end:

.end method

.method public get_array()[I
.limit stack 2
.limit locals 1


get_array_init:


aload 0
getfield FindMaximum/test_arr [I

areturn
get_array_end:

.end method

.method public static main([Ljava/lang/String;)V
.limit stack 4
.limit locals 3

.var 2 is fm LFindMaximum; from main_init to main_end

main_init:

new FindMaximum
dup
invokespecial FindMaximum/<init>()V
astore 2

aload 2
invokevirtual FindMaximum/build_test_arr()I

aload 2
aload 2
invokevirtual FindMaximum/get_array()[I
invokevirtual FindMaximum/find_maximum([I)I
invokestatic ioPlus/printResult(I)V

return
main_end:

.end method


