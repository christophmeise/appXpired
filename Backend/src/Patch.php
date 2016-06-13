<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 02.05.16
 * Time: 23:28
 */



class Patch extends Request
{
    public function execute() {
        $setFieldsAndValues = $this->getSetFieldsAndValues();
        $setFields = $setFieldsAndValues[0];
        $setValues = $setFieldsAndValues[1];

        $whereFieldsAndValues = $this->getWhereFieldsAndValues();
        $whereFields = $whereFieldsAndValues[0];
        $whereValues = $whereFieldsAndValues[1];

        if ($this->user->authorized()) {
            $ret = $this->db->update($this->usedHeaders["table"],$setFields,$setValues,$whereFields,$whereValues);
            if ($ret[0]) {
                HeaderManager::getInstance()->addHeader(new Header("Success","true"));
                HeaderManager::getInstance()->addHeader(new Header("LastId",$ret[1]));
            }
            else {
                $this->makeUpHeadersFromDBReturn($ret);
            }
        }
        $this->outputString =  "";
    }
}