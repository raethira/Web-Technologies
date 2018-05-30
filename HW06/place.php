<?php
		ini_set('display_errors', 1); 
		ini_set('display_startup_errors', 1); 
		error_reporting(E_ALL);
	
		$key="AIzaSyBIA6mtiLEUWQfhIh5IzxiYCIq48W-AqaU";
		$keyword = '';
		$which='';

		if (isset ($_GET['which']))
		{	
			switch($_GET['which'])
			{
				case "form":
				{
					if (isset ($_GET['keyword']))
							{	$keyword=$_GET['keyword'];
								
							}
							if (isset ($_GET['distance']))
							{	$distance=$_GET['distance'];
								
							}
							
							if (isset ($_GET['type']))
							{	$type=$_GET['type'];
								// $type=str_replace (" ", "_",urlencode($type));
								//urlencode($type);
								//echo $type;
							}
							
							if (isset ($_GET['location']))
							{	
								$location=$_GET['location'];
									
								if (strpos($location, 'here') === false) 
								{
									
									$location = str_replace (" ", "+", urlencode($location));
									$details_url = "https://maps.googleapis.com/maps/api/geocode/json?address=".$location."&key=AIzaSyBIA6mtiLEUWQfhIh5IzxiYCIq48W-AqaU";
									$urlContent = json_decode(file_get_contents($details_url), true);
									$result = $urlContent['results'][0]['geometry']['location']['lat'].",".$urlContent['results'][0]['geometry']['location']['lng'];
									$location=$result;
								
								}
								else
								{
									$location=substr($location,4);
								}
								
								$details_url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=".$location."&radius=".$distance."&type=".$type."&keyword=".$keyword."&key=AIzaSyBIA6mtiLEUWQfhIh5IzxiYCIq48W-AqaU";
								$urlContent = json_decode(file_get_contents($details_url),true);
								$table=array();
								$a=array();
								$map=array();
								$a[]=array(array("name" => "value"));
								
								$pl_ids=array();
								for ($x = 0; $x <= sizeof($urlContent['results'])-1; $x++) 
								{	
									$place_id=$urlContent['results'][$x]['place_id'];
									$pl_ids[]=array($place_id);
									$table[]=array(
												array("icon" => $urlContent['results'][$x]['icon']),
					 							array("name" => $urlContent['results'][$x]['name']),
												array("vicinity" => $urlContent['results'][$x]['vicinity'])
												);
									$map[]=array(
												array("from_location" => $location), 
												array("to_location" => $urlContent['results'][$x]['geometry']['location'])
												);							
								}
								$a[]=($table);
								$a[]=($pl_ids);
								$a[]=($map);
									
							}
								$json = json_encode($a);
								echo $json;		
				}
					break;
			
				case "review":
				{
						if (isset ($_GET['pl_id']))
						{
								$place_id=$_GET['pl_id'];
								
								$reviews=array();
								
										$url="https://maps.googleapis.com/maps/api/place/details/json?placeid=".$place_id."&key=AIzaSyBIA6mtiLEUWQfhIh5IzxiYCIq48W-AqaU";
										$urldata = json_decode(file_get_contents($url),true);
									
										if(!array_key_exists('reviews',$urldata['result']))
										{			//$json = json_encode(null);
													//echo $json;
													//echo null;
										}
										else
										{
											for ($y = 0; $y <= sizeof($urldata['result']['reviews'])-1; $y++)
											{
												if($y===5)
													break;
												else
													{	
														$reviews[]=array(
																		array("profile_photo_url" => $urldata['result']['reviews'][$y]['profile_photo_url']), 
														
																		array("author_name" => $urldata['result']['reviews'][$y]['author_name']),
																		
																		array("text" => $urldata['result']['reviews'][$y]['text'])
																		
																		);
													}
											} 
											
										
										}
										
										if(!(array_key_exists('photos',$urldata['result'])))
										{	
											//$json1 = json_encode(null);
											//echo $json1;
											//echo null;
										}	
										else
										{	
											for ($y = 0; $y < sizeof($urldata['result']['photos'])-1; $y++)
											{	
												
												if($y===5)
													break;
												else
												{	
													$photo_reference=$urldata['result']['photos'][$y]['photo_reference'];
													$url2="https://maps.googleapis.com/maps/api/place/photo?maxwidth=750&photoreference=".$photo_reference."&key=AIzaSyBIA6mtiLEUWQfhIh5IzxiYCIq48W-AqaU";
													$photodata = (file_get_contents($url2));
													
													file_put_contents("Images/img".$y.$place_id.".jpg",$photodata);
										
												}
											}
										}
								$json = json_encode($reviews);
								echo $json;
							
						}							
				}break;
							
			}
			
			exit;
		}
				
	

		
