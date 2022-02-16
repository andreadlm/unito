<?php
# Script is called to write an article.
# Each article is associated to a title, description, text, category, list of tags and an image.
# The image is stored in the /img/articles folder.
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
require_once('../functions/util.php');

const QUERIES = array(
    'insertTag' => 'INSERT INTO tag (name) VALUES (:name) ON DUPLICATE KEY UPDATE name = name',
    'insertCategory' => 'INSERT INTO category (name) VALUES (:name) ON DUPLICATE KEY UPDATE name = name',
    'insertArticle' => 'INSERT INTO article (author_id, title, description, text, date, img, category_name)
                        VALUES (:author_id, :title, :description, :text, STR_TO_DATE(:date, \'%Y-%m-%d\'), :img, :category_name)',
    'insertTags' => 'INSERT INTO tags (article_id, tag_name) VALUES (:article_id, :tag_name)'
);

if(!verifyLogInRole('editor')) error('User must be editor');

if(isset($_REQUEST['title']) &&
    isset($_REQUEST['description']) &&
    isset($_REQUEST['text']) &&
    isset($_REQUEST['category']) &&
    isset($_REQUEST['tags']) &&
    isset($_FILES['img'])) {

    $title = htmlspecialchars($_REQUEST['title']); # Prevents XSS attacks
    $description = htmlspecialchars($_REQUEST['description']);
    $text = htmlspecialchars($_REQUEST['text']);
    $category = htmlspecialchars($_REQUEST['category']);
    $tags = explode(' ', htmlspecialchars($_REQUEST['tags']));
    $img = $_FILES['img'];

    $PDO = new DBConnection();

    if(!uploadImage('../../img/articles/', $img)) error('Error uploading image');

    $PDO->beginTransaction(); # Transaction start
        insertCategory($PDO, $category);
        $article_id = insertArticle($PDO, $title, $description, $text, $img['name'], $category); # Creates article
        foreach($tags as $tag) {
            insertTag($PDO, $tag); # Creates new tag
            insertTags($PDO, $article_id, $tag); # Stores association tag - article
        }
    if(!$PDO->commit()) error('Error executing query'); # Transaction end

    echo json_encode(array('status' => 'success'));
} else error('Malformed request');

function insertCategory(PDO $connection, string $name) {
    $statement = $connection->prepare(QUERIES['insertCategory']);
    $statement->bindValue(':name', $name);
    $statement->execute();
}

function insertArticle(PDO $connection, string $title, string $description, string $text, string $img, string $category): int {
    $statement = $connection->prepare(QUERIES['insertArticle']);
    $statement->bindValue(':author_id', $_SESSION['user_id'], PDO::PARAM_INT);
    $statement->bindValue(':title', $title);
    $statement->bindValue(':description', $description);
    $statement->bindValue(':text', $text);
    $statement->bindValue(':date', date('y-m-d'));
    $statement->bindValue(':img', $img);
    $statement->bindValue(':category_name', $category);
    $statement->execute();
    return intVal($connection->lastInsertId());
}

function insertTag(PDO $connection, string $name) {
    $statement = $connection->prepare(QUERIES['insertTag']);
    $statement->bindValue(':name', $name);
    $statement->execute();
}

function insertTags(PDO $connection, int $article_id, string $tag_name) {
    $statement = $connection->prepare(QUERIES['insertTags']);
    $statement->bindValue(':article_id', $article_id, PDO::PARAM_INT);
    $statement->bindValue(':tag_name', $tag_name);
    $statement->execute();
}
