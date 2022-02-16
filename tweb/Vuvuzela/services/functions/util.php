<?php

const IMG_TYPES = array('jpg', 'jpeg', 'png');

function uploadImage(string $targetDir, $img): bool {
    $completePath = $targetDir . $img['name'];
    $imageFileType = strtolower(pathinfo($completePath,PATHINFO_EXTENSION));

    if(!getimagesize($img["tmp_name"])) return false;
    if(!in_array($imageFileType,IMG_TYPES)) return false;
    if(file_exists($completePath)) return true;

    move_uploaded_file($img["tmp_name"], $completePath);

    return true;
}