<?php 

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
		//Cerere rating-uri primite de user
		case '1' : {
			$user_id = $message['user_id'];
			$query = "SELECT r.* , us.username FROM rating r 
												INNER JOIN users us
													WHERE rated_entity_id = ".$user_id." AND 
														  us.uid=r.user_id 
													ORDER BY r.rating_date DESC";
			$result = $db->query($query);
			
			if(!$result) {$response=null; break;}
			
			$response=array();
			$count=0;		
			
			while($x=mysqli_fetch_array($result)){
				$line = array();
				$line['type'] = $x['type'];
				$line['value'] = $x['value'];
				$line['body'] = $x['body'];
				$line['rating_date'] = $x['rating_date'];
				$line['user_id'] = $x['user_id'];
				$line['username']=$x['username'];
				
				$response[$count] = $line;
				$count+=1;
			}
			
			
		}break;
		//Cerere rating-uri locatie
		case '2' : {
			$location_id = $message['location_id'];
			$query = "SELECT r.* , us.username FROM rating r 
													INNER JOIN users us 
													WHERE   r.rated_entity_id=".$location_id." AND
															us.uid = r.user_id AND
															r.type=1
													ORDER BY r.rating_date DESC ";									

			$result = $db->query($query);
			if(!$result) {$response=null; break;}
			$response=array();
			$count=0;		
			while($x=mysqli_fetch_array($result)){
				$line = array();
				$line['type'] = $x['type'];
				$line['body'] = $x['body'];
				$line['value'] = $x['value'];
				$line['rating_date'] = $x['rating_date'];
				$line['user_id'] = $x['user_id'];
				$line['username']=$x['username'];
				
				$response[$count] = $line;
				$count+=1;
			}

		}break;
	}
	
	$reply = json_encode($response);
	echo $reply;

?>