<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\addr_gue;
use App\estate;
use App\User;
use App\estate_category;
use App\estate_land;
use App\estate_building;
use DB;
use Auth;

class EstateController extends Controller
{
    public function index()
    {
        // 
    }

    public function create()
    {
        //
    }

    public function store(Request $request)
    {
        // 객체 생성
        $estate = new estate();
        $estate_category = new estate_category();
        $estate_land = new estate_land();
        $estate_building = new estate_building();

        // 매물 아이디, 사용자 아이디, 매물 타입
        $estate_id = $estate::all()->max()->id + 1;

        $estate->user_id = $request->input('user_id');
        // $deal_type = $this->deal_type($user_id);
        $estate_type = $request->input('type');

        ################################################# 0. 파일 업로드 및 데이터베이스에 저장될 파일 이름 목록 설정 ####################################################
        // echo "gggg:";

        $photo_names = "";

        $photos0 = $request->file('uploaded_file_0');
        $photos1 = $request->file('uploaded_file_1');
        $photos2 = $request->file('uploaded_file_2');
        $photos3 = $request->file('uploaded_file_3');
        $photos4 = $request->file('uploaded_file_4');
        $photos5 = $request->file('uploaded_file_5');
        $photos6 = $request->file('uploaded_file_6');
        $photos7 = $request->file('uploaded_file_7');
        $photos8 = $request->file('uploaded_file_8');
        $photos9 = $request->file('uploaded_file_9');
        $photos10 = $request->file('uploaded_file_10');
        $photos11 = $request->file('uploaded_file_11');
        $photos12 = $request->file('uploaded_file_12');
        $photos13 = $request->file('uploaded_file_13');
        $photos14 = $request->file('uploaded_file_14');
        $photos15 = $request->file('uploaded_file_15');


        if($photos0!=null) {
            $photos0->storeAs('public/files', $estate_id . "." . $photos0->getClientOriginalName());
            $photo_names .= $estate_id . "." . $photos0->getClientOriginalName();
        }
        if($photos1!=null) {
            $photos1->storeAs('public/files', $estate_id . "." . $photos1->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos1->getClientOriginalName();
        }
        if($photos2!=null) {
            $photos2->storeAs('public/files', $estate_id . "." . $photos2->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos2->getClientOriginalName();
        }
        if($photos3!=null) {
            $photos3->storeAs('public/files', $estate_id . "." . $photos3->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos3->getClientOriginalName();
        }
        if($photos4!=null) {
            $photos4->storeAs('public/files', $estate_id . "." . $photos4->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos4->getClientOriginalName();
        }
        if($photos5!=null) {
            $photos5->storeAs('public/files', $estate_id . "." . $photos5->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos5->getClientOriginalName();
        }
        if($photos6!=null) {
            $photos6->storeAs('public/files', $estate_id . "." . $photos6->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos6->getClientOriginalName();
        }
        if($photos7!=null) {
            $photos7->storeAs('public/files', $estate_id . "." . $photos7->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos7->getClientOriginalName();
        }
        if($photos8!=null) {
            $photos8->storeAs('public/files', $estate_id . "." . $photos8->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos8->getClientOriginalName();
        }

        if($photos9!=null) {
            $photos9->storeAs('public/files', $estate_id . "." . $photos9->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos9->getClientOriginalName();
        }

        if($photos10!=null) {
            $photos10->storeAs('public/files', $estate_id . "." . $photos10->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos10->getClientOriginalName();
        }

        if($photos11!=null) {
            $photos11->storeAs('public/files', $estate_id . "." . $photos11->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos11->getClientOriginalName();
        }

        if($photos12!=null) {
            $photos12->storeAs('public/files', $estate_id . "." . $photos12->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos12->getClientOriginalName();
        }

        if($photos13!=null) {
            $photos13->storeAs('public/files', $estate_id . "." . $photos13->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos13->getClientOriginalName();
        }

        if($photos14!=null) {
            $photos14->storeAs('public/files', $estate_id . "." . $photos14->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos14->getClientOriginalName();
        }

        if($photos15!=null) {
            $photos15->storeAs('public/files', $estate_id . "." . $photos15->getClientOriginalName());
            $photo_names .= "," . $estate_id . "." . $photos15->getClientOriginalName();
        }

        $estate->photo = $photo_names;


        // echo "complete";



        ################################################# 1. 파라미터 받아오기 ####################################################

        // 매물 위치와 관련된 정보
        $si_id = $request->input('addr_si_id');
        $gu_id = $request->input('addr_gu_id');
        $dong_id = $request->input('addr_dong_id');
        $addr1 = $request->input('addr1'); 
        $addr2 = $request->input('addr2');
        $latitude = $request->input('latitude');
        $longtitude = $request->input('longitude');

        // 매물 가격과 관련된 정보
        $price_type = $request->input('price_type');
        $price = $request->input('price');
        $monthly = $request->input('monthly');
        $monthly_or_annual = $request->input('monthly_or_annual');

        // 그 외 토지/건물 공통 컬럼
        $public_price = $request->input('public_price');
        $extent = $request->input('extent');
        $loan = $request->input('loan');

        // 지목, 구분 과 건폐율, 용적률 관련 정보 (지목구분에 관련된 별도의 테이블에 저장)
        $category = $request->input('category');
        $usearea = $request->input('usearea');
        $land_ratio = $request->input('land_ratio');
        $area_ratio = $request->input('area_ratio');

        // 건물 테이블에만 저장되는 컬럼 (건물관련 정보를 담고 있는 별도의 테이블에 저장)
        $private_extent = $request->input('private_extent'); // 크기의 전용면적
        $support_extent = $request->input('support_extent'); // 크기의 공급면적
        $height = $request->input('height'); // 층고
        $movein = $request->input('movein'); // 입주가능일
        $total_floor = $request->input('total_floor'); //건물 총 층수
        $floor = $request->input('floor'); //해당층
        $heater = $request->input('heater'); // 난방방식
        $fuel = $request->input('fuel'); //난방연료
        $complete = $request->input('complete');
        $parking = $request->input('parking'); // 주차대수
        $manage_price = $request->input('manage_price'); // 관리비


        ################################################# 2. 객체에 받아온 파라메터 세팅 및 데이터베이스에 저장 ####################################################

        // estate 테이블에 저장하기 위한 객체 세팅 (토지와 건물의 공통된 컬럼)
        //$estate->user_id = 179;//Auth::user()->id;
        $estate->deal_type = 1;//$this->deal_type($user_id);
        $estate->type = $estate_type;

        $estate->addr_si_id = $si_id;
        $estate->addr_gu_id = $gu_id;
        $estate->addr_dong_id = $dong_id;
        $estate->addr1 = $addr1; 
        $estate->addr2 = $addr2;
        $estate->latitude = $latitude;
        $estate->longtitude = $longtitude;        

        // 매물 가격과 관련된 정보
        $estate->price_type = $price_type;
        $estate->price = $price;
        // 임대인 경우에는 임대료가 월 단위냐 년 단위냐에 따라 데이터가 저장되는 컬럼이 다르다.
        if($price_type=="3"){
            switch($monthly_or_annual){
                case "1" : 
                    $estate->monthly_price = $monthly;
                    break;
                case "2" :
                    $estate->annual_price = $monthly;
                    break;
            }
        }

        $estate->save();                                        // 데이터베이스에 저장
             
        // estate_category 테이블에 저장하기 위한 객체 세팅
        $estate_category->estate_id = $estate_id;               // 매물 아이디는 외래키이므롤 반드시 세팅한다.
        $estate_category->category = $category;
        $estate_category->usearea = $usearea;
        $estate_category->land_ratio = $land_ratio;
        $estate_category->area_ratio = $area_ratio;
        $estate_category->save();                               // 데이터베이스에 저장

        // estate_type에 따라 저장되는 데이터와 데이터가 저장되는 테이블이 달라진다.
        switch($estate_type){
            // 매물이 토지인 경우 estate_land 객체에 세팅
            case "1" : 
                $estate_land->estate_id = $estate_id;           // 매물 아이디는 외래키이므롤 반드시 세팅한다.
                $estate_land->public_price = $public_price;
                $estate_land->loan = $loan;
                $estate_land->save();                           // 데이터베이스에 저장
                break;
            // 매물이 건물인 경우 estate_building 객체에 세팅
            case "2" : 
                $estate_building->estate_id = $estate_id;       // 매물 아이디는 외래키이므롤 반드시 세팅한다.
                $estate_building->public_price = $public_price;
                $estate_building->loan = $loan;
                $estate_building->private_extent = $private_extent;
                $estate_building->support_extent = $support_extent;
                $estate_building->height = $height;
                $estate_building->movein = $movein;
                $estate_building->total_floor = $total_floor;
                $estate_building->floor = $floor;
                $estate_building->heater = $heater;
                $estate_building->fuel = $fuel;
                $estate_building->complete = $complete;
                $estate_building->parking = $parking;
                $estate_building->maintenance_price = $manage_price;
                $estate_building->save();                       // 데이터베이스에 저장
                break;
        }



        ################################################# 3.  ####################################################
        echo "1";
        ################################################# 4. 매물 등록이 완료되면 푸시알람 서버에 요청하기 ####################################################

        //데이터베이스에 접속해서 토큰들을 가져와서 FCM에 발신요청
        $sql = "Select Token From userTest";

        $result = DB::select($sql);
        $tokens = array();

        if(sizeof($result) > 0 ){

                foreach ($result as $Rresult) {
                        $tokens[] = $Rresult->Token;
                }
        }

        $myMessage = "새로운 매물이 등록되었습니다.";
        $message = array("message" => $myMessage);

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
        #######################################################################################################
    }

    public function show($id)
    {
        $type=estate::find($id)->type;

        $estate_detail;
        switch($type){
            case 1 :
                $estate_detail = DB::table('estates')
                                    ->join('estate_lands', 'estates.id', '=', 'estate_lands.estate_id')
                                    ->join('estate_categories', 'estates.id', '=', 'estate_categories.estate_id')
                                    ->where('estates.id', $id)
                                    ->select('estates.*', 
                                    'estate_lands.public_price', 'estate_lands.loan', 'estate_lands.type',
                                    'estate_categories.category', 'estate_categories.usearea', 'estate_categories.land_ratio', 'estate_categories.area_ratio')->first();//->get();
                break;
            case 2 :
                $estate_detail = DB::table('estates')
                                    ->join('estate_buildings', 'estates.id', '=', 'estate_buildings.estate_id')
                                    ->join('estate_categories', 'estates.id', '=', 'estate_categories.estate_id')
                                    ->where('estates.id', $id)
                                    ->select('estates.*', 
                                    'estate_buildings.public_price', 'estate_buildings.private_extent', 'estate_buildings.support_extent', 'estate_buildings.height',
                                    'estate_buildings.movein', 'estate_buildings.loan', 'estate_buildings.total_floor', 'estate_buildings.floor', 'estate_buildings.heater',
                                    'estate_buildings.fuel', 'estate_buildings.complete', 'estate_buildings.parking', 'estate_buildings.maintenance_price',
                                    'estate_categories.category', 'estate_categories.usearea', 'estate_categories.land_ratio', 'estate_categories.area_ratio')->first();//->get();
                break;
        }

        $user_detail = User::find($estate_detail->user_id);

        return view('estate.show')->with(['estate_detail'=>$estate_detail, 'user_detail'=>$user_detail]);
    }

    public function edit($id)
    {
        //
    }

    public function update(Request $request, $id)
    {
        //
    }

    public function destroy($id)
    {
        //
    }
}

