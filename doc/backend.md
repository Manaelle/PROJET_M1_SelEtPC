Back-end
========

In this document, we describe the *back-end* of the compiler, i.e., how from
the Intermediate Representation (here, ASML) we can obtain assembly code.

ASML (described in details in another document) is close to the ARM assembly but
still contains "high-level" constructions, such as variables (called
"temporaries",  instead of registers), function calls, or if-then-else
constructs, and is actually still  architecture agnostic (i.e., still generic
and not yet targeted for any  particular architecture).

The back-end proceeds in two steps.

* The first step is to assign all the temporaries in ASML to actual machine 
registers by performing register allocation.

* The second step is to translate the new ASML code to ARM. For the most part, it
is a matter of translating the ASML instructions to their ARM counterparts. The
difficult part will be to implement memory management (stack/heap allocation)
and function calls respecting the calling conventions.

Register allocation
-------------------

Register allocation will convert all variables to registers. If there is not
enough registers, you must *spill* (save) variables to  memory: insert a store
after each definition, and a load before each use  (can actually be refined,
since maybe not all loads are useful). In your implementation, you can
implement register allocation by mapping registers to variables in the ASML
code. 

Example: 

    let _succ x =                                                                
       let t = 1 in                                                              
       add x t                                                                 

    let _ =                                                                          
       let y = 1 in                                                                
       let u = call _succ y in                                               
       call _min_caml_print_int u

After register allocation, variables are replaced by ARM registers.

    let _succ r4 =                                                                
       let r5 = 1 in                                                              
       add r4 r5                                                                 

    let _ =                                                                          
       let r4 = 1 in                                                                
       let r5 = call _succ r4 in                                               
       call _min_caml_print_int r5 

Variable can also be allocated on the stack, in which case we would use indirect
addressing such as `[fp, i]` (offset i from the frame pointer).

Note that depending on your design, you can add new instructions in ASML 
(for instance to load and store variables from the stack) in order to make
the ARM generation easier.

There are many strategies to allocate registers.

### Very basic allocation

As a first step to have something working, assign to each variable a 
different register and stop compiling if you run out of register. Of course,
this is not a viable strategy since you won't be able to compile all programs.

### Basic allocation: spill everything

To make the previous approach work (but still inefficently) you can  spill all
variables to the stack. It is also the case for instance in gcc if you compile
without  any optimization (`-O0`). In that case, all variables will be assigned
an address `[fp, i]`. 

The following strategies are more intricate and can be seen as improvements
when everything else is working.

### Linear scan

An algorithm that yield acceptable results, fast enough to be used in
constrained environments. Consider the function as a single basic block. Each
variable has a live-range from its first definition to its last use: they are
intervals.

Order the intervals by increasing starting point, and assign them to  registers
in this order. When there is no register available, spill *among the current
live  variables* the one that has the farthest use.

* It works ok as long as you separate registers for load/store from the others.
(this can be refined with care).  

* A particular good point: it will spill in priority variables from    callee-
save first (denoted above x**) since their use is at the end of    the function.

### Graph coloring

Actually, live-ranges of variables are not intervals as a function is not  a
simple basic block but a more general control-flow graph (CFG). It is  possible
to construct the exact interference graph using the exact  live-ranges with a
control-flow analysis. In that graph, every variable is a node and two adjacent
nodes cannot have  the same register. This is graph coloring where registers are
colors. In that case, we know how many colors we have (the registers), and
whenever we can't color the graph, we need to spill variables. This  analysis
yields better results, however, we face two problems:

* graph-coloring is NP-complete for k>=3 colors
* when spilling a variable, it creates multiple "small" variables which are 
  short-lived (for the loads and stores) but needs to be taken into account.

The most common way to treat this problem is to use a simplification technique,
and iterate as long as required. A very good algorithm is the Iterated Register
Coalescing by Appel    & George, which has the added benefit of taking into
account register    /coalescing/, i.e., putting in the same registers to
variables linked    by a move instruction in order to save the instruction.

### Tree scan

A benefit of compiling from a functional language is that each variable is
defined only once! We can do even better in that case, as live-ranges are
subtrees of the  dominance tree (which you don't know about, but you can ask me
:-) In short, while in general the graph coloring problem is NP-complete, in
that case the graph is particular and we know how to color it in polynomial
time. The algorithm looks a lot like linear scan, but works on a tree (and sub-
trees) instead of an interval (and sub-intervals).

Note: special care must be taken for `let x = if ... then ... else ...`
constructs.

<!-- 
### Other possible optimizations

* No need to reserve `r11` as frame pointer if you know the size of your 
  frames.
 -->

## ARM Generation

### Heap

Values in MinCaml are always one word (4 bytes) long. They are either 
primitive types (integers), or addresses of values allocated in the heap 
(arrays, closures, tuples).

Heap allocation is done using `new` in ASML. In ARM, you can simple allocate
a large static area of memory and store somewhere a pointer on the first
available address. Memory in the heap is never freed but it would be 
a nice extension to implement a garbage collector.

### Function calls

In order to implement function calls, we must answer a few questions. Typically,
 
* Where to store the parameters?
* Where to store the returned value(s)
* How are used the registers and who (the caller or the callee) should save them.

Usually, this is specified by *calling conventions*. These conventions allow 
programs to interact even if they were generated by different compilers.
We will use the calling conventions used for ARM.  

#### Registers

* `r15` is the program counter (alias `pc`)
* `r14` is the link register (alias `lr`). (The branch `BL` instruction, used in 
 a subroutine call, stores the return address in this register).
* `r13` is the stack pointer (alias `sp`).
* `r4` to `r12`: used to hold local variables.
* `r11` can be used as frame pointer (alias `fp`).
* `r0` to `r3`: used to hold argument values passed to a subroutine, and 
 also hold results returned from a subroutine.

#### Parameters passing 

* Up to four parameters can be passed to a function using registers `r0` to `r3`.
 Additional parameters are pushed on the stack.
* Values are returned in `r0`.

In MinCaml, the arguments always fit in registers  (they are either integers,
bools or addresses) and only one value can be returned by a function (in `r0`).

To make things simpler, suppose that functions are limited to four parameters
(at least in a first step).

#### Saving registers

* Callee-save registers: subroutines must preserve the contents of `r4` to 
 `r11` and the stack pointer (`r13`).
* Caller-save registers: `r0-r3`, `r12`, `r14`, `r15`: you must save them 
 before calling another function (`r15` automatically put in `r14`, but 
 `r14` must be saved on the stack!)
## Tips

* Debug ARM code with a debugger. See [here](./debugging.html).
* You can compile C programs using the arm compiler
`arm-none-eabi-gcc -S foobar.c -O0` (in `/opt/gnu/arm` on UFR computers).






