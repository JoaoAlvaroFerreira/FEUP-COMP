.source Fac.jmm
.class public Fac
.super java/lang/Object



; default constructor
.method public <init>()V
aload_0
invokespecial java/lang/Object/<init>()V
return
.end method

.method public ComputeFac(I)I
.limit stack 7
.limit locals 3

.var 2 is num_aux I from ComputeFac_init to ComputeFac_end

ComputeFac_init:
iload 1
istore 2

if0: 
iload 1
bipush 1
if_icmplt true0
bipush 0
goto endComp0
true0:
bipush 1
endComp0:
ifeq endIf0

bipush 1
istore 2

else0: 
endElse0:
endIf0:
bipush 0
istore 1

if1: 
iload 1
bipush 1
if_icmplt true1
bipush 0
goto endComp1
true1:
bipush 1
endComp1:
ifeq endIf1

bipush 2
istore 2
bipush 55
istore 1

if2: 
bipush 2
bipush 5
if_icmplt true2
bipush 0
goto endComp2
true2:
bipush 1
endComp2:
ifeq endIf2

bipush 3
bipush 5
if_icmplt true3
bipush 0
goto endComp3
true3:
bipush 1
endComp3:
ifeq endIf2

iload 1

invokestatic io/println(I)V
iload 2
istore 1

else1: 
endElse1:
endIf2:

else2: 
endElse2:
endIf1:
iload 2
ireturn
ComputeFac_end:

.end method

.method public static main([Ljava/lang/String;)V
.limit stack 7
.limit locals 6

.var 2 is lol LFac; from main_init to main_end
.var 3 is exe LFac; from main_init to main_end
.var 4 is num I from main_init to main_end
.var 5 is num2 I from main_init to main_end

main_init:
new exemplo3
dup
invokespecial exemplo3/<init>()V
astore 3
new Fac
dup
invokespecial Fac/<init>()V
astore 2
aload 3
aload 3
bipush 2

invokevirtual exemplo3/getNum(I)I

bipush 3

invokevirtual exemplo3/getMult(II)I
istore 4
aload 2
bipush 10

invokevirtual Fac/ComputeFac(I)I

invokestatic io/println(I)V
bipush 0
istore 4
bipush 0
istore 5

while0: 
iload 5
bipush 10
if_icmplt true4
bipush 0
goto endComp4
true4:
bipush 1
endComp4:
ifeq endWhile0

bipush 0
istore 4

while1: 
iload 4
bipush 5
if_icmplt true5
bipush 0
goto endComp5
true5:
bipush 1
endComp5:
ifeq endWhile1

iload 4
bipush 1
iadd
istore 4
goto while1
endWhile1:
iload 5
bipush 1
iadd
istore 5
goto while0
endWhile0:
return
main_end:

.end method


