What is a closure?
==================

In low level languages (think C, or assembly), functions can be passed 
as parameters to other functions using *function pointers*. That is, pointers
to the first instruction of the function.

Example: the C function `signal()` is used to register a signal handler.

		typedef void (*sig_t) (int);

		sig_t signal(int sig, sig_t func);

Things get more complicated when function definitions can be nested. 
For instance, in GNU C, one can write:

		int add (double a, double b) {
		  int add_b (double x) { return x + b; }
		  return add_b(a);
		}

Suppose now that instead of an integer, `add` returns a pointer to `add_b`. 

		typedef int (*func) (int);

		func add(double b) {
		  int add_b (double x) { return x + b; }
		  return &add_b;
		}

Unfortunately, this is not correct. The variable `b` may no longer exists 
when `add` returns. Presumably, `b` is allocated on the stack, and will be 
freed before we try to the function returned by add.

To solve this issue, functional languages use the notion of closure. A closure 
is the runtime representation of a function together with its context (the 
values of the local variables that the function was using when it was defined).

We could implement this behavior in C by defining a closure type. 
For instance, a closure type for functions of type `int -> int` could be:

		struct Closure {
		  int b;
		  func f;
		};

		typedef struct Closure *PClosure;

We can rewrite the initial program:

		int add_b(PClosure this, double x) {
		  return (x + this->b);
		}

		PClosure add (double b) {
		  PClosure cl = (PClosure) malloc(sizeof(struct Closure));
		  cl->b = b;
		  cl->f = (func) &add_b;
		  return cl;
		}

This is essentially the transformation you'll have to perform in the 
front-end of the compiler in ASML generation
(see [frontend](./frondend.html) and [ASML](./asml.html)).

Of course, in programming languages that support closures, all of this is taken
care of by the compiler or the runtime. Some examples:

#### OCaml

	let add b = 
		let add_b x = x + b in 
		add_b

#### Javascript

	function add(b) {
	    add_b = function(x) {
	        return x + b;
	    }
	    return add_b;
	}

In object-oriented languages like Java or C++, closures can be coded using 
objects instead of structures. A closure is an object with one method, and
fields capturing the local variables. 



























