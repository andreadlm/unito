-- Es1
maybeLength :: Maybe a -> Int
maybeLength Nothing  = 0
maybeLength (Just _) = 1

maybeMap :: (a -> b) -> Maybe a -> Maybe b
maybeMap _ Nothing  = Nothing 
maybeMap f (Just x) = Just (f x)

maybeFilter :: (a -> Bool) -> Maybe a -> Maybe a
maybeFilter f (Just x) | f x = Just x
maybeFilter _ _              = Nothing

-- Es2
somma :: Either Int Float -> Either Int Float -> Either Int Float
somma (Left i) (Right f)      = Right (fromIntegral i + f)
somma (Right f) (Left i)      = Right (f + fromIntegral i)
somma (Left i_1) (Left i_2)   = Left (i_1 + i_2)
somma (Right f_1) (Right f_2) = Right (f_1 + f_2)

-- Es3
data List a = Nil | Cons a (List a)

length_ :: List a -> Int
length_ Nil           = 0
length_ (Cons _ tail) = 1 + length_ tail

-- Es4
data Stream a = Head a (Stream a)

forever :: a -> Stream a
forever x = Head x (forever x)

from :: Enum a => a -> Stream a
from n = Head n (from (succ n))

take_ :: Int -> Stream a -> [a]
take_ 0 _           = []
take_ n (Head x xs) = x : take_ (n - 1) xs