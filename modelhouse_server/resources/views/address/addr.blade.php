[
@foreach($results as $result)
	{
		"id":"{{$result->id}}",
		"name":"{{$result->name}}"
	},
@endforeach
]
