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
    Page description: moderate page of the website. This page allows editors to moderate comments. It is implemented as
                      a simple list of comments that can be accepted or rejected. Rejected comments are deleted from the
                      system. Only comments that haven't been moderated yet are listed.
    --->
    <meta charset="UTF-8">
    <title>Vuvuzela | Moderate </title>

    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/moderate.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="../script/moderate.js"></script>
</head>
<body>
<section id="main">
<?php require('header.html') ?>
    <div class="content">
        <div id="moderate-title">
            <h2>Moderate comments</h2>
            <button id="back-button" class="button-fill">⬅️ Back</button>
        </div>
        <div id="comments-list">
        </div>
        <button id="load-more-button" type="button">+</button>
        <div id="list-end">
            <img src="../img/whistle.png" alt="whistle">
            <p>End of the match</p>
        </div>
    </div>
<?php
require('footer.html');
}
?>
