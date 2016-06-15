<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 23.05.16
 * Time: 11:38
 */
class dbGet extends databaseConnection
{

    public function get($table, $selectFields, $whereFields, $values) {

        if (count($whereFields) == count($values)) {
            $sql = $this->getSQLString($table,$selectFields,$whereFields,$values);
            $this->connect();
            $result = $this->conn->query($sql);
            $rows = [];
            if ($result->num_rows > 0) {
                while($row = $result->fetch_assoc()) {
                    $this->conn->close();
                    array_push($rows,$row);
                }

                return $rows;
            } else {
                $this->conn->close();
                return [];
            }
        }

    }

    /**
     * @param $table
     * @param $selectFields
     * @param $whereFields
     * @param $values
     * @return string
     */
    private function getSQLString($table,$selectFields,$whereFields,$values) {
        $sql = "SELECT ";
        if ($selectFields[0] == "*") {
            $sql .= "*";
        }
        else {
            for ($i=0;$i<count($selectFields);$i++) {
                $sql .= "`" . $selectFields[$i] . "`";

                if ($i != count($selectFields)-1) {
                    $sql .= ", ";
                }
            }
        }
        $sql .= " FROM " . $table ." WHERE ";
        for ($i=0;$i<count($whereFields);$i++) {
            $sql .= "`" . $whereFields[$i] . "` = '" . $values[$i] . "'";
            if ($i != count($whereFields)-1) {
                $sql .= " AND ";
            }

        }
        return $sql;
    }


}