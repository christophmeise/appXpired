<?php
/**
 * User: Yannick Winter
 * Date: 14.10.15
 * Time: 13:42
 */

include "databaseConnection.php";

$api = new api();


/**
 * Class api
 *
 * Header forms: "prefix-" [Appxpired-]:
 * - Username:
 *      the username of the user sending the request
 * - Password:
 *      the password
 * - Household:
 *      the household.id of the used household
 * - Household-Pw:
 *      the household password
 * - Table:
 *      the table where you need the get from
 * - Wherevalues:
 *
 *      the "where" values from the table please provide this information in the following format:
 *      "Field1","Value1";"Field2","Value2"
 *      (use "," to separate the inner array and ";" to separate the outer array.
 * - Setvalues
 *      the values you want to set, use just as the wherevalues
 * - Selectvalues:
 *      the "select" values of the Select statement (Select * from...)
 *      please provide those values ; seperated.
 * - Token:
 *      the token for user authorization
 *
 */

class api {

    private $usedHeaders;
    private $headerPrefix;
    private $headerNames;

    private $db;
    /**
     * api constructor.
     */
    public function __construct() {
        //setting up variables
        $this->usedHeaders = [];
        $this->headerPrefix = "Appxpired-";
        $this->headerNames = ["Username", "Password", "Household", "Household-Pw", "Table", "Wherevalues", "Setvalues", "Selectvalues", "Token","Expand"];
        // get headers
        $this->getHeaders();
        //connect to db;
        $this->db = new databaseConnection();
        // get the used HTTP Method (CRUD)
        $this->getHTTPMethod();
    }

