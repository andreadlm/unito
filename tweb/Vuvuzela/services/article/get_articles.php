<?php
# Script is called to retrieve a number of articles from the archive.
# For example the request
# {
#    type: 'tag',
#    from: 0,
#    num: 3,
#    tag: 'ClayPigeonShooting'
# }
# returns the articles in the range [1, 3] tagged with #ClayPigeonShooting.
# The result is encoded in json.

if(!isset($_SERVER["REQUEST_METHOD"]) || $_SERVER["REQUEST_METHOD"] != "GET") {
    header("HTTP/1.1 400 Invalid Request");
    exit("ERROR 400: Invalid request - This service accepts only GET requests.");
}

header('Content-type: application/json');

require_once('../DBConnection.php');
require_once('../functions/login.php');
require_once('../functions/errors.php');
require_once('../functions/get_articles.php');

const TYPES = array('newest', 'noisiest', 'read-later', 'favourites', 'tag', 'keyword');

if(!verifyLogIn()) error('user must be log in');

if(!isset($_REQUEST['type']) || !isset($_REQUEST['from']) || !isset($_REQUEST['num'])) error('Malformed request');
if($_REQUEST['type'] === 'tag' && !isset($_REQUEST['tag'])) error('Malformed request: tag must be specified');
if($_REQUEST['type'] === 'keyword' && !isset($_REQUEST['keyword'])) error('Malformed request: keyword must be specified');
if(!in_array($_REQUEST['type'], TYPES)) error('Unsupported type');

$type = $_REQUEST['type'];
$from = $_REQUEST['from'];
$num = $_REQUEST['num'];
$tag = $_REQUEST['tag'] ?? '';
$keyword = trim($_REQUEST['keyword'] ?? '');

$connection = new DBConnection();

$rows = match($type) {
    'newest' => getNewest($connection, $from, intval($num)),
    'noisiest' => getNoisiest($connection, $from, intval($num)),
    'favourites' => getFavourites($connection, $from, intval($num)),
    'tag' => getTag($connection, $from, $num, $tag),
    'read-later' => getReadLater($connection, $from, intval($num)),
    'keyword' => getKeyword($connection, $from, $num, $keyword)
};

if($rows === false) error('Error executing query');

echo json_encode(array('status' => 'success', 'data' => $rows), JSON_NUMERIC_CHECK);