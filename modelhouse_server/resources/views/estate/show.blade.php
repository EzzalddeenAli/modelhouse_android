
[
    {
        "id" : "{{$estate_detail->id}}",
        "type" : @if($estate_detail->type=='2')"건물"@else"토지"@endif,
        "deal_type" : @if($estate_detail->deal_type==1)"중개사무소"@else"직거래"@endif,
        "addr1" : "{{$estate_detail->addr1}}",
        "photo" : "{{$estate_detail->photo}}",
        "facility" : "{{$estate_detail->facility}}",
        "extent" : "{{$estate_detail->extent}}",
        "info" : "{{$estate_detail->info}}",
        "price_type" : @if($estate_detail->price_type==1)"매매"@elseif($estate_detail->price_type==2)"전세"@else"임대"@endif,
        "price" : "{{$estate_detail->price}}",
        "annual_price" : "{{$estate_detail->annual_price}}",
        "monthly_price" : "{{$estate_detail->monthly_price}}",
        "latitude" : "{{$estate_detail->latitude}}",
        "longtitude" : "{{$estate_detail->longtitude}}",
        "category" : "{{$estate_detail->category}}",
        "usearea" : "{{$estate_detail->usearea}}",
        "land_ratio" : "{{$estate_detail->land_ratio}}",
        "area_ratio" : "{{$estate_detail->area_ratio}}",

@if($estate_detail->type=='2')
        "public_price" : "{{$estate_detail->public_price}}",
        "private_extent" : "{{$estate_detail->private_extent}}",
        "support_extent" : "{{$estate_detail->support_extent}}",
        "height" : "{{$estate_detail->height}}",
        "movein" : "{{$estate_detail->movein}}",
        "loan" : "{{$estate_detail->loan}}",
        "total_floor" : "{{$estate_detail->total_floor}}",
        "floor" : "{{$estate_detail->floor}}",
        "heater" : "{{$estate_detail->heater}}",
        "fuel" : "{{$estate_detail->fuel}}",
        "complete" : "{{$estate_detail->complete}}",
        "parking" : "{{$estate_detail->parking}}",
        "maintenance_price" : "{{$estate_detail->maintenance_price}}",
@else
        "public_price" : "{{$estate_detail->public_price}}",
        "loan" : "{{$estate_detail->loan}}",
        "type" : "{{$estate_detail->type}}",
@endif

        "name" : "{{$user_detail->name}}",
        "mobile" : "{{$user_detail->mobile}}",
        "type" : @if($user_detail->type==1)"기업"@elseif($user_detail->type==2)"중개사무소"@else"일반"@endif


    }
]
