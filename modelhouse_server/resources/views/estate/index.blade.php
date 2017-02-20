[
@foreach($estates as $estate)
{   "id":"{{$estate->id}}",
    "type":{{$estate->type}},
    "photo":"{{$estate->photo}}", 
    "price_type":"{{$estate->price_type}}", 
    "price":"{{$estate->price}}",
    "monthly_price":"{{$estate->monthly_price}}",
    "annual_price":"{{$estate->annual_price}}",
    "extent":"{{$estate->extent}}",
    "category":"{{$estate->category}}",
    "usearea":"{{$estate->usearea}}",
    "facility":"{{$estate->facility}}",
    "addr1":"{{$estate->addr1}}",
    "latitude":"{{$estate->longtitude}}",
    "longtitude":"{{$estate->longtitude}}",
    "info":"{{$estate->info}}"
}
,
<br>
@endforeach
]