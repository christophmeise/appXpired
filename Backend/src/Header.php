<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 02.05.16
 * Time: 18:34
 */



class Header
{
    /**
     * @var string
     */
    private $key;
    /**
     * @var string
     */
    private $value;

    /**
     * @return string
     */
    public function getKey()
    {
        return $this->key;
    }

    /**
     * @return string
     */
    public function getValue()
    {
        return $this->value;
    }

    /**
     * Header constructor.
     * @param string $key
     * @param string $value
     */
    public function __construct($key, $value)
    {
        $this->key = $key;
        $this->value = $value;
    }

    /**
     * @return string
     */
    public function toHeaderString() {
        return $this->key . ": " . $this->value;
    }
}