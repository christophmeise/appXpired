<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 02.05.16
 * Time: 17:52
 */



class User
{
    private $id = "-1";
    private $username = "";
    private $password = "";
    private $token = "";
    private $email = "";
    private $firstname = "";
    private $lastname = "";

    /**
     * @var databaseConnection
     */
    private $db = null;

    /**
     * User constructor.
     * @param string $username
     * @param string $password
     * @param string $token
     * @param string $email
     * @param string $firstname
     * @param string $lastname
     */
    public function __construct($username = "", $password = "", $token = "", $email = "", $firstname = "", $lastname="")
    {
        $this->username = $username;
        $this->password = $password;
        $this->token = $token;
        $this->email = $email;
        $this->firstname = $firstname;
        $this->lastname = $lastname;
    }

    /**
     * @return int
     */
    public function getId()
    {
        if ($this->id == "-1") {
            $this->id = $this->db->getUserId(self);
            return $this->id;
        }

    }

    /**
     * @return string
     */
    public function getUsername()
    {
        return $this->username;
    }

    /**
     * @return string
     */
    public function getEmail()
    {
        return $this->email;
    }

    /**
     * @return string
     */
    public function getToken()
    {
        return $this->token;
    }

    /**
     * @return string
     */
    public function getFirstname()
    {
        return $this->firstname;
    }

    /**
     * @return string
     */
    public function getLastname()
    {
        return $this->lastname;
    }

    /**
     * @return string
     */
    public function getPassword()
    {
        return $this->password;
    }

    /**
     * returns true if the user is authorized
     * @return bool
     */
    public function authorized() {
        if ($this->db != null) {
            if (strlen($this->token) > 2) {
                $authorized = $this->db->checkAuthorizationWithToken($this->id,$this->token);
                if (!$authorized[0]) {
                    HeaderManager::getInstance()->addError($authorized[1]);
                }
                return $authorized[0];
            }

            $authorized = $this->db->checkAuthorizationWithPassword($this->id,$this->password);
            if ($authorized[0]) {
                $newToken = $authorized[1];
                HeaderManager::getInstance()->addHeader(new Header("Token",$newToken));
            }
            else {
                HeaderManager::getInstance()->addError($authorized[1]);
            }
            return $authorized[0];
        }
        return false;
    }
}