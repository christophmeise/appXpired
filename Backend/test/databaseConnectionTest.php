<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 11.05.16
 * Time: 09:41
 */

require_once __DIR__ . '/../src/databaseConnection.php';
require_once __DIR__ . '/../src/Post.php';
require_once __DIR__ . '/../src/RequestFactory.php';
require_once __DIR__ . '/../src/HeaderManager.php';
require_once __DIR__ . '/../src/Header.php';

class databaseConnectionTest extends PHPUnit_Framework_TestCase
{
    /**
     * @var databaseConnection
     */
    public $test;

    protected function setUp()
    {
        $this->test = databaseConnection::getInstance();
    }

    function testGetInstance() {
        $instance2 = databaseConnection::getInstance();
        $this->assertTrue($instance2 === $this->test);
    }

    function testPasswordValid() {
        $valid = $this->test->validatePassword("Abcdfghijkl980");
        $this->assertTrue($valid[0] === true);
        $valid = $this->test->validatePassword("");
        $this->assertTrue($valid[0] === false);
        $this->assertTrue($valid[1] === ErrorCode::UNAUTHORIZED);
    }

    function testGetHash() {
        $hash = $this->test->getHashedPw("Abcdfghijkl980");
        $this->assertTrue($hash != "Abcdfghijkl980");
    }

    function testGetConnected() {
        $connected = $this->test->getConnected();
        $this->assertTrue($connected === false);
    }

    function testGetServerName() {
        $servername = $this->test->getServerName();
        $this->assertTrue($servername === "db596387337.db.1and1.com");
    }

    function testGetUserName() {
        $username = $this->test->getUserName();
        $this->assertTrue($username === "dbo596387337");
    }

    function testGetPassword() {
        $password = $this->test->getPassword();
        $this->assertTrue($password === "aMy-SeM2");
    }

    function testGetDbName() {
        $dbname = $this->test->getDbname();
        $this->assertTrue($dbname === "db596387337");
    }
}
