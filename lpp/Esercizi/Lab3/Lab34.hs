ordinataNonDec :: [Int] -> Bool 
ordinataNonDec []           = True 
ordinataNonDec [_]          = True 
ordinataNonDec (x : y : xs) = (x <= y) && ordinataNonDec(y : xs)

stessaLunghezza :: [Int] -> [Int] -> Bool 
stessaLunghezza [] []             = True 
stessaLunghezza [] _              = False 
stessaLunghezza _ []              = False 
stessaLunghezza (x : xs) (y : ys) = stessaLunghezza xs ys

prodotto :: [Int] -> Int 
prodotto []       = 1
prodotto (x : xs) = x * prodotto xs

inverti :: [Int] -> [Int]
inverti [] = []
inverti (x : xs) = inverti xs ++ [x]

sommaCongiunta :: [Int] -> [Int] -> [Int]
sommaCongiunta [] _ = []
sommaCongiunta _ [] = []
sommaCongiunta (x : xs) (y : ys) = (x + y) : sommaCongiunta xs ys

maxList :: [Int] -> Int
maxList []       = undefined
maxList [n]      = n
maxList (x : xs) = aux x (maxList xs)
  where
    aux m n | m > n     = m
            | otherwise = n

appartiene :: Int -> [Int] -> Bool
appartiene _ []                 = False 
appartiene x (y : ys) | x == y  = True 
                    | otherwise = appartiene x ys

rimuoviDup :: [Int] -> [Int]
rimuoviDup []                         = []
rimuoviDup (x : xs) | appartiene x xs = rimuoviDup xs
                    | otherwise       = x : rimuoviDup xs

rimuoviPari :: [Int] -> [Int]
rimuoviPari [] = []
rimuoviPari (x : xs) | x `mod` 2 == 0 = rimuoviPari xs
                     | otherwise = x : rimuoviPari xs

dividi :: [(Int, Int)] -> ([Int], [Int])
dividi xs = (primaLista xs, secondaLista xs)
  where
    primaLista [] = []
    primaLista ((m, n) : l) = m : primaLista l
    
    secondaLista [] = []
    secondaLista ((m, n) : l) = n : secondaLista l