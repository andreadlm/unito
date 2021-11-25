<?php
header('Content-type: application/json');

$dbConnectionString = 'mysql:dbname=imdb;host=localhost:3306';
$dbUser = 'root';
$dbPassword = 'password';

try {
    $db = new PDO($dbConnectionString, $dbUser, $dbPassword);
} catch(PDOException $PDOe) {
    error('Unable to connect to the database', $PDOe);
}

if($_SERVER['REQUEST_METHOD'] != 'GET') {
    error('Request method must be GET', '');
} else {
    $firstName = $_GET['first_name'];
    $lastName = $_GET['last_name'];
    $allFlag = $_GET['all'];

    $allQuery = 'SELECT movies.name,
                        movies.year
                 FROM roles
                     JOIN actors ON roles.actor_id = actors.id
                     JOIN movies ON roles.movie_id = movies.id
                 WHERE actors.id = :id
                 ORDER BY movies.year DESC, 
                          movies.name ASC';

    $withActorQuery = 'SELECT movies.name,
                              movies.year
                       FROM (actors actors1,
                             actors actors2)
                           JOIN roles roles1 ON actors1.id = roles1.actor_id
                           JOIN roles roles2 ON actors2.id = roles2.actor_id
                           JOIN movies ON roles1.movie_id = movies.id
                       WHERE actors1.id != actors2.id AND
                             roles1.movie_id = roles2.movie_id AND
                             actors1.id = :id_1 AND
                             actors2.id = :id_2
                       ORDER BY movies.year DESC, 
                                movies.name ASC';

    try {
        if ($allFlag == 'true') {
            $query = $db->prepare($allQuery);
            $id = getActorID($firstName, $lastName);
            if($id != null)
                $query->execute(array(':id' => $id));
            else
                error('Actor does not exist', '');
        } else {
            $query = $db->prepare($withActorQuery);
            $id_2 = getActorID($firstName, $lastName);
            if($id_2 != null)
                $query->execute(array(':id_1' => getActorID('Massimo', 'Boldi'),
                                      ':id_2' => $id_2));
            else
                error('Actor does not exist', '');
        }

        $rows = $query->fetchAll(PDO::FETCH_ASSOC);
    } catch(PDOException $PDOe) {
        error('Unable to retrieve data from database', $PDOe);
    }

    echo getActorID($firstName, $lastName);
    echo json_encode($rows);
}

function getActorID($firstName, $lastName) {
    global $db;
    $query = $db->prepare('SELECT actors.id,
                                  COUNT(*) AS moviesCount
                           FROM actors
                               JOIN roles ON actors.id = roles.actor_id
                           WHERE actors.first_name LIKE CONCAT(:first_name, \'%\') AND
                                 actors.last_name = :last_name
                           GROUP BY actors.id
                           ORDER BY moviesCount DESC, actors.id
                           LIMIT 1');
    $query->execute(array(':first_name' => $firstName, ':last_name' => $lastName));
    $idRow = $query->fetch(PDO::FETCH_ASSOC);
    return $idRow != false ? $idRow['id'] : null;
}

function error($message, $error) {
    echo json_encode(array('message' => $message,
                           'error' => $error));
    exit(-1);
}