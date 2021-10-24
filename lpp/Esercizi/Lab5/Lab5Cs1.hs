primo :: Integer -> Bool
primo n = aux 2
  where
    aux k | k >= n         = k == n
          | n `mod` k == 0 = False
          | otherwise      = aux (k + 1)

primi :: Int -> [Integer]
primi n = take n (filter primo (enumFrom 2))

primoMaggioreDi :: Integer -> Integer
primoMaggioreDi n = head(filter primo (enumFrom (n + 1)))

primiGemelli :: Int -> [(Integer, Integer)]
primiGemelli n = take n (filter (\ (x, y) -> y - x == 2) (zip ls (tail ls)))
  where
    ls = filter primo (enumFrom 2)