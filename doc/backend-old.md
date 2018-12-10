# vim: spell:spelllang=en
#+TITLE: Back-end


In this document, we describe the _back-end_ of the compiler, i.e., how from 
the Intermediate Representation (here, ASML) we can obtain assembly code.

In this project, we use a virtual machine code called ASML (described in 
details in another document). It is close the ARM assembly but contains still 
"high-level" constructions, such as the use variables (called "temporaries", 
instead of registers) or if-then-else constructs, and is actually still 
architecture agnostic (i.e., still generic and not yet targeted for any 
particular architecture).

The goal of the back-end is to generate target-specific assembly from this 
generic intermediate representation. In particular, you will need to 
implement memory management, convert the =call= operator so as to generate 
actual assembly calls respecting the [[Calling conventions][calling 
conventions]], and assign all the temporaries to actual machine registers by 
performing [[Register allocation][register allocation]].

** Memory management (stack / heap)

   A program has access to two kind of memory for its calculations, the 
   registers and the main memory. The main memory itself is usually divided 
   into two zones, the stack and the heap.

*** Stack

    Functions use the stack a local memory, by creating a frame in it. This 
    is where you will find for instance local variables that do not fit in 
    registers (if there are two many of them), or local structures such as 
    structures or arrays, since they are accessed through the use of 
    pointers.  For instance, this is where the =alloca= function (in C) 
    allocates memory.

*** Heap

    This area holds objects that need to live outside the scope of the 
    functions that defines them. For instance, if a function creates and 
    returns an array, it cannot be created on the frame of a function as it 
    would be overwritten by later function call. This is where the =malloc= 
    C function allocates memory.

*** Parameter passing

    In the next session, we will see that up to four parameters can be passed 
    to a function using registers =r0= to =r3=. Up to four values can be 
    returned by a function using the same registers. If a function requires 
    more parameters, or if some parameters do not fit in a register (for 
    instance an array, or a structure), you must use the stack. For instance 
    if you have 6 arguments, the first four will be in registers, then you 
    need to push the 6th, then the 5th on the stack, so that the remaining 
    arguments are just above the stack pointer when entering the function.



** Calling conventions

   Calling conventions is what allows program to perform functions calls in 
   general. Some conventions are there to allow programs to use libraries, as 
   without conventions it would be impossible to know for instance where the 
   arguments should be stored.

   The conventions for ARM reserve some registers for some particular 
   purpose, and assume properties on others.

   *  =r15= is the program counter.
   *  =r14= is the link register. (The branch =BL= instruction, used in 
     a subroutine call, stores the return address in this register).
   *  =r13= is the stack pointer.
   *  =r4= to =r12=: used to hold local variables.
   *  =r11= can be used as frame pointer.
   *  =r0= to =r3=: used to hold argument values passed to a subroutine, and 
     also hold results returned from a subroutine.


   * Callee-save registers: subroutines must preserve the contents of =r4= to 
     =r11= and the stack pointer (=r13=).
   * Caller-save registers: =r0-r3=, =r12=, =r14=, =r15=: you must save them 
     before calling another function (=r15= automatically put in =r14=, but 
     =r14= must be saved on the stack!)

   This calling convention causes a "typical" ARM subroutine to do the 
     following:

   0) Decrease the stack pointer to create the functions's frame.
   1) In the prologue, push all =r4-r11= to the stack, as well as the return 
      address (=r14=). (This can be done with a single STM instruction).
   2) copy any passed arguments (in =r0= to =r3=) to the local scratch 
      registers (=r4= to =r12=).
   3) do calculations and call other subroutines as necessary using BL, 
      assuming =r0= to =r3=, =r12= and =r14= will not be preserved.
   4) put the result in =r0=
   5) In the epilogue, pull =r4= to =r11= from the stack, and pulls the 
      return address to the program counter =r15=. (This can be done with 
      a single LDM instruction).
   6) Put the stack pointer back to it's previous value.


