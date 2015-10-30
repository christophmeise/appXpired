<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 22.10.15
 * Time: 12:17
 */


class errorCodes {
    function __construct()
    {

    }

    const invalidData =  001; //- the entered data is not valid (e.g. emailadress to short)
    const usernameAlreadyRegistered = 002; // - username already registered
    const emailadressAlreadyRegistered = 003; //- emailadress already registered
    const invalidPassword = 004; //- password not valid
    const mySQLConnectionError = 100; //- mysql connection error
    const updateValuesIncorrect = 101; //- update values are incorrect

    const userUnauthorizedForHousehold = 200; //- user is not authorized to access the requested household

    const unauthorized =  404; //- unauthorized
    const tokenHasExpired = 403; //- token has expired

}
