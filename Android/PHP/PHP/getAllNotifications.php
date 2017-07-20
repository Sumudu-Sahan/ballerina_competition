<?php
	include_once('connection.php');

	$query = "
	SELECT 
	
	notification_id,
    notification_mainValue,
    notification_subValue1,
    notification_subValue2,
    notification_subValue3,
    notification_subValue4,
    notification_added_datetime

	FROM 
	
	notification_details 
	
	WHERE notification_active = 1";

	$result = mysqli_query($connection, $query);

	while($rows = mysqli_fetch_array($result)){
		$ress[] = $rows;
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