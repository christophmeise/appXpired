<?php
/**
 * User: Yannick Winter
 * Date: 14.10.15
 * Time: 13:42
 */

spl_autoload_register(function($className)
{
    $namespace = str_replace("\\","/",__NAMESPACE__);
    $className = str_replace("\\","/",$className);
    $class = "./".(empty($namespace)?"":$namespace."/")."{$className}.php";
    include_once($class);
});

HeaderManager::getInstance()->addHeader(new Header("Content-Type","application/json"));

$request = RequestFactory::create(apache_request_headers());
$printer = new Printer($request);
$printer->execute();
logThisRequest();

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


function logThisRequest() {
    $file = 'log.json';
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
    //print_r($current);
    //$end = json_encode($current);
    file_put_contents($file, json_encode($current));
}

