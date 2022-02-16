<?php
# Script is called to write a comment on a specified article.
# For example request
# {
#    article: 1,
#    text: 'I really love road tennis'
# }
# writes the comment 'I really love road tennis' on the article with id 1.
# The result is encoded in json.

if(!isset($_SERVER["REQUEST_METHOD"]) || $_SERVER["REQUEST_METHOD"] != "POST") {
    header("HTTP/1.1 400 Invalid Request");
    exit("ERROR 400: Invalid request - This service accepts only POST requests.");
}

header('Content-type: application/json');

require_once('../DBConnection.php');
require_once('../functions/errors.php');
require_once('../functions/login.php');

if(!verifyLogIn()) redirectLogIn();

const QUERY = 'INSERT INTO comment(text, date, verified, author_id, article_id)
               VALUES(:text, CURRENT_DATE(), false, :author, :article);';

if(isset($_REQUEST['article']) &&
    isset($_REQUEST['text'])) {

    $text = htmlspecialchars($_REQUEST['text']); // Prevents XSS attacks
    $article = $_REQUEST['article'];

    $connection = new DBConnection();

    $statement = $connection->prepare(QUERY);
    $statement->bindValue(':text', $text);
    $statement->bindValue(':author', $_SESSION['user_id'], PDO::PARAM_INT);
    $statement->bindValue(':article', $article, PDO::PARAM_INT);
    if(!$statement->execute()) error('Error executing query');

    echo json_encode(
        array('status' => 'success',
              'data' => array(
                'author' => $_SESSION['username'],
                'date' => date('Y-m-d'),
                'text' => $text,
                'id' => $connection->lastInsertId()
              )
        )
    );
} else error('Malformed request');