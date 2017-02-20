<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class addr_sie extends Model
{
    public function estates(){
        return $this->hasMany('App\estate');
    }

    public function addr_gues(){
        return $this->hasMany('App\addr_gue');
    }    
}
