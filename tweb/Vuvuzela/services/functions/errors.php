<?php
# Sends a json response to the client specifying the error condition.
# Pay attention: this is a terminal operation, the execution of the script ends
# after this call.
function error(string $errorMsg) {
    echo json_encode(array('status' => 'error', 'errorMsg' => $errorMsg));
    exit(-1);
}
