<?php
header('Content-type: application/json');

$dbConnectionString = 'mysql:dbname=imdb;host=localhost:3306';
$dbUser = 'root';
$dbPassword = 'password';

try {
    $db = new PDO($dbConnectionString, $dbUser, $dbPassword);
} catch(PDOException $PDOe) {
    error('Unable to connect to the database');
}

if($_SERVER['REQUEST_METHOD'] != 'POST') {
    error('Request method must be POST');
} else {
    if(!isset($_POST['first_name'])) error('first_name not set');
    else $firstName = $_POST['first_name'];
    if(!isset($_POST['last_name'])) error('last_name not set');
    else $lastName = $_POST['last_name'];
    if(!isset($_POST['all'])) error('all not set');
    else $allFlag = $_POST['all'];

    $allQuery = 'SELECT movies.name,
                        movies.year
                 FROM roles
                     JOIN actors ON roles.actor_id = actors.id
                     JOIN movies ON roles.movie_id = movies.id
                 WHERE actors.id = :id
                 ORDER BY movies.year DESC, 
                          movies.name ASC';

    $withActorQuery = 'WITH common_movies AS (    
                               SELECT roles.movie_id
                               FROM roles
                               WHERE roles.actor_id = :id_2
                          INTERSECT
                               SELECT roles.movie_id
                               FROM roles
                               WHERE roles.actor_id = :id_1
                      )
                      SELECT movies.name, movies.year
                      FROM movies 
                          JOIN common_movies ON movies.id = common_movies.movie_id';

    try {
        if ($allFlag == 'true') {
            $query = $db->prepare($allQuery);
            $id = getActorID($firstName, $lastName);
            if($id != null)
                $query->execute(array(':id' => $id));
            else
                error('Actor does not exist');
        } else {
            $query = $db->prepare($withActorQuery);
            $id_2 = getActorID($firstName, $lastName);
            if($id_2 != null)
                $query->execute(array(':id_1' => getActorID('Massimo', 'Boldi'),
                                      ':id_2' => $id_2));
            else
                error('Actor does not exist');
        }

        $rows = $query->fetchAll(PDO::FETCH_ASSOC);
    } catch(PDOException $PDOe) {
        error('Unable to retrieve data from database');
    }

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

function error($error) {
    echo json_encode(array('error' => $error));
    exit(-1);
}