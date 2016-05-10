<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 02.05.16
 * Time: 22:15
 */



class Post extends Request
{
    /**
     * @return string
     */
    public function execute() {
        if ($this->user->authorized()) {
            $setFieldsAndValues = $this->getSetFieldsAndValues();
            $fields = $setFieldsAndValues[0];
            $values = $setFieldsAndValues[1];

            $ret = $this->db->insert($this->headers["Table"],$fields,$values);
            if ($ret[0]) {
                HeaderManager::getInstance()->addHeader(new Header("Success","true"));
                HeaderManager::getInstance()->addHeader(new Header("LastId",$ret[1]));
            }
            else {
                HeaderManager::getInstance()->addHeader(new Header("Success","false"));
            }
        }
        $this->outputString =  "";
    }
}