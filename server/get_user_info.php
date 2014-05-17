<?php
  require_once('helpers.inc.php');
  // Connect to database
	require_once('global.inc.php');

	$db = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME, DB_PORT);
	if ($db->connect_error) {
		die("CONECTION ERROR (".$db->connect_errno.")".$db->connect_error);
	}

  $params = $_GET;

  if (!isset($params['uid'])) {
    Helpers::printResponse('error', 'Incorect parameters');
  }

  $userId = $db->real_escape_string($params['uid']);

  $query = $db->query("SELECT * FROM users WHERE uid = '" . $userId . "'");

  if ($query !== FALSE) {
    if ($query->num_rows == 0) {
      Helpers::printResponse('error', 'User does not exist');
    }
  }

  $responseObj = $query->fetch_object();
  $message = array(
    'username' => $responseObj->username,
    'nume' => $responseObj->nume,
    'prenume' => $responseObj->prenume,
    'mail' => $responseObj->mail,
    'rating' => Helpers::get_user_rating($userId),
  );

  Helpers::printResponse('success', $message);
