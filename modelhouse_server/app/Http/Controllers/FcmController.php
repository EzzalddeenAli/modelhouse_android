<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use DB;

class FcmController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
//return view('test.push_notification');


	//데이터베이스에 접속해서 토큰들을 가져와서 FCM에 발신요청
	$sql = "Select Token From userTest";

        $result = DB::select($sql);
	$tokens = array();

	if(sizeof($result) > 0 ){

		foreach ($result as $Rresult) {
			$tokens[] = $Rresult->Token;
		}
	}


	
//        $myMessage = $_POST['message']; //폼에서 입력한 메세지를 받음
//	if ($myMessage == ""){
		$myMessage = "새글이 등록되었습니다.";
//	}

	$message = array("message" => $myMessage);

// function send_notification ($tokens, $message)
//      {
                $url = 'https://fcm.googleapis.com/fcm/send';
                $fields = array(
                         'registration_ids' => $tokens,
                         'data' => $message
                        );

                $headers = array(
                        'Authorization:key =' . 'AAAAieQVIvY:APA91bFqu3E3CqzFJNefWiKV4BvMUFHUBA6gkenQ8ZqDWb6PmSO3ohsVMetud5IQ7pdKid_OsADmbwJx1eJSUUmB3YfEJ4J6Whz_7Mtr0tTdn32CkIw3bMKDjo90IwH4a5nB8pzhKcaF',
                        'Content-Type: application/json'
                        );

           $ch = curl_init();
       curl_setopt($ch, CURLOPT_URL, $url);
       curl_setopt($ch, CURLOPT_POST, true);
       curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
       curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
       curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);
       curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
       curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
       $result = curl_exec($ch);
       if ($result === FALSE) {
           die('Curl failed: ' . curl_error($ch));
       }
       curl_close($ch);
      // return $result;
    //    }


	//$message_status = send_notification($tokens, $message);
//	echo $message_status;
echo $result;
 
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        
echo 1;
//	if(isset($request->input("Token"))){
echo 2;
		$token = $request->input("Token");
		//?곗씠?곕쿋?댁뒪???묒냽?댁꽌 ?좏겙?????
//define("DB_HOST", "52.79.106.71");
//	define("DB_USER", "root");
//	define("DB_PASSWORD", "1234");
//	define("DB_NAME", "modelhouse");

//	define("GOOGLE_API_KEY", "AIzaSyCtvmgPka_WZEM3PAze0zSCGsy59qbyOGE"); 

//		$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);


		$query = "INSERT INTO userTest(Token) Values ('$token') ON DUPLICATE KEY UPDATE Token = '$token' ";
DB::insert($query);
//		mysqli_query($conn, $query);

//		mysqli_close($conn);
//	}
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        //
    }
}
