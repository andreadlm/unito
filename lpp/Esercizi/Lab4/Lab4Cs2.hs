match :: Eq a => [a] -> [a] -> Bool
match xs = any (uncurry (==)) . zip xs

adiacenti :: Eq a => [a] -> Bool
adiacenti xs = any (uncurry(==)) (zip xs (tail xs))

polinomio :: [Float] -> Float -> Float
polinomio xs x = foldr (\ (a, i) s -> (a * x^i) + s) 0 (zip xs [0 ..])

polinomio_ :: [Float] -> Float -> Float
polinomio_ xs x = sum (map (uncurry (*)) (zip xs (map (x ^) [0 ..])))

perfetto :: Int -> Bool 
perfetto x = sum (filter (\ n -> x `mod` n == 0) [1 .. x - 1]) == x

perfetto_ :: Int -> Bool
perfetto_ x = sum (filter ((== 0) . (x `mod`)) [1 .. x - 1]) == x

ordinata :: Ord a => [a] -> Bool
ordinata xs = all (uncurry (<=)) (zip xs (tail xs))