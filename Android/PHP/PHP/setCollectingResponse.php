<?php
	include_once('connection.php');
	
	$pointID = $_POST['pointID'];
	$results = $_POST['results'];

	$query = "
	UPDATE

	garbage_point_driver_tractor_merge_details
	SET 
	
	garbage_point_driver_tractor_merge_status = $results

	WHERE garbage_point_driver_tractor_merge_active = 1 AND
	garbage_point_driver_tractor_merge_point_id = $pointID";

	mysqli_query($connection, $query);
	mysqli_close($connection);        
?>