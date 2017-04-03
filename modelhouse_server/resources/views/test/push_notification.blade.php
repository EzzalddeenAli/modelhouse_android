<?php 
    
    function send_notification ($tokens, $message)
    {
        $url = 'https://fcm.googleapis.com/fcm/send';
        $fields = array(
             'registration_ids' => $tokens,
             'data' => $message
            );
 
        $headers = array(
            'Authorization:key =' . 'AAAA__hPr4o:APA91bGFeZfdlnV5Dvlt6VFHC_6IwjVgrU68NibYBat6saoUiZb-Ct6Dg_Wa6uQDQynCbR_3noXgh7OQZc3pzk4UKqaiq0ke6WobvVai2-SeeOZ2iBK-8h7-jrF54d6Qk6no12bXqzL4',
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
       return $result;
    }
    
 
    //데이터베이스에 접속해서 토큰들을 가져와서 FCM에 발신요청
    
    $conn = mysqli_connect("52.79.106.71", "root", "1234", "modelhouse");
 
    $sql = "Select token From userTest";
 
    $result = mysqli_query($conn,$sql);
    $tokens = array();
 
    if(mysqli_num_rows($result) > 0 ){
 
        while ($row = mysqli_fetch_assoc($result)) {
            $tokens[] = $row["token"];
        }
    }
    
    mysqli_close($conn);
    
//    $myMessage = $_POST['message']; //폼에서 입력한 메세지를 받음
//    if ($myMessage == ""){
        $myMessage = "새글이 등록되었습니다.";
//    }
 
    $message = array("message" => $myMessage);
    $message_status = send_notification($tokens, $message);
    echo $message_status;
?>
