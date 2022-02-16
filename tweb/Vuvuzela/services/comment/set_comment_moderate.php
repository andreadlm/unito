<?php
if(!isset($_SERVER["REQUEST_METHOD"]) || $_SERVER["REQUEST_METHOD"] != "POST") {
    header("HTTP/1.1 400 Invalid Request");
    exit("ERROR 400: Invalid request - This service accepts only GET requests.");
}

header('Content-type: application/json');

require_once('../DBConnection.php');
require_once('../functions/errors.php');
require_once('../functions/login.php');

# Selects username, date, text and id of all the comments numbered (:first - num: :first - 1)
const SET_MODERATE_QUERIES = array(
    'accept' => 'UPDATE comment SET comment.verified = 1 WHERE comment.id = :id',
    'reject' => 'DELETE FROM comment WHERE comment.id = :id'
);

const TYPES = array('accept', 'reject');

if(!verifyLogInRole('editor')) error('User must be log in');

if(isset($_REQUEST['comment_id']) &&
    isset($_REQUEST['type'])) {

    $comment_id = $_REQUEST['comment_id'];
    $type = $_REQUEST['type'];

    if(!in_array($type, TYPES)) error('Invalid type'); # Type not supported

    $connection = new DBConnection();

    $query = SET_MODERATE_QUERIES[$type];
    $statement = $connection->prepare($query);
    $statement->bindValue(':id', $comment_id, PDO::PARAM_INT);
    if(!$statement->execute()) error('Error executing query');

    echo json_encode(array('status' => 'success'));
} else error('Malformed request');
