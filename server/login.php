<?php

  require_once('helpers.inc.php');
  // Connect to database
	require_once('global.inc.php');

	$db = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME, DB_PORT);
	if ($db->connect_error) {
		die("CONECTION ERROR (".$db->connect_errno.")".$db->connect_error);
	}

  $params = $_POST;
  // Check if correct params are specified
  if (
    (!isset($params['username']))
    || (!isset($params['pass']))
  ) {
    Helpers::printResponse('error', 'Incorect parameters');
  }

  // Check if correct user and password were specified
  $username = $db->real_escape_string($params['username']);
  $userPass = $params['pass'];


  // Check if user is defined
  $query = $db->query("SELECT * FROM users WHERE username = '" . $username . "'");
  if ($query === FALSE) {
    Helpers::printResponse('error', 'Select query failed');
  }
  if ($query->num_rows !== 1) {
    Helpers::printResponse('error', 'Username does not exist');
  }

  $responseObj = $query->fetch_object();

  $hashedPass = $responseObj->password;

  if (crypt($userPass, $hashedPass) != $hashedPass) {
    Helpers::printResponse('error', 'Incorect password');
  }

  Helpers::printResponse('success', $responseObj->uid);
