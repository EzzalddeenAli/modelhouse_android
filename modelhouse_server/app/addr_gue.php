<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class addr_gue extends Model
{
    public function estates(){
        return $this->hasMany('App\estate');
    }

    public function addr_sie(){
        return $this->belongsTo('App\addr_sie');
    }   

    public function addr_dongs(){
        return $this->hasMany('App\addr_dong');
    }   
}
