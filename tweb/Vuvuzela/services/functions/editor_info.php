<?php
const EDITOR_INFO_QUERIES = array(
    # Selects name and image the editor associated to the specified id.
    'get' => 'SELECT coalesce(complete_name, username) as name, coalesce(img, \'placeholder.png\') as img
               FROM user
               WHERE user.id = :id AND
                     user.role = \'editor\'',
    # Sets a new image path for the editor associated to the specified id.
    'set' => 'UPDATE user
              SET user.img = :img
              WHERE user.id = :id AND
                    user.role = \'editor\''
);

# Sets a new image path for the current user.
# Current user must be an editor to perform this action.
function setImage(PDO $connection, string $img): bool {
    if($_SESSION['role'] !== 'editor') return false;
    $statement = $connection->prepare(EDITOR_INFO_QUERIES['set']);
    $statement->bindValue(':img', $img);
    $statement->bindValue('id', $_SESSION['user_id']);
    return $statement->execute();
}

# Gets name and image associated to the current user.
# Current user must be an editor to perform this action.
function getEditorInfo(PDO $connection) {
    if($_SESSION['role'] !== 'editor') return false;
    $statement = $connection->prepare(EDITOR_INFO_QUERIES['get']);
    $statement->bindValue(':id', $_SESSION['user_id'], PDO::PARAM_INT);
    $statement->execute();
    $ret = $statement->fetch(PDO::FETCH_ASSOC);
    $ret['img'] = 'img/authors/' . $ret['img'];
    return $ret;
}