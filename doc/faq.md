FAQ
===

# Front-end

## Do I need to create a new datatype for K-normal form and ASML?

You'll need a datatype for ASML. 

K-normal forms are a strict subset of the source language of MinCaml. Hence you 
can reuse the MinCaml AST to encode K-normal programs. However,
it's interesting to have a new datatype that will prevent you from defining ill-formed K-normal form accidentally. On the downside, defining
a new AST takes additional work (especially in Java) but it's probably a good 
idea nonetheless.

## I need more explanation on closure conversion

### Direct function calls

First, restrict your compiler to MinCaml code that only uses *direct* function calls. A function call is direct if we know what function is called from the app node. 

For instance, in
```
let rec f x = print_int x in
f 0
```

The call `f 0` refers to the function `f` defined above. This wouldn't be the case with a call of the form `((g 0) 1)`. We also need to assume that function don't use free variables (i.e. variable defined outside their scope). With these restrictions, closure conversion is straightforward. There are no closures!
 
In that case, you traverse the AST and generate a label from each function name.  (this label will later be used in the ARM assembly code to identify the function address).

For each function call `f x y ...`. If `f` corresponds to a function already
seen, generate an `apply_direct` instruction to the label corresponding to `f`.
Otherwise, `f` should be an external function. In that case, create the label
by appending `_min_caml_` to the name.

In ASM generation, `apply_direct` will be translated into ASML `call`. Then 
the backend will simply translate this call to a branching instruction. 
References to `_min_caml_` symbols will be resolved by the linker.
<!-- 
Example:
```
let rec u x =                                                                    
  let rec v x = u x in v 0                                                     
in u 0  
````
is transformed to ASML program:
```
let _v x =                                                                   
    call _u x                                                                 

let _u y =                                                                   
    let w = 0 in                                                              
    call _v w                                                               

let _ =                                                                          
    let t = 0 in                                                              
    call _u t                                                               
```

Only when direct function calls work (*including ASML and ARM generation*),
start extending this to closures. -->

### Closures

The previous scheme is ineffective in several cases. We already explained 
why in the [frontend](./frontend.html) document. We give more detail 
on how to implement the closure conversion phase (see also 4.10 in the article).
We want generalize the closure-conversion described above for direct calls to all types of functions.

Consider this example:
```
let rec succ x = 
  let rec add y = x + y in 
  add 1 
in
print_int (succ 42)
```

The previous algorithm isn't working anymore. We can't simply "extract" `add` from `succ` because it uses `x` that only exists in the context of `succ`. 

To solve the issue, when processing the `let rec add` node, we determine the free variables appearing in `add` and separate them from the parameters. 

```
label: _add 
  free variables: x
  parameters: y
  code:
    x + y
```

For `succ`, we should get something like

```
label: _succ 
  free variables: None
  parameters: x
  code:
     ...
```

The code part `add 1` should be converted to some sort of call to `_add`
but it can't be a direct call, as we also need to provide the value
of the free variable `x`. 

The trick is to translate `add 1` into two operation:

* a closure creation (make_closure). `add` is now a closure made of the 
 label `_add` and the value of free variable `x`.
* an apply closure operation for `add 1`.

```
label: _succ 
  free variables: None
  parameters: x
  code:
    let add = make_closure(_add, x) in
    let w = 1 in
    apply_closure(add, w)
```

Here is what the corresponding ASML may look like.

```
let _add.8 y.9 =
   let x.5 = mem(%self + 4) in
   add x.5 y.9

let _succ.4 x.5 =
   let add.8 = new 8 in
   let l.12 = _add.8 in
   let tu13 = mem(add.8 + 0) <- l.12 in
   let tu11 = mem(add.8 + 4) <- x.5 in
   let ti3.10 = 1 in
   call_closure add.8 ti3.10

let _ = 
   let ti1.7 = 42 in
   let ti2.6 = call _succ.4 ti1.7 in
   call _min_caml_print_int ti2.6
