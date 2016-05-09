<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 02.05.16
 * Time: 23:11
 */
class Delete extends Request
{
    public function execute() {
        if ($this->user->authorized()) {
            $whereFieldsAndValues = $this->getWhereFieldsAndValues();
            $fields = $whereFieldsAndValues[0];
            $values = $whereFieldsAndValues[1];
            $ret = $this->db->delete($this->headers["Table"],$fields,$values);
            if ($ret[0]) {
                HeaderManager::getInstance()->addHeader(new Header("Success","true"));
                HeaderManager::getInstance()->addHeader(new Header("LastId",$ret[1]));
            }
            else {
                parent::makeUpHeadersFromDBReturn($ret);
            }
        }
        $this->outputString = "";
    }
}