<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 03.05.16
 * Time: 18:02
 */
class HouseholdCreate extends Request
{
    public function execute()
    {
        $ret = $this->db->createHousehold($this->user->getId(),$this->headers["Token"],apache_request_headers()["Appxpired-Name"],apache_request_headers()["Appxpired-". "Location"],$this->headers["Password"]);
        $this->makeUpHeadersFromDBReturn($ret);
        $retu = $this->db->get("household",["id","name","location","createUser.id"],["id"],[$ret[1]]);
        $this->outputString = json_encode($retu);
    }
}