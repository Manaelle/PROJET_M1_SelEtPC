let rec f x = x + 40 in 
let y = 0 in 
let rec g x = x + 2 in 
print_int (y + (f 0) + (g 0)) 
