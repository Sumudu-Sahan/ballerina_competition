<?php
date_default_timezone_set('Asia/Colombo');
 $date = date('Y-m-d H:i:s');
 
	$loginType = $_POST['loginType'];
	$userName = $_POST['userName'];
	$userEmail = $_POST['userEmail'];
	$userImage = $_POST['userImage'];
	$token = $_POST['token'];
	
	include_once('connection.php');
	
	$query1 = "SELECT user_id FROM user_details WHERE user_active = 1 AND user_email = '$userEmail' AND user_account_type = $loginType";
	$result = mysqli_query($connection, $query1);
	
	if(mysqli_num_rows($result) == 0){
	$query2 = "INSERT INTO user_details (user_email, user_name, user_image, user_account_type, user_device_token) 
	VALUES ('$userEmail', '$userName', '$userImage', $loginType, '$token')";
	mysqli_query($connection, $query2);	
	$lastID = mysqli_insert_id($connection);
	
	$arr = array('userID' => $lastID);
	print json_encode($arr);
	}
	
	else{
		$query3 = "UPDATE user_details SET user_device_token = '$token' WHERE user_active = 1 AND user_email = '$userEmail' AND user_account_type = $loginType";
		mysqli_query($connection, $query3);
		while($rows = mysqli_fetch_array($result)){
			$arr = array('userID' => $rows['user_id']);
			print json_encode($arr);			
		}
	}
			
mysqli_close($connection);        
?>