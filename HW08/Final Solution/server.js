const express = require('express')
const cors = require('cors')
const app = express()
const request = require('request')
const path = require('path');
const http = require('http');

var oembed=require("oembed-auto");
var bodyParser = require("body-parser");


// Parsers
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false}));



app.use(cors())

// Angular DIST output folder
app.use(express.static(path.join(__dirname, 'dist')));




app.get('/api/getDetails', function(req, res){

	var pl_id = req.query.url;
	pl_id=(pl_id);
	
    var request = require('request');
    request.get({ 
								
				url: "https://maps.googleapis.com/maps/api/place/details/json?placeid="+pl_id+"&key=AIzaSyBIA6mtiLEUWQfhIh5IzxiYCIq48W-AqaU"},
	
    	       function(error, response, body) { 
              if (!error && response.statusCode == 200) { 
                  resp = response;
                    //return res.send(resp);
                  //console.log(body); 
                  console.log("Success Node:Details");
				 return res.json((body));
                  
                 }
              else
              {	
              	 console.log(body); 
              	 return res.send("Error: Details ");
              }
             }); 

});

	
app.get('/api/getGeocode', function(req, res, next){

	var loc = req.query.url;
	
	loc=(loc);
	
    var request = require('request');
    request.get({ url: "https://maps.googleapis.com/maps/api/geocode/json?address="+loc+"&key=AIzaSyBIA6mtiLEUWQfhIh5IzxiYCIq48W-AqaU"},
    	       function(error, response, body) { 
              if (!error && response.statusCode == 200) { 
                  resp = response;
                    //return res.send(resp);
                  //console.log(body); 
                  console.log("Success Node:getGeocode");
				 return res.json((body));
                  
                 }
              else
              {	
              	 console.log(body); 
              	 return res.send("Error: getGeocode ");
              }
             }); 

});

app.get('/api/getResults', function(req, res, next){

	var keyword = (req.query.keyword);
	var type = (req.query.type);
	var distance = (req.query.distance);
	var loc2 = (req.query.loc);
	
	
    var request = require('request');
    request.get({ 
	 
								
				url: "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+loc2+"&radius="+distance+"&type="+type+"&keyword="+keyword+"&key=AIzaSyBIA6mtiLEUWQfhIh5IzxiYCIq48W-AqaU"},
	
    	       function(error, response, body) { 
              if (!error && response.statusCode == 200) { 
                  resp = response;
                    //return res.send(resp);
                  //console.log(body); 
                  console.log("Success Node:getResults");
				 return res.json((body));
                  
                 }
              else
              {	
              	 console.log(body); 
              	 return res.send("Error: getResults ");
              }
             }); 

});

app.get('/api/getTable', function(req, res){

	var next2 = (req.query.next);
	
	
    var request = require('request');
    request.get({ 
								
				url: "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken="+next2+"&key=AIzaSyBIA6mtiLEUWQfhIh5IzxiYCIq48W-AqaU"},
	
    	       function(error, response, body) { 
              if (!error && response.statusCode == 200) { 
                  resp = response;
                    //return res.send(resp);
                  //console.log(body); 
                  console.log("Success: Table");
				 return res.json((body));
                  
                 }
              else
              {	
              	 console.log(body); 
              	 return res.send("Error: Table ");
              }
             }); 

});

app.get('/api/Yelpmatch', function(req, res){

	var next2 = req.query.match;
	var name=req.query.name;
	var address1=req.query.address1;
	var city=req.query.city;
	var state=req.query.state;
	var postal=req.query.postal;
	
    var request = require('request');
    request.get( 
								
						//		{url: "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken="+next2+"&key=AIzaSyBIA6mtiLEUWQfhIh5IzxiYCIq48W-AqaU"},
	
				{
					url:"https://api.yelp.com/v3/businesses/matches/best?name="+(name)+"&address1="+(address1)+"&city="+(city)+"&state="+(state)+"&country=US"+"&postal_code="+(postal),
				 
				 headers:{
						  Authorization:'Bearer '+'62OnJrDaggN1Qz6EXEeOZDfZcMYoIxbuViONRBmF2tGWrswpLFEW86M9mjImriI0krqUFXhL39VdC3LSo0SZwQ6phaxL0cieQdN2zAFarb1bv14guut4SBVsEQDMWnYx',
					}
					
				},
				
    	       function(error, response, body) { 
              if (!error && response.statusCode == 200) { 
                  resp = response;
                    //return res.send(resp);
                  //console.log(body); 
                  console.log("Success Node:getYelp");
				 return res.json((body));
                  
                 }
              else
              {	
              	 console.log(body); 
              	 return res.send("Error: getYelp ");
              }
             }); 

});


app.get('/api/Yelprev', function(req, res){

	//var id = req.query.id;
	var id=(req.query.id);
	
    var request = require('request');
    request.get( 
								
						//		{url: "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken="+next2+"&key=AIzaSyBIA6mtiLEUWQfhIh5IzxiYCIq48W-AqaU"},
	
				{
					url:"https://api.yelp.com/v3/businesses/"+id+"/reviews",
				 
				 headers:{
						  Authorization:'Bearer '+'62OnJrDaggN1Qz6EXEeOZDfZcMYoIxbuViONRBmF2tGWrswpLFEW86M9mjImriI0krqUFXhL39VdC3LSo0SZwQ6phaxL0cieQdN2zAFarb1bv14guut4SBVsEQDMWnYx',
					}
					
				},
				
    	       function(error, response, body) { 
              if (!error && response.statusCode == 200) { 
                  resp = response;
                    //return res.send(resp);
                  //console.log(body); 
                  console.log("Success Node:Yelpreviews");
				 return res.json((body));
                  
                 }
              else
              {	
              	 console.log(body); 
              	 return res.send("Error: Yelpreviews ");
              }
             }); 

});


app.get('/rahul',function(req,res) {
        console.log("This happened1");
       	res.send('Welcome to my AWS Rahul!')
})

app.get('/',function(req,res) {
        console.log("This happened2");
       	res.send('Welcome to my AWS!')
})

//Set Port
const port = process.env.PORT || '8081';
app.set('port', port);

const server = http.createServer(app);

server.listen(port, () => console.log(`Running on localhost:${port}`));