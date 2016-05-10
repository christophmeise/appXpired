<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 03.05.16
 * Time: 16:51
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
class Login extends Request
{
    public function execute() {
        if ($this->user->authorized()) {
            HeaderManager::getInstance()->addHeader(new Header("Success", "true"));
        }
        $this->outputString = "";
    }
}