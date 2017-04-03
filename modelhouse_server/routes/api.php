<?php



use Illuminate\Http\Request;
use App\Services\FCMHandler;

use LaravelFCM\Message\OptionsBuilder;
use LaravelFCM\Message\PayloadDataBuilder;
use LaravelFCM\Message\PayloadNotificationBuilder;
/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/
Route::get('/', function () {
    return view('welcome');
});

Route::post('/user/login', 'UserController@login');
Route::resource('/user', 'UserController');

Auth::routes();

Route::resource('/estates', 'EstateController');
Route::post('/address', 'AddressController@address');
Route::resource('/search', 'SearchController');
Route::resource('/upload', 'UploadController');

Route::resource('/fmctest', 'FcmController');


Route::get('fcm', function (Request $request, FCMHandler $fcm) {
    // 푸쉬 메시지를 수신할 단말기의 토큰 목록을 추출한다.
    $user = $request->user();
    $to = $user->devices()->pluck('push_service_id')->toArray();

    if (! empty($to)) {
        // 보낼 내용이 마땅치 않아 로그인한 사용자 모델을 푸쉬 메시지 본몬으로 ㅡㅡ;. 
        $message = array_merge(
            $user->toArray(),
            ['foo' => 'bar']
        );

        // FCMHandler 덕분에 코드는 이렇게 한 줄로 간결해졌다.
        $fcm->to($to)->data($message)->send();
    }

    return response()->json([
        'success' => 'HTTP 요청 처리 완료'
    ]);
})->middleware('auth.basic.once');


//Route::get('fcm', function () {
  //  $optionBuilder = new OptionsBuilder();
//    $optionBuilder->setTimeToLive(60*20);

//    $notificationBuilder = new PayloadNotificationBuilder('알림 제목');
//    $notificationBuilder->setBody('알림 본문')->setSound('default');

//    $dataBuilder = new PayloadDataBuilder();
//    $dataBuilder->addData(['foo' => 'bar']);

//    $option = $optionBuilder->build();
//    $notification = $notificationBuilder->build();
//    $data = $dataBuilder->build();

//    $token = 'eI..b0:APA...FJx';

//    $downstreamResponse = app('fcm.sender')->sendTo($token, $option, $notification, $data);

//    var_dump('numberSuccess', $downstreamResponse->numberSuccess());
//    var_dump('numberFailure', $downstreamResponse->numberFailure());
//    var_dump('numberModification', $downstreamResponse->numberModification());
//    var_dump('tokensToDelete', $downstreamResponse->tokensToDelete());
//    var_dump('tokensToModify', $downstreamResponse->tokensToModify());
//    var_dump('tokensToRetry', $downstreamResponse->tokensToRetry());
//    var_dump('tokensWithError', $downstreamResponse->tokensWithError());
//});
