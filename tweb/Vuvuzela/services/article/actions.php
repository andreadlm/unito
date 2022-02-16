<?php
# Script is called tho perform an action on a specific article.
# For example the request
# {
#    action: 'add',
#    type: 'favourite',
#    article: 1
# }
# adds the article with id 1 to the favourites list of the current user.
# The result is encoded in json.

if(!isset($_SERVER["REQUEST_METHOD"]) || $_SERVER["REQUEST_METHOD"] != "POST") {
    header("HTTP/1.1 400 Invalid Request");
    exit("ERROR 400: Invalid request - This service accepts only POST requests.");
}

header('Content-type: application/json');

require_once('../DBConnection.php');
require_once('../functions/errors.php');
require_once('../functions/login.php');

const ACTIONS = array('add', 'remove');
const TYPES = array('favourite', 'read_later', 'noise');

if(!verifyLogIn())
    error('User must be log in');

if(isset($_REQUEST['type']) &&
    isset($_REQUEST['article']) &&
    isset($_REQUEST['action'])) {

    $type = $_REQUEST['type'];
    $article = $_REQUEST['article'];
    $action = $_REQUEST['action'];

    if(!in_array($action, ACTIONS)) error('Invalid action'); # Action not supported
    if(!in_array($type, TYPES)) error('Invalid type'); # Type not supported

    $connection = new DBConnection();

    $query = getQuery($type, $action);
    $statement = $connection->prepare($query);
    $statement->bindValue(':article', $article, PDO::PARAM_INT);
    $statement->bindValue(':user', $_SESSION['user_id'], PDO::PARAM_INT);
    if($statement->execute()) echo json_encode(array('status' => 'success'));
    else error('Query error');

} else error('Invalid request');

# Returns the query associated to the specified $action to be performed in the specified database $table.
# Supported actions are 'add' and 'remove', otherwise false is returned:
function getQuery(string $table, string $action): string|bool {
    return match ($action) {
        'add' => "INSERT INTO $table VALUES(:user, :article)",
        'remove' => "DELETE FROM $table WHERE $table.article_id = :article AND $table.user_id = :user",
        default => false,
    };
}
