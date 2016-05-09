<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 09.05.16
 * Time: 11:27
 */
class UserTest extends PHPUnit_Framework_TestCase
{
    function constructTest() {
        $user = new User("kyc3","YannickWinter123456","");
        $this->assertEquals("kyc3",$user->getUsername());
        $this->assertEquals("YannickWinter123456",$user->getPassword());
    }

    function authorizedTest() {
        $user1 = new User("kyc3","YannickWinter123456","");
        $this->assertEquals(true,$user1->authorized());
        $user2 = new User("kyc3","falschesPW","");
        $this->assertEquals(false,$user2->authorized());

    }
}
