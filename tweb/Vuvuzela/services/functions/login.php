<?php
# Checks whether a user is logged in the current session.
function verifyLogIn(): bool {
    if(session_status() == PHP_SESSION_NONE)
        session_start();

    if(!(isset($_SESSION['username']) && isset($_SESSION['user_id']) && isset($_SESSION['role'])))
        return false;
    return true;
}

# Checks whether a user with a specified role is logged in the current session.
function verifyLogInRole(string $role): bool {
    return verifyLogIn() && $_SESSION['role'] == $role;
}

# Redirects to the login page. User, if logged, is logged out.
function redirectLogIn() {
    header('Location: /Vuvuzela/services/access/logout.php');
    exit;
}

# Returns the current user role
function getUserRole(): bool|string {
    if(verifyLogIn())
        return $_SESSION['role'];
    else
        return false;
}