```

`make_closure` in `succ` has been translated to
a memory allocation followed by two updates in that structure. Moreover,
free variable `x` in `add` is looked up in the closure. `%self` is an implicit
parameter that contains the address of the closure. In ARM, it will have to be
passed explicitely by the generated code for `call_closure`.

### Remark

`make_closure`, `apply_direct` and `apply_closure` correspond to nodes that are introduced in the closure conversion step.  We use a syntactic notation in the examples, but you don't have to produce this type of output. 
Eventually, `apply_direct` and `apply_closure` will be converted to
`call` and `call_closure` instructions in the last phase of the front-end.

## What is the 13-bit immediate optimization phase mentioned in the article?

After k-normalization, all operations are of the form: op(x1,..,xn): They 
operate on variables, and not on litterals. However, in SPARC assembly (the 
target in the article), most integer instructions can take as operands 
immediate values if they need less than 13 bits. This phase is an optimization 
that uses those instructions (for instance, `MOV r0, #1; ADD r1, r0` is 
rewritten to `ADD r1, #1`). In ARM, there are different constraints on 
immediate values, but you should be able to perform similar operations. It is not required though.

# Back-end

## Do I need to write a parser for ASML?

No. It is useful if you want to test the backend before the frontend is working, but it's not required. 

## Code generation for function calls

We explain here a bit more the calling conventions in ARM code. In ASML, 
functions can be called using the `call` instruction. For instance,

     let _g x1 = 
       let x2 = 1 in
       let x3 = 2 in 
       let x = call _f x1 x2 x3  
       ...
       ...

     let _f u v w = 
        ...
        ...

We say that `_g` is the caller and `_f` is the callee. 

The generated ARM code for `call` should copy the parameters in `r0` to `r3`,
and on the stack if there are more than four parameters. In the example,
 `x1`, `x2` and `x3` will be placed in `r0`, `r1`, `r2`.

The code generated for `_f` should contain a *prologue*  (first instructions)
and an *epilogue* (last instructions).

#### Prologue

    stmfd  sp!, {fp, lr}   # save fp and lr on the stack
    add fp, sp, #4         # position fp on the address of old fp
    sub sp, #n             # allocate memory to store local variables

#### Epilogue

The epilogue restore `sp`, `fp` and `lr` and returns to caller

    sub sp, fp, #4         
    ldmfd  sp!, {fp, lr}  
    bx lr                  

After the prologue, the instructions contained in the function body can
access the parameters and the local variables using `fp`. The stack should
have the following shape (remember that pushing on the stack means decreasing
`sp`).

    #  (higher addresses)
    #    param_n                          
    #    ...
    #    param_5  <- 4(fp) 
    #    old_fp  <- fp
    #    lr     
    #    temp1   <- -8(fp)
    #    temp2   <- -12(fp)
    #    ...
    #    tempn   <- SP
    #    (saved local registers)
    #  (lower addresses)   


The function body can access parameters using `r0` to `r3` and (if needed) addresses
`4(fp)`, `8(fp)` and so on. Local variables can be accessed using 
`-8(fp)`, `-12(fp)` and so on.

The first instructions in the function body after the prologue should copy the
parameters contained in `r0` to `r3` to the temporary variables in the stack  or
in the local registers `r4` to `r12`, depending on your register allocation
strategy. After that registers `r0` to `r3` can be used as *scratch registers*.
The callee doesn't have to save them hence they can be modifed without being
restored. However, if you use local registers `r4` to `r12` they should be be
saved on the stack right after the prologue (*callee-saved registers*), and
restored  just before the epilogue. Be careful though with `r11` which is
register `fp`.

## What register allocation algorithm should I use?

Use the basic allocation algorithm that spills everything. It is simple and
sufficient to generate correct ARM code. Other algorithms are possible 
extensions. If you do implement other algorithms, add a command line option so that the use can choose between the algorithms.

# Typing

## What is `option ref` in the OCaml `Type.t` datatype? 

In the provided OCaml implementation, the `Type.t` datatype has a case
`Var of t option ref`. 

    type t =
    | Unit
    | Bool
    | Int
    | Float     
    | Fun of t list * t    
    | Tuple of t list
    | Array of t   
    | Var of t option ref 

This is done to implement type checking more efficiently. However, to simplify
your typing algorithm, you can instead use `Var of string` and generate *fresh*
type variables in the parser whenever you have to provide a type. Modify
`Type.gentyp()` so that it returns a new variable name each time it is called
(this is already the case in the java implementation).

## Partial application in mincaml
<!-- 
The case `Fun of t list * t` represents types of the form
`t1 -> (t2 -> (t3 -> ... -> tn))`. For instance, `Fun ([Int; Bool], Unit)`
represents the type `Int -> Bool -> Unit` (don't confuse it with 
`Int * Bool -> Unit`). -->

In OCaml, one can use partial application such as:

```
let rec f x y z = x + y + z in
let rec u x = f x 42 in   (* u has type int -> (int -> int) *)
()
```

However, this is forbidden in MinCaml and should be written:

```
let rec f x y z = x + y + z in
let rec u x z = f x 42 z in
()
```

### What is the difference between monomorphism and polymorphism

Consider
```
Let f x = () in (f 1) + (f true)
```
In OCaml, function `f` has *polymorphic* type `'a -> unit`. 
Mincaml only has *monomorphic* types and this program is not well-typed as the typechecker will generate two incompatible equations (type of `x` should be `int` and `bool`). 

See [Hindly Milner](https://en.wikipedia.org/wiki/Hindley–Milner_type_system#Algorithm_W) 
for a polymorphic typechecking algorithm. You can implement it as an extension.


### What equations should we generate for `let-rec` and `app`?

These cases were left as exercises in the `GenEquations` algorithm
(typing document).

```
GenEquations(env, expr, type) =
  case on expr:
  ...
  App ... ->
  Letrec ...  ->  
```

Remember that `GenEquations` returns a list of equations that hold if and only if term *expr* has type *type* in environment *environment*.

We explain here the missing cases for functions and application with *one* parameter. You'll have to generalize this to several parameters.

Suppose `App` is of the form `(expr1 expr2)`. `expr1` must have
type `A -> type` for some type variable `A` and `expr2` must have
type `A`. This gives:

```
App (expr1, expr2) -> 
    define A as a new variable
    eq1 = genEquations(env, expr1, A -> type) 
    eq2 = genEquations(env, expr2, A)
    return eq1 :: eq2 // concatenation of eq1 and eq2  
```

Suppose `Letrec` is of the form `Letrec (f, t2, x, t1, expr1, expr2)`. `t2`
is the type of the value returned by `f`, `t1` is the type of `x`. 
(In mincaml the parser fills these two types with type variables, but they could
be types specified by the user.)

`expr2` must have type `type` in the environment `env` extended with 
`f : t1 -> t2`. `expr1` must have type `t2` in the environment `env` extended
with `x : t1`. 

```
Letrec (f, t2, x, t1, expr1, expr2) ->
   eq1 = genEquations(env + (f : t1 -> t2), expr2, type)
   eq2 = genEquations(env + (x : t1), expr1, t2)
   return eq1 :: eq2 // concatenation of eq1 and eq2  
```

The operation `env + (x : t)` masks any previous definition of `x` in `env`. Environments can be implemented as linked lists of associations. New associations are inserted on the head of the list. Lookup (in `Var` rule) returns the first association.

### Do we really need this complex typing algorithm? 

In simple case, one can compute function types by simple tree walking
and type propagation from the leaves to the root.
For instance, one can easily infer that function `fun x -> x + 1`
has type `int -> int`. This strategy doesn't work on the following program.

```
let f g = g 1 2 in
let h a b = a + b in
print_int (f h)
```

# General questions

## Do we have to implement string or float types?

No. String or floats may still be mentioned in the documentation as they were part of previous years instances of this project but you don't have to implement them. They can be implemented as extensions if everything else is working.

## What library functions should be implemented.

Only `print_int` is mandatory. Other functions are optional and can be seen as extensions.  

## Can I extend the ASML language?

The ASML language is sufficient to encode simple features of MinCaml programs
such as arithmetic expressing, functions, closures, tuples. However, you'll 
probably need additional instructions for other features.

To implement arrays efficiently in ARM, you can use load and store operations
with register offset and shifting. For instance:
```
STR R0, [R1, R2, LSL #2] ; Stores R0 to an address equal to sum of R1
                         ; and four times R2.
```

This is useful for array access (e.g `a.(i)` in mincaml). There is no corresponding
 instruction in ASML, but you can define
an instruction `mem(x + y, n) <- z`.

Another limitation of ASML concerns "big" integers. In ARM, not all 32-bits
integers can be used as immediate values. For instance `mov r0, #1456` is not
permitted. Such integers can be put in the data section
but you'll have to extend ASML if you want to perform this operation in 
the front-end.

If you add new instructions, you can add a warning when generating the ASML (with `-asml` option) to say that your generated ASML is not supported by the simulator.

## I want to work on the float extension. What representation of float should I use?

Use a 32-bit representation for floats. This simplifies code generation as all
MinCaml values fit in one word of memory (otherwise, one would need
to use typing information to know the size of values in order to generate
appropriate code). 

Also, the type-checker doesn't need to decorate the AST with types as it is not needed (this is a simplification with respect to the article). The drawback is
that MinCaml will differ from OCaml on operations involving
floats.

## How to set up an Eclipse/Netbeans/... project?  Can I compile my project using Ant/Maven/...?

Use any editor/IDE you are able to use. For compiling, the only 
constraint is that we can build your project using `make` at the root of your repository on UGA servers. 

Maven is a popular choice to build Java programs. You are encouraged to use it if you can. 

## Do I need to use JUnit for testing in Java (or OUnit in OCaml)

There are several types of tests, notably

* unit test: to test one class or one function,
* system test: for testing the whole system.
                
We focus on system tests in this project. System tests work by:

* giving an input (in this project, a mincaml program)
* running the compiler (possibly with an option such as -asml, -t ...)
* checking the result (for instance, checking the [exit code](https://shapeshed.com/unix-exit-codes/) or running the generated program)

To automatize system tests you need to use scripting language. You can extend the bash script given in the archive or write your own python scripts.

JUnit/OUnit is useful for unit testing. It's great if you use it but it's less important.

## Can you provide examples of ARM code generated from mincaml programs?

Here are [two examples](./examples-ARM.tar.gz) of ARM programs generated
From mincaml programs. You can compile them with `make` and run them with `make test`.

In these examples, we use the basic register allocation strategy. 
All parameters and local variables are stored in the stack at a fixed offset
from the *frame pointer*. Whenever variables or parameters are needed for an operation, they are copied from the stack to registers. In your code generation algorithm, you simply need to maintain a table that maps a parameter or a local variable to an offset in the stack

The second example uses tuples. See how tuples are allocated in a static area of memory (the *heap*) allocated in `libmincaml.S`. `_heap_ptr`, also defined in `libmincaml.S` points to the first available word of the heap.

### Important note
To compile these examples, we use a different  `libmincaml.S` file and different linker options than what we used for the `ARM/helloworld.s`. It's important to understand that An ARM executable must define symbols `_start` and `_exit` as entry and exit points of the program. For simplicity, we define them directly in `ARM/helloworld.s`, but *not* in `example1.s` or `example2.s`. For these two examples, we rely instead on a library provided by `gcc`. This library defines `_start` and `_exit` in a way that interacts nicely with a `main` function. In particular, it sets the command line parameters that may be used by `main`, and it uses the value returned by `main` as the exit value of the process.

For your compiler, you can do either way, as long as you generate ARM code with the right behavior.

## Why can't we allocate arrays on the stack?

Consider this program
```
let rec f _ = 
	Array.create 10 () in
let rec g _ = 
  (f ()).(0) in
g ()
```

`f` returns an array (which is subsequently used in `g`). If the array
is on the stack, we can't return its address as it is no longer valid after
`f` returns.

It is the same issue you face in `C` with 
```
int *f() {
   int t[10];
   return t;
}
```
Indeed, `gcc` complains with 
```
warning: address of stack memory associated with local variable 't' returned
   return t;
          ^
```

The strategy used by mincaml is to represent arrays (and closures, tuples) as addresses on heap-allocated structures.













