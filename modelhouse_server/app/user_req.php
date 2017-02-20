<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class user_req extends Model
{
    public function User(){
        return $this->belongsTo('App\User');
    }
}
