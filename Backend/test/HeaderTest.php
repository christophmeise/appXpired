<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 10.05.16
 * Time: 18:33
 */

require_once __DIR__ . '/../src/Header.php';

class HeaderTest extends PHPUnit_Framework_TestCase
{
    /**
     * @var Header
     */
    public $test;

    protected function setUp()
    {
        $this->test = new Header("Key","Value");
    }

    function testKey() {
        $key = $this->test->getKey();
        $this->assertTrue($key === "Key");
    }

    function testValue() {
        $value = $this->test->getValue();
        $this->assertTrue($value === "Value");
    }

    function testHeaderString() {
        $headerString = $this->test->toHeaderString();
        $this->assertTrue($headerString === "Key: Value");
    }
}
