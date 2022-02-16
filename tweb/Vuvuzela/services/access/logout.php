<?php
# Script to free all the session variables associated to the current user.
# User is then redirected to the login page.

if(session_start()) {
    unset($_SESSION['user_id']);
    unset($_SESSION['username']);
    unset($_SESSION['role']);

    header('Location: /Vuvuzela/pages/login.html');
}
