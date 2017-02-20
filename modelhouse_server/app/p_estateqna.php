<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class p_estateqna extends Model
{
    public function user(){
        return $this->belongsTo('App\user');
    }

    public function estate(){
        return $this->belongsTo('App\estate');
    }
}
