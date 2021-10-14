media :: [Int] -> Double 
media x = fromIntegral (sum x) / fromIntegral (length x)

fattoriale :: Int -> Int 
fattoriale n = product [2..n]

intervallo :: Int -> Int -> [Int]
intervallo m n | m <= n    = m : intervallo (m + 1) n
               | otherwise = []

primo :: Int -> Bool
primo n = aux 2
  where
    aux k | k >= n         = k == n
          | n `mod` k == 0 = False
          | otherwise      = aux (k + 1)


primi :: Int -> [Int]
primi n = aux 2
  where
    aux m | m > n     = []
          | primo m   = m : aux (m + 1)
          | otherwise = aux (m + 1)
