	.text
	.global main
_f:
	str fp, [sp, #-4]
	add fp, sp, #0
	sub sp, sp, #8
	push {r5-r10,r12-r13}
	add r12, r0 r1
	ldr r5, r12
	mov r12, #2
	ldr r6, r12
	sub r12, r6 r5
	pop {r5-r10,r12-r13}
	ldr fp, [sp], #4
	bx lr
main:
	add r4, sp, #0
	sub sp, sp, #1000
	push {fp, lr}
	add fp, sp, #4
	sub sp, sp, #12
	mov r12, #0
	ldr r5, r12
	mov r12, #1
	ldr r6, r12
	push {r0-r3}
	ldr r0, r5
	ldr r1, r6
	bl _f
	pop {r0-r3}
	ldr r7, r12
	push {r0-r3}
	ldr r0, r7
	bl _min_caml_print_int
	pop {r0-r3}
	ldr fp, [sp], #4
	bx lr
