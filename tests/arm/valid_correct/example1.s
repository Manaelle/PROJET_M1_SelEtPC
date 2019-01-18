.data
.text
.global main
main:
  @ prologue
  stmfd  sp!, {fp, lr}
  add fp, sp, #4
  sub sp, #8
  @ copy parameters
  @ function code
  mov r0, #1
  str r0, [fp, #-8]
  @ copy parameters
  ldr r0, [fp, #-8]
  @ call
  bl _f.3
  @ copy direct call result
  str r0, [fp, #-12]
  @ copy parameters
  ldr r0, [fp, #-12]
  @ call
  bl _min_caml_print_int
  @ copy direct call result
  @ epilogue
  sub sp, fp, #4
  ldmfd  sp!, {fp, lr}
  bx lr

_f.3:
  @ prologue
  stmfd  sp!, {fp, lr}
  add fp, sp, #4
  sub sp, #4
  str r0, [fp, #-8]
  @ function code
  ldr r0, [fp, #-8]
  @ epilogue
  sub sp, fp, #4
  ldmfd  sp!, {fp, lr}
  bx lr

