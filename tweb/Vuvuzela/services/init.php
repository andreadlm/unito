<?php
# Script is called to initialize site data.
# This is a convenience script: it reduces HTTP requests by grouping multiple initializations
# actions in a single script call.
# The result is encoded in json.

if(!isset($_SERVER["REQUEST_METHOD"]) || $_SERVER["REQUEST_METHOD"] != "GET") {
    header("HTTP/1.1 400 Invalid Request");
    exit("ERROR 400: Invalid request - This service accepts only GET requests.");
}

header('Content-type: application/json');

require_once('functions/login.php');
require_once('functions/errors.php');
require_once('functions/util.php');
require_once('functions/get_articles.php');
require_once('functions/tags.php');
require_once('functions/authors.php');
require_once('functions/editor_info.php');
require_once('DBConnection.php');

if(!verifyLogIn()) error('User must be editor');

$PDO = new DBConnection();

$ret = array(
    'newestArticles' => getNewest($PDO, 0, 3, '%'),
    'noisiestArticles' => getNoisiest($PDO, 0, 5, '%'),
    'tags' => getTags($PDO, 10),
    'authors' => getAuthors($PDO, 5),
    'role' => getUserRole()
);

if(in_array(false, $ret)) error('Error executing query');

if(getUserRole() === 'editor')
    $ret['editor_info'] = getEditorInfo($PDO);

echo json_encode(array('status' => 'success', 'data' => $ret), JSON_NUMERIC_CHECK);