<?php
	require_once('global.inc.php');
class Helpers
{

	public static function printResponse($status, $message) {
		$response = array(
					'status' => $status,
					'message' => $message,
					);
		echo json_encode($response);
		exit();
	}
	public static function get_location_rating($location_id) {

		$query = 'SELECT AVG(value) as val FROM rating WHERE rated_entity_id ='.$location_id.' AND
																				type =1';

		$db = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME);
		if($db->connect_error){
			die("CONECTION ERROR (".$db->connect_errno.")".$db->connect_error);
		}
		$result = $db->query($query);
		$x = mysqli_fetch_array($result);
		if($x['val']==null){
			$x = "unrated";
			return $x;
		}else{
			return $x['val'];
		}
	}

	public static function get_user_rating ($user_id) {
	
		$query = 'SELECT AVG(value) as val FROM rating WHERE rated_entity_id ='.$user_id.' AND
																				type =0';

		$db = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME);
		if($db->connect_error){
			die("CONECTION ERROR (".$db->connect_errno.")".$db->connect_error);
		}
		$result = $db->query($query);
		$x = mysqli_fetch_array($result);
		if($x['val']==null){
			$x = "unrated";
			return $x;
		}else{
			return $x['val'];
		}
	}
	public static function get_subscription_status($user_id, $location_id){
		$query = "SELECT * FROM user_places WHERE user_id='".$user_id."' AND
												  location_id='".$location_id."'";
		
		$db = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME);
		if($db->connect_error){
			die("CONECTION ERROR (".$db->connect_errno.")".$db->connect_error);
		}
		$result = $db->query($query);
		
		if($result->num_rows == 0){
			return "You are not subscribed to this location";	
		}else{
			return "You are subscribed to this location";
		}
	}
}
?>