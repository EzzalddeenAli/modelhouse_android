<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class model_house extends Model
{
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
 