<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\addr_gue;
use DB;

class EstateController extends Controller
{
    public function index()
    {
        $latitude = addr_gue::where('name', '=', '영등포구')->value('latitude');
        $longtitude = addr_gue::where('name', '=', '영등포구')->value('longtitude');

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
        //
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
