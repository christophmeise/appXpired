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
 *
 * login using the GET Method
 *
 * Appxpired-Username or Appxpired-Email and
 * Appxpired-Password or Appxpired-Token
 *
 * You get a "Success" flag in the response header which is true or false
 * If you use a password to login, you will get a "Token" in the response header. With that you can login.
 * The body will be a JSON with the userdata.
 *
 *
 * update the user data using the PATCH method
 *
 * Appxpired-Method
 *
 * Appxpired-Username
 * Appxpired-Email
 * Appxpired-Firstname
 * Appxpired-Lastname
 * Appxpired-Password
 *
 * Appxpired-Oldpassword
 * Appxpired-Newpassword
 *
 * with the method "Userdata" you can edit the user data emailadress, firstname, lastname
 *
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
        $z=0;
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
        else {
            //$retu = $db->get("user",["*"],["id"],[$db->getUserId(["Username" => $username])]);
            $retu = $db->get("user",["id","userName","emailAdress","firstName","lastName"],["username"],[$username]);
            echo json_encode($retu);
        }
    }
    else { //there was no token sent, so login with password
        $ret = $db->checkAuthorizationWithPassword($db->getUserId($user),$password); //try the login with the password
        $z=0;
        for ($i=0;$i < count($ret);$i++) {
            if ($i == 0 and $ret[$i] == true) {
                header("Success: true");
                header("Token: " . $ret[1]);
                $i++;
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
        else {
            $retu = $db->get("user",["id","userName","emailAdress","firstName","lastName"],["username"],[$username]);
            echo json_encode($retu);
        }
    }
}
else if ($_SERVER['REQUEST_METHOD'] == "DELETE") {

    //TODO: Delete

}
else if ($_SERVER['REQUEST_METHOD'] == "PATCH") {
    $db = new databaseConnection(); //establish a database connection
    $headers = apache_request_headers(); // get the headers
    //parse the headers

    $method = $headers["Appxpired-". "Method"];

    $username = $headers["Appxpired-". "Username"];
    $email = $headers["Appxpired-". "Email"];
    $firstname = $headers["Appxpired-". "Firstname"];
    $lastname = $headers["Appxpired-". "Lastname"];
    $password = $headers["Appxpired-". "Password"];

    $oldPassword = $headers["Appxpired-". "Oldpassword"];
    $newPassword = $headers["Appxpired-". "Newpassword"];


    if ($method == "Userdata") { //change the userdata (not the password)

        /**
         *
         * if you want to change the userdata you have to enter the correct username and password! You can not change the username.
         *
         */

        $authorized = false;
        $ret = null;
        $errors = [];
        if (strlen($username) > 2) {
            $ret = $db->checkAuthorizationWithPassword($db->getUserId(["Username" => $username]),$password);
            $authorized = $ret[0];
            if ($ret[0]) {
                header("Token: " . $ret[1]);
            }
            else {
                array_push($errors,$ret[1]);
            }
        }
        elseif (strlen($email)) {
            $ret = $db->checkAuthorizationWithPassword($db->getUserId(["Email" => $email]),$password);
            $authorized = $ret[0];
            if ($ret[0]) {
                header("Token: " . $ret[1]);

            }
            else {
                array_push($errors,$ret[1]);
            }
        }
        if ($authorized) {
            // user is authorized

            $setfields = [];
            $setvalues = [];
            $i = 0;
            if (strlen($email) >0) {
                $em = $db->doesEmailAdressAlreadyExist($email);
                if(!$em[0]) {
                    $setfields[$i] = "emailAdress";
                    $setvalues[$i] = $email;
                    $i++;
                }
                else {
                    array_push($errors,$em[1]);
                }
            }
            if (strlen($firstname) >0) {
                $setfields[$i] = "firstName";
                $setvalues[$i] = $firstname;
                $i++;
            }
            if (strlen($lastname) >0) {
                $setfields[$i] = "lastName";
                $setvalues[$i] = $lastname;
                $i++;
            }

            $ret = $db->update("user",$setfields,$setvalues,["userName"],[$username]);
            if ($ret[0]) {
                header("Success: true");
                $user = $db->get("user",["id","username","firstName","lastName","emailAdress"],["userName"],[$username]);
                echo json_encode($user);
            }
            else {
                array_push($errors,$ret[1]);
                $errorString = "";
                for ($i=0;$i<count($errors);$i++) {
                    $errorString .= strval($errors[$i]) . ";";
                }
                header("Success: false");
                header("Errors: " . $errorString);
            }
        }
        else {
            $errorString = "";
            for ($i=0;$i<count($errors);$i++) {
                $errorString .= strval($errors[$i]) . ";";
            }
            header("Success: false");
            header("Errors: " . $errorString);
        }




    }


}
else {

    //TODO failure message!

}
