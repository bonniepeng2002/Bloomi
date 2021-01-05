<?php

require "conn.php";

$username = $_POST["Username"];
$password = $_POST["Password"];

if($conn){
	$sqlCheckEmail = "SELECT * FROM users WHERE username LIKE '$username'";
	$usernameQuery = mysqli_query($conn,$sqlCheckEmail);

	if(mysqli_num_rows($usernameQuery) > 0){
		$sqlLogin = "SELECT * FROM users WHERE username LIKE '$username' AND password LIKE '$password'";
		$loginQuery = mysqli_query($conn,$sqlLogin);
	
		if(mysqli_num_rows($loginQuery) > 0){
			echo "Login Success";
		}else{
			echo "Wrong Password";
		}
	}else{
		echo $username;
	}
}else{
	echo "Connection Error";
} 
?>

