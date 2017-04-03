<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class UploadController extends Controller
{

    public function index()
    {
       echo "index";
    }


    public function create()
    {
        //
    }

    public function store(Request $request)
    {

    echo "gggg:";


        $photos0 = $request->file('uploaded_file_0');
        $photos1 = $request->file('uploaded_file_1');
        $photos2 = $request->file('uploaded_file_2');

        if($photos0!=null) {
            $photos0->storeAs('public/testupload', $photos0->getClientOriginalName());
        }

        if($photos1!=null) {
            $photos1->storeAs('public/testupload', $photos1->getClientOriginalName());
        }

        if($photos2!=null) {
            $photos2->storeAs('public/testupload', $photos2->getClientOriginalName());
        }

        echo "complete";



    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
               $photos = $request->file('uploadedfile');

        

        
            $photos->storeAs('public/testupload', $photos->getClientOriginalName());

        echo "complete";
            

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

