<?php
require_once('../services/functions/login.php');

if(!verifyLogIn()) redirectLogIn();
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
    Page description: main page of the website. This page allows to navigate the articles, to filter them and to access
                      other pages.
    --->
    <meta charset="UTF-8">
    <title>Vuvuzela | Home </title>

    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/home.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="../script/home.js"></script>
</head>
<body>
    <section id="main">
<?php require('header.html') ?>
        <div id="home-content" class="content">
            <section id="articles">
                <h2 class="section-title section-title-button">
                    <span class="section-title-title">Latest articles</span>
                    <button id="back-button" class="button-fill">⬅️ Back</button>
                </h2>
                <div id="articles-big-list"></div>
                <button id="load-more-button" type="button">Load more</button>
                <div id="list-end">
                    <img src="../img/whistle.png" alt="whistle">
                    <p>End of the match</p>
                </div>
            </section>
            <aside>
                <section id="filter">
                    <h2 class="section-title">My lists</h2>
                    <div class="horizontal-list">
                        <p id="favourites-button">🌟 <span class="text-clickable">Favourites</span></p>
                        <p id="read-later-button">⏰ <span class="text-clickable">Read later</span></p>
                    </div>
                    <h2 class="section-title">Tags</h2>
                    <div id="filter-tags"></div>
                    <h2 class="section-title">Search</h2>
                    <div class="horizontal-list">
                        <label for="search-input-text">For: </label>
                        <input id="search-input-text" type="text" class="input-field-text" autocomplete="off">
                        <button id="search-input-button" class="button-fill" disabled>🔍 Search</button>
                    </div>
                </section>
                <section id="authors">
                    <h2 class="section-title">Authors</h2>
                    <div id="authors-widget">
                        <button id="button-prev" class="button-widget"> &lt;- </button>
                        <div id ="wrapper">
                            <div id="slider"></div>
                        </div>
                        <button id="button-next" class="button-widget"> -&gt; </button>
                    </div>
                </section>
                <section id="articles-small">
                    <h2 class="section-title section-title-button">
                        <span id="section-title-title"></span>
                        <button id="see-all-button" class="button-fill">👁️ See all</button>
                    </h2>
                    <div id="articles-small-list"></div>
                </section>
                <form action="../services/access/logout.php">
                    <button id="log-out-button" class="button-fill" type="submit">🚪 Log out</button>
                </form>
            </aside>
        </div>
<?php
require('footer.html');
}
?>
