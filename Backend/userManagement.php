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
    if (count($ret) == 2) {
        echo "true;" . $ret[1] . ";";
    }
    else {
        echo "false;";
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
        if ($db->checkAuthorizationWithToken($db->getUserId($user),$token)) { //log in with that token
            echo "true;"; //login was successful
        }
        else {
            echo "false;"; //login failed
        }
    }
    else { //there was no token sent, so login with password
        $return = $db->checkAuthorizationWithPassword($db->getUserId($user),$password); //try the login with the password
        if ($return[0] == true) { //login was successful
            echo "true;";
            echo $return[1] . ";"; //echo the new token
        }
        else {//login was not successful
            echo "false;";
        }
    }
}
