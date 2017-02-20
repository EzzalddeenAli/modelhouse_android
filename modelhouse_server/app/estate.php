<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class estate extends Model
{
    public function user(){
        return $this->belongsTo('App\user');
    }    

    public function p_tacks(){
        return $this->hasMany('App\p_tack');
    }    
    
    public function p_reports(){
        return $this->hasMany('App\p_report');
    }

    public function p_estateqnas(){
        return $this->hasMany('App\p_estateqna');
    }
    
    public function estate_land(){
        return $this->hasOne('App\estate_land');
    }

    public function estate_building(){
        return $this->hasOne('App\estate_building');
    }

    public function estate_category(){
        return $this->hasOne('App\estate_category');
    }

    public function addr_sie(){
        return $this->belongsTo('App\addr_sie');
    }

    public function addr_gue(){
        return $this->belongsTo('App\addr_gue');
    }

    public function addr_dong(){
        return $this->belongsTo('App\addr_dong');
    }
}