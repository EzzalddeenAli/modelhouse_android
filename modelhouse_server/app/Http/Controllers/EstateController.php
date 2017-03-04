<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\addr_gue;
use App\estate;
use App\user;
use DB;

class EstateController extends Controller
{
    public function index(Request $request)
    {
        $latitude = $request->input('latitude');
        $longtitude = $request->input('longtitude');

        $estates = DB::table('estates')->join('estate_categories', 'estates.id', '=', 'estate_categories.estate_id')
                ->select('estates.id', 'estates.price_type', 'estates.photo', 'estates.monthly_price', 'estates.info', 'estates.annual_price', 'estates.price',
                        'estates.type', 'estates.extent', 'estate_categories.category', 'estate_categories.usearea', 'estates.facility', 
                        'estates.addr1', 'estates.latitude', 'estates.longtitude')
                ->where(DB::raw("6371 * 2 * ATAN2(SQRT(POW(SIN(RADIANS(latitude - $latitude)/2), 2) + POW(SIN(RADIANS(longtitude - $longtitude)/2), 2) *
                        COS(RADIANS($latitude)) * COS(RADIANS(latitude))), SQRT(1 - POW(SIN(RADIANS(latitude - $latitude)/2), 2) + POW(SIN(RADIANS(longtitude - $longtitude)/2), 2)
                        * COS(RADIANS($latitude)) * COS(RADIANS(latitude))))"), "<=", 8)
                ->get();

        return view('estate.index')->with('estates', $estates);
    }

    public function create()
    {
        //
    }

    public function store(Request $request)
    {
        //
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
