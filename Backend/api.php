<?php
/**
 * User: yannickwinter
 * Date: 14.10.15
 * Time: 13:42
 */

include "databaseConnection.php";

$api = new api();


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
        $this->headerNames = ["Username", "Password", "Household", "Household-Pw", "Table"];
        // get headers
        $this->getHeaders();
        //connect to db;
        $this->db = new databaseConnection();
        $this->db->connect();
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

        global $headerNames, $headerPrefix, $usedHeaders;
        $headers = apache_request_headers();
        foreach ($headerNames as $headerName) {
            $usedHeaders[$headerName] = $headers[$headerPrefix . $headerName];
        }
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
        // body

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


        if(isset($_POST['name'])and $_POST['name'] =='cottton'){
            // any code ...
            $whatever = 'cottton';
            $anything = 'bla';
            $lolz = 13*13*13;

            // sort data ...
            $data['whatever'] = $whatever;
            $data['anything'] = $anything;
            $data['lolz'] = $lolz;
        }else{
            // error message ...
            $data['Error'] = 'wrong name!';
        }


    // json_encode data and 'echo it out'
        echo json_encode($data);

    }
}
