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
	echo $message;
	$type = $message['type'];
	echo $type;
	switch($type){
		
		case '1' : {
			$user_id = $message['user_id'];
			
			$query = "SELECT l.location_name,l.location_id , up.* , users.username FROM updates up 
																		INNER JOIN locations l
											   							INNER JOIN user_places us
																		INNER JOIN users
																		WHERE us.user_id = '".$user_id."' AND
																		up.location_id = us.location_id AND
																		l.location_id = us.location_id AND
																		users.uid = up.user_id
																		ORDER BY up.date DESC";
			
			
			echo $query;
			$result = $db->query($query);
			if(!$result) {$response=null; break;}
			$response=array();
			$count=0;		
			while($x=mysqli_fetch_array($result)){
				$line = array();
				$line['location_id'] = $x['location_id'];
				$line['location_name'] = $x['location_name'];
				$line['update_id'] = $x['update_id'];
				$line['type'] = $x['type'];
				$line['text'] = $x['text'];
				$line['date'] = $x['date'];
				$line['user_id'] = $x['user_id'];
				$line['username']=$x['username'];
				$line['location_rating'] =  Helpers::get_location_rating($x['location_id']);
				if($x['type']!=1){
					$line['url'] = $x['url'];
				}
				
				$response[$count] = $line;
				$count+=1;
			}
			
		}break;
			
		case '2' : {
			$user_id = $message['user_id'];
			
			$query = "SELECT l.location_name,l.location_id , up.* , users.username FROM updates up 
																		INNER JOIN locations l
																		INNER JOIN users
																		WHERE up.location_id = l.location_id AND
																		users.uid = up.user_id
																		ORDER BY up.date DESC";
			
			

			$result = $db->query($query);
			if(!$result) {$response=null; break;}
			$response=array();
			$count=0;		
			while($x=mysqli_fetch_array($result)){
				$line = array();
				$line['location_id'] = $x['location_id'];
				$line['location_name'] = $x['location_name'];
				$line['update_id'] = $x['update_id'];
				$line['type'] = $x['type'];
				$line['text'] = $x['text'];
				$line['date'] = $x['date'];
				$line['user_id'] = $x['user_id'];
				$line['username']=$x['username'];
				$line['location_rating'] =  Helpers::get_location_rating($x['location_id']);
				if($x['type']!=1){
					$line['url'] = $x['url'];
				}
				
				$response[$count] = $line;
				$count+=1;
			}			
		}break;
	
		case '3' : {
			$location_id = $message['location_id'];
			
			$query = "SELECT  up.* , users.username FROM updates up 
													INNER JOIN users
															WHERE up.location_id = '".$location_id."' AND
															users.uid = up.user_id
															ORDER BY up.date DESC";
			$result = $db->query($query);
			if(!$result) {$response=null; break;}
			$response=array();
			$count=0;		
			while($x=mysqli_fetch_array($result)){
				$line = array();
				$line['update_id'] = $x['update_id'];
				$line['type'] = $x['type'];
				$line['text'] = $x['text'];
				$line['date'] = $x['date'];
				$line['user_id'] = $x['user_id'];
				$line['username']=$x['username'];
				$line['location_rating'] =  Helpers::get_location_rating($x['location_id']);
				if($x['type']!=1){
					$line['url'] = $x['url'];
				}
				
				$response[$count] = $line;
				$count+=1;
			}																
			
		}break;
	/*
		case '4' : {}break;
	*/
	}
	
	$reply = json_encode($response);
	echo $reply;
?>