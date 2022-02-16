<?php
require_once('../services/functions/login.php');

if(!verifyLogInRole('editor')) redirectLogIn();
else {
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <!--
    Author: Andrea Delmastro
    Website description: online sport publication. Users can read, comment, like articles and create custom lists of
                         favourites and saved for later articles. Editor users can create, delete articles and moderate
                         comments.
    Page description: editor page of the website. This page allows editors to write new articles. The page is a simple
                      form to be filled with information about the article.
    --->
    <meta charset="UTF-8">
    <title>Vuvuzela | Editor </title>

    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/editor.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="../script/editor.js"></script>
</head>
<body>
    <section id="main">
<?php require('header.html') ?>
        <div class="content">
            <form>
                <div id="form-title">
                    <h2>Write new article</h2>
                    <button id="back-button" class="button-fill">⬅️ Back</button>
                </div>
                <label id="title-label" for="title">Title:</label>
                <input id="title" class="input-field-text" type="text" autocomplete="off" required>
                <label id="description-label" for="description">Description:</label>
                <input id="description" class="input-field-text" type="text" autocomplete="off" required>
                <label id="text-label" for="text">Text:</label>
                <textarea id="text" class="input-field-text" rows="10" autocomplete="off" required></textarea>
                <label id="img-label" for="img">Image:</label>
                <input id="img" type="file" name="avatar" accept="image/png, image/jpeg" required>
                <label id="category-label" for="category">Category:</label>
                <input id="category" class="input-field-text" type="text" autocomplete="off" required>
                <label id="tags-label" for="tags">Tags:</label>
                <input id="tags" class="input-field-text" type="text" autocomplete="off" placeholder="Required format: #tag1 #tag2 #tag3 ..."required>
                <button id="upload-button" class="button-fill" type="button" disabled>Upload</button>
            </form>
        </div>
<?php
require('footer.html');
}
?>