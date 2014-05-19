	Raspunsurile la request-urile trimise de client sunt in format JSON.

Format mesaje :

$subdomeniu = "";

$subdomeniu/get_user_info.php:
	- parametri in request : uid -> id-ul utilizatorului pentru care se cer informatii

	- raspuns: 
			{
				"nume" => "...",
				"prenume" => "...",
				"username" => "...",
				"raiting" => "...",
				"mail" => "...",
			}

$subdomeniu/get_location_info : 

	-get_location_info.php?type=1&location_id=x&user_id=y

	-Exemplu Output :
		{"location_id":"1",
		"location_name":"Location 1",
		"latitude":"45","longitude":"45",
		"creation_date":"2013-11-24 11:13:05",
		"image_url":null,
		"rating":"5.8333",
		"user_id":"3",
		"username":"eugen"
		"subscription":"You are subscribed to this location"}

$subdomeniu/get_update : 

	a.Return update-uri recente sortate dupa data
	
		-get_update.php/?type=2&user_id=x;

		-Exemplu Output : 
			[{"location_id":"1",
			"location_name":"Location 1",
			"update_id":"2",
			"type":"1",
			"text":"Test Test Test",
			"date":"2013-11-24 00:45:00",
			"user_id":"2",
			"username":"vlad"},
			{...}]


	b.Return update-uri 'MyPlaces' ale user-ului cu ID=x
	
		-get_update.php/?type=1&user_id=x;

		-Exemplu Output :
			[{"location_id":"1",
			"location_name":"Location 1",
			"update_id":"2",
			"type":"1",
			"text":"Test Test Test",
			"date":"2013-11-24 00:45:00",
			"user_id":"2",
			"username":"vlad"},
			{...}]

$subdomeniu/get_rating : 
	
	Lista rating-urilor pe care le-a primit user-ul cu id x

		-get_rating.php?type=1&user_id=x

	Lista rating-urilor pe care le-a primit locatia cu id x

		-get_rating.php?type=2&location_id=x


$subdomeniu/post_new : 

	a. Update nou
		Tip text :

		-post_new.php?type=1&user_id=x&location_id=x&update_type=1&update_text=x
		
		Tip media :		
		
		-post_new.php?type=1&user_id=x&location_id=x&update_type=<2||3>&update_text=x&url=x


	b. Rating nou
		
		-post_new.php?type=2&user_id=x&rating_type=x&value=x&body=x&rated_entity_id=x


	c. Add to my places

		-post_new.php?type=3&user_id=x&location_id=x 

	d. Remove from my places 

		-post_new.php?type=4&user_id=x&location_id=x 
