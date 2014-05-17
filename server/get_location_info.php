<?php
	require_once('helpers.inc.php');
	require_once('global.inc.php');
	$db = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME,DB_PORT);
	if($db->connect_error){
		die("CONECTION ERROR (".$db->connect_errno.")".$db->connect_error);
	}
	
	if(!isset($_GET['type'])){
		die("Incorrect message");
	}
  $message = $_GET;
	$type = $message['type'];
	switch($type){
		case '1': {
			$location_id = $message['location_id'];
			$user_id = $message['user_id'];
			$query = "SELECT l.* , us.username FROM locations l 
								INNER JOIN users us
								WHERE l.location_id = '".$location_id."' AND 
									  l.user_id = us.uid";
									  
			$result = $db->query($query);
			if(!$result) {$response=null; break;}
			$response=array();
			$x=mysqli_fetch_array($result);
			$response['location_id'] = $x['location_id'];
			$response['location_name'] = $x['location_name'];
			$response['latitude'] = $x['latitude'];
			$response['longitude'] = $x['longitude'];
			$response['creation_date'] = $x['creation_date'];
			$response['description'] = $x['description'];
			$response['image_url'] = $x['image_url'];
			$response['rating'] =  Helpers::get_location_rating($location_id);
			$response['user_id'] = $x['user_id'];
			$response['username']=$x['username'];
			$response['subscription']= Helpers::get_subscription_status($message['user_id'],$location_id);

		}break;
	}
	
	
	
	echo json_encode($response);

?>
