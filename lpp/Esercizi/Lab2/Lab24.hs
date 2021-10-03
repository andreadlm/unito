-- Es1
massimo :: Int -> Int -> Int
massimo x y | x > y = x
            | otherwise = y

minimo :: Int -> Int -> Int
minimo x y | x > y =  y
           | otherwise = x

-- Es2
potenza :: Int -> Int -> Int
potenza m n | n == 0 = 1
            | otherwise = m * potenza m (n - 1)

-- Es3
pow2 :: Int -> Int
pow2 = potenza 2

-- Es4
sottrazione :: Int -> Int -> Int
sottrazione x y = x - y

opposto :: Int -> Int
opposto = sottrazione 0
