ASML
====

In this document, we  specify  an intermediary language (called ASML) that
defines the interface between the [front-end](./frontend.html) and the 
[back-end](./backend.html) of the compiler.

ASML is an addition to the implementation described in the MinCaml paper. It is
essentially a syntax for the *Virtual Machine Code* described in section 4.11,
and more precisely the `SparcAsm.t` datatype.

Your front-end must be able to generate syntactically correct ASML code with the
option `-asml`. Optionally, you can implement an ASML parser  so that you can
also generate SPARC code from ASML code. In any case, the  front-end and back-
end should use the same internal representation of  ASML code (similar to the
`SparcAsm.t` datatype).

ASML is a high-level assembly language with a functional flavor  and an ML-like
syntax. Like regular assembly language, values are stored in temporaries, i.e.,
registers but in an unbounded number.  Functions are referenced by labels.
Instructions operate either on  variable or immediate values. On the other
hand, ASML uses higher-level abstractions.  There is an unbounded number of
temporaries. Parameters passing when calling functions is implicit. There are
if-then-else  instructions, operators for memory allocation, and for closure
calls.

Examples
--------

In this section, we describe the syntax and semantics of ASML with a series 
of examples.

### Basics

An ASML program always has an entry point starting with `let _ =` (we call it
the *main* function).

	let _ = 
	  let x = 0 in
	  call _min_caml_print_int x

Functions are called using the `call` operator. The only accepted parameters
are variables. There are two types of identifiers: variables and labels. Labels
must start with an underscore. Some functions like `_min_caml_print_int` are
*external* and provided by the runtime. Variables contain values that can be
either addresses, labels, nil, booleans or integers.  Each of them takes one
memory *word* of space. A word is equal to 4 bytes. ASML is not statically
typed. Typing errors cause undefined behavior at runtime.

Just like in ML, functions and operators always return a value. The nil value
is used when the returned value is irrelevant. It is assumed that the main
function returns nil. 

The next example performs two consecutive prints. The first call to
`_min_caml_print_int` returns nil. As a consequence, the variable `tu`
(`tu` as in "*type unit*") isn't needed in the rest of the program.

	let _ =
	   let x = 0 in 
	   let tu = call _min_caml_print_int x in
	   call _min_caml_print_int x


Before the main function, an arbitrary number of functions can be defined,
each with an arbitrary number of arguments. The following example also uses
two binary operators, `add` and `sub`.

    let _f x y =
       let z = add x y in
       let t = 2 in
       sub t z

    let _ =
       let x = 0 in
       let y = 1 in
       let z = call _f x y in
       call _min_caml_print_int z

There are some restrictions:

- a variable cannot be defined twice 
- and "let" can't be nested (except for variable definitions inside functions).

For instance, the following two program fragments are illegal.

    let x = 0 in
    let x = add x x

    let x = let y = 1 in y

Unlike functions call, some operators accept integer immediate values.

    let _f x =
       let z = add x 1 in
       sub z 2

ASML has if-then-else constructs.

    let _ = 
        let a = 0 in
        let b = 1 in
        if a = b then (
            let res1 = 0 in
            call _min_caml_print_int res1
        ) else (
            let res2 = 1 in
            call _min_caml_print_int res2 
        )

### Memory (Imperative features)

Memory can be accessed in ASML using addresses, offsets, and the `mem` 
operator.

    let _ = 
       let size = 2 in
       let init = 0 in
       let arr = call _min_caml_create_array size init in
       let i0 = 0 in
       let v0 = 1 in
       let tu = mem(arr + i0) <- v0 in 
       let i1 = 1 in
       let v1 = mem(arr + i0) in
       mem(arr + i1) <- v1 

The external function `_min_caml_create_array` allocates `size` words, and
initializes them with value `init`. This could be done directly in ASML using
the `new` operator for memory allocation but it's convenient to have a dedicated
external function.

`mem(x + y) <- z` and `mem(x + y)` are syntactic constructs reminiscent
of OCaml for memory operators. In particular `x + y` *is not* a sub-expression, 
but can only be a memory address plus an offset (in bytes).

Access to non-initialized or non-allocated addresses leads to undefined
behavior.

### Floats

Float are used differently than integers. They can't be used as immediate values
in the program. In the reference paper, this has to do with the limitation of
the Sparc architecture (in Sparc assembly, an immediate float value doesn't fit
in the mem code of operators, and immediate integers are restricted to 13 
bits).  Instead, float litterals are stored in memory at addresses defined by 
*labels*.

Consequently, float are declared at the top level, along with function
definitions and are associated to labels.

	let _x1 = 2.4 
	let _x2 = 1.7 
	let _ = 
	    let addr1 = _x1 in
	    let addr2 = _x2 in
	    let r1 = mem(addr1 + 0) in
	    let r2 = mem(addr2 + 0) in
	    let r3 = fadd r1 r2 in
	    let r4 = call _min_caml_int_of_float r3 in
	    call _min_caml_print_int r4

### Closures

Consider the following MinCaml program

    let x = 1 in
    let rec add_x y = y + x in
    let rec apply_to_zero u = u 0 in
    print_int (apply_to_zero add_x)

