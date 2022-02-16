<?php
# Selects a collection of all-time and last-month best tags, ordered for articles count.
const TAGS_QUERY = 'WITH best_tags AS
                        (SELECT tag_name, count(*) AS count
                         FROM tags
                             JOIN article ON tags.article_id = article.id
                         GROUP BY tag_name
                             UNION
                         SELECT tag_name, count(*) AS count
                         FROM tags
                             JOIN article ON tags.article_id = article.id
                         WHERE DATEDIFF(CURDATE(), article.date) <= 30)
                    SELECT DISTINCT tag_name
                    FROM best_tags
                    ORDER BY count DESC
                    LIMIT :num ;';

# Returns an array containing a collection of all-time and last-month best tags, ordered for articles count.
# On error, returns false.
function getTags(PDO $connection, int $num): bool|array {
    $statement = $connection->prepare(TAGS_QUERY);
    $statement->bindValue(':num', $num, PDO::PARAM_INT);
    $statement->execute();
    return $statement->fetchAll(PDO::FETCH_ASSOC);
}
