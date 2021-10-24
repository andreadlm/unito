lastSum :: (Eq a, Num a) => [a] -> Bool
lastSum = aux 0
  where
    aux s [x] = x == s
    aux s (x : xs) = aux (x + s) xs

lastSum_ :: (Eq a, Num a) => [a] -> Bool
lastSum_ x = sum x - ls == ls
  where 
    ls = last x

maxL :: [[a]] -> [[a]]
maxL xs = filter ((== maximum(map length xs)) . length) xs

map_ :: (a -> b) -> [a] -> [b]
map_ f = foldr ((:) . f) []

filter_ :: (a -> Bool) -> [a] -> [a]
filter_ f = foldr ((++) . \ x -> if f x then [x] else []) []  