    function getHTTPMethod() {
        // get the HTTP Method
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                $this->get();
                break;
            case "POST":
                $this->post();
                break;
            case "DELETE":
                $this->delete();
                break;
            case "PATCH":
                $this->patch();
                break;
        }
    }
    /**
     * get the custom headers and add them to the $usedHeaders array.
     */
    function getHeaders() {
        $headers = apache_request_headers();
        foreach ($this->headerNames as $headerName) {
            $this->usedHeaders[$headerName] = $headers[$this->headerPrefix . $headerName];

        }
        $this->processHeaders();
    }

    function processHeaders() {
        $this->usedHeaders["Wherevalues"] = explode(";",$this->usedHeaders["Wherevalues"]);

        for ($i = 0;$i<count($this->usedHeaders["Wherevalues"]);$i++) {
            $this->usedHeaders["Wherevalues"][$i] = explode(",",$this->usedHeaders["Wherevalues"][$i]);
        }

        $this->usedHeaders["Setvalues"] = explode(";",$this->usedHeaders["Setvalues"]);

        for ($i = 0;$i<count($this->usedHeaders["Setvalues"]);$i++) {
            $this->usedHeaders["Setvalues"][$i] = explode(",",$this->usedHeaders["Setvalues"][$i]);
        }

        $this->usedHeaders["Selectvalues"] = explode(";",$this->usedHeaders["Selectvalues"]);
        if ($this->usedHeaders["Expand"] == "True") {
            $this->usedHeaders["Expand"] = true;
        }
        else {
            $this->usedHeaders["Expand"] = false;
        }

    }
    private function login() {
        $user['Username'] = $this->usedHeaders['Username'];
        return $this->db->checkAuthorizationWithPassword($this->db->getUserId($user),$this->usedHeaders['Password']);
    }

    /**
     * GET Method was called
     */
    function get() {
        $ret = null;
        $authorized = true;
        if ($this->usedHeaders["Table"] == "household") { //if the household table shall be accessed check if the user is authorized
            $authorized = false;
            for ($i=0;$i<count($this->usedHeaders["Wherevalues"]);$i++) {
                if ($this->usedHeaders["Wherevalues"][$i][0] == "id") {
                    $ret = $this->db->userIdAuthorizedToAccessHousehold($this->db->getUserId(["Username"=>$this->usedHeaders["Username"]]),$this->usedHeaders["Wherevalues"][$i][1]);
                    $authorized = $ret[0];
                }
            }
        }
        if ($authorized) {
            if (strlen($this->usedHeaders["Token"]) > 2) {
                $ret = $this->db->checkAuthorizationWithToken($this->db->getUserId(["Username" =>$this->usedHeaders["Username"]]),$this->usedHeaders["Token"]);
                $authorized = $ret[0];
            }
            elseif (strlen($this->usedHeaders["Password"]) > 2) {
                $ret = $this->db->checkAuthorizationWithPassword($this->db->getUserId(["Username" =>$this->usedHeaders["Username"]]),$this->usedHeaders["Password"]);
                if ($ret[0]) {
                    header("Token: " . $ret[1]);
                }
                $authorized = $ret[0];
            }
            else {
                $authorized = false;
            }

            if ($authorized) {

                $whereFields = null;
                $whereValues = null;

                for ($i = 0;$i<count($this->usedHeaders["Wherevalues"]);$i++) {
                    $whereFields[$i] = $this->usedHeaders["Wherevalues"][$i][0];
                    $whereValues[$i] = $this->usedHeaders["Wherevalues"][$i][1];
                }

                $dbOut = $this->db->get($this->usedHeaders["Table"],$this->usedHeaders["Selectvalues"],$whereFields,$whereValues);


                header("Success: true");
                unset($dbOut["password"]);
                unset($dbOut["token"]);
                echo json_encode($dbOut);

            }
            else {
                $this->makeUpHeadersFromDBReturn($ret);
            }
        }
        else {
            $this->makeUpHeadersFromDBReturn($ret);
        }
    }

    /**
     * POST Method was called
     */
    function post() {
        $fields = [];
        $values = [];
        $i = 0;
        foreach ($this->usedHeaders["Setvalues"] as $value => $field) {
            $values[$i] = $value;
            $fields[$i] = $field;
            $i = $i+1;
        }
        $auth = false;
        if ($this->usedHeaders['Token']) {
            $ret = $this->db->checkAuthorizationWithToken($this->db->getUserId(["Username" => $this->usedHeaders["Username"]]),$this->usedHeaders["Token"]);
            if ($ret[0]) {
                $auth = true;
            }
            else {
                $this->makeUpHeadersFromDBReturn($ret);
            }
        }
        else {
            $ret = $this->db->checkAuthorizationWithPassword($this->db->getUserId(["Username" => $this->usedHeaders["Username"]]),$this->usedHeaders["Password"]);
            if ($ret[0]) {
                $auth = true;
            }
            else {
                $auth = false;
                $this->makeUpHeadersFromDBReturn($ret);
                header("Success: false");
            }
        }
        if ($auth) {
            if ($this->usedHeaders["Table"] == "Household") {
                $id = 0;
                for ($i=0;$i<$this->usedHeaders["Wherevalues"];$i++) {
                    if ($this->usedHeaders["Wherevalues"][$i][0] == "id") {
                        $id = $this->usedHeaders["Wherevalues"][$i][1];
                    }
                }
                $ret = $this->db->userIdAuthorizedToAccessHousehold($this->db->getUserId(["Username" => $this->usedHeaders["Username"]]),$id);
                if ($ret[0]) {
                    $auth = true;
                }
                else {
                    $auth = false;
                    $this->makeUpHeadersFromDBReturn($ret);
                    header("Success: false");
                }
            }
        }
        if ($auth) {
            $ret = $this->db->insert($this->usedHeaders["Table"],$fields,$values);
            if ($ret[0]) {
                header("Success: true");
                header("LastId: " . $ret[1]);
            }
            else {
                header("Success: false");
            }
        }
    }
    /**
     * DELETE Method was called
     */
    function delete() {

        $whereFields = [];
        $whereValues = [];
        $i = 0;
        foreach ($this->usedHeaders["Wherevalues"] as $value => $field) {
            $whereValues[$i] = $value;
            $whereFields[$i] = $field;
            $i = $i+1;
        }
        $auth = false;
        if ($this->usedHeaders['Token']) {
            $ret = $this->db->checkAuthorizationWithToken($this->db->getUserId(["Username" => $this->usedHeaders["Username"]]),$this->usedHeaders["Token"]);
            if ($ret[0]) {
                $auth = true;
            }
            else {
                $this->makeUpHeadersFromDBReturn($ret);
            }
        }
        else {
            $ret = $this->db->checkAuthorizationWithPassword($this->db->getUserId(["Username" => $this->usedHeaders["Username"]]),$this->usedHeaders["Password"]);
            if ($ret[0]) {
                $auth = true;
            }
            else {
                $auth = false;
                $this->makeUpHeadersFromDBReturn($ret);
                header("Success: false");
            }
        }
        if ($auth) {
            if ($this->usedHeaders["Table"] == "Household") {
                $id = 0;
                for ($i=0;$i<$this->usedHeaders["Wherevalues"];$i++) {
                    if ($this->usedHeaders["Wherevalues"][$i][0] == "id") {
                        $id = $this->usedHeaders["Wherevalues"][$i][1];
                    }
                }
                $ret = $this->db->userIdAuthorizedToAccessHousehold($this->db->getUserId(["Username" => $this->usedHeaders["Username"]]),$id);
                if ($ret[0]) {
                    $auth = true;
                }
                else {
                    $auth = false;
                    $this->makeUpHeadersFromDBReturn($ret);
                    header("Success: false");
                }
            }
        }
        if ($auth) {
            $ret = $this->db->delete($this->usedHeaders["table"],$whereFields,$whereValues);
            if ($ret[0]) {
                header("Success: true");
                header("LastId: " . $ret[1]);
            }
            else {
                header("Success: false");
                $this->makeUpHeadersFromDBReturn($ret);
            }
        }

    }
    /**
     * PATCH Method was called
     * update
     */
    function patch() {
        $fields = [];
        $values = [];
        $i = 0;
        foreach ($this->usedHeaders["Setvalues"] as $value => $field) {
            $values[$i] = $value;
            $fields[$i] = $field;
            $i = $i+1;
        }
        $whereFields = [];
        $whereValues = [];
        $i = 0;
        foreach ($this->usedHeaders["Wherevalues"] as $value => $field) {
            $whereValues[$i] = $value;
            $whereFields[$i] = $field;
            $i = $i+1;
        }
        $auth = false;
        if ($this->usedHeaders['Token']) {
            $ret = $this->db->checkAuthorizationWithToken($this->db->getUserId(["Username" => $this->usedHeaders["Username"]]),$this->usedHeaders["Token"]);
            if ($ret[0]) {
                $auth = true;
            }
            else {
                $this->makeUpHeadersFromDBReturn($ret);
            }
        }
        else {
            $ret = $this->db->checkAuthorizationWithPassword($this->db->getUserId(["Username" => $this->usedHeaders["Username"]]),$this->usedHeaders["Password"]);
            if ($ret[0]) {
                $auth = true;
            }
            else {
                $auth = false;
                $this->makeUpHeadersFromDBReturn($ret);
                header("Success: false");
            }
        }
        if ($auth) {
            if ($this->usedHeaders["Table"] == "Household") {
                $id = 0;
                for ($i=0;$i<$this->usedHeaders["Wherevalues"];$i++) {
                    if ($this->usedHeaders["Wherevalues"][$i][0] == "id") {
                        $id = $this->usedHeaders["Wherevalues"][$i][1];
                    }
                }
                $ret = $this->db->userIdAuthorizedToAccessHousehold($this->db->getUserId(["Username" => $this->usedHeaders["Username"]]),$id);
                if ($ret[0]) {
                    $auth = true;
                }
                else {
                    $auth = false;
                    $this->makeUpHeadersFromDBReturn($ret);
                    header("Success: false");
                }
            }
        }
        if ($auth) {
            $ret = $this->db->update($this->usedHeaders["table"],$fields,$values,$whereFields,$whereValues);
            if ($ret[0]) {
                header("Success: true");
                header("LastId: " . $ret[1]);
            }
            else {
                header("Success: false");
                $this->makeUpHeadersFromDBReturn($ret);
            }
        }
    }

    function readPost() {
        // test code
        $whatever['1'] = 'cottton';
        $whatever['2'] ='yoyoyoyo';

        $whatever = json_encode($whatever);
        $anything = 'bla';
        $lolz = 13*13*13;

        // sort data ...
        $data['whatever'] = $whatever;
        $data['anything'] = $anything;
        $data['lolz'] = $lolz;


    // json_encode data and 'echo it out'
        echo json_encode($data);

    }
    function makeUpHeadersFromDBReturn($ret) {
        $z = 0;
        $errors = null;
        for ($i=0;$i < count($ret);$i++) {
            if ($i == 0 and $ret[$i] == true) {
                header("Success: true");
            }
            else if ($i == 0 and $ret[$i] == false) {
                header("Success: false");
            }
            else if ($i != 0 and $ret[0] == false){
                $errors = strval($ret[$i]) . ";";
                $z += 1;
            }
        }
        if ($ret[0] ==false) {
            header("Errors: " . $errors);
        }
    }
}
