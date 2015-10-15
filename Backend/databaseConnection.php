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

    private $connected;

    /**
     * @return mixed
     */
    public function getConnected()
    {
        return $this->connected;
    }

    /**
     * databaseConnection constructor.
     */
    public function __construct() {
        $this->serverName = "db596387337.db.1and1.com";
        $this->userName = "dbo596387337";
        $this->password = "aMy-SeM2";

        $this->connected = false;
    }

    public function connect() {
        if (!$this->connected) {
            $this->connected = true;
            // Create connection
            $this->conn = new mysqli($this->serverName, $this->userName, $this->password);

            // Check connection
            if ($this->conn->connect_error) {
                $this->connected = false;
            }
        }
        return $this->connected;
    }

    public function __toString() {
        return "";
    }

}