<?php

$db_name = "bloomi";
$mysql_username = "root";
$mysql_password = "";
$server_name = "localhost";
$conn = mysqli_connect($server_name, $mysql_username, $mysql_password, $db_name);

if(!$conn){
    die("ERROR: Could not connect. " . mysqli_connect_error());
} 

?>