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

/*$request = RequestFactory::create(apache_request_headers(),"household");
$printer = new Printer($request);
$printer->execute();
*/