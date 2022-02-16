<?php
# Script is called to retrieve a number of comments under a specified article.
# For example the request
# {
#    first: 1,
#    num: 3,
#    article: 1
# }
# returns comments number (2, 4) under article with id 1.
# The result is encoded in json.

if(!isset($_SERVER["REQUEST_METHOD"]) || $_SERVER["REQUEST_METHOD"] != "GET") {
    header("HTTP/1.1 400 Invalid Request");
    exit("ERROR 400: Invalid request - This service accepts only GET requests.");
}

header('Content-type: application/json');

require_once('../DBConnection.php');
require_once('../functions/errors.php');
require_once('../functions/login.php');

const MARIA_DB_MAX_INT = 4294967295;

# Selects username, date, text and id of all the comments numbered (:first - num: :first - 1)
const QUERY = 'SELECT user.username AS author, DATE_FORMAT(date, \'%M %d, %Y\') AS date, text, comment.id AS id
              FROM comment
                  JOIN user ON comment.author_id = user.id
              WHERE article_id = :article && comment.id < :first
              ORDER BY comment.id DESC
              LIMIT :num ;';

if(!verifyLogIn()) error('User must be log in');

if(isset($_REQUEST['first']) &&
    isset($_REQUEST['num']) &&
    isset($_REQUEST['article'])) {

    $first = $_REQUEST['first'];
    $num = $_REQUEST['num'];
    $article = $_REQUEST['article'];

    $connection = new DBConnection();
    if($first === '-1') $first = MARIA_DB_MAX_INT;

    $statement = $connection->prepare(QUERY);
    $statement->bindValue(':first', $first);
    $statement->bindValue(':num', $num, PDO::PARAM_INT);
    $statement->bindValue(':article', $article, PDO::PARAM_INT);
    if(!$statement->execute()) error('Error executing query');
    $rows = $statement->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode(array('status' => 'success', 'data' => $rows));
} else error('Malformed request');
