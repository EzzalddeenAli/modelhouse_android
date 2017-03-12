<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\addr_gue;
use App\addr_dong;

class AddressController extends Controller
{

    public function address(Request $request){
        $table = $request->input('table');
        $value = $request->input('value');

        $results="";
        $gudong="";
        switch($table){
            case "addr_gues" :
                $results = addr_gue::where('addr_si_id', $value)->select('id', 'name')->orderby('id','asc')->get();
                $gudong = "gu";
                break;
            case "addr_dongs" :
                $results = addr_dong::where('addr_gu_id', $value)->select('id', 'name')->orderby('id','asc')->get();
                $gudong = "dong";
                break;
        }

        return view('address.addr')->with(['results'=>$results, 'gudong'=>$gudong]);
    }    
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
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

