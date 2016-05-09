<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 02.05.16
 * Time: 17:31
 */
abstract class Request {

    /**
     * @var databaseConnection
     */
    protected $db;
    /**
     * @var array
     */
    protected $headers;
    /**
     * @var User
     */
    protected $user;

    /**
     * @var string
     */
    protected $outputString;

    /**
     * Request constructor.
     * @param array $headers
     * @param User $user
     */
    public function __construct(array $headers, User $user)
    {
        $this->headers = $headers;
        $this->user = $user;
        $this->db = databaseConnection::getInstance();
    }


    public function getOutputString() {
        return $this->outputString;
    }

    protected function makeUpHeadersFromDBReturn($ret) {
        $z = 0;
        $errors = null;
        for ($i=0;$i < count($ret);$i++) {
            if ($i == 0 and $ret[$i] == true) {
                HeaderManager::getInstance()->addHeader(new Header("Success","true"));
            }
            else if ($i == 0 and $ret[$i] == false) {
                HeaderManager::getInstance()->addHeader(new Header("Success","false"));
            }
            else if ($i != 0 and $ret[0] == false){
                $errors = strval($ret[$i]) . ";";
                $z += 1;
            }
        }
        if ($ret[0] ==false) {
            HeaderManager::getInstance()->addError($errors);
        }
    }

    public function execute() {
        //implement in child classes
    }

    /**
     * @return array
     */
    protected function getWhereFieldsAndValues() {
        $fields = null;
        $values = null;

        for ($i = 0; $i < count($this->headers["Wherevalues"]); $i++) {
            $fields[$i] = $this->headers["Wherevalues"][$i][0];
            $values[$i] = $this->headers["Wherevalues"][$i][1];
        }
        return [$fields,$values];
    }

    /**
     * @return array
     */
    protected function getSetFieldsAndValues() {
        $i = 0;
        $values = [];
        $fields = [];
        foreach ($this->headers["Setvalues"] as $value => $field) {
            $values[$i] = $field[1];
            $fields[$i] = $field[0];
            $i = $i+1;
        }

        return [$fields,$values];
    }
}