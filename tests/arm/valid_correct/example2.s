.data
.text
.global main
main:
  @ prologue
  stmfd  sp!, {fp, lr}
  add fp, sp, #4
  sub sp, #28
  @ copy parameters
  @ function code
  mov r0, #2
  str r0, [fp, #-8]
  mov r0, #3
  str r0, [fp, #-12]
  @ new 8
  ldr r1, =_heap_ptr
  ldr r2, [r1]
  add r2, #8
  str r2, [r1]
  ldr r1, [r1]
  str r1, [fp, #-16]
  ldr r1, [fp, #-16]
  mov r2, #4
  ldr r3, [fp, #-12]
  str r3, [r1, r2]
  ldr r1, [fp, #-16]
  mov r2, #0
  ldr r3, [fp, #-8]
  str r3, [r1, r2]
  ldr r0, [fp, #-16]
  str r0, [fp, #-28]
  @ copy parameters
  ldr r0, [fp, #-28]
  @ call
  bl _first.5
  @ copy direct call result
  str r0, [fp, #-32]
  @ copy parameters
  ldr r0, [fp, #-32]
  @ call
  bl _min_caml_print_int
  @ copy direct call result
  @ epilogue
  sub sp, fp, #4
  ldmfd  sp!, {fp, lr}
  bx lr

_first.5:
  @ prologue
  stmfd  sp!, {fp, lr}
  add fp, sp, #4
  sub sp, #8
  str r0, [fp, #-8]
  @ function code
  ldr r1, [fp, #-8]
  mov r2, #0
  ldr r3, [r1, r2]
  str r3, [fp, #-12]
  ldr r0, [fp, #-12]
  @ epilogue
  sub sp, fp, #4
  ldmfd  sp!, {fp, lr}
  bx lr

