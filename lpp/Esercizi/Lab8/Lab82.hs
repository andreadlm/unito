putStrLn :: String -> IO ()
putStrLn = foldr ((>>) . putChar) (putChar '\n')

putLines :: [String] -> IO () 
putLines = foldr ((>>) . putStrLn) (return ())

getLines :: IO [String]
getLines = getLine >>= \ line -> 
           if null line then return [] 
           else getLines >>= \ lines -> return (line : lines)

getInt :: IO Int
getInt = getLine >>= return . read

somma :: IO ()
somma = getInt >>= aux 0
  where
    aux s 0 = print s
    aux s n = getInt >>= \ i -> aux (s + i) (n - 1)