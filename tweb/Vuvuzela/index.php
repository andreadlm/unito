<?php
require_once('services/functions/login.php');
if(verifyLogIn())
    header('Location: /Vuvuzela/pages/home.php');
else
    header('Location: /Vuvuzela/pages/login.html');
