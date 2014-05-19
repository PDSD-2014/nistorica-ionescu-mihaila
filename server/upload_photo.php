<?php
require_once('helpers.inc.php');
$target_path  = "./img/";
$target_path = $target_path . basename( $_FILES['uploadedfile']['name']);
if(move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_path)) {
	Helpers::printResponse("success", "The file ".  basename( $_FILES['uploadedfile']['name']).
 " has been uploaded");
} else{
 Helpers::printResponse("error","There was an error uploading the file, please try again!");
}
?>