<?php
	include_once('connection.php');

	$query = "
	SELECT 
	
	garbage_collecting_point_id,
	garbage_collecting_point_name,
	garbage_collecting_point_description,
	garbage_collecting_point_lat,
	garbage_collecting_point_lon

	FROM 
	
	garbage_collecting_point_details 
	
	WHERE garbage_collecting_point_active = 1";

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