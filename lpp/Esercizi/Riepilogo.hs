data Tree a = Empty | Node a [Tree a]
  deriving Show

union :: Ord a => [a] -> [a] -> [a]
union xs [] = xs
union [] ys = ys
union (x : xs) (y : ys) | x == y    = x : union xs ys
                        | x < y     = x : union xs (y : ys)
                        | otherwise = y : union (x : xs) ys

intersection :: Ord a => [a] -> [a] -> [a]
intersection _ [] = []
intersection [] _ = []
intersection (x : xs) (y : ys) | x < y     = intersection  xs (y : ys)
                               | y < x     = intersection (x : xs) ys
                               | otherwise = x : intersection xs ys

difference :: Ord a => [a] -> [a] -> [a]
difference [] _  = []
difference xs [] = xs
difference (x : xs) (y : ys) | y < x = difference (x : xs) ys
                             | x < y = x : difference xs (y : ys)
                             | otherwise = difference xs ys

listap :: [a] -> [a]
listap = map snd . filter (even . fst) . zip [0..]

inversioni :: Ord a => [a] -> Int
inversioni xs = length (filter (uncurry (>)) (zip xs (tail xs)))

elements :: Tree a -> [a]
elements Empty       = []
elements (Node n cl) = n : foldr (++) [] (map elements cl)

normalize :: Tree a -> Tree a
normalize (Node n [Empty]) = Node n []
normalize (Node n cl)      = Node n (map normalize cl)
normalize Empty            = Empty


ultimop :: Integral a => [a] -> Maybe a
ultimop xs = case filter even $ reverse xs of
              []      -> Nothing
              (x : _) -> Just x

diverso :: Eq a => [a] -> [a] -> Bool
diverso xs ys = foldr aux False xs
  where
    aux _ True = True
    aux x _    = all (/= x) ys

diverso_ :: Eq a => [a] -> [a] -> Bool
diverso_ xs ys = any (\ x -> all (/= x) ys) xs

{-
listapᵣ :: [a] -> [a]
listapᵣ = aux 0
  where
    aux _ [] = []
    aux i (x : xs) | mod i 2 == 0 = x : aux (i + 1) xs
    aux i (_ : xs) = aux (i + 1) xs
-}

listapᵣ :: [a] -> [a]
listapᵣ []           = []
listapᵣ (x : _ : xs) = x : listapᵣ xs
listapᵣ (_ : xs)     = listapᵣ xs


