<?php
    // == Saves info about the user on the file-db
    include "top.html";

    if($_SERVER['REQUEST_METHOD'] == 'POST') {
        $userData = implode(',', $_POST);
        $userData = "\n".$userData;
        $ret = file_put_contents('singles.txt', $userData, FILE_APPEND);
    }
?>

<div>
    <p><strong>Thank you!</strong></p>
    <p>Welcome to NerdLuv, <?= $_POST['name']; ?></p>
    <p>Now <a href="matches.php">log in to see your matches!</a></p>
</div>

<?php include "bottom.html" ?>
