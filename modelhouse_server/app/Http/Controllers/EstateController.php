<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\addr_gue;
use App\estate;
use App\user;
use DB;

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
        $user_id = Auth::user()->id;
        $deal_type = $this->deal_type($user_id);
        $sido = $request->input('sido');
        $sigungu = $request->input('sigungu');
        $bname = $request->input('bname');
        
        $si_id = DB::table('addr_sies')->where('name', $sido)->value('id'); // 시 id 검증
        $gu_id = DB::table('addr_gues')->where('name', $sigungu)->value('id'); // 구 id 검증
        $dong_id = DB::table('addr_dongs')->where('name', $bname)->where('addr_gu_id', $gu_id)
        ->value('id');

        $estate = new estate();
        $estate_category = new estate_category();
        $estate_land = new estate_land();
        $estate_building = new estate_building();
        $estate_id = $estate::all()->max()->id;
        $estate_id = $estate_id + 1;

        //estate insert 객체.
        $estate->user_id = $user_id;
        $estate->addr1 = $request->input('addr1'); 
        $estate->addr_si_id = $si_id; // 시, 구, 동 id 외래키 insert
        $estate->addr_gu_id = $gu_id;
        $estate->addr_dong_id = $dong_id;
        $estate->latitude = $request->input('latitude');
        $estate->longtitude = $request->input('longtitude');
        $estate->addr2 = $request->input('addr2');
        $estate->deal_type = $deal_type;
        
        $photo="";
        // $photos = $request->file('photo');
        $photo_names = $request->get('photo_name');

        for($i=0; $i<sizeof($photo_names); $i++){
            if($photo_names[$i]!=null){
                $photo .= $estate_id.'.'.$photo_names[$i];

                if($i==sizeof($photo_names)-1){

                }else{
                    $photo .= ",";
                }
            }

            // if(strpos($photo_names, $photos[$i]->getClientOriginalName())!==false){
            //     //echo "$i A match was found"."<br>";
            // }else{
            //     //echo "$i Continue"; 
            //     continue;
            // }

            // 이름 DB 저장
            

            // $photo .= $estate_id.'.'.$photos[$i]->getClientOriginalName();
            // // 파일 저장
            // $photos[$i]->storeAs('public/files', $estate_id.'.'.$photos[$i]->getClientOriginalName());
            // //$photos[$i]->move('public/files',$photos[$i]->getClientOriginalName());

        }
        
        // 문자열 맨 마지막의 쉼표는 반드시 지운다.
        $photo = trim($photo, ",");
        //echo $photo;
        $estate->photo = $photo;
        $estate->info = $request->input('info');
        if($request->get('facility') != null)
            $estate->facility = implode(",", $request->get('facility'));
        
        $estate_type = $request->input('type');
        if($estate_type==1) // 토지 관련 Insert
        {
            $estate_category->estate_id = $estate_id;
            $estate_land->estate_id = $estate_id;
            $estate->type = $estate_type;

            $extent = $request->extent;
            if($extent != null)
                $estate->extent = $extent;
            else
                $estate->extent = 0;
            //$estate->extent = $request->input('extent');
            $estate_category->category = $request->input('category');
            $estate_category->usearea = $request->input('usearea');
            $estate_land->public_price = $request->input('public_price'); //개별공시지가

            //나중에 java-script 이용해서 건폐율/용적률 자동으로 바꿔줘야 한다. 
            $land_ratio_min = $request->input('land_ratio_min');
            $land_ratio_max = $request->input('land_ratio_max');
            $area_ratio_min = $request->input('area_ratio_min');
            $area_ratio_max = $request->input('area_ratio_max');
            $estate_category->land_ratio = $land_ratio_min.'~'.$land_ratio_max;
            $estate_category->area_ratio = $area_ratio_min.'~'.$area_ratio_max;
            
            $estate_land->loan = $request->input('loan'); //융자금

            if($request->get('detail_info') != null)
                $estate_land->type = implode(",", $request->get('detail_info'));// 단지정보(land.type) 
        }
        else// 건물 관련 insert
        {
            $estate_category->estate_id = $estate_id;
            $estate_building->estate_id = $estate_id;
            $estate->type = $estate_type;
            $estate_category->category = $request->input('category');
            $estate_category->usearea = $request->input('usearea');
            $estate_building->public_price = $request->input('public_price1'); //개별공시지가

            //건물 건폐율/용적률 직접 입력
            $land_ratio_min1 = $request->input('land_ratio_min1');
            $land_ratio_max1 = $request->input('land_ratio_max1');
            $area_ratio_min1 = $request->input('area_ratio_min1');
            $area_ratio_max1 = $request->input('area_ratio_max1');
            $estate_category->land_ratio = $land_ratio_min1.'~'.$land_ratio_max1;
            $estate_category->area_ratio = $area_ratio_min1.'~'.$area_ratio_max1;
            
            $estate->extent = $request->input('extent1'); // 크기의 대지면적
            $estate_building->private_extent = $request->input('private_extent'); // 크기의 전용면적
            $estate_building->support_extent = $request->input('support_extent'); // 크기의 공급면적
            $estate_building->height = $request->input('height'); // 층고
            $estate_building->movein = $request->input('movein'); // 입주가능일
            $estate_building->loan = $request->input('loan1'); // 융자금
            $estate_building->total_floor = $request->input('total_floor'); //건물 총 층수
            $estate_building->floor = $request->input('floor'); //해당층
            $estate_building->parking = $request->input('parking'); // 주차대수
            $estate_building->heater = $request->input('heater'); // 난방방식

            $complete = $request->input('complete');
            if($complete!=null) { $estate_building->complete = $complete; } //준공년월
            $estate_building->fuel = $request->input('fuel'); //난방연료
        }

        //거래가격 로직.
        $price_type = $request->get('price_type');
        if($price_type==1) // 매매일 경우
        {
            $price0 = $request->input('price0');
            $estate->price_type = $price_type;;
            if($price0 != null)
                $estate->price = $price0;
            else
                $estate->price = 0;
        }
        elseif($price_type==2) // 전세일 경우
        {
            $price1 = $request->input('price1');
            $estate->price_type = $price_type;;
            if($price1 != null)
                $estate->price = $price1;
            else
                $estate->price = 0;
        }
        elseif($price_type==3) // 임대일 경우
        {
            $estate->price_type = $price_type;
            $estate->price = $request->input('price2'); // price 컬럼이 보증금이 된다.
            $ym = $request->input('ym'); // 년세, 월세 구분
            if($ym==1)
                $estate->monthly_price = $request->input('price3');
            else
                $estate->annual_price = $request->input('price3'); // 년세 혹은 월세 입력
        }


        
        if($estate_type==1)
        {
            $estate->save();
            $estate_land->save();
            $estate_category->save();   
        }
        else{
            $estate->save();
            $estate_building->save();
            $estate_category->save();
        }
        echo "1";
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

        $user_detail = user::find($estate_detail->user_id);

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
