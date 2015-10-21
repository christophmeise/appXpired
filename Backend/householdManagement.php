<?php
/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 20.10.15
 * Time: 19:05
 */

/**
 *
 * create a user using the POST Method:
 * Appxpired-Username
 * Appxpired-Location
 * Appxpired-Name
 * Appxpired-Token
 * Appxpired-Password
 */

include "databaseConnection.php";

if ($_SERVER['REQUEST_METHOD'] == "POST") { //createHousehold
    //get a database object (databaseConnection)
    $db = new databaseConnection();
    // get the headers
    $headers = apache_request_headers();
    // parse the headers
    $username = $headers["Appxpired-". "Username"];
    $location = $headers["Appxpired-". "Location"];
    $name = $headers["Appxpired-". "Name"];
    $token = $headers["Appxpired-". "Token"];
    $password = $headers["Appxpired-". "Password"];

    // create the user in the DB (data validation will be done by the databaseConnection)
    $ret = $db->createHousehold($db->getUserId(["Username"=>$username]),$token,$name,$location,$password);
    // if successful $ret[1] contains the household id of the new household, so that the client can save it.
    $z = 0;
    for ($i=0;$i < count($ret);$i++) {
        if ($i == 0 and $ret[$i] == true) {
            header("Success: true");
        }
        else if ($i == 0 and $ret[$i] == false) {
            header("Success: false");
        }
        else if ($i != 0 and $ret[0] == false){
            $errors = $ret[$i] . ";";
            $z += 1;
        }
    }
    if ($ret[0] ==false) {
        header("Errors: " . $errors);
    }
    else {
        $retu = $db->get("household",["id","name","location","createUser.id"],["id"],[$ret[1]]);
        echo json_encode($retu);

    }
}
