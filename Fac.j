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
.limit stack 0
.limit locals 5

.var 2 is lol LFac; from main_init to main_end
.var 3 is exe LFac; from main_init to main_end
.var 4 is num I from main_init to main_end

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
iload 4

invokestatic io/println(I)V
bipush 0
istore 4
while: 
iload 4
bipush 5
if_icmplt endWhile
.method public null()LFac;
.limit stack 0
.limit locals 1


null_init:
null_end:

.end method

endWhile:
main_end:

.end method


