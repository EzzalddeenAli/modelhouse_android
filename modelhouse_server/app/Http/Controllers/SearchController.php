<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\addr_sie;
use App\addr_gue;
use App\addr_dong;

use DB;

class SearchController extends Controller
{
    public function search(Request $request)
    {
        $addr_si_id = $request->input('addr_si_id');
        $addr_gu_id = $request->input('addr_gu_id');
        $addr_dong_id = $request->input('addr_dong_id');
        $estate_type = $request->input('estate_type');
        $deal_type = $request->input('deal_type');
        $price_type = $request->input('price_type');
        $price_from = $request->input('price_from');
        $price_to = $request->input('price_to');
        $monthly_from = $request->input('monthly_from');
        $monthly_to = $request->input('monthly_to');
        $extent_from = $request->input('extent_from');
        $extent_to = $request->input('extent_to');
        $monthly_annual = $request->input('monthly_annual');

        if($addr_dong_id!=0 || $addr_gu_id!=0){
            $latitude=addr_gue::where('id', $addr_gu_id)->value('latitude');
            $longitude=addr_gue::where('id', $addr_gu_id)->value('longtitude');
        }elseif($addr_si_id!=0){
            $latitude=addr_sie::where('id', $addr_si_id)->value('latitude');
            $longitude=addr_sie::where('id', $addr_si_id)->value('longtitude');
        }else{
            $latitude = $request->input('latitude');
            $longitude = $request->input('longitude');
        }

        $query = DB::table('estates')->join('estate_categories', 'estates.id', '=', 'estate_categories.estate_id')
                        ->select('estates.id', 'estates.price_type', 'estates.photo', 'estates.monthly_price', 'estates.info', 'estates.annual_price', 'estates.price',
                                'estates.type', 'estates.extent', 'estate_categories.category', 'estate_categories.usearea', 'estates.facility', 
                                'estates.addr1', 'estates.latitude', 'estates.longtitude')
                        ->when($addr_gu_id!=0, function ($query) use ($addr_gu_id){
                                return  $query->where('estates.addr_gu_id', $addr_gu_id);
                        })
                        ->when($addr_si_id!=0, function ($query) use ($addr_si_id){
                                return  $query->where('estates.addr_si_id', $addr_si_id);
                        })                                
                        ->when( ($addr_gu_id==0 && $addr_si_id==0), function ($query) use ($latitude, $longitude){
                                return $query->where(DB::raw("6371 * 2 * ATAN2(SQRT(POW(SIN(RADIANS(latitude - $latitude)/2), 2) + POW(SIN(RADIANS(longtitude - $longitude)/2), 2) *
                                                            COS(RADIANS($latitude)) * COS(RADIANS(latitude))), SQRT(1 - POW(SIN(RADIANS(latitude - $latitude)/2), 2) + POW(SIN(RADIANS(longtitude - $longitude)/2), 2)
                                                            * COS(RADIANS($latitude)) * COS(RADIANS(latitude))))"), "<=", 8);
                        })
                        ->when($estate_type!=0, function ($query) use ($estate_type) {
                            return $query->where('estates.type', $estate_type);
                        })
                        ->when($deal_type!=0, function ($query) use ($deal_type) {
                            return $query->where('estates.price_type', $deal_type);
                        })
                        ->when($price_type!=0, function ($query) use ($price_type) {
                            return $query->where('estates.price_type', $price_type);
                        })
                        ->when($price_to!=10000, 
                            function ($query) use ($price_from, $price_to) {
                                return $query->whereBetween('estates.price', [$price_from, $price_to]);
                            },
                            function ($query) use ($price_from){
                                return $query->where('estates.price', '>=', $price_from);
                        })
                        ->when($extent_to!=10000, 
                            function ($query) use ($extent_from, $extent_to) {
                                return $query->whereBetween('estates.extent', [$extent_from, $extent_to]);
                            },
                            function ($query) use ($extent_from){
                                return $query->where('estates.extent', '>=', $extent_from);
                        })
                        ->when( ($price_type==3 && $monthly_annual==1 && $monthly_to!=10000), 
                            function ($query) use ($monthly_from, $monthly_to) {
                                return $query->whereBetween('estates.monthly_price', [$monthly_from, $monthly_to]);
                        })   
                        ->when( ($price_type==3 && $monthly_annual==1 && $monthly_to==10000), 
                            function ($query) use ($monthly_from, $monthly_to) {
                                return $query->where('estates.monthly_price', $monthly_from);
                        }) 
                        ->when( ($price_type==3 && $monthly_annual==2 && $monthly_to!=10000), 
                            function ($query) use ($monthly_from, $monthly_to) {
                                return $query->whereBetween('estates.monthly_price', [$monthly_from, $monthly_to]);
                        })   
                        ->when( ($price_type==3 && $monthly_annual==2 && $monthly_to==10000), 
                            function ($query) use ($monthly_from, $monthly_to) {
                                return $query->where('estates.annual_price', $monthly_from);
                        }) 
                        ->get();

        if($addr_gu_id==0 && $addr_si_id==0){
            return view('search.result')->with('results', $query);
        }else{
            return view('search.result')->with(['latitude'=>$latitude, 'longitude'=>$longitude, 'results'=>$query]);
        }
}


    public function index()
    {
        //
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
        //
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

