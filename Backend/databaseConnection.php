<?php

/**
 * User: Yannick Winter
 * Date: 14.10.15
 * Time: 13:22
 */
class databaseConnection {
    private $serverName;
    private $userName;
    private $password;
    private $conn;

    public function init($pw){
        $this->serverName = "localhost";
        $this->userName = "username";
        $this->password = $pw;

    }
    public function connect() {
        // Create connection
        $this->conn = new mysqli($this->serverName, $this->userName, $this->password);

        // Check connection
        if ($this->conn->connect_error) {
            die("Connection failed: " . $this->conn->connect_error);
        }
        echo "Connected successfully";
    }

    public function __toString()
    {
        return "";
    }

}