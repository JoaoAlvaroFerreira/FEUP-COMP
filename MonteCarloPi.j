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
bipush 1
istore 1
bipush 1
istore 2
bipush 0
bipush 100
isub

bipush 100

invokevirtual MonteCarloPi/random(istore 1
bipush 0
bipush 100
isub

bipush 100

invokevirtual MonteCarloPi/random(istore 2
iload 1
iload 1
imul
iload 2
iload 2
imul
iadd
bipush 100
idiv
istore 4
aload 3
ireturn
performSingleEstimate_end:

.end method

.method public estimatePi100(I)I
.limit stack 2
.limit locals 5

.var 2 is samples_in_circle I from estimatePi100_init to estimatePi100_end
.var 3 is samples_so_far I from estimatePi100_init to estimatePi100_end
.var 4 is pi_estimate I from estimatePi100_init to estimatePi100_end

estimatePi100_init:
bipush 0
istore 3
bipush 0
istore 2
bipush 400
iload 2
imul
iload 1
idiv
istore 4
iload 4
ireturn
estimatePi100_end:

.end method

.method public static main([Ljava/lang/String;)V
.limit stack 0
.limit locals 4

.var 2 is pi_estimate_times_100 I from main_init to main_end
.var 3 is num_samples I from main_init to main_end

main_init:
invokevirtual MonteCarloPi/requestNumber(istore 3
new MonteCarloPi
dup
invokespecial MonteCarloPi/<init>()V
iload 3

invokevirtual MonteCarloPi/estimatePi100(I)Iistore 2
iload 2

invokevirtual MonteCarloPi/printResult(return
main_end:

.end method


