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
 * 404 - UNAUTHORIZED
 * 403 - token has expired
 *
 */

include('ErrorCode.php');

class databaseConnection {

    private $serverName;
    private $userName;
    private $password;
    private $dbname;
    private $conn;

    private $connected;

    private static $instance = null;


    /**
     * @return databaseConnection
     */
    public static function getInstance() {
        if (databaseConnection::$instance == null) {
            databaseConnection::$instance = new databaseConnection();
            return databaseConnection::getInstance();
        }
        return databaseConnection::$instance;
    }

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
    private function __construct() {
        $this->serverName = "db596387337.db.1and1.com";
        $this->userName = "dbo596387337";
        $this->password = "aMy-SeM2";
        $this->dbname = "db596387337";
        $this->connected = false;
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
     * @param $token
     * @return array [bool, ErrorCode]
     */
    public function checkAuthorizationWithToken($userid,$token) {

        $this->connect();
        // check if the token is in the db
        $sql = "SELECT * FROM user WHERE token = '" . $token . "'";
        $result = $this->conn->query($sql);
        if ($result->num_rows > 0) {
            while($row = $result->fetch_assoc()) {
                //check if the userid is correct
                if ($row["id"] == $userid ) {
                    $today = date('Y-m-d H:i:s');
                    // check if last login is max. 2 hours (2*60*60 seconds) ago, so user can login with token
                    if ((strtotime($today)  - strtotime($row['lastLogin'])) >= (-7200)) {
                        //update last login time so that user can maintain logged in
                        $this->updateLastLogin($userid);
                        //close the connection
                        $this->conn->close();
                        //return true because user is logged in successfully
                        return [true];
                    }
                    else {
                        // token has expired
                        return [false, ErrorCode::TOKENHASEXPIRED];
                    }

                }
                else {
                    $this->conn->close();
                    return [false, ErrorCode::TOKENHASEXPIRED];
                }
            }
        } else {
            $this->conn->close();
            return [false, ErrorCode::UNAUTHORIZED];
        }
    }

    /**
     * Check User Authorization with userid and password
     *
     * @param $userid
     * @param $password
     * @return array [bool, ErrorCode/token]
     */
    public function checkAuthorizationWithPassword($userid,$password) {
        $this->connect();
        //get the correct entry in the user table
        $sql = "SELECT* FROM user WHERE id = " . $userid;
        $result = $this->conn->query($sql);
        if ($result->num_rows > 0) {
            while($row = $result->fetch_assoc()) {
                // check if the password is correct
                if ($row["password"] == crypt($password, $row["password"]) ) {
                    //close the connection
                    $this->conn->close();
                    //update last login time so that user can maintain logged in
                    $token = $this->generateToken($userid);
                    $this->updateLastLogin($userid,$token);
                    //return true because user is logged in successfully
                    //return token too
                    return [true,$token];
                }
                else {

                    $this->conn->close();
                    return [false, ErrorCode::UNAUTHORIZED];
                }
            }
        } else {
            $this->conn->close();
            return [false, ErrorCode::UNAUTHORIZED];
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
            $this->connect();
            $result = $this->conn->query($sql);
            $rows = [];
            if ($result->num_rows > 0) {
                while($row = $result->fetch_assoc()) {
                    $this->conn->close();
                    array_push($rows,$row);
                }

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

        if (count($setFields) == count($setValues) && count($whereFields) == count($whereValues) && count($setFields) > 0 && count($whereFields) > 0 && strlen($table) > 1) {
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
            if ($this->conn->query($sql) === TRUE) {
                $this->conn->close();
                return [true];
            }
            else { //failure
                $this->conn->close();
                return [false, $this->conn->error];
            }
        }
        else {
            return [false,ErrorCode::UPDATEVALUESINCORRECT];
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
        if (count($whereFields) == count($whereValues) && count($whereFields) > 0 && strlen($table) > 1) {
            $sql = "DELETE FROM " . $table . " WHERE ";
            for ($i=0;$i<count($whereFields);$i++) {
                $sql .= "`" . $whereFields[$i] . "`" . " = '" . $whereValues[$i] . "'";
                if (!($i == count($whereValues)-1)) {
                    $sql .= " AND ";
                }
            }
            $this->connect();
            if ($this->conn->query($sql) === TRUE) {
                $this->conn->close();
                return [true];
            }
            else {
                $this->conn->close();
                return [false, $this->conn->error];
            }
        }
        else {
            return [false,ErrorCode::DELETEVALUESINCORRECT];
        }


    }

    /**
     * create a user
     *
     * @param $user User
     * @return array|bool
     */
    public function createUser($user) {
        //check if entered data is valid
        if (strlen($user->getEmail()) > 4 && strlen($user->getFirstname()) > 1 && strlen($user->getLastname()) > 1 && $this->validatePassword($user->getPassword())[0] && strlen($user->getUsername()) >3) {
            $ret = $this->validateUserData($user->getUsername(),$user->getEmail());
            // check if email and username are already in the db
            if ($ret[0]) {
                // entered data is valid, continue to register user.
                //get the hasehd pw
                $password = $this->getHashedPw($user->getPassword());
                $fields = ["userName","password","emailAdress","firstName","lastName"];
                $values = [$user->getUsername(),$password,$user->getEmail(),$user->getFirstname(),$user->getLastname()];
                //insert new user into db
                return $this->insert("user",$fields,$values);
            }
            else {
                return $ret;
            }
        }
        else {
            return [false, ErrorCode::INVALIDDATA];
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
     * The userid is being returned.
     *
     * @param $user User
     * @return string
     */
    public function getUserId(User $user) {

        if (strlen($user->getUsername()) >2) {
            $sql = "SELECT id FROM user WHERE userName = '" . $user->getUsername() . "'";
        }
        else if (strlen($user->getEmail()) >2) {
            $sql = "SELECT id FROM user WHERE emailAdress = '" . $user->getEmail() . "'";
        }
        else {
            return "-1";
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
            return "-1";
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
            return [false, ErrorCode::UNAUTHORIZED];
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
        if ($result->num_rows > 0) {
            while($row = $result->fetch_assoc()) {
                // if the username already exists
                if ($row['userName'] == $username) {
                    $this->conn->close();
                    return [false, ErrorCode::USERNAMEALREADYREGISTERED];
                }
                // if the emailadress already exists
                else {
                    $this->conn->close();
                    return [false, ErrorCode::EMAILADRESSALREADYREGISTERED];
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
        // if there are results
        if ($result->num_rows > 0) {
            while($row = $result->fetch_assoc()) {
                $this->conn->close();
                return [true];
            }
        } else {
            $this->conn->close();
            return [false,ErrorCode::EMAILADRESSALREADYREGISTERED];
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
        //just use the getHashedPw method with userid and time
        return $this->getHashedPw($userid .  date('Y-m-d H:i:s'));
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
            return [false, ErrorCode::USERUNAUTHORIZEDFORHOUSEHOLD];
        }
    }


    public function __toString() {
        return "";
    }
}