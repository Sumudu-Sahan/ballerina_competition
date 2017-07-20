<?php
$userName = $_POST['userName'];
$password = $_POST['password'];
$token = $_POST['token'];

$encPassword = md5($password);

	include_once('connection.php');

	$query = "
	SELECT 
	
	user_id,
	user_email,
	user_name,
	user_role,
	user_account_type,
	user_image

	FROM 
	
	user_details WHERE user_active = 1 AND user_email = '$userName' AND user_password = '$encPassword'";

	$result = mysqli_query($connection, $query);
	
	if(mysqli_num_rows($result)){
	$query2 = "UPDATE user_details SET user_device_token = '$token' WHERE user_active = 1 AND user_email = '$userName' AND user_password = '$encPassword'";
	mysqli_query($connection, $query2);
	
	while($rows = mysqli_fetch_array($result)){
		$ress[] = $rows;
	}	
	}
mysqli_close($connection);
print json_encode(utf8ize($ress), JSON_UNESCAPED_SLASHES);

function utf8ize($d) {
    if (is_array($d)) {
        foreach ($d as $k => $v) {
            $d[$k] = utf8ize($v);
        }
    } else if (is_string ($d)) {
        return utf8_encode($d);
    }
    return $d;
}
        
?>