-- Es 1
pari :: Int -> Bool
pari n = n `mod` 2 == 0

successore :: Int -> Int
successore n = n + 1

assoluto :: Int -> Int
assoluto n | n >= 0 = n
           | otherwise = negate n

fun :: Int -> Int
fun n | pari n = successore n
      | otherwise = assoluto n

fun2 :: Int -> Int
fun2 n = if pari n then successore n else assoluto n

-- Es2
bisestile :: Int -> Bool
bisestile n = (n `mod` 4 == 0 && n `mod` 100 /= 0) || (n `mod` 400 == 0)

giorni :: Int -> Int
giorni n | bisestile n = 366
         | otherwise = 365