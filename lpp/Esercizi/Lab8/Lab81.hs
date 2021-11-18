import Data.Char (ord, chr)

showHex :: Int -> String
showHex n | n < 0           = '-' : showHex (negate n)
          | n < 16          = [singleHex (mod n 16)]
          | otherwise       = showHex (n `div` 16) ++ [singleHex (n `mod` 16)]
  where
    singleHex n | n < 10    = chr (ord '0' + n)
                | n >= 10   = chr (ord 'A'  + n)
                | otherwise = ' '

singleInt :: Char -> Int
singleInt h | ord h <= ord '9'  = ord h - ord '0'
            | ord h <= ord 'F'  = ord h - ord 'A' + 10
            | ord h <= ord 'f'  = ord h - ord 'a' + 10
            | otherwise = 0

readHex :: String -> Int
readHex = aux 0
  where
    aux res (h : hs) = aux (16 * res + singleInt h) hs
    aux res []       = res

readHex_ :: String -> Int
readHex_ = foldl (\ sum h -> singleInt h + 16 * sum) 0