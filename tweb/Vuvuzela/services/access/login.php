<?php
# Script is called to log in/sign in a user.
# For example, the request
# {
#      username: andrea
#      password: drowssap
#      editor: true
#      type: TYPE_LOG_IN
# }
# verifies the credentials and returns success if user is registered, error otherwise.
# Different types of errors are defined to handle different situations.

if(!isset($_SERVER["REQUEST_METHOD"]) || $_SERVER["REQUEST_METHOD"] != "POST") {
    header("HTTP/1.1 400 Invalid Request");
    exit("ERROR 400: Invalid request - This service accepts only POST requests.");
}

header('Content-type: application/json');

require_once('../DBConnection.php');

const TYPE_LOG_IN = 0;
const TYPE_SIGN_UP = 1;

const MSG_OK = 0;
const MSG_WRONG_CREDENTIALS = 1; // Username, password and role combination not recognised
const MSG_USER_ALREADY_EXISTS = 2;
const MSG_GENERIC_ERROR = 3;

if(isset($_REQUEST['username']) &&
    isset($_REQUEST['password']) &&
    isset($_REQUEST['editor']) &&
    isset($_REQUEST['type'])) {

    $username = $_REQUEST['username'];
    $password = $_REQUEST['password'];
    if($_REQUEST['editor'] === 'true') $role = 'editor';
    else $role = 'reader';
    $type = intval($_REQUEST['type']);

    $connection = new DBConnection();

    $message = match($type) {
        TYPE_LOG_IN => verifyCredentials($connection, $username, $password, $role),
        TYPE_SIGN_UP => createUser($connection, $username, $password, $role),
        default => MSG_GENERIC_ERROR
    };

    if($message === MSG_OK) {
        session_start();
        session_regenerate_id();
        $_SESSION['user_id'] = getUserId($connection, $username);
        $_SESSION['username'] = $username;
        $_SESSION['role'] = $role;
    }

    echo json_encode($message);
}

function verifyCredentials(PDO $pdo, string $username, string $password, string $role): int {
    $query = 'SELECT user.password
              FROM user
              WHERE user.username = :username AND
                    user.role = :role';
    try {
        $statement = $pdo->prepare($query);
        $statement->execute(array(':username' => $username, ':role' => $role));
        $rows = $statement->fetchAll(PDO::FETCH_ASSOC);

        if(isset($rows[0]['password'])) {
            $passwordHash = $rows[0]['password']; # Password is stored in db as hash
            $passwordCorrect = password_verify($password, $passwordHash);
            if($passwordCorrect) $message = MSG_OK;
            else $message = MSG_WRONG_CREDENTIALS;
        } else {
            $message = MSG_WRONG_CREDENTIALS;
        }
    } catch(PDOException) {
        $message = MSG_GENERIC_ERROR;
    } finally {
        return $message;
    }
}

function createUser(PDO $pdo, string $username, string $password, string $role): int {
    $query = 'INSERT INTO user (username, password, role) 
              VALUES (:username, :password, :role)';
    try {
        $statement = $pdo->prepare($query);
        $statement->execute(array(
            ':username' => $username,
            ':password' => password_hash($password, PASSWORD_DEFAULT), # Password is stored in db as hash
            ':role' => $role));
        $message = MSG_OK;
    } catch(PDOException $PDOException) {
        if($PDOException->getCode() == 23000) $message = MSG_USER_ALREADY_EXISTS; # Duplicated unique field error code
        else $message = MSG_GENERIC_ERROR;
    } finally {
        return $message;
    }
}

function getUserId(PDO $pdo, string $username): int {
    $query = 'SELECT user.id FROM user WHERE user.username = :username';
    try {
        $statement = $pdo->prepare($query);
        $statement->execute(array(':username' => $username));
        $row = $statement->fetch(PDO::FETCH_ASSOC);
        return intVal($row['id']);
    } catch(PDOException) {
        return -1;
    }
}
