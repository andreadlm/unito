<?php
const ARTICLE_QUERIES = array(
    # Selects the information associated to :num articles starting from the :from position.
    # For each article, id, title, description, author name, date, image path, category, noise count and user info
    # are retrieved. User info include data about whether the user has added the article to one of his lists or has
    # clicked the noise button on the article: 0 means false and 1 means true.
    'newest' => 'SELECT 
                     article.id, 
                     title, 
                     description, 
                     coalesce(user.complete_name, user.username) AS author, 
                     DATE_FORMAT(date, \'%M %d, %Y\') AS date, 
                     article.img, 
                     category.name AS category, 
                     count(distinct all_noise.user_id) AS noise, 
                     IF(user_noise.user_id, 1, 0) as noised,
                     IF(favourite.user_id, 1, 0) AS favourite, 
                     IF(read_later.user_id, 1, 0) AS read_later
                 FROM article
                     JOIN user ON article.author_id = user.id
                     JOIN category ON article.category_name = category.name
                     LEFT JOIN noise AS all_noise ON article.id = all_noise.article_id
                     LEFT JOIN noise AS user_noise ON (article.id = user_noise.article_id AND :user = user_noise.user_id)
                     LEFT JOIN favourite ON (article.id = favourite.article_id AND :user = favourite.user_id)
                     LEFT JOIN read_later ON (article.id = read_later.article_id AND :user = read_later.user_id)
                 GROUP BY article.id, article.date
                 ORDER BY article.date DESC, article.id DESC
                 LIMIT :num OFFSET :from ;',
    # Selects the information associated to :num articles starting from the :from position in the :user noise list.
    # For each article, id, title, description, author name, date, image path, category, noise count and user info
    # are retrieved. User info include data about whether the user has added the article to one of his lists or has
    # clicked the noise button on the article: 0 means false and 1 means true.
    'noisiest' => 'SELECT
                       article.id, 
                       title, 
                       description, 
                       coalesce(user.complete_name, user.username) AS author, 
                       DATE_FORMAT(date, \'%M %d, %Y\') AS date, 
                       article.img, 
                       category.name AS category, 
                       count(distinct all_noise.user_id) AS noise, 
                       IF(user_noise.user_id, 1, 0) as noised,
                       IF(favourite.user_id, 1, 0) AS favourite, 
                       IF(read_later.user_id, 1, 0) AS read_later
                   FROM article
                       JOIN user ON article.author_id = user.id
                       JOIN category ON article.category_name = category.name
                       LEFT JOIN noise AS all_noise ON article.id = all_noise.article_id
                       LEFT JOIN noise AS user_noise ON (article.id = user_noise.article_id AND :user = user_noise.user_id)
                       LEFT JOIN favourite ON (article.id = favourite.article_id AND :user = favourite.user_id)
                       LEFT JOIN read_later ON (article.id = read_later.article_id AND :user = read_later.user_id)
                   GROUP BY article.id, article.date
                   ORDER BY count(distinct all_noise.user_id) DESC, article.date DESC, article.id DESC
                   LIMIT :num OFFSET :from;',
    # Selects the information associated to :num articles starting from the :from position in the :user read-later list.
    # Articles are filtered on the associated tag. If '?' is passed as :tag parameter, no filtering on tag is executed.
    # For each article, id, title, description, author name, date, image path, category, noise count and user info
    # are retrieved. User info include data about whether the user has added the article to one of his lists or has
    # clicked the noise button on the article: 0 means false and 1 means true.
    'read-later' => 'SELECT
                       article.id, 
                       title, 
                       description, 
                       coalesce(user.complete_name, user.username) AS author, 
                       DATE_FORMAT(date, \'%M %d, %Y\') AS date, 
                       article.img, 
                       category.name AS category, 
                       count(distinct all_noise.user_id) AS noise, 
                       IF(user_noise.user_id, 1, 0) as noised,
                       IF(favourite.user_id, 1, 0) AS favourite, 
                       IF(read_later.user_id, 1, 0) AS read_later
                   FROM article
                       JOIN user ON article.author_id = user.id
                       JOIN category ON article.category_name = category.name
                       LEFT JOIN noise AS all_noise ON article.id = all_noise.article_id
                       LEFT JOIN noise AS user_noise ON (article.id = user_noise.article_id AND :user = user_noise.user_id)
                       LEFT JOIN favourite ON (article.id = favourite.article_id AND :user = favourite.user_id)
                       JOIN read_later ON (article.id = read_later.article_id AND :user = read_later.user_id)
                   GROUP BY article.id, article.date
                   ORDER BY article.date DESC, article.id DESC
                   LIMIT :num OFFSET :from;',
    # Selects the information associated to :num articles starting from the :from position in the :user favourites list.
    # For each article, id, title, description, author name, date, image path, category, noise count and user info
    # are retrieved. User info include data about whether the user has added the article to one of his lists or has
    # clicked the noise button on the article: 0 means false and 1 means true.
    'favourites' => 'SELECT
                       article.id, 
                       title, 
                       description, 
                       coalesce(user.complete_name, user.username) AS author, 
                       DATE_FORMAT(date, \'%M %d, %Y\') AS date, 
                       article.img, 
                       category.name AS category, 
                       count(distinct all_noise.user_id) AS noise, 
                       IF(user_noise.user_id, 1, 0) as noised,
                       IF(favourite.user_id, 1, 0) AS favourite, 
                       IF(read_later.user_id, 1, 0) AS read_later
                   FROM article
                       JOIN user ON article.author_id = user.id
                       JOIN category ON article.category_name = category.name
                       LEFT JOIN noise AS all_noise ON article.id = all_noise.article_id
                       LEFT JOIN noise AS user_noise ON (article.id = user_noise.article_id AND :user = user_noise.user_id)
                       JOIN favourite ON (article.id = favourite.article_id AND :user = favourite.user_id)
                       LEFT JOIN read_later ON (article.id = read_later.article_id AND :user = read_later.user_id)
                   GROUP BY article.id, article.date
                   ORDER BY article.date DESC, article.id DESC
                   LIMIT :num OFFSET :from;',
    # Selects the information associated to :num articles starting from the :from position in the :user favourites list.
    # Articles are filtered on the associated tag.
    # For each article, id, title, description, author name, date, image path, category, noise count and user info
    # are retrieved. User info include data about whether the user has added the article to one of his lists or has
    # clicked the noise button on the article: 0 means false and 1 means true.
    'tag' => 'SELECT 
                 article.id, 
                 title, 
                 description, 
                 coalesce(user.complete_name, user.username) AS author, 
                 DATE_FORMAT(date, \'%M %d, %Y\') AS date, 
                 article.img, 
                 category.name AS category, 
                 count(distinct all_noise.user_id) AS noise, 
                 IF(user_noise.user_id, 1, 0) as noised,
                 IF(favourite.user_id, 1, 0) AS favourite, 
                 IF(read_later.user_id, 1, 0) AS read_later
             FROM article
                 JOIN user ON article.author_id = user.id
                 JOIN category ON article.category_name = category.name
                 JOIN tags ON article.id = tags.article_id
                 LEFT JOIN noise AS all_noise ON article.id = all_noise.article_id
                 LEFT JOIN noise AS user_noise ON (article.id = user_noise.article_id AND :user = user_noise.user_id)
                 LEFT JOIN favourite ON (article.id = favourite.article_id AND :user = favourite.user_id)
                 LEFT JOIN read_later ON (article.id = read_later.article_id AND :user = read_later.user_id)
             WHERE tag_name = :tag
             GROUP BY article.id, article.date
             ORDER BY article.date DESC, article.id DESC
             LIMIT :num OFFSET :from ;',
    # Selects the information associated to :num articles starting from the :from position in the :user favourites list.
    # Articles are filtered on the on keyword in title.
    # For each article, id, title, description, author name, date, image path, category, noise count and user info
    # are retrieved. User info include data about whether the user has added the article to one of his lists or has
    # clicked the noise button on the article: 0 means false and 1 means true.
    'keyword' => 'SELECT 
                     article.id, 
                     title, 
                     description, 
                     coalesce(user.complete_name, user.username) AS author, 
                     DATE_FORMAT(date, \'%M %d, %Y\') AS date, 
                     article.img, 
                     category.name AS category, 
                     count(distinct all_noise.user_id) AS noise, 
                     IF(user_noise.user_id, 1, 0) as noised,
                     IF(favourite.user_id, 1, 0) AS favourite, 
                     IF(read_later.user_id, 1, 0) AS read_later
                 FROM article
                     JOIN user ON article.author_id = user.id
                     JOIN category ON article.category_name = category.name
                     LEFT JOIN noise AS all_noise ON article.id = all_noise.article_id
                     LEFT JOIN noise AS user_noise ON (article.id = user_noise.article_id AND :user = user_noise.user_id)
                     LEFT JOIN favourite ON (article.id = favourite.article_id AND :user = favourite.user_id)
                     LEFT JOIN read_later ON (article.id = read_later.article_id AND :user = read_later.user_id)
                 WHERE article.title LIKE :keyword
                 GROUP BY article.id, article.date
                 ORDER BY article.date DESC, article.id DESC
                 LIMIT :num OFFSET :from ;'
);

# Returns an array of all the information associated to $num articles in the current user read later
# list starting from the $from position.
# If something goes wrong, returns false.
function getReadLater(PDO $connection, int $from, int $num): bool|array {
    $statement = $connection->prepare(ARTICLE_QUERIES['read-later']);
    $statement->bindValue(':from', $from, PDO::PARAM_INT);
    $statement->bindValue(':num', $num, PDO::PARAM_INT);
    $statement->bindValue(':user', $_SESSION['user_id'], PDO::PARAM_INT);
    $statement->execute();
    return $statement->fetchAll(PDO::FETCH_ASSOC);
}

# Returns an array of all the information associated to $num articles in the current user favourites
# list starting from the $from position.
# If something goes wrong, returns false.
function getFavourites(PDO $connection, int $from, int $num): bool|array {
    $statement = $connection->prepare(ARTICLE_QUERIES['favourites']);
    $statement->bindValue(':from', $from, PDO::PARAM_INT);
    $statement->bindValue(':num', $num, PDO::PARAM_INT);
    $statement->bindValue(':user', $_SESSION['user_id'], PDO::PARAM_INT);
    $statement->execute();
    return $statement->fetchAll(PDO::FETCH_ASSOC);
}

# Returns an array of all the information associated to the $num newest articles starting from the $from position.
# If something goes wrong, returns false.
function getNewest(PDO $connection, int $from, int $num): bool|array {
    $statement = $connection->prepare(ARTICLE_QUERIES['newest']);
    $statement->bindValue(':from', $from, PDO::PARAM_INT);
    $statement->bindValue(':num', $num, PDO::PARAM_INT);
    $statement->bindValue(':user', $_SESSION['user_id'], PDO::PARAM_INT);
    $statement->execute();
    return $statement->fetchAll(PDO::FETCH_ASSOC);
}

# Returns an array of all the information associated to the $num noisiest articles starting from the $from position.
# If something goes wrong, returns false.
function getNoisiest(PDO $connection, int $from, int $num): bool|array {
    $statement = $connection->prepare(ARTICLE_QUERIES['noisiest']);
    $statement->bindValue(':from', $from, PDO::PARAM_INT);
    $statement->bindValue(':num', $num, PDO::PARAM_INT);
    $statement->bindValue(':user', $_SESSION['user_id'], PDO::PARAM_INT);
    $statement->execute();
    return $statement->fetchAll(PDO::FETCH_ASSOC);
}

# Returns an array of all the information associated to $num articles tagged as $tag starting from the $from position.
# If something goes wrong, returns false.
function getTag(PDO $connection, int $from, int $num, string $tag): bool|array {
    $statement = $connection->prepare(ARTICLE_QUERIES['tag']);
    $statement->bindValue(':from', $from, PDO::PARAM_INT);
    $statement->bindValue(':num', $num, PDO::PARAM_INT);
    $statement->bindValue(':tag', $tag);
    $statement->bindValue(':user', $_SESSION['user_id'], PDO::PARAM_INT);
    $statement->execute();
    return $statement->fetchAll(PDO::FETCH_ASSOC);
}

# Returns an array of all the information associated to $num articles that contains $keyword in title as $tag starting
# from the $from position.
# If something goes wrong, returns false.
function getKeyword(PDO $connection, int $from, int $num, string $keyword): bool|array {
    $statement = $connection->prepare(ARTICLE_QUERIES['keyword']);
    $statement->bindValue(':from', $from, PDO::PARAM_INT);
    $statement->bindValue(':num', $num, PDO::PARAM_INT);
    $statement->bindValue(':keyword', '%'.$keyword.'%');
    $statement->bindValue(':user', $_SESSION['user_id'], PDO::PARAM_INT);
    $statement->execute();
    return $statement->fetchAll(PDO::FETCH_ASSOC);
}

