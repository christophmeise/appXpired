<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 03.05.16
 * Time: 16:51
 */
class Login extends Request
{
    public function execute() {
        if ($this->user->authorized()) {
            HeaderManager::getInstance()->addHeader(new Header("Success", "true"));
        }
        $this->outputString = "";
    }
}