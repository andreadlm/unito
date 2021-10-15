insertionSort :: [Int] -> [Int]
insertionSort []       = []
insertionSort (x : xs) = insert x (insertionSort xs)
  where 
    insert x []                   = [x]
    insert x (y : ys) | x <= y    = x : y : ys
                      | otherwise = y : insert x ys

split :: [Int] -> ([Int], [Int])
split [] = ([], [])
split [x] = ([x], [])
split (x : y : xs) = (x : ys, y : zs)
  where
    (ys, zs) = split xs

merge :: ([Int], [Int]) -> [Int]
merge ([], []) = []
merge ([], xs)  = xs
merge (xs, [])  = xs
merge (x : xs, y : ys) | x < y     = x : merge (xs, y : ys)
                       | otherwise = y : merge (x : xs, ys)

mergeSort :: [Int] -> [Int]
mergeSort [] = []
mergeSort [x] = [x]
mergeSort xs = merge (mergeSort ys, mergeSort zs)
  where
    (ys, zs) = split xs