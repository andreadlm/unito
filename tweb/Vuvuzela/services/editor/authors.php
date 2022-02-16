<?php
# Script is called to retrieve the list of most active authors.
# For example the request
# {
#    num: 3
# }
# returns the list of the 3 most active authors.
# The result is encoded in json.

if(!isset($_SERVER["REQUEST_METHOD"]) || $_SERVER["REQUEST_METHOD"] != "GET") {
    header("HTTP/1.1 400 Invalid Request");
    exit("ERROR 400: Invalid request - This service accepts only GET requests.");
}

header('Content-type: application/json');

require_once('../DBConnection.php');
require_once('../functions/errors.php');
require_once('../functions/login.php');
require_once('../functions/authors.php');

if(!verifyLogIn()) error('User must be log in');

if(isset($_REQUEST['num'])) {
    $num = $_REQUEST['num'];

    $PDO = new DBConnection();

    if(($rows = getAuthors($PDO, intval($num))) === false)
        error('Query error');

    echo json_encode(array('status' => 'success', 'data' => $rows));
}

