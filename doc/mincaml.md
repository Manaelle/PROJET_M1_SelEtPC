# Mincaml

## Expressions
'''
M, N, e ::=                                      expressions
            c                                    constants
            op(M1,...,Mn)                        arithmetic operations
            if M then N1 else N2                 conditional branches 
            let x = M in N                       variable definitions
            x                                    variables
            let rec x y1 ... yn = M and ... in N function definitions
            M N1 ... Nn                          function application 
            (M1,...,Mn)                          tuple creations
            let (x1,...,xn) = M in N             reading from tuples
            Array.create M N                     array creation
            M1.(M2)                              reading from arrays
            M1.(M2) ← M3                         writing to arrays
''

## Types
'''
ρ,σ,τ ::=                                        types
         π                                       primitive types                         
         τ1 -> ...→τn ->τ                        function types
         τ1 ×...×τn                              tuple types
         τ array                                 array types
         α                                       type variables
'''

## K-Normalisation

'''
   K(c) = c K(op(M1,...,Mn)) =
      let x1 = K(M1) in ...
      let xn = K(Mn) in
      op(x1,...,xn)
   K(if M1 = M2 then N1 else N2) =
      let x = K(M1) in 
      let y = K(M2) in 
      if x = y then K(N1) else K(N2)
   K(if M1 != M2 then N1 else N2) = K(if M1 = M2 then N2 else N1)
   K(i fM1 <= M2 then N1 else N2) =
      let x = K(M1) in let y = K(M2) in if x <= y then K(N1) else K(N2)
   K(if M1 >= M2 then N1 else N2 = 
      K(if M2 ≤ M1 then N1 else N2)
   K(if M1 > M2 then N1 else N2) = 
      K(if M1 ≤ M2 then N2 else N1)
   K(ifM1 < M2 then N1 else N2) = 
      K(if M2 ≤ M1 then N2 else N1)
   K(if M then N1 else N2) =
      K(if M = false then N2 else N1)
         (if M is not a comparison) 
   K(let x = M in N) =
      let x = K(M) in K(N) 
   K(x) = x
   K(let rec f x1 ... xn = M and ... in N)= 
      let rec f x1 ... xn =K(M) and ... in K(N)
   K(M N1 ... Nn) = let x = K(M) in
      let y1 = K(N1) in ...
      let yn = K(Nn) in
      x y1 ... yn
   ...
'''



