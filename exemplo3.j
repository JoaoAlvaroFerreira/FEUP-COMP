.source exemplo3.jmm
.class public exemplo3
.super java/lang/Object



; default constructor
.method public <init>()V
aload_0
invokespecial java/lang/Object/<init>()V
return
.end method

.method public getMult(I;I)I
.limit stack 2
.limit locals 3


getMult_init:
iload 1
iload 2
imul
ireturn
getMult_end:

.end method

.method public getNum(I)I
.limit stack 1
.limit locals 2


getNum_init:
iload 1
ireturn
getNum_end:

.end method