?>
	
	<html>

	<head>

	<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<style>
	
			html, body {
						width: 100%;
						height: 100%;
						margin: 0;
						padding: 0;
					}
			fieldset {
					width: 50%;
					margin: 0 auto 50px;
					}
			#map {
				  height: 400px;
				  width: 50%;
				  position: absolute;
				  z-index: 5;
				 
				 }
			a {
					color: black;
				}
			table
			{
				z-index:-1;
				border-collapse: collapse;
				
			}
							
	</style>

			<title>Homework 6</title>
		
		
		<script	 
				src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBIA6mtiLEUWQfhIh5IzxiYCIq48W-AqaU">
		</script>		
		
		<script>
		
			function dummy()
			{
				console.log('do nothing');
			}
			
			function imageExists(image_url)
			{

				var http = new XMLHttpRequest();

				http.open('HEAD', image_url, false);
				http.send();

				return http.status != 404;

			}

			function fetch_geo()
			{	
				document.getElementById("distance").placeholder="10";
				document.getElementById("search_btn").setAttribute("disabled",true);
				document.getElementById("location2").setAttribute("disabled",true);
				var lat = "";
				var lon = "";
				
				try
				{	
						url="http://ip-api.com/json";
						xmlhttp=new XMLHttpRequest();
						xmlhttp.open("GET",url,false);
						xmlhttp.send();
						jsonObj= JSON.parse(xmlhttp.responseText);
						lat=jsonObj.lat;
						lon=jsonObj.lon;
						
				}
				catch(e)
				{
						lat = "";
						lon = "";
						
				}	
				
				try
				{
					if (lat === "" && lon === "")
					{
							url="freegeoip.net/json/github.com";
							xmlhttp=new XMLHttpRequest();
							xmlhttp.open("GET",url,false);
							xmlhttp.send();
							jsonObj= JSON.parse(xmlhttp.responseText);
							lat=jsonObj.latitude;
							lon=jsonObj.longitude;
						
					}
				}
				
				catch(e)
				{
						lat = "";
						lon = "";
						
				}
				
				
				if (lat === "" && lon === "")
				{
							lat=34.0223519;								// Hardcoded USC location values
							lon=-118.285117;
						
				}
				
				
				document.getElementById("here").value="here"+lat+","+lon;
				document.getElementById("search_btn").removeAttribute("disabled");					
				
			}
			
			function resetForm() 
			{
				
				document.getElementById("form1").reset();
				document.getElementById("distance").placeholder="10";
				document.getElementById("location2").placeholder="location";
				
			//	if(document.getElementById("keyword").hasAttribute("required"))
				//	document.getElementById("keyword").required = false;	
				//document.getElementById("keyword").removeAttribute("required");
				//if(document.getElementById("location2").hasAttribute("required"))
					//document.getElementById("location2").removeAttribute("required");
				
				//document.getElementById("search_btn").setAttribute("disabled",true);
				//document.getElementById("location2").setAttribute("disabled",true);
			
				/*
				document.getElementById("keyword").value="";
				var e = document.getElementById("type");
				e.options[e.selectedIndex].text="default";
				document.getElementById("location2").value="";
				document.getElementById("distance").value="";
				document.getElementById("here").value="";	
				*/
				
				document.getElementById("tablePrint").innerHTML="";
				document.getElementById("map").innerHTML="";
				//document.getElementById('map').style.display = "none";
				document.getElementById('how').style.display = "none";
										
				document.getElementById("topname").innerHTML="";
				document.getElementById("rev").innerHTML="";
				document.getElementById("rev_table").innerHTML="";
				document.getElementById("img").innerHTML="";
				document.getElementById("img_table").innerHTML="";
				
		
				fetch_geo();
			
			}
			
			function show_rev(pl_id,name)
			{				No_photos=1;
							console.log("pl_id="+pl_id);
							console.log(name);
								var hr1 = new XMLHttpRequest();
								var url = "place.php";
								var which= "review";
								
								url=url+"?"+"which="+which+"&pl_id="+pl_id;
						
								hr1.open("GET", url, false);
								
								// Set content type header information for sending url encoded variables in the request
								//hr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
								// Access the onreadystatechange event for the XMLHttpRequest object
								
								hr1.onreadystatechange = function() {
									if(hr1.readyState == 4 && hr1.status == 200) 
									{	//console.log("200ok")
											var return_data = hr1.responseText;
											//document.write(hr.responseText);
											console.log(return_data);
											console.log(jsonObj);
									if(jsonObj.length !==0)
											jsonObj= JSON.parse(return_data);
									if(jsonObj.length ===0)
										 rev_tab= "<center><b><br>No Reviews Found</b></center>";
									else
										{
													 rev_tab= "<fieldset><center><table border=1 >";
												

													for (var i=0; i<jsonObj.length ; i+=1) 
													{
													rev_tab+="<tr><th> <img src=" + jsonObj[i][0].profile_photo_url + "width='60' height='60'></img>";
													//console.log(jsonObj[2][i][0]);
													rev_tab+= jsonObj[i][1].author_name+"</th></tr>";
													rev_tab+="<tr> <td>" + jsonObj[i][2].text + " " +" </td></tr>";
													}  
												  
												  rev_tab+="</table></center></fieldset>";
										}
										//document.getElementById("tablePrint").style.visibility = "hidden";
										document.getElementById("tablePrint").innerHTML="";
									/*
									var mydiv = document.getElementById('tablePrint');
											while(mydiv.firstChild) {
											  mydiv.removeChild(mydiv.firstChild);
											}
									*/
									//	document.getElementById("tablePrint").innerHTML="";
										//document.getElementById("map").innerHTML="";
										//document.getElementById("how").innerHTML="";
										/*
										var mydiv = document.getElementById('map');
											while(mydiv.firstChild) {
											  mydiv.removeChild(mydiv.firstChild);
											}
										
										var mydiv = document.getElementById("how");
											while(mydiv.firstChild) {
											  mydiv.removeChild(mydiv.firstChild);
											}
											
										*/	
										
										document.getElementById('map').style.display = "none";
										document.getElementById('how').style.display = "none";
										
										document.getElementById('topname').innerHTML=name;
										
										var rev="<center>click to show reviews<br><input type='image' id='button1' src=http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png width='2%' height='3%'/></center>";								
										document.getElementById('rev').innerHTML = rev;	
		
										var img="<center>click to show photos<br><input type='image' id='button2' src=http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png width='2%' height='3%'/></center>";
										document.getElementById('img').innerHTML = img;		
										
										photos="<center><table  cellpadding=10 border=1>";
										if (imageExists("Images/img0"+pl_id+".jpg"))
										{	No_photos=0;
											photos+="<tr><td><center><a href='Images/img0"+pl_id+".jpg' target='_blank'><img src=Images/img0"+pl_id+".jpg onerror='this.style.visibility = 'hidden''/></a></center></td></tr>";
										}
										if (imageExists("Images/img1"+pl_id+".jpg"))
											photos+="<tr><td><center><a href='Images/img1"+pl_id+".jpg' target='_blank'><img src=Images/img1"+pl_id+".jpg onerror='this.style.visibility = 'hidden''/></a></center></td></tr>";
										if (imageExists("Images/img2"+pl_id+".jpg"))
											photos+="<tr><td><center><a href='Images/img2"+pl_id+".jpg' target='_blank'><img src=Images/img2"+pl_id+".jpg onerror='this.style.visibility = 'hidden''/></a></center></td></tr>";
										if (imageExists("Images/img3"+pl_id+".jpg"))
											photos+="<tr><td><center><a href='Images/img3"+pl_id+".jpg' target='_blank'><img src=Images/img3"+pl_id+".jpg onerror='this.style.visibility = 'hidden''/></a></center></td></tr>";
										if (imageExists("Images/img4"+pl_id+".jpg"))
											photos+="<tr><td><center><a href='Images/img4"+pl_id+".jpg' target='_blank'><img src=Images/img4"+pl_id+".jpg onerror='this.style.visibility = 'hidden''/></a></center></td></tr>";
										
										photos+="</table></center>";
										
										
										if (No_photos)
											photos+="<center><b><br>No Photos Found</b></center>";
											
										
										var openrev = true;
										var openphoto = true;
																					
										document.getElementById('button1').onclick=function(){
											
											//alert("button1");
											document.getElementById("button1").src="http://cs-server.usc.edu:45678/hw/hw6/images/arrow_up.png";
											document.getElementById("rev_table").innerHTML = rev_tab;
											var x = document.getElementById("rev_table");
											if (x.style.display === "none") {
												
												document.getElementById("img_table").style.display = "none";
												
												document.getElementById("button2").src="http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png";
												x.style.display = "block";
											} else {
												x.style.display = "none";
												
												document.getElementById("button1").src="http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png";
											}
											
										}	
			
											document.getElementById('button2').onclick=function(){
											
											//alert("button1");
											document.getElementById("button2").src="http://cs-server.usc.edu:45678/hw/hw6/images/arrow_up.png";
											document.getElementById("img_table").innerHTML = photos;
											var x = document.getElementById("img_table");
											if (x.style.display === "none") {
												
												document.getElementById("rev_table").style.display = "none";
												
												document.getElementById("button1").src="http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png";
												x.style.display = "block";
											} else {
												x.style.display = "none";
												
												document.getElementById("button2").src="http://cs-server.usc.edu:45678/hw/hw6/images/arrow_down.png";
											}
											
										}
										
									}
									}
								hr1.send();
							return false;
			}
							
			function on_search()
			{
				
				document.getElementById("keyword").setAttribute("required","");
				document.getElementById("keyword").reportValidity();
				
				if(document.forms[0].location1.checked)											// checks for no-value in the location-box
							//	if(document.forms[0].location2.value === "" )
								{	document.getElementById("location2").setAttribute("required","");
									document.getElementById( "location2" ).reportValidity();
									
								}
				
				
				
				//document.getElementById("search_btn").click();
				
				//document.getElementById("keyword").setCustomValidity( "Please fill out this field" );
				
				document.getElementById("tablePrint").innerHTML="";
				document.getElementById("map").innerHTML="";
				//document.getElementById('map').style.display = "none";
				document.getElementById('how').style.display = "none";
										
				document.getElementById("topname").innerHTML="";
				document.getElementById("rev").innerHTML="";
				document.getElementById("rev_table").innerHTML="";
				document.getElementById("img").innerHTML="";
				document.getElementById("img_table").innerHTML="";
				
				
					if(document.getElementById("distance").value.length === 0)
							document.getElementById("distance").value=10;

					var hr = new XMLHttpRequest();
					//Create some variables we need to send to our PHP file
					var url = "place.php";
					var which= "form";
					var keyword = document.getElementById("keyword").value;
					var type= document.getElementById("type").options[document.getElementById("type").selectedIndex].value;
					var distance = document.getElementById("distance").value*1609.34;
				
					//type.replace(" ","_");
					sNum=document.getElementById("distance").value;
					
						var pattern = /^\d+(\.\d+)?$/;
					if(pattern.test(sNum))
						 valid=1;
					else	
						 valid=0;	
					
					if(valid===0)
					{	alert("Invalid number entered");
						return false;
					}
					else
					{
						if(document.forms[0].location1.checked)										
						{	var location = document.forms[0].location2.value;
							here=0;
						
						}
						else
						{	var location = document.getElementById("here").value;
							here=1;
						}
						
						if(keyword!=="" && ( (here===0  && document.forms[0].location2.value!=='') || (here===1  && document.getElementById("here").value!=='') ) )
						{
							url=url+"?"+"which="+which+"&keyword="+keyword+"&type="+type+"&distance="+distance+"&location="+location;
							
							hr.open("GET", url, false);
							
							// Set content type header information for sending url encoded variables in the request
							//hr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
							// Access the onreadystatechange event for the XMLHttpRequest object
							
							hr.onreadystatechange = function() 
							{
								if(hr.readyState == 4 && hr.status == 200) 
								{
									var return_data = hr.responseText;
									console.log(return_data);
									if(jsonObj.length !==0)
										jsonObj= JSON.parse(return_data);						
									console.log(jsonObj);
									
									
									if(jsonObj[1].length ===0)
												var myTable= "<center><br>No Records has been found</center>";
									else
									{		
											var myTable= "<center><table border=1><td><b><center>Category</center></b></td>";
												myTable+= "<td><b><center>Name</center></b></td>";
												myTable+="<td><b><center>Address</center></b></td>";

											for (var i=0; i<jsonObj[1].length ; i+=1) 
											{
											var str=jsonObj[3][0][0].from_location;
											var res=str.split(",");
											
											
											var directionsService = new google.maps.DirectionsService;
											var directionsDisplay = new google.maps.DirectionsRenderer;
									
											
												//console.log(x);
												//console.log(y);
													
												  //var res = from_loc.split(",");
											var uluru = {lat: Number(res[0]), lng: Number(res[1])};
											var map = new google.maps.Map(document.getElementById("map"), {
													zoom: 15,
													center: uluru
													//disableDefaultUI:true,
													//mapTypeControl: true
													
												  });
											
											directionsDisplay.setMap(map);
												 
											console.log("Sruthi");
											
												
												var marker = new google.maps.Marker({
													position: uluru,
													map: map
												  });
											//map.controls[google.maps.ControlPosition.LEFT_TOP].push(document.getElementById('how'));
											// setTimeout(function() {google.maps.event.trigger(map, 'resize') } , 12000);
												
											
											
												
												myTable+="<tr><td> <img src=" + jsonObj[1][i][0].icon + "></td>";
												//console.log(jsonObj[2][i][0]);
												change=jsonObj[1][i][1].name;
												//JSON.stringify(change).replace(/&/,"&amp;").replace(/"/g,"&quot;");
												//JSON.stringify(change).replace(/&/, "&amp;").replace(/"/g, "&quot;");
												x=change.replace(/'/g,'&apos;');
												console.log(x);
												myTable+="<td> <a style='text-decoration:none' href='#' onclick='return show_rev(\"" + jsonObj[2][i][0] + "\",\"" + x  + "\");'>" + jsonObj[1][i][1].name + "</a></td>";
												//myTable+="<td> <a href='#' onclick='return show_rev(\"" + jsonObj[2][i][0] + "\");'>" + jsonObj[1][i][1].name + " </a></td>";
												
												var str=jsonObj[3][i][0].from_location;
												var res=str.split(",");
												console.log(res);
												lat=res[0];
												lng=res[1];
												
												window.initMap=function(event,to_lat,to_lng)
												{	
												
													var x = document.getElementById("map");
													var y = document.getElementById("how");

													if (x.style.display === "none")
													{
																
																 var x_co = event.clientX;
																 var y_co = event.clientY;
																
																x.style.display = "block";
																y.style.display = "block";
																
																x.style.left=x_co+'px';
																x.style.top=185+y_co+'px';
																//x.style.right = x_co+400;
																//x.style.bottom=y_co+300;
																
																y.style.left=x_co+'px';
																y.style.top=185+y_co+'px';
																//y.style.right = x_co+400;
																//y.style.bottom=y_co+300;
																
																console.log(x_co);
																console.log(y_co);
																
													}
													else 
													{
																marker.setMap(null);
																x.style.display = "none";
																y.style.display = "none";
															//	var parent = document.getElementById('parent'+i);
																//var child = document.getElementById("map");
																//parent.removeChild(child);
																return false;
																
													}
													document.getElementById('button3').addEventListener('click', function() {Walk(directionsService, directionsDisplay);});
								
													//	google.maps.event.addDomListener(document.getElementById('button3'), 'click',function(){Walk(directionsService, directionsDisplay);});
														
													document.getElementById('button4').addEventListener('click', function() {Bike(directionsService, directionsDisplay);});
														
													document.getElementById('button5').addEventListener('click', function() {Drive(directionsService, directionsDisplay);});
														/*
														
														if (typeof(window.google) !== 'undefined' && google.maps ) {
														  google.maps.event.addDomListener(document.getElementById('button3'), "click",function(){Walk(directionsService, directionsDisplay);});
															google.maps.event.addDomListener(document.getElementById('button4'), "click",function(){Bike(directionsService, directionsDisplay);});
														google.maps.event.addDomListener(document.getElementById('button5'), "click",function(){Drive(directionsService, directionsDisplay);});
														
														
														}*/
													  

													  function Walk(directionsService, directionsDisplay) 
													  {
																
														marker.setMap(null);				
														directionsService.route({
														  origin: {lat: Number(lat), lng: Number(lng)},  //Height.
														  destination: {lat: Number(to_lat), lng: Number(to_lng)},  // Ocean Beach.
														  // Note that Javascript allows us to access the constant
														  // using square brackets and a string value as its
														  // "property."
														  travelMode: google.maps.TravelMode["WALKING"]
														}, function(response, status) {
														  if (status == 'OK') {
															directionsDisplay.setDirections(response);
															
														  } else {
															//window.alert('Directions request failed due to ' + status);
														  }
														});
										
																
														}
														
														
													  function Bike(directionsService, directionsDisplay) 
													  {
																
														
														marker.setMap(null);				
														directionsService.route({
														  origin: {lat: Number(lat), lng: Number(lng)},  // Height.
														  destination: {lat: Number(to_lat), lng: Number(to_lng)},  // Ocean Beach.
														  // Note that Javascript allows us to access the constant
														  // using square brackets and a string value as its
														  // "property."
														  travelMode: google.maps.TravelMode["BICYCLING"]
														}, function(response, status) {
														  if (status == 'OK') {
															directionsDisplay.setDirections(response);
														  } else {
															//window.alert('Directions request failed due to ' + status);
														  }
														});
										
																
													   }
									
													   
													  function Drive(directionsService, directionsDisplay) 
													  {
																
															console.log('inside'+lat+lng+to_lat+to_lng);
														marker.setMap(null);			
														directionsService.route({
														  origin: {lat: Number(lat), lng: Number(lng)},  // Height.
														  destination: {lat: Number(to_lat), lng: Number(to_lng)},  // Ocean Beach.
														  // Note that Javascript allows us to access the constant
														  // using square brackets and a string value as its
														  // "property."
														  travelMode: google.maps.TravelMode["DRIVING"]
														}, function(response, status) {
														  if (status == 'OK') {
															directionsDisplay.setDirections(response);
														  } else {
															//window.alert('Directions request failed due to ' + status);
														  }
														});
										
																
													   }
													return false;
												}
												
												myTable+="<td id='parent'"+i+"> <a style='text-decoration:none' href='#' onclick='return initMap(event,\"" + jsonObj[3][i][1].to_location.lat +"\",\"" + jsonObj[3][i][1].to_location.lng  + "\");'>" + jsonObj[1][i][2].vicinity + "</a></td>";
												
												
												myTable+="</tr>";
												
												}  
											  
											  myTable+="</table></center>";
									}
									document.getElementById('tablePrint').innerHTML = myTable;											
								}
							
							}
							// Send the data to PHP now... and wait for response to update the status div
							hr.send(); // Actually execute the request
						}
					}
			}
			
			
			function enable_loc()
			{	
				if(document.forms[0].location1.checked)											// checks for no-value in the location-box
								if(document.forms[0].location2.value === "" )
								{	//if(document.getElementById("location2").hasAttribute("location2"))
										document.getElementById("location2").removeAttribute("disabled");
									//document.getElementById("location2").setAttribute("required","");
									//document.getElementById("location2").reportValidity();
				
									
								}
				
			}
			
			function enable_here()
			{	
				if(document.forms[0].here.checked)											// checks for value in the here-box
					{			document.forms[0].location2.value = "";
							//	if(document.getElementById("location2").hasAttribute("location2"))
									//document.getElementById("location2").removeAttribute("required");
								document.getElementById("location2").setAttribute("disabled",true);
								
					}
				
			}
			
			
		</script>

	</head>
  
	<body onload="fetch_geo()">
		<form id = "form1"   >  		<!-- Form awaiting for JSON file/URL input on the first page -->
			<fieldset>
				<center><H1><I>Travel and Entertainment Search</I></H1></center>
				<hr>
				<p>
					<b><label for="keyword"  >Keyword</label></b>
					<input type="text" id="keyword" name="keyword"  >
				</p>
				
				<p>
					<b><label for="type">Category</label></b>
					<select id="type" name="type" >
						<option value="default">default</option>
						<option value="cafe" >A cafe</option>
						<option value="bakery">Bakery</option>
						<option value="restaurant">Restaurant</option>
						<option value="beauty_Salon" >Beauty Salon</option>
						<option value="casino">Casino</option>
						<option value="movie_theater">Movie Theater</option>
						<option value="lodging">Lodging</option>
						<option value="airport">Airport</option>
						<option value="train_station">Train station</option>
						<option value="subway_station">Subway station</option>
						<option value="bus_station">Bus station</option>
					</select>
				</p>
				<p>
					<b><label for="distance"> Distance(miles)</label><input type="text" id="distance" name="distance" placeholder="10" ></b>
					<b><label for="Here"> from</label></b><input type="radio" name="location"  checked="checked" id="here" onclick="enable_here()"> Here<br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" name="location"  id="location1" onclick="enable_loc()" > <input type="text" id="location2" name="location2" placeholder="location"   >

				</p>
				<p>
				 
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" id="search_btn" value="Search" onclick= "on_search();" />								<!-- Submit the JSON file form -->
					<input type="button" value="Clear" onclick="resetForm()">
				</p>
			
			
			</fieldset>
		</form>
		
		<div id="tablePrint"> </div>
		
		
		<div id="map" style="display: none; position:absolute; overflow:visible; width:400px;height:300px; ">
		</div>
		
		<div id="how" name="type" style=" z-index:10; display:none; overflow:visible; position:absolute; " >
				<button type='button' id='button3' style="font-size:11px;  width: 80px; height:30px;">Walk there</button><br>		
				<button type='button' id='button4' style="font-size:11px;  width: 80px; height:30px;">Bike there</button><br>
				<button type='button' id='button5' style="font-size:11px;  width: 80px; height:30px;">Drive there</button><br>				
		</div>
		
		
		<center><h3><div id="topname"></div></h3></center><br><br>
		
		<div id='rev'>
		</div>
		<div id='rev_table'>
			</div>
		
		<br><br>
		
		<div id='img'>
			</div>
		<div id='img_table'>
			</div>


	</body>

</html>