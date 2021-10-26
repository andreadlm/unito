<?php
# A PHP script that prints all query string parameters it was sent.
# Used to test HTML forms.

header("Content-type: text/plain");
error_reporting(15);

$url = 'http';
if (isset($_SERVER["HTTPS"])) {
	$url .= "s";
}
$url .= "://" . $_SERVER["SERVER_NAME"] . $_SERVER["SCRIPT_NAME"];
if (isset($_SERVER["QUERY_STRING"]) && $_SERVER["QUERY_STRING"]) {
	$url .= "?" . $_SERVER["QUERY_STRING"];
}
print "Request URL: $url\n";
print "Request method: {$_SERVER['REQUEST_METHOD']}\n\n";

print "\$_REQUEST: request parameters:\n";
print_r($_REQUEST);

print "\n\$_FILES: uploaded files:\n";
print_r($_FILES);

print "\n\$_COOKIE: cookies:\n";
print_r($_COOKIE);

print "\n\$_SERVER: server variables:\n";
print_r($_SERVER);

print "\n\$_ENV: environment variables:\n";
print_r($_ENV);

if (isset($_SESSION)) {
	print "\n\$_SESSION: session data:\n";
	print_r($_SESSION);
}

print "\n$_GET: GET request parameters:\n";
print_r($_GET);

print "\n$_POST: POST request parameters:\n";
print_r($_POST);
?>
