<?php
# Script is calle to retrieve the text associated to a specified article.
# For example the request
# {
#    article_id: 1
# }
# returns the text associated to the article with id 1.
# The result is encoded in json.

if(!isset($_SERVER["REQUEST_METHOD"]) || $_SERVER["REQUEST_METHOD"] != "GET") {
    header("HTTP/1.1 400 Invalid Request");
    exit("ERROR 400: Invalid request - This service accepts only GET requests.");
}

header('Content-type: application/json');

require_once('../DBConnection.php');
require_once('../functions/login.php');
require_once('../functions/errors.php');

# Select the text associated to the specified article.
const TEXT_QUERY = 'SELECT article.text
                    FROM article
                    WHERE article.id = :article_id';

if(!verifyLogIn()) error('user must be log in');

if(isset($_REQUEST['article_id'])) {
    $article_id = $_REQUEST['article_id'];

    $connection = new DBConnection();

    $statement = $connection->prepare(TEXT_QUERY);
    $statement->bindValue(':article_id', $article_id, PDO::PARAM_INT);
    if(!$statement->execute()) error('Error executing query');

    echo json_encode(array('status' => 'success', 'data' => $statement->fetch(PDO::FETCH_ASSOC)));
} else error('Malformed request');