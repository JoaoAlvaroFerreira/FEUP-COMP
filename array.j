.class public array
.super java/lang/Object

;fields

.field private elements [I
.field private currentSize I
.field private totalSize I


; constructor

.method public <init>()V

   .limit stack 2
   .limit locals 1
   
   aload 0
   invokespecial java/lang/Object/<init>()V
   
   ; initialize totalSize
   aload 0                   ; <this>
   iload 1                  ; <size this>
   putfield array/totalSize I  ; <> && this.totalSize = size

   
   ; initialize currentSize
   aload 0                   ; <this>
   ldc 0                     ; <0 this>
   putfield array/currentSize I      ; <> && this.currentSize = 0
   
   ; initialize elements:
   aload 0                   ; <this>
   dup                       ; <this this>
   getfield array/totalSize I  ; <100 this>
   newarray int              ; <ref this> where ref = new int[100]
   putfield array/elements [I   ; <> && this.elements = ref       
   
   return
.end method


; void validateIndex(index)

.method private validateIndex(I)V

   .limit stack 2
   .limit locals 2
   
   ; validate index = locals[1]
   iload 1                   ; <index>
   iflt INDEX_ERROR          ; <> && if (index < 0) goto INDEX_ERROR
   iload 1                   ; <index>
   aload 0                   ; <this index>
   getfield array/currentSize I      ; <this.currentSize index>
   if_icmpge INDEX_ERROR     ; <> && if (index >= this.currentSize) goto INDEX_ERROR
   return                    ; index is valid

INDEX_ERROR:

   ; push java.lang.System.out (type PrintStream)
   getstatic java/lang/System/out Ljava/io/PrintStream;
   ; push string to be printed
   ldc "Error: array index out of range!"
   ; invoke println
   invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
   ; terminate program with System.exit(1)
   ldc 1
   invokestatic java/lang/System/exit(I)V
   return   ; never reached
   
.end method


; int getElement(index)

.method public getElement(I)I

   .limit stack 2
   .limit locals 2
   
   ; validate index
   aload 0                   ; <this>
   iload 1                   ; <index this>
   invokevirtual array/validateIndex(I)V     ; <>
   
   aload 0                   ; <this>
   getfield array/elements [I   ; <this.elements>
   iload 1                   ; <index this.elements>
   iaload                    ; <this.elements[index]>
   ireturn
   
.end method

 
; int getcurrentSize()
 
.method public getcurrentSize()I

   .limit stack 1
   .limit locals 1
   
   aload 0                   ; <this>
   getfield array/currentSize I      ; <this.currentSize>
   ireturn
   
.end method


; void addElement(element)

.method public addElement(I)V

   .limit stack 3
   .limit locals 2
   
   ; check totalSize
   aload 0                       ; <this>
   getfield array/totalSize I      ; <this.totalSize>
   aload 0                       ; <this this.totalSize>
   getfield array/currentSize I          ; <this.currentSize this.totalSize>
   if_icmple DONE                ; <> && if (this.totalSize <= this.currentSize) goto DONE
   
   ; elements[currentSize] = element
   aload 0                       ; <this>
   getfield array/elements [I       ; <this.elements>
   ;iload 1                       ; <element this.elements>
   aload 0                       ; <this this.elements>
   getfield array/currentSize I          ; <this.currentSize this.elements>
   iload 1                       ; <element this.currentSize this.elements>
   iastore                       ; <> && this.elements[this.currentSize] = element
   
   ; currentSize = currentSize + 1
   aload 0                       ; <this>
   dup                           ; <this this>
   getfield array/currentSize I          ; <this.currentSize this>
   ldc 1                         ; <1 this.currentSize this>
   iadd                          ; <1 + this.currentSize this>
   putfield array/currentSize I          ; <> && this.currentSize = 1 + this.currentSize
   
   return
   
DONE:
   ; push java.lang.System.out (type PrintStream)
   getstatic java/lang/System/out Ljava/io/PrintStream;
   ; push string to be printed
   ldc "Error: array full!"
   ; invoke println
   invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
   return

   
.end method