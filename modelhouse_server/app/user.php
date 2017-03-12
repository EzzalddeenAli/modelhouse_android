<?php

namespace App;

use Illuminate\Notifications\Notifiable;
use Illuminate\Foundation\Auth\User as Authenticatable;

class User extends Authenticatable
{
    use Notifiable;

    protected $fillable = [
        'name', 'email', 'password',
    ];

    protected $hidden = [
        'password', 'remember_token'
    ];

    public function estates(){
        return $this->hasMany('App\estate');
    }

    public function user_reqs(){
        return $this->hasMany('App\user_req');
    }

    public function user_messages(){
        return $this->hasMany('App\user_message');
    }
    
    public function user_corps(){
        return $this->hasMany('App\user_corp');
    }

    public function user_estateoffices(){
        return $this->hasMany('App\user_estateoffice');
    }
    
    public function p_tacks(){
        return $this->hasMany('App\p_tack');
    }    

    public function p_estateqnas(){
        return $this->hasMany('App\p_estateqnas');
    }  

    public function p_reports(){
        return $this->hasMany('App\p_reports');
    }  
}
