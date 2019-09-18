.source MonteCarloPi.jmm
.class public MonteCarloPi
.super java/lang/Object



; default constructor
.method public <init>()V
aload_0
invokespecial java/lang/Object/<init>()V
return
.end method

.method public performSingleEstimate()Z
.limit stack 3
.limit locals 5

.var 1 is rand1 I from performSingleEstimate_init to performSingleEstimate_end
.var 2 is rand2 I from performSingleEstimate_init to performSingleEstimate_end
.var 3 is in_circle Z from performSingleEstimate_init to performSingleEstimate_end
.var 4 is squareDist I from performSingleEstimate_init to performSingleEstimate_end

performSingleEstimate_init:






ldc_w 0
ldc_w 100
isub
ldc_w 100
invokestatic MathUtils/random(II)I
istore 1

ldc_w 0
ldc_w 100
isub
ldc_w 100
invokestatic MathUtils/random(II)I
istore 2

iload 1
iload 1
imul
iload 2
iload 2
imul
iadd
ldc_w 100
idiv
istore 4


if0: 
iload 4
ldc_w 100
if_icmplt true0
bipush 0
goto endComp0
true0:
bipush 1
endComp0:
ifeq else0

bipush 1
istore 3
goto endIf0


else0: 
bipush 0
istore 3
goto endIf0
endElse0:
endIf0:

iload 3

ireturn
performSingleEstimate_end:

.end method
;tamanh final 0
.method public estimatePi100(I)I
.limit stack 4
.limit locals 5

.var 2 is samples_in_circle I from estimatePi100_init to estimatePi100_end
.var 3 is samples_so_far I from estimatePi100_init to estimatePi100_end
.var 4 is pi_estimate I from estimatePi100_init to estimatePi100_end

estimatePi100_init:





ldc_w 0
istore 3

ldc_w 0
istore 2


while0: 
iload 3
iload 1
if_icmplt true1
bipush 0
goto endComp1
true1:
bipush 1
endComp1:
ifeq endWhile0


if1: 
aload 0
invokevirtual MonteCarloPi/performSingleEstimate()Z
ifeq else1

iload 2
ldc_w 1
iadd
istore 2
goto endIf1

else1: 
goto endIf1
endElse1:
endIf1:
iload 3
ldc_w 1
iadd
istore 3
goto while0
endWhile0:

ldc_w 400
iload 2
iload 1
idiv
imul
istore 4

iload 4

ireturn
estimatePi100_end:

.end method
;tamanh final 1
.method public static main([Ljava/lang/String;)V
.limit stack 3
.limit locals 4

.var 2 is pi_estimate_times_100 I from main_init to main_end
.var 3 is num_samples I from main_init to main_end

main_init:


invokestatic ioPlus/requestNumber()I
istore 3

iload 3
new MonteCarloPi
dup
invokespecial MonteCarloPi/<init>()V
iload 3

invokevirtual MonteCarloPi/estimatePi100(I)I
istore 2

iload 2
invokestatic ioPlus/printResult(I)V

return
main_end:

.end method
;tamanh final 2

