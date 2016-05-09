<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 22.10.15
 * Time: 12:17
 */


class ErrorCode {
    function __construct()
    {

    }
    //- the entered data is not valid (e.g. emailadress to short)
    const INVALIDDATA =  001;
    // - username already registered
    const USERNAMEALREADYREGISTERED = 002;
    //- emailadress already registered
    const EMAILADRESSALREADYREGISTERED = 003;
    //- password not valid
    const INVALIDPASSWORD = 004;
    //- mysql connection error
    const MYSQLCONNECTIONERROR = 100;
    //- update values are incorrect
    const UPDATEVALUESINCORRECT = 101;
    const DELETEVALUESINCORRECT = 102;
    //- user is not authorized to access the requested household
    const USERUNAUTHORIZEDFORHOUSEHOLD = 200;
    //- UNAUTHORIZED
    const UNAUTHORIZED =  404;
    //- token has expired
    const TOKENHASEXPIRED = 403;

}
