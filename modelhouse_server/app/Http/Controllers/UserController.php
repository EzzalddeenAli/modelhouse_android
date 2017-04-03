<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\User;
use Auth;

class UserController extends Controller
{
    public function login(Request $request){
        $email = $request->input('email');
	$password = $request->input('password');
        
        $user = User::where('email', $email)->first();

	if($user==null){
//            echo "해당하는 아이디 존재하지 않음<br>";
        }else{
            if($user->password != $password){
//                echo "패스워드 불일치";
            }else{
//                echo "로그인을 시작하지<br>";
//                echo $user->name . ", " .  $user->email;

                Auth::login($user);

                return view('user.user_info')->with('user', $user);
            }
        }
    }

    // 사용자가 로그인 상태인지 확
    public function index()
    {
        if(Auth::user()!=null){
	    $user = User::find(Auth::user()->id);

            return view('user.user_info')->with('user', $user);
        }else{
	    $user = new User();
//	    echo "not logged in";
        }
 	
        return view('user.user_info')->with('user', $user);
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
