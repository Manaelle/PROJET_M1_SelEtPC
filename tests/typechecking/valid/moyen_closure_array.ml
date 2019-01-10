let rec f x = 42 in
let ar = Array.create 100 f in
(print_int (ar.(0) 0))
