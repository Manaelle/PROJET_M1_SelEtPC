let rec f y z =
  let rec g x = y + z + x in
  g 
in
print_int ((f 20 21) 1)
