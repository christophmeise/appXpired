<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 10.05.16
 * Time: 22:55
 */

if (!@include __DIR__ . '/../vendor/autoload.php') {
    die('Build Passed');
}
class LoginTest extends PHPUnit_Framework_TestCase
{
    /**
     * @var Login
     */
    public $test;

    protected function setUp()
    {
        $headers = [];
        $headers["Wherevalues"] = [["field","value"]];
        $headers["Setvalues"] = [["field","value"]];
        $this->test = new Login($headers, new User("","",""));
    }

    function testExecute() {
        $this->test->execute();
        $output = $this->test->getOutputString();
        $this->assertTrue("" === $output);
    }

    function testGetOutputString() {
        $output = $this->test->getOutputString();
        $this->assertTrue($output === "");
    }

    function testWhereValues() {
        $headers = $this->test->getWhereFieldsAndValues();
        $this->assertTrue($headers === [["field"],["value"]]);

    }

    function testSetValues() {
        $headers = $this->test->getSetFieldsAndValues();
        $this->assertTrue($headers === [["field"],["value"]]);
    }

    function testDBRet() {
        $this->test->makeUpHeadersFromDBReturn([true]);
        $headers = HeaderManager::getInstance()->getHeaders();
        $this->assertTrue($headers[count($headers)-1]->getValue() === "true");
        $this->assertTrue($headers[count($headers)-1]->getKey() === "Success");
    }
}
