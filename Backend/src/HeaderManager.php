<?php

/**
 * Created by PhpStorm.
 * User: Yannick Winter
 * Date: 02.05.16
 * Time: 17:18
 */



class HeaderManager {

    /**
     * @var Header[]
     */
    private $headers = [];

    /**
     * @var HeaderManager
     */
    private static $instance;

    /**
     * @return HeaderManager
     */
    public static function getInstance() {
        if (HeaderManager::$instance == null) {
            HeaderManager::$instance = new HeaderManager();
            return HeaderManager::getInstance();
        }
        return HeaderManager::$instance;
    }

    /**
     * Header constructor.
     */
    private function __construct() {}

    /**
     * Adds a header to the response headers
     * @param Header $header
     */
    public function addHeader(Header $header) {
        array_push($this->headers,$header);
    }

    /**
     * Adds the errorCode to the Error Header
     * @param Int $errorCode
     */
    public function addError($errorCode) {
        $i = 0;
        $added = false;
        while ($i < count($this->headers) && !$added) {
            $header = $this->headers[$i]->getKey();
            if ($header == "Error") {
                $value = $this->headers[$i]->getValue();
                $value .= strval($errorCode) . ";";
                $this->headers[$i] = new Header("Error",$value);
                $added = true;
            }
            $i++;
        }
        if (!$added) {
            $value = strval($errorCode) . ";";
            $this->addHeader(new Header("Error",$value));
        }
    }

    /**
     * Sets the headers for the output
     */
    public function setForHeadersResponse() {
        foreach ($this->headers as $header) {
            header($header->toHeaderString());
        }
    }

    public function getHeaders() {
        return $this->headers;
    }
}