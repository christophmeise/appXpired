<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 02.05.16
 * Time: 17:30
 */

class Get extends Request {

    /**
     * Executes the request
     * @return string
     */
    public function execute() {
        if (!$this->user->authorized()) {
            HeaderManager::getInstance()->addHeader(new Header("Success", "false"));
            return parent::getOutputString();
        }
        $whereFieldsAndValues = $this->getWhereFieldsAndValues();
        $fields = $whereFieldsAndValues[0];
        $values = $whereFieldsAndValues[1];

        $dbOut = $this->db->get($this->headers["Table"], $this->headers["Selectvalues"], $fields, $values);

        HeaderManager::getInstance()->addHeader(new Header("Success", "true"));
        unset($dbOut["password"]);
        unset($dbOut["token"]);
        $this->outputString = json_encode($dbOut);
    }
}