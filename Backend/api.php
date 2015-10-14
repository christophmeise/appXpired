<?php
/**
 * User: yannickwinter
 * Date: 14.10.15
 * Time: 13:42
 */

$usedHeaders = [];
$headerPrefix = "AppXpired_";
$headerNames = ["username", "password", "household", "household-pw", "table"];

getHeaders();

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
    $headers = apache_request_headers();

    foreach ($this->headerNames as $headerName => $value) {
        if ($headers[$this->headerPrefix . $value] != 0) {
            $this->usedHeaders[$value] = $headers[$this->headerPrefix . $value];
        }
    }
    if ($this->headerPrefix . "username");

    echo "My ECHO HERE: " . $headers["Name"];
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

