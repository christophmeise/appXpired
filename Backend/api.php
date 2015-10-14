<?php
/**
 * User: yannickwinter
 * Date: 14.10.15
 * Time: 13:42
 */

include "databaseConnection.php";


$usedHeaders = [];
$headerPrefix = "Appxpired-";
$headerNames = ["Db-Pw","Username", "Password", "Household", "Household-Pw", "Table"];

getHeaders();

$db = new databaseConnection();
$db->init($usedHeaders["Db-Pw"]);
$db->connect();



switch ($_SERVER['REQUEST_METHOD']) {
    case "GET":
        get();
        break;
    case "POST":
        post();
        break;
    case "DELETE":
        delete();
        break;
    case "PATCH":
        patch();
        break;
}

function getHeaders() {

    global $headerNames, $headerPrefix, $usedHeaders;

    $headers = apache_request_headers();
    foreach ($headerNames as $headerName) {
        $usedHeaders[$headerName] = $headers[$headerPrefix . $headerName];
    }
}


function get() {
    // kein body


}

function post() {
    // body

}

function delete() {
    // body

}
function patch() {
    // body
}


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

