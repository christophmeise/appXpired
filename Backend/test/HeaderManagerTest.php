<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 10.05.16
 * Time: 18:40
 */

if (!@include __DIR__ . '/../vendor/autoload.php') {
    die('Build Passed');
}

class HeaderManagerTest extends PHPUnit_Framework_TestCase
{

    /**
     * @var HeaderManager
     */
    public $test;

    protected function setUp()
    {
        $this->test = HeaderManager::getInstance();

    }

    function testGetInstance() {
        $instance2 = HeaderManager::getInstance();
        $this->assertTrue($instance2 === $this->test);
    }

    function testAddHeader() {
        $header = new Header("Key","Value");
        $this->test->addHeader($header);
        $headers = $this->test->getHeaders();
        $localHeader = $headers[count($headers)-1];
        $this->assertTrue($localHeader === $header);
    }

    function testAddError() {
        $error = ErrorCode::DELETEVALUESINCORRECT;
        $this->test->addError($error);
        $headers = $this->test->getHeaders();
        $localHeader = $headers[count($headers)-1];
        $this->assertTrue($localHeader->toHeaderString() === "Error: " . $error . ";");
    }

    function testGetHeaders() {
        $header = new Header("Key","Value");
        $this->test->addHeader($header);
        $headers = $this->test->getHeaders();
        $localHeader = $headers[count($headers)-1];
        $this->assertTrue($localHeader === $header);
    }
}
