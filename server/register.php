<?php
	require_once('helpers.inc.php');
  // Connect to database
	require_once('global.inc.php');

	$db = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME, DB_PORT);
	if ($db->connect_error) {
		die("CONECTION ERROR (".$db->connect_errno.")".$db->connect_error);
	}

  // Check if correct params are specified
  if (
    (!isset($_POST['username']))
    || (!isset($_POST['pass']))
    || (!isset($_POST['nume']))
    || (!isset($_POST['prenume']))
    || (!isset($_POST['mail']))
  ) {
    Helpers::printResponse('error', 'Incorect parameters');
  }

  // Check if nume and prenume are not empty
  if ( (strlen($_POST['nume'] > 0))
    && (strlen($_POST['prenume'] > 0))
  ) {
    Helpers::printResponse('error', 'Campurile nume si prenume trebuie sa fie completate');
  }

  // Check if password is more than 6 characters
  if (strlen($_POST['pass']) < 6) {
    Helpers::printResponse('error', 'Password must be more than 6 characters');
  }

  // Check if user is not already defined
  $username = $db->real_escape_string($_POST['username']);
  $query = $db->query("SELECT * FROM users WHERE username = '" . $username . "'");
  if ($query !== FALSE) {
    if ($query->num_rows != 0) {
      Helpers::printResponse('error', 'Username already exists');
    }
  } else {
      Helpers::printResponse('error', 'Select query failed');
  }

  // Insert new user
  // hash pasword
  $hashedPass = crypt($_POST['pass']);
  $nume = $db->real_escape_string($_POST['nume']);
  $prenume = $db->real_escape_string($_POST['prenume']);
  $mail = $db->real_escape_string($_POST['mail']);

  $queryString =
    "INSERT INTO users (username, password, nume, prenume, mail)"
    . " VALUES ('" . $username . "', '" . $hashedPass . "', '"
    . $nume . "', '" . $prenume . "', '" . $mail .  "')";

  $query = $db->query($queryString);

  if ($query === FAlSE) {
    Helpers::printResponse('error', 'Insert failed');
  }

  Helpers::printResponse('success', 'User was created. Please sign in.');
