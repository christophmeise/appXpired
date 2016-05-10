<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 02.05.16
 * Time: 21:47
 */



class RequestFactory
{

    const HEADERPREFIX = "Appxpired-";
    const HEADERNAMES = ["Username", "Password", "Household", "Household-Pw", "Table", "Wherevalues", "Setvalues", "Selectvalues", "Token","Expand"];

    /**
     * @param $apache_headers
     * @param string $source
     * @return Request
     */
    public static function create($apache_headers, $source = "api")
    {
        $headers = RequestFactory::parseHeaders($apache_headers);
        $user = RequestFactory::createUserObject($headers);
        if ($source == "user") {
            $request = RequestFactory::createUserManagementRequestObject($headers,$user);
            return $request;
        }
        if ($source == "api") {
            $request = RequestFactory::createRequestObject($headers,$user);
            return $request;
        }
        if ($source == "household") {
            $request = RequestFactory::createRequestObject($headers,$user);
            return $request;
        }
    }

    /**
     * Parse the apache request headers to Appxpired Headers
     * @param array $apacheRequestHeaders
     */
    private static function parseHeaders(Array $apacheRequestHeaders) {
        $headers = $apacheRequestHeaders;
        foreach (RequestFactory::HEADERNAMES as $headerName) {
            $headers[$headerName] = $headers[RequestFactory::HEADERPREFIX . $headerName];
        }
        return RequestFactory::processHeaders($headers);
    }

    private static function processHeaders($headers) {
        $headers["Wherevalues"] = explode(";",$headers["Wherevalues"]);
        for ($i = 0;$i<count($headers["Wherevalues"]);$i++) {
            $headers["Wherevalues"][$i] = explode(",",$headers["Wherevalues"][$i]);
        }

        $headers["Setvalues"] = explode(";",$headers["Setvalues"]);

        for ($i = 0;$i<count($headers["Setvalues"]);$i++) {
            $headers["Setvalues"][$i] = explode(",",$headers["Setvalues"][$i]);
        }

        $headers["Selectvalues"] = explode(";",$headers["Selectvalues"]);
        if ($headers["Expand"] == "True") {
            $headers["Expand"] = true;
        }
        else {
            $headers["Expand"] = false;
        }
        return $headers;
    }

    private static function createUserObject($headers) {
        return new User($headers["Username"],$headers["Password"],$headers["Token"],$headers["Email"],$headers["Firstname"],$headers["Lastname"]);
    }

    /**
     * get the HTTP Method and create the Request object
     *
     * @param $headers array
     * @param $user User
     * @return Request
     */
    private static function createRequestObject($headers,$user) {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                return new Get($headers,$user);
            case "POST":
                return new Post($headers,$user);
            case "DELETE":
                return new Delete($headers,$user);
            case "PATCH":
                return new Patch($headers,$user);
        }
    }

    /**
     * @param $headers array
     * @param $user User
     * @return Request
     */
    private static function createUserManagementRequestObject($headers,$user) {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "GET":
                return new Login($headers,$user);
            case "POST":
                return new UserCreate($headers,$user);
        }
    }

    /**
     * @param $headers array
     * @param $user User
     * @return Request
     */
    private static function createHouseholdManagementRequestObject($headers,$user) {
        switch ($_SERVER['REQUEST_METHOD']) {
            case "POST":
                return new HouseholdCreate($headers,$user);
        }
    }
}