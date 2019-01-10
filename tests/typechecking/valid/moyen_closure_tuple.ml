let rec f x = 42 in
let rec g x = 43 in
let t = (f, g) in 
let (a,b) = t in
(a 0)
