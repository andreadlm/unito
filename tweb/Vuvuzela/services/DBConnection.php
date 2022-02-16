<?php

# Simple PDO class with pre-defined connection string, username and password
class DBConnection extends PDO {
    private const DB_CONNECTION_STRING = 'mysql:dbname=vuvuzela;host=localhost:3306';
    private const DB_USER = 'root';
    private const DB_PASSWORD = '';

    public function __construct() {
        parent::__construct(self::DB_CONNECTION_STRING, self::DB_USER, self::DB_PASSWORD);
    }
}