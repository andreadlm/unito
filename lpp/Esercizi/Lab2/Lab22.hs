-- Es1
sumN :: Int -> Int
sumN 0 = 0
sumN n = n + sumN (n - 1)

-- Es2
pow2 :: Int -> Int
pow2 0 = 1
pow2 n = 2 * pow2 (n - 1)

-- Es3
bits :: Int -> Int
bits n | n == 0 =  0
       | n `mod` 2 == 0 = bits (n `div` 2)
       | n `mod` 2 == 1 = bits (n `div` 2) + 1

-- Es4
potenzaDi2 :: Int -> Bool
potenzaDi2 n | n == 1 = True
             | n `mod` 2 /= 0 = False
             | otherwise = potenzaDi2 (n `div` 2)