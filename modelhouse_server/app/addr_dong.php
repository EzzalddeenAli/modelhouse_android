<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class addr_dong extends Model
{
    public function estates(){
        return $this->hasMany('App\estate');
    }

    public function addr_gue(){
        return $this->belongsTo('App\addr_gue');
    }   
}
