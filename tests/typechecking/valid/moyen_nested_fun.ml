let rec f x = 
  let rec add x y = x + y in 
  add x 2 in 
print_int (f 40)
