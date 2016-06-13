<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 09.05.16
 * Time: 11:27
 */
require_once '/home/travis/build/Shark919/appXpired/Backend/src/User.php';
require_once '/home/travis/build/Shark919/appXpired/Backend/src/databaseConnection.php';
require_once '/home/travis/build/Shark919/appXpired/Backend/src/ErrorCode.php';

class UserTest extends PHPUnit_Framework_TestCase
{
    /**
     * @var User
     */
    public $test;

    protected function setUp() {
        $this->test = new User("kyc3","YannickWinter123456","token","test@test.de","Yannick","Winter");
    }

    function testUsername() {
        $username = $this->test->getUsername();
        $this->assertTrue($username === "kyc3");
    }

    function testFirstName() {
        $firstName = $this->test->getFirstname();
        $this->assertTrue($firstName === "Yannick");
    }

    function testPassword() {
        $password = $this->test->getPassword();
        $this->assertTrue($password === "YannickWinter123456");
    }

    function testLastName() {
        $lastName = $this->test->getLastname();
        $this->assertTrue($lastName === "Winter");
    }

    function testEmail() {
        $email = $this->test->getEmail();
        $this->assertTrue($email === "test@test.de");
    }

    function testToken() {
        $token = $this->test->getToken();
        $this->assertTrue($token === "token");
    }

    function testAuthorized() {
        $this->assertTrue($this->test->authorized() == false);
    }
}
