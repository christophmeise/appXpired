<?php
/**
 * User: Yannick Winter
 * Date: 14.10.15
 * Time: 13:22
 */


/**
 * Class databaseConnection
 *
 * Error Codes:
 * 001 - the entered data is not valid (e.g. emailadress to short)
 * 002 - username already registered
 * 003 - emailadress already registered
 * 004 - password not valid
 *
 * 100 - mysql connection error
 * 101 - update values are incorrect
 *
 * 200 - user is not authorized to access the requested household
 *
 * 404 - unauthorized
 * 403 - token has expired
 *
 */

include('errorCodes.php');

class databaseConnection {

    private $serverName;
    private $userName;
    private $password;
    private $dbname;
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
        $this->dbname = "db596387337";
        $this->connected = false;
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
            // Create connection
            $this->conn = null;
            $this->conn = new mysqli($this->serverName, $this->userName, $this->password, $this->dbname);

            // Check connection
            if ($this->conn->connect_error) {
                echo "100;"; //TODO
                $this->connected = false;
            }

        return $this->connected;
    }

    /**
     * @param $userid
     * @param $password
     * @return bool
     *
     * check Authentication with token
     */
    public function checkAuthorizationWithToken($userid,$token) {

        $this->connect();

        $sql = "SELECT * FROM user WHERE token = '" . $token . "'"; // check if the token is in the db
        $result = $this->conn->query($sql);
        if ($result->num_rows > 0) {
            while($row = $result->fetch_assoc()) {
                if ($row["id"] == $userid ) { //check if the userid is correct
                    $today = date('Y-m-d H:i:s');
                    if ((strtotime($today)  - strtotime($row['lastLogin'])) >= (-7200)) { // check if last login is max. 2 hours (2*60*60 seconds) ago, so user can login with token
                        //update last login time so that user can maintain logged in
                        $this->updateLastLogin($userid);
                        //close the connection
                        $this->conn->close();
                        //return true because user is logged in successfully
                        return [true];
                    }
                    else { // token has expired
                        return [false, errorCodes::tokenHasExpired];
                    }

                }
                else {
                    $this->conn->close();
                    return [false, errorCodes::tokenHasExpired];
                }
            }
        } else {
            $this->conn->close();
            return [false, errorCodes::unauthorized];
        }
    }
    /**
     * @param $userid
     * @param $password
     * @return array [bool, (iftrue:)token]
     *
     * Check User Authorization with userid and password
     *
     */
    public function checkAuthorizationWithPassword($userid,$password) {
        $this->connect();
        $sql = "SELECT* FROM user WHERE id = " . $userid; //get the correct entry in the user table
        $result = $this->conn->query($sql);
        if ($result->num_rows > 0) {
            while($row = $result->fetch_assoc()) {
                if ($row["password"] == crypt($password, $row["password"]) ) { // check if the password is correct
                    //close the connection
                    $this->conn->close();
                    //update last login time so that user can maintain logged in
                    $token = $this->generateToken($userid);
                    $this->updateLastLogin($userid,$token);
                    //return true because user is logged in successfully
                    return [true,$token]; //return token too
                }
                else {

                    $this->conn->close();
                    return [false, errorCodes::unauthorized];
                }
            }
        } else {
            $this->conn->close();
            return [false, errorCodes::unauthorized];
        }
    }

    /**
     * @param $userid
     * @return array
     *
     * updates the last login of the user
     * if the user adds a token, the token will be updated too.
     *
     *
     */
    private function updateLastLogin($userid,$token = null) {
        if ($token == null) {
            $sql  = "UPDATE user SET lastLogin = '" .  date('Y-m-d H:i:s') . "' WHERE id = " . $userid;
        }
        else {
            $sql  = "UPDATE user SET lastLogin = '" .  date('Y-m-d H:i:s') . "' , token = '" . $token . "' WHERE id = " . $userid;
        }

        $this->connect();

        if ($this->conn->query($sql) === TRUE) {
            $this->conn->close();
            return true;
        } else {
            echo "Error: " . $sql . "<br>" . $this->conn->error;
            $this->conn->close();
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
    public function insert($table, $fields,$values) {

        $sql = "INSERT INTO " . $table ." (";
        $i = 0;
        foreach ($fields as $field) {
            $sql .= "`" . $field . "`";
            $i++;
            if ($i != sizeof($fields)) {
                $sql .= ", ";
            }
        }
        $sql .= ") VALUES (";
        $i = 0;
        foreach ($values as $value) {
            $sql .= "'" . $value . "'";
            $i++;
            if ($i != sizeof($values)) {
                $sql .= ", ";
            }
        }
        $sql .= ")";

        $this->connect();
        if ($this->conn->query($sql) === TRUE) {
            $last_id = $this->conn->insert_id;
            $this->conn->close();
            return [true,$last_id];
        } else {
            echo "Error: " . $sql . "<br>" . $this->conn->error;
            $this->conn->close();
            return [false];
        }

    }

    /**
     * @param $table
     * @param $selectFields
     * @param $whereFields
     * @param $values
     * @return array
     *
     * !!important!! DO A SECURITY CHECK BEFORE! !!important!!
     *
     * This method returns the requested data. Call the method like this:
     *
     * get("myTable",["*"],["id"],["42"]) //case sensitive so name it like the database columns
     *
     * The array of the selected data will be returned.
     *
     */

    public function get($table, $selectFields, $whereFields, $values) {

        if (count($whereFields) == count($values)) {
            $sql = "SELECT ";
            if ($selectFields[0] == "*") {
                $sql .= "*";
            }
            else {
                for ($i=0;$i<count($selectFields);$i++) {
                    $sql .= "`" . $selectFields[$i] . "`";

                    if ($i != count($selectFields)-1) {
                        $sql .= ", ";
                    }
                }
            }
            $sql .= " FROM " . $table ." WHERE ";
            for ($i=0;$i<count($whereFields);$i++) {
                $sql .= "`" . $whereFields[$i] . "` = '" . $values[$i] . "'";
                if ($i != count($whereFields)-1) {
                    $sql .= " AND ";
                }

            }
            //echo $sql;
            $this->connect();
            $result = $this->conn->query($sql);
            $rows = [];
            if ($result->num_rows > 0) {
                while($row = $result->fetch_assoc()) {
                    $this->conn->close();
                    //print_r($row);
                    array_push($rows,$row);
                    //return $row;
                }
                //print_r($rows);
                return $rows;
            } else {
                $this->conn->close();
                return [];
            }
        }

    }

    /**
     * @param $table
     * @param $setFields
     * @param $setValues
     * @param $whereFields
     * @param $whereValues
     * @return array
     *
     *
     * !! do the authorization checks before !!
     *
     * update the table for the $table
     *
     */

    public function update($table,$setFields,$setValues,$whereFields,$whereValues) {

        if (count($setFields) == count($setValues) and count($whereFields) == count($whereValues) and count($setFields) > 0 and count($whereFields) > 0 and strlen($table) > 1) {
            $sql = "UPDATE " . $table . " SET ";
            for ($i=0;$i<count($setFields);$i++) {
                $sql .= "`" . $setFields[$i] . "`" . " = '" . $setValues[$i] . "'";
                if (!($i == count($setValues)-1)) {
                    $sql .= ", ";
                }
            }
            $sql .= " WHERE ";
            for ($i=0;$i<count($whereFields);$i++) {
                $sql .= "`" . $whereFields[$i] . "`" . " = '" . $whereValues[$i] . "'";
                if (!($i == count($whereValues)-1)) {
                    $sql .= " AND ";
                }
            }
            $this->connect();
            if ($this->conn->query($sql) === TRUE) { //success
                $this->conn->close();
                return [true];
            }
            else { //failure
                $this->conn->close();
                return [false, $this->conn->error];
            }
        }
        else {
            return [false,errorCodes::updateValuesIncorrect];
        }
    }

    /**
     * @param $table
     * @param $whereFields
     * @param $whereValues
     * @return array
     *
     * !! do the authorization checks before !!
     *
     * delete an entry in the $table
     *
     */

    public function delete ($table,$whereFields,$whereValues) {
        if (count($whereFields) == count($whereValues) and count($whereFields) > 0 and strlen($table) > 1) {
            $sql = "DELETE FROM " . $table . " WHERE ";
            for ($i=0;$i<count($whereFields);$i++) {
                $sql .= "`" . $whereFields[$i] . "`" . " = '" . $whereValues[$i] . "'";
                if (!($i == count($whereValues)-1)) {
                    $sql .= " AND ";
                }
            }
            //print($sql);
            $this->connect();
            if ($this->conn->query($sql) === TRUE) { //success
                $this->conn->close();
                return [true];
            }
            else { //failure
                $this->conn->close();
                return [false, $this->conn->error];
            }
        }
        else {
            return [false,errorCodes::deleteValuesIncorrect];
        }


    }

    /**
     * @param $email
     * @param $firstname
     * @param $lastname
     * @param $password
     * @param $username
     * @return array|bool
     *
     * create a user
     *
     */

    public function createUser($email,$firstname,$lastname,$password,$username) {
        if (strlen($email) > 4 && strlen($firstname) > 1 && strlen($lastname) > 1 && $this->validatePassword($password)[0] && strlen($username) >3) { //check if entered data is valid
            $ret = $this->validateUserData($username,$email);
            if ($ret[0]) { // check if email and username are already in the db
                // entered data is valid, continue to register user.
                $password = $this->getHashedPw($password); //get the hasehd pw
                $fields = ["userName","password","emailAdress","firstName","lastName"];
                $values = [$username,$password,$email,$firstname,$lastname];

                return $this->insert("user",$fields,$values); //insert new user into db
            }
            else {
                return $ret;
            }
        }
        else {
            return [false, errorCodes::invalidData];
        }

    }

    /**
     * @param $userid
     * @param $token
     * @param $name
     * @param $location
     * @param $password
     * @return array|bool
     *
     * creates a new household + the relation in the userHousehold table
     *
     */
    public function createHousehold($userid,$token,$name,$location,$password) {
        if ($this->checkAuthorizationWithToken($userid,$token)[0]){
            $fields = ["name","location","createUser.id","password"];
            $password = $this->getHashedPw($password);
            $values = [$name,$location,$userid,$password];
            $ret = $this->insert("household",$fields,$values,$userid);
            if ($ret[0]) {
                $householdid = $ret[1];
                $ret = $this->insert("userHousehold",["user.id","household.id"],[$userid,$householdid]);
                if ($ret[0]) {
                    return [true,$householdid];
                }
                else {
                    return $ret;
                }
            }
            else {
                return $ret;
            }

        }
    }

    /**
     * @param $user
     * @return string
     * $user is an array where at least one of 'Username' or 'Email' must be filled. The userid is being returned.
     */
    public function getUserId($user) {

        if (strlen($user["Username"]) >2) {
            $sql = "SELECT id FROM user WHERE userName = '" . $user['Username'] . "'";
        }
        else if (strlen($user["Email"]) >2) {
            $sql = "SELECT id FROM user WHERE emailAdress = '" . $user['Email'] . "'";
        }
        else {
            return false;
        }
        $this->connect();
        $result = $this->conn->query($sql);
        if ($result->num_rows > 0) {
            while($row = $result->fetch_assoc()) {
                $this->conn->close();
                return $row['id'];
            }
        } else {
            $this->conn->close();
            return "0";
        }
    }

    /**
     * @param $password
     * @return bool
     * validates the new password. A password has to be at least 8 characters long and has to contain at least one capital letter,
     * one lower case and one number.
     */
    private function validatePassword($password) {
        if (strlen($password) >= 8 && preg_match('/[A-Z]+[a-z]+[0-9]+/', $password)) {
            return [true];
        }
        else {
            return [false, errorCodes::unauthorized];
        }
    }

    /**
     * @param $username
     * @param $emailadress
     * @return bool
     *
     * checks if the userdata (username and emailadress) are not in the database yet.
     */
    private function validateUserData($username,$emailadress) {
        $this->connect();
        $sql = "SELECT userName,emailAdress FROM user WHERE emailAdress = '" . $emailadress . "'OR userName = '" . $username . "'";
        $result = $this->conn->query($sql);
        if ($result->num_rows > 0) { // if there are results
            while($row = $result->fetch_assoc()) {
                if ($row['userName'] == $username) { // if the username already exists
                    $this->conn->close();
                    return [false, errorCodes::usernameAlreadyRegistered];
                }
                else { // if the emailadress already exists
                    $this->conn->close();
                    return [false, errorCodes::emailadressAlreadyRegistered];
                }
            }
        } else {
            $this->conn->close();
            return [true];
        }
    }

    /**
     * @param $emailadress
     * @return bool
     *
     * does the given emailadress already exist?
     *
     */
    public function doesEmailAdressAlreadyExist($emailadress) {

        $this->connect();
        $sql = "SELECT id FROM user WHERE emailAdress = '" . $emailadress . "'";
        $result = $this->conn->query($sql);
        if ($result->num_rows > 0) { // if there are results
            while($row = $result->fetch_assoc()) {
                $this->conn->close();
                return [true];
            }
        } else {
            $this->conn->close();
            return [false,errorCodes::emailadressAlreadyRegistered];
        }

    }

    /**
     * @param $pw
     * @return string
     * generates the Password Hash to a give $pw (string)
     */
    private function getHashedPw($pw) {
        $cost = 10;
        // Create a random salt
        $salt = strtr(base64_encode(mcrypt_create_iv(16, MCRYPT_DEV_URANDOM)), '+', '.');
        $salt = sprintf("$2a$%02d$", $cost) . $salt;
        // Hash the password with the salt
        $hash = crypt($pw, $salt);
        return $hash;
    }

    /**
     * @param $userid
     * @return string
     *
     * generates the token for the login
     */
    private function generateToken($userid) {
        return $this->getHashedPw($userid .  date('Y-m-d H:i:s')); //just use the getHashedPw method with userid and time
    }

    /**
     * @param $userid
     * @param $householdid
     * @return bool
     *
     * check if the user is authorized to access the requested household.
     *
     */
    public function userIdAuthorizedToAccessHousehold($userid,$householdid) {
        if(count($this->get("userHousehold",["id"],["user.id","household.id"],[$userid,$householdid])) == 1) {
            return [true];
        }
        else {
            return [false, errorCodes::userUnauthorizedForHousehold];
        }
    }


    public function __toString() {
        return "";
    }
}