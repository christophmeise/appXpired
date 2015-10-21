<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 19.10.15
 * Time: 11:34
 */


/**
 *
 * create a user using the POST Method:
 * Appxpired-Username
 * Appxpired-Email
 * Appxpired-Firstname
 * Appxpired-Lastname
 * Appxpired-Password
 * Appxpired-Token
 *
 */

include "databaseConnection.php";

if ($_SERVER['REQUEST_METHOD'] == "POST") { //createUser
    //get a database object (databaseConnection)
    $db = new databaseConnection();
    // get the headers
    $headers = apache_request_headers();
    // parse the headers
    $username = $headers["Appxpired-". "Username"];
    $email = $headers["Appxpired-". "Email"];
    $firstname = $headers["Appxpired-". "Firstname"];
    $lastname = $headers["Appxpired-". "Lastname"];
    $password = $headers["Appxpired-". "Password"];
    // create the user in the DB (data validation will be done by the databaseConnection)
    $ret = $db->createUser($email,$firstname,$lastname,$password,$username);
    // $ret[1] contains the userid of the new user, so that the client can save it.
    $errors = "";
    $z = 0;
    for ($i=0;$i < count($ret);$i++) {
        if ($i == 0 and $ret[$i] == true) {
            header("Success: True");
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
        $retu = $db->get("user",["id","userName","emailAdress","firstName","lastName"],["id"],[$ret[1]]);
        echo json_encode($retu);

    }

}

else if ($_SERVER['REQUEST_METHOD'] == "GET") { //login
    $db = new databaseConnection(); //establish a database connection
    $headers = apache_request_headers(); // get the headers
    //parse the headers
    $username = $headers["Appxpired-". "Username"];
    $email = $headers["Appxpired-". "Email"];
    $password = $headers["Appxpired-". "Password"];
    $user["Username"] = $username;
    $user["Email"] = $email;
    $token = $headers["Appxpired-". "Token"];
    if (strlen($token) > 4) { //check if a token was sent; if true: log in with token
        //loginWithToken
        $ret = $db->checkAuthorizationWithToken($db->getUserId($user),$token);
        for ($i=0;$i < count($ret);$i++) {
            if ($i == 0 and $ret[$i] == true) {
                header("Success: True");
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
            //$retu = $db->get("user",["*"],["id"],[$db->getUserId(["Username" => $username])]);
            $retu = $db->get("user",["id","userName","emailAdress","firstName","lastName"],["username"],[$username]);
            echo json_encode($retu);
        }
    }
    else { //there was no token sent, so login with password
        $ret = $db->checkAuthorizationWithPassword($db->getUserId($user),$password); //try the login with the password
        for ($i=0;$i < count($ret);$i++) {
            if ($i == 0 and $ret[$i] == true) {
                header("Success: True");
                header("Token: " . $ret[1]);
                $i++;
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
            $retu = $db->get("user",["id","userName","emailAdress","firstName","lastName"],["username"],[$username]);
            echo json_encode($retu);
        }
    }
}
else if ($_SERVER['REQUEST_METHOD'] == "DELETE") {

    //TODO: Delete

}
