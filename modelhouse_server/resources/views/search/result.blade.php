@if(isset($latitude))
{{$latitude}}`{{$longitude}}`
@endif
[
@foreach($results as $result)
{   "id":"{{$result->id}}",
    "type":@if($result->type==1)"토지"@elseif($result->type==2)"건물"@endif,
    "photo":"{{explode(",", $result->photo)[0]}}", 
    "price_type":@if($result->price_type==1)"매매"@elseif($result->price_type==2)"전세"@elseif($result->price_type==3)"임대"@endif, 
    "price":@if($result->price_type!=3)"{{$result->price}}"@elseif($result->monthly_price!=null)"{{$result->price}}/{{$result->monthly_price}}(월)"@elseif($result->monthly_price==null)"{{$result->price}}/{{$result->monthly_price}}(년)"@endif,
    "extent":"{{$result->extent}}",
    "category":"{{$result->category}}",
    "usearea":"{{$result->usearea}}",
    "facility":"{{$result->facility}}",
    "addr1":"{{$result->addr1}}",
    "info":"{{$result->info}}",
    "latitude":"{{$result->latitude}}",
    "longtitude":"{{$result->longtitude}}"
}
,
@endforeach
]

