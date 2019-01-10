let rec f x = 
  let (a,b,c) = x in print_int (a + b + c)
in
  f (20,21,1)
