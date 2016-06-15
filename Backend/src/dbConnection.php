<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 23.05.16
 * Time: 11:35
 */
class dbConnection
{
    protected $serverName;
    protected $userName;
    protected $password;
    protected $dbname;
    protected $conn;

    protected $connected;

    protected static $instance = null;

    /**
     * @return dbConnection
     */
    public static function getInstance() {
        if (dbConnection::$instance == null) {
            dbConnection::$instance = new dbConnection();
            return dbConnection::getInstance();
        }
        return dbConnection::$instance;
    }

    /**
     * dbConnection constructor.
     */
    protected function __construct() {
        $this->serverName = "db596387337.db.1and1.com";
        $this->userName = "dbo596387337";
        $this->password = "aMy-SeM2";
        $this->dbname = "db596387337";
        $this->connected = false;
    }

    public function connect() {
        // Create connection
        $this->conn = null;
        $this->conn = new mysqli($this->serverName, $this->userName, $this->password, $this->dbname);

        // Check connection
        if ($this->conn->connect_error) {
            echo "100;";
            $this->connected = false;
        }

        return $this->connected;
    }
}