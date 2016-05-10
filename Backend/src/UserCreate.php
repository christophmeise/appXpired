<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 03.05.16
 * Time: 16:30
 */



class UserCreate extends Request
{
    public function execute() {
        $ret = $this->db->createUser($this->user);
        $this->makeUpHeadersFromDBReturn($ret);
        if ($ret[0]) {
            $this->outputString = json_encode($this->getCreatedUser());
        }
        $this->outputString = "";
    }
    private function getCreatedUser() {
        return $this->db->get("user",["id","userName","emailAdress","firstName","lastName"],["id"],[$this->user->getId()]);
    }
}