*** Register constraints (ABI)

   More details on how to deal with ABI constraints and register allocation.
   * arguments in =r0-r3=
   * return value in =r0=
   * return address =r14= (important to save!!!)

   Callee duties:
   - When entering a function: store callee-save then put arguments in 
         variables.
   - When exiting a function: put result variable in =r0= then load 
     callee-save.

   Caller duties:
   - Before function call: store caller-save then put args in =r0-r3=.
   - After function call: put =r0= in variable then load caller-save.


   Maybe not all caller-save or callee-save register need to be stored in memory.
   This will depend on register allocation.

   To simplify you can use this convention:
     - terminal functions (which do not call other functions) use in priority
       =r0-r3=, and can store some in =r4-r11= if they need more.

     - non-terminal functions use in priority =r4-r11= (make sure to save 
       them first!) and only use =r0-r3= as temporary registers (load/store, 
       swap variables) and for short-lived variables (e.g., variables not 
       alive across functions calls).


    Programs before register allocation should look like (v** and x** being 
       regular variables):
       : x0-x8 <- r4-r11 (can be converted to store)
       : v1 <- r0
       : v2 <- r1
       :
       :  (stuff)
       :
       : r0 <- v4
       : r1 <- v5
       : call f
       : v6 <- r0
       :
       :  (stuff)
       :
       : r0 <- v10
       : r4-r11 <- x0-x8 (can be converted to loads)


** Register allocation

   Register allocation will convert all v** and x** variables to registers.
   If there is not enough registers, you must spill (save) variables to 
   memory: insert a store after each definition, and a load before each use 
   (can actually be refined, since maybe not all loads are useful).

*** Basic allocation: spill everything

    As a first step to have something working, you can spill all variables to 
    the stack. It is also the case for instance in gcc if you compile without 
    any optimization (=-O0=).
    To do this, you must reserve for each variable a slot in the function 
    frame (on the stack, i.e., an offset from the "frame pointer" (=r11=) or 
    the "stack pointer" (=r13=) if your frame size is constant).
    Then, insert a store after each definition and a load before each use.
    Warning: you still need to put the arguments of an instruction in 
    different registers!


*** Linear scan

    An algorithm that yield acceptable results, fast enough to be used in 
    constrained environments.
    Consider the function as a single basic block.
    Each variable has a live-range from its first definition to its last use:
    they are intervals.

    Order the intervals by increasing starting point, and assign them to 
    registers in this order.
    When there is no register available, spill *among the current live 
    variables* the one that has the farthest use.

    * It works ok as long as you separate registers for load/store from the others. (this can be refined with care).
    * A particular good point: it will spill in priority variables from 
      callee-save first (denoted above x**) since their use is at the end of 
      the function.


*** Graph coloring

    Actually, live-ranges of variables are not intervals as a function is not 
    a simple basic block but a more general control-flow graph (CFG). It is 
    possible to construct the exact interference graph using the exact 
    live-ranges with a control-flow analysis.
    In that graph, every variable is a node and two adjacent nodes cannot have 
    the same register. This is graph coloring where registers are colors.
    In that case, we know how many colors we have (the registers), and 
    whenever we can't color the graph, we need to spill variables. This 
    analysis yields better results, however, we face two problems:
    * graph-coloring is NP-complete for k>=3 colors
    * when spilling a variable, it creates multiple "small" variables which are 
      short-lived (for the loads and stores) but needs to be taken into account.

    The most common way to treat this problem is to use a simplification technique, and iterate as long as required.
    A very good algorithm is the Iterated Register Coalescing by Appel 
      & George, which has the added benefit of taking into account register 
      /coalescing/, i.e., putting in the same registers to variables linked 
      by a move instruction in order to save the instruction.


*** Tree scan

    A benefit of compiling from a functional language is that each variable is 
    defined only once!
    We can do even better in that case, as live-ranges are subtrees of the 
    dominance tree (which you don't know about, but you can ask me :-)
    In short, while in general the graph coloring problem is NP-complete, in 
    that case the graph is particular and we know how to color it in polynomial 
    time.
    The algorithm looks a lot like linear scan, but works on a tree (and sub-trees) instead of an interval (and sub-intervals).

    Note:
    * special care must be taken for (let x = if ... then ... else ... )
      constructs.


*** Other possible optimizations

    * No need to reserve =r11= as frame pointer if you know the size of your 
      frames.
