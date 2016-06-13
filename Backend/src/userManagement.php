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

require_once __DIR__ . '/User.php';
require_once __DIR__ . '/databaseConnection.php';
require_once __DIR__ . '/ErrorCode.php';
require_once __DIR__ . '/Delete.php';
require_once __DIR__ . '/Get.php';
require_once __DIR__ . '/Header.php';
require_once __DIR__ . '/HeaderManager.php';
require_once __DIR__ . '/HouseholdCreate.php';
require_once __DIR__ . '/householdManagement.php';
require_once __DIR__ . '/Login.php';
require_once __DIR__ . '/Patch.php';
require_once __DIR__ . '/Post.php';
require_once __DIR__ . '/Printer.php';
require_once __DIR__ . '/Request.php';
require_once __DIR__ . '/RequestFactory.php';
require_once __DIR__ . '/UserCreate.php';
require_once __DIR__ . '/userManagement.php';

HeaderManager::getInstance()->addHeader(new Header("Content-Type","application/json"));
/*
$request = RequestFactory::create(apache_request_headers(),"user");
$printer = new Printer($request);
$printer->execute();
*/
// to log the request:
/*
logThisRequest();
function logThisRequest() {
    $file = 'userLog.json';
    $current = json_decode(file_get_contents($file));
    $headers = apache_request_headers();
    $headers["Request-Method"] = $_SERVER['REQUEST_METHOD'];
    $headers["Date"] = date(DATE_RFC822);
    if ($headers["Appxpired-Password"] != null) {
        $headers["Appxpired-Password"] = "hidden";
    }
    if ($headers["Appxpired-Token"] != null) {
        $headers["Appxpired-Token"] = "hidden";
    }
    array_unshift($current, $headers);
    file_put_contents($file, json_encode($current));
}

*/