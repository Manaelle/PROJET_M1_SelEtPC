let rec f x = 
  let (a,b) = x in print_int (a + b)
in
  f (20,22)
