<?php
# Selects a randomly ordered list of the :num authors that have written the most
# articles and have set the profile picture
const AUTHOR_QUERY = 'SELECT * 
                      FROM (
                          SELECT coalesce(user.complete_name, user.username) as name, user.img
                          FROM user
                              JOIN article ON user.id = article.author_id
                          WHERE user.img IS NOT NULL
                          GROUP BY user.id, user.complete_name, user.img
                          ORDER BY COUNT(DISTINCT article.id) DESC
                          LIMIT :num
                      ) AS cni 
                      ORDER BY rand()';

# Returns an array of the data associated to the $num most active authors,
# randomly ordered.
# On error, returns false.
function getAuthors(PDO $connection, int $num): bool|array {
    $statement = $connection->prepare(AUTHOR_QUERY);
    $statement->bindValue(':num', $num, PDO::PARAM_INT);
    $statement->execute();
    return $statement->fetchAll(PDO::FETCH_ASSOC);
}
