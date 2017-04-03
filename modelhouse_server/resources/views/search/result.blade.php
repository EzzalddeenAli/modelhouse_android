@if(isset($latitude))
{{$latitude}}`{{$longitude}}`
@endif
[
@for($i=0; $i<sizeof($results); $i++)
{   "id":"{{$results[$i]->id}}",
    "type":@if($results[$i]->type==1)"토지"@elseif($results[$i]->type==2)"건물"@endif,
    "photo":"{{explode(",", $results[$i]->photo)[0]}}", 
    "price_type":@if($results[$i]->price_type==1)"매매"@elseif($results[$i]->price_type==2)"전세"@elseif($results[$i]->price_type==3)"임대"@endif, 
    "price":@if($results[$i]->price_type!=3)"{{$results[$i]->price}}"@elseif($results[$i]->monthly_price!=null)"{{$results[$i]->price}}/{{$results[$i]->monthly_price}}(월)"@elseif($results[$i]->monthly_price==null)"{{$results[$i]->price}}/{{$results[$i]->monthly_price}}(년)"@endif,
    "extent":"{{$results[$i]->extent}}",
    "category":"{{$results[$i]->category}}",
    "usearea":"{{$results[$i]->usearea}}",
    "facility":"{{$results[$i]->facility}}",
    "addr1":"{{$results[$i]->addr1}}",
    "info":"{{$results[$i]->info}}",
    "latitude":"{{$results[$i]->latitude}}",
    "longtitude":"{{$results[$i]->longtitude}}"
}
@if($i!=sizeof($results)-1)
,
@endif
@endfor
]
