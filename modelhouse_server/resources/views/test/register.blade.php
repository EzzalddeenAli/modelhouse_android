<?php
echo 1;
	if(isset($_POST["Token"])){
echo 2;
		$token = $_POST["Token"];
		//데이터베이스에 접속해서 토큰을 저장

define("DB_HOST", "52.79.106.71");
	define("DB_USER", "root");
	define("DB_PASSWORD", "1234");
	define("DB_NAME", "modelhouse");

	define("GOOGLE_API_KEY", "AIzaSyCtvmgPka_WZEM3PAze0zSCGsy59qbyOGE"); 

		$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);
		$query = "INSERT INTO userTest(Token) Values ('$token') ON DUPLICATE KEY UPDATE Token = '$token'; ";
		mysqli_query($conn, $query);

		mysqli_close($conn);
	}
?>
