[
@foreach($estates as $estate)
{   "id":"{{$estate->id}}",
    "type":@if($estate->type==1)"토지"@elseif($estate->type==2)"건물"@endif,
    "photo":"{{explode(",", $estate->photo)[0]}}", 
    "price_type":@if($estate->price_type==1)"매매"@elseif($estate->price_type==2)"전세"@elseif($estate->price_type==3)"임대"@endif, 
    "price":@if($estate->price_type!=3)"{{$estate->price}}"@elseif($estate->monthly_price!=null)"{{$estate->price}}/{{$estate->monthly_price}}(월)"@elseif($estate->monthly_price==null)"{{$estate->price}}/{{$estate->monthly_price}}(년)"@endif,
    "extent":"{{$estate->extent}}",
    "category":"{{$estate->category}}",
    "usearea":"{{$estate->usearea}}",
    "facility":"{{$estate->facility}}",
    "addr1":"{{$estate->addr1}}",
    "info":"{{$estate->info}}",
    "latitude":"{{$estate->latitude}}",
    "longtitude":"{{$estate->longtitude}}"
}
,
@endforeach
]