A possible translation to ASML is the following (explanations below):

	let _add_x y =
            let x = mem(%self + 4) in
	    add y x

	let _apply_to_zero u =
	    let t = 0 in
            apply_closure u t

	let _ = 
	    let x0 = 1 in
	    let add_x = new 8 in
	    let addr_add_x = _add_x in
	    let tu0 = mem(add_x + 0) <- addr_add_x in
	    let tu1 = mem(add_x + 4) <- x0 in
	    let res = call _apply_to_zero add_x in
	    call _min_caml_print_int res 

Remember that a closure can be viewed as an object that contains one method 
(in this example, `_add_x`) and fields that correspond to a function environment
when it is declared (here `x`) (see [closure](./closure.html)).

The instruction `let add_x = new 8` allocates 8 bytes and stores the pointer to
the allocated memory in `add_x`. The first word is then initialized to the
address of the *method* of the closure, that is `_add_x`, and the second word is
initialized to the value of `x` when function `add_x` was defined.

The first argument `u` of `_apply_to_zero` is a closure (more precisely, its 
address), and is called using the `apply_closure` instruction. This effectively 
copies address `u` in a special temporary `%self`, and call the function 
pointed to by `u`.

Functions `_add_x` is always called in the context of a `apply_closure`
instruction and can thus retrieve its environment using temporary `%self` (in
[closure](./closure.html) the closure address is always pushed on the stack of
the closure).

### Comparison with datatype Sparcasm.t

The correspondance between the ASML syntax and the `SparcAsm.t` datatype 
is mostly straightforward. Most constructors in `SparcAsm.t` and `SparcAsm.exp` 
have a direct syntactic countepart.

There a few exceptions. `SparcAsm.exp` constructors `Set`, `SetL`,
and `SparcAsm.t` constructor `Ans` don't need a specific keyword as they are 
implict from the context. Constructors `Ld` and `St` are syntactically 
represented by the array notation `mem(x + y)` and `mem(x + y) <- z`, closer
to the ML syntax.

We ignore the `LdDF` and `StDF` constructor that are float versions of 
`Ld` and `St`. We won't make use of this feature. 

Syntax
------

### Grammar

ASML use ML-style comments. Integer (`INT`) and float litterals (`FLOAT`) 
are defined as in OCaml. Identifiers (`IDENT`) are strings
of characters and numbers, starting lowercase character. A label (`LABEL`) is 
an identifier starting with an `_`. `%self` is a special identifier.

The other tokens are:

	LPAREN  '(' 
	RPAREN  ')'
	PLUS    '+'
	EQUAL   '=' 
	FEQUAL  "=."
	LE      "<=" 
	FLE     "<=."
	GE      ">=" 
	IF      "if" 
	THEN    "then"
	ELSE    "else"
	LET     "let" 
	IN      "in" 
	DOT     "." 
	NEG     "neg" 
	FNEG    "fneg" 
	MEM     "mem" 
	FMUL    "fmul" 
	FDIV    "fdiv"
	FSUB    "fsub" 
	FADD    "fadd" 
	ASSIGN  "<-" 
	ADD     "add" 
	SUB     "sub" 
	CALL    "call" 
	NEW     "new" 
        NOP     "nop"
        APPCLO  "apply_closure"
        UNDERSC '_'

The syntax is given by the following rules:

	ident_or_imm:
	| INT   
	| IDENT 

	exp: 
	| NOP 
	| LPAREN exp RPAREN
	| INT
	| IDENT
	| LABEL
	| NEG IDENT
	| FNEG IDENT
	| FADD IDENT IDENT
	| FSUB IDENT IDENT
	| FMUL IDENT IDENT
	| FDIV IDENT IDENT
	| NEW ident_or_imm 
	| ADD IDENT ident_or_imm 
	| SUB IDENT ident_or_imm 
	| MEM LPAREN IDENT PLUS ident_or_imm RPAREN
	| MEM LPAREN IDENT PLUS ident_or_imm RPAREN ASSIGN IDENT
	| IF IDENT EQUAL ident_or_imm THEN asmt ELSE asmt  
	| IF IDENT LE ident_or_imm THEN asmt ELSE asmt  
	| IF IDENT GE ident_or_imm THEN asmt ELSE asmt  
	| IF IDENT FEQUAL IDENT THEN asmt ELSE asmt  
	| IF IDENT FLE IDENT THEN asmt ELSE asmt  
	| CALL LABEL formal_args 
	| CALLCLO IDENT formal_args 

	asmt: 
	| LPAREN asmt RPAREN 
	| LET IDENT EQUAL exp IN asmt 
	| exp 

	formal_args:
	| IDENT formal_args
	| IDENT
	| NIL

	fundefs:
	| LET UNDERSC EQUAL asmt 
	| LET LABEL EQUAL FLOAT fundefs 
	| LET LABEL formal_args EQUAL asmt fundefs 

	toplevel:
	|  fundefs 

Simulator
---------

We provide the *asml* interpreter in the `tool` directory of the
archive. To run an ASML program `foobar.v`, simply type

    ./asml foobar.v

or

  	./asml -v foobar.v
    
for verbose mode.

Most examples of this document are given in `asmlexamples/`.

The predefined functions are:

  	_min_caml_create_array
  	_min_caml_create_float_array
  	_min_caml_print_int
  	_min_caml_print_newline
  	_min_caml_sin
  	_min_caml_cos
  	_min_caml_sqrt
  	_min_caml_abs_float
  	_min_caml_int_of_float
  	_min_caml_float_of_int
  	_min_caml_truncate
	
