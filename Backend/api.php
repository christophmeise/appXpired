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
 * - SetValues
 *      the values you want to set, use just as the
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
        $this->headerNames = ["Username", "Password", "Household", "Household-Pw", "Table", "Wherevalues", "Setvalues"];
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
    }
    private function login() {
        $user['Username'] = $this->usedHeaders['Username'];
        return $this->db->checkAuthorizationWithPassword($this->db->getUserId($user),$this->usedHeaders['Password']);
    }

    /**
     * GET Method was called
     */
    function get() {
        // kein body

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

        $ret = $this->db->insert($this->usedHeaders["Table"],$fields,$values, $this->db->getUserId(["Username"=>$this->usedHeaders["Username"]]),$this->usedHeaders["Password"]);
        if ($ret[0]) {
            echo "true;" . $ret[1];
        }
        else {
            echo "false;";
        }
    }
    /**
     * DELETE Method was called
     */
    function delete() {
        // body

    }
    /**
     * PATCH Method was called
     */
    function patch() {
        // body
    }
    //TODO
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
}
