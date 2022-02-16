<?php
# Script is called to retrieve information about the current editor user or to update the editor image.
# For example the request
# {
#    type: 'get'
# }
# returns name and image associated to the current editor user.
# This call can be performed only by an editor user.
# The result is encoded in json.

if(!isset($_SERVER["REQUEST_METHOD"]) || $_SERVER["REQUEST_METHOD"] != "POST") {
    header("HTTP/1.1 400 Invalid Request");
    exit("ERROR 400: Invalid request - This service accepts only POST requests.");
}

header('Content-type: application/json');

require_once('../functions/login.php');
require_once('../functions/errors.php');
require_once('../functions/editor_info.php');
require_once('../DBConnection.php');
require_once('../functions/util.php');

const TYPES = array('get', 'set');

if(!verifyLogInRole('editor')) error('User must be editor');

if(isset($_REQUEST['type']) &&
    ($_REQUEST['type'] === 'get' || ($_REQUEST['type'] === 'set' && isset($_FILES['img'])))) {

    $type = $_REQUEST['type'];

    if(!in_array($type, TYPES)) error('Invalid type'); # Type not supported

    $PDO = new DBConnection();

    if($type == 'set') {
        $img = $_FILES['img'];
        uploadImage('../../img/authors/', $img);
        if(!setImage($PDO, $img['name'])) error('Error executing query');
    }

    $res = getEditorInfo($PDO);
    echo json_encode(array('status' => 'success', 'data' => $res));

} else error('Malformed request');

