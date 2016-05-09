<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 02.05.16
 * Time: 23:50
 */
class Printer
{
    /**
     * @var Request
     */
    private $request;

    /**
     * @var HeaderManager
     */
    private $headerManager;

    /**
     * Printer constructor.
     * @param Request $request
     */
    public function __construct(Request $request)
    {
        $this->request = $request;
        $this->headerManager = HeaderManager::getInstance();
    }

    /**
     * Sets the response Headers and echos the output string
     */
    public function execute() {
        $this->request->execute();
        $this->headerManager->setForHeadersResponse();
        echo $this->request->getOutputString();
    }
}