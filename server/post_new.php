<?php 
	require_once('helpers.inc.php');
	require_once('global.inc.php');
	$db = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME,DB_PORT);
	if($db->connect_error){
		die("CONECTION ERROR (".$db->connect_errno.")".$db->connect_error);
	}
	
	

	$message = $_GET;
	if(!isset($message['type'])){
		die("Incorrect message");
	}
	
	
	$type = $message['type'];

	switch($type){
		
		//Post update
		case '1' : {
			if($message['update_type'] == 1){
				$query = "INSERT INTO updates (user_id,location_id,type,text) 
											VALUES ('".$message['user_id']."','".$message["location_id"]."',
													'".$message['update_type']."','".urldecode($message['update_text'])."')";
			}
			else{
				$query = "INSERT INTO updates (user_id,location_id,type,text,url) 
											VALUES ('".$message['user_id']."','".$message["location_id"]."',
													'".$message['update_type']."','".urldecode($message['update_text'])."',
													'".urldecode($message['url'])."' )";


			}
			$error_message = "Your update could not be posted. Please try again later";
			$success_message = "Your update has been posted succesfully";
		}break;
		//Post rating
		case '2' : {
		
			$query = "INSERT INTO rating (user_id,type,value,body,rated_entity_id) 
										VALUES ('".$message['user_id']."','".$message['rating_type']."',
												'".$message['value']."','".urldecode($message['body'])."','".$message['rated_entity_id']."')";
		
			$error_message = "Your rating could not be posted. Please try again later";
			$success_message = "Your rating has been posted succesfully";		
		
		}break;
		//Add to my places
		case '3' : {
			
			$query = "INSERT INTO user_places (user_id , location_id)
										VALUES ('".$message['user_id']."','".$message['location_id']."')";
			
			$error_message = "Your subscription could not be added. Please try again later";
			$success_message = "You have been subscribed to this location";
		}break;
		//Remove my places subscription
		case '4' :	{
			$query  = "DELETE FROM user_places WHERE user_id ='".$message['user_id']."' AND
													 location_id='".$message['location_id']."'";	
			$error_message = "Your subscription could not be removed. Please try again later";
			$success_message = "You have been unsubscribed from this location";										 
		}break;
		//Post user rating
		case '5':{
			$query = "INSERT INTO rating (user_id,type,value,body,rated_entity_id) 
										VALUES ('".$message['user_id']."','".$message['rating_type']."',
												'".$message['value']."','".urldecode($message['body'])."','".$message['rated_entity_id']."')";
		
			$error_message = "Your rating could not be posted. Please try again later";
			$success_message = "Your rating has been posted succesfully";	
		}break;
		
		case '6':{
			$query = "INSERT INTO locations (user_id,latitude,longitude,location_name,description,image_url) 
										VALUES ('".$message['user_id']."','".urldecode($message['latitude'])."',
												'".urldecode($message['longitude'])."','".urldecode($message['location_name'])."',
												'".urldecode($message['description'])."','".urldecode($message['image_url'])."')";
			$error_message = "Location could not be added. Please try again later";
			$success_message = "Location added succesfully";	
		}break;
		
	}
		
	if(!$db->query($query)) {
		Helpers::printResponse('error', $error_message);
	}else{
		Helpers::printResponse('success', $success_message);
	}
	
?>