Debugging
=========

    qemu-arm ./gcd
    qemu: uncaught target signal 11 (Segmentation fault) - core dumped

View assembly:

     arm-none-eabi-objdump -td gcd

Launch in singlestep with debug port 1234:

     qemu-arm -singlestep -g 1234 ./gcd

Connect your favorite debugger <!-- `layout asm` and `layout regs` -->

    arm-none-eabi-gdb ./gcd
    (gdb)target remote :1234
    (gdb)start
    (gdb)stepi

Using `ddd` instead of `gdb`:

    ddd --debugger arm-none-eabi-gdb ./gcd
