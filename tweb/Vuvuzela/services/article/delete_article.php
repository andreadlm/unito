<?php
# Script is called to delete an article.
# For example the request
# {
#    article_id: 1
# }
# deletes the article with id 1.
# This call can be performed only by an editor user.
# The result is encoded in json.

if(!isset($_SERVER["REQUEST_METHOD"]) || $_SERVER["REQUEST_METHOD"] != "POST") {
    header("HTTP/1.1 400 Invalid Request");
    exit("ERROR 400: Invalid request - This service accepts only POST requests.");
}

header('Content-type: application/json');

require_once('../functions/login.php');
require_once('../functions/errors.php');
require_once('../DBConnection.php');

const DELETE_QUERY = 'DELETE FROM article WHERE article.id = :article_id';

if(!verifyLogInRole('editor')) error('User must be editor');

if(isset($_REQUEST['article_id'])) {
    $article_id = $_REQUEST['article_id'];

    $connection = new DBConnection();

    $statement = $connection->prepare(DELETE_QUERY);
    $statement->bindValue(':article_id', $article_id, PDO::PARAM_INT);
    if(!$statement->execute()) error('Error executing query');

    echo json_encode(array('status' => 'success'));

} else error('Malformed request');
