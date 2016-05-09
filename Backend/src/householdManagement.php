<?php
/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 20.10.15
 * Time: 19:05
 */

/**
 *
 * create a household using the POST Method:
 * Appxpired-Username
 * Appxpired-Location
 * Appxpired-Name
 * Appxpired-Token
 * Appxpired-Password
 */

spl_autoload_register(function($className)
{
    $namespace = str_replace("\\","/",__NAMESPACE__);
    $className = str_replace("\\","/",$className);
    $class = "./".(empty($namespace)?"":$namespace."/")."{$className}.php";
    include_once($class);
});

HeaderManager::getInstance()->addHeader(new Header("Content-Type","application/json"));

$request = RequestFactory::create(apache_request_headers(),"household");
$printer = new Printer($request);
$printer->execute();
