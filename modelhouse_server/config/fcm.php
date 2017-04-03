<?php

return [
	'driver'      => env('FCM_PROTOCOL', 'http'),
	'log_enabled' => true,

	'http' => [
		'server_key'       => env('FCM_SERVER_KEY', 'AAAA__hPr4o:APA91bGFeZfdlnV5Dvlt6VFHC_6IwjVgrU68NibYBat6saoUiZb-Ct6Dg_Wa6uQDQynCbR_3noXgh7OQZc3pzk4UKqaiq0ke6WobvVai2-SeeOZ2iBK-8h7-jrF54d6Qk6no12bXqzL4'),
		'sender_id'        => env('FCM_SENDER_ID', '1099382632330'),
		'server_send_url'  => 'https://fcm.googleapis.com/fcm/send',
		'server_group_url' => 'https://android.googleapis.com/gcm/notification',
		'timeout'          => 30.0, // in second
	]
];
