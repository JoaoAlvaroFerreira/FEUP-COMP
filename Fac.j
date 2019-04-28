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
.limit stack 1
.limit locals 3

.var 2 is num_aux I from ComputeFac_init to ComputeFac_end

ComputeFac_init:
iload 1
istore 2
iload 2
ireturn
ComputeFac_end:

.end method

.method public static main([Ljava/lang/String;)V
.limit stack 2
.limit locals 3

.var 2 is lol LFac; from main_init to main_end

main_init:
new Fac
dup
invokespecial Fac/<init>()V
astore 2
aload 2
bipush 10

invokevirtual Fac/ComputeFac(I)I
invokestatic io/println(I)V
return
main_end:

.end method


