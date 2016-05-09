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

    const INVALIDDATA =  001; //- the entered data is not valid (e.g. emailadress to short)
    const USERNAMEALREADYREGISTERED = 002; // - username already registered
    const EMAILADRESSALREADYREGISTERED = 003; //- emailadress already registered
    const INVALIDPASSWORD = 004; //- password not valid
    const MYSQLCONNECTIONERROR = 100; //- mysql connection error
    const UPDATEVALUESINCORRECT = 101; //- update values are incorrect
    const DELETEVALUESINCORRECT = 102;

    const USERUNAUTHORIZEDFORHOUSEHOLD = 200; //- user is not authorized to access the requested household

    const UNAUTHORIZED =  404; //- UNAUTHORIZED
    const TOKENHASEXPIRED = 403; //- token has expired

}
