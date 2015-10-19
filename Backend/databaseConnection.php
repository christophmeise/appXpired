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
        return $this->testConnection();
    }

    /**
     * @return bool
     * test connection in constructor
     */
    private function testConnection() {
        $this->connect();
        $this->conn->close();
        return $this->connected;

    }

    /**
     * @return bool
     * connect to mysql database;
     */

    private function connect() {
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

    /**
     * @param $userid
     * @param $password
     * @return bool
     *
     * Check User Authorization
     *
     */
    private function checkAuthorization($userid,$password) {

        $this->connect();

        $sql = "SELECT * FROM User WHERE id = " . $userid . "AND password = " . $password;
        $result = $this->conn->query($sql);
        $user = [];
        if ($result->num_rows > 0) {
            // output data of each row
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param $table
     * @param $fields
     * @param $values
     * @return bool
     *
     * Insert the $values into $fields after checking the user credentials
     */

    public function insert($table, $fields,$values,$userid,$password) {

        if ($this->checkAuthorization($userid,$password)) { // check if the user is authorized
            $this->connect();
            //TODO: Check authorization for ever user (e.g. userHousehold)

            $sql = "INSERT INTO" . $table ." ( ";
            $i = 1;
            foreach ($fields as $field) {
                $sql .= $field;
                $i++;
                if ($i != sizeof($fields)) {
                    $sql .= ", ";
                }
            }
            $sql .= ") VALUES (";
            $i = 1;
            foreach ($values as $value) {
                $sql .= "'" . $value . "'";
                $i++;
                if ($i != sizeof($values)) {
                    $sql .= ", ";
                }
            }
            $sql .= ")";

            if ($this->conn->query($sql) === TRUE) {
                return true;
            } else {
                return false;
            }
            $this->conn->close();
        }
        else {
            return false;
        }
    }
    //TODO: get, delete update

    public function __toString() {
        return "";
    }

}