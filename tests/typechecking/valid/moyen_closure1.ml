let rec f y =
  let rec g x = x in
  g 
in
print_int ((f 0) 42)
