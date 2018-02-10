
function handleMovieResult(resultData) {
	console.log("handleStarResult: populating star table from resultData" + resultData);
	
	console.log("new");
	
	//console.log("movie_title    "+resultData[1]["movie_title"]);
	// populate the movie table
	
	var TableBodyElement = jQuery("#movie_list_body");

	//var page_data = split_data(2, resultData)
	
	for (var i = 0; i < resultData.length; i++) {
		
		var new_str = split_data(resultData[i]["star_name"]);
		console.log("newwwwwwwww"+new_str);
		var rowHTML = "";
		rowHTML += "<tr>";
		
		rowHTML += "<th>" + resultData[i]["movie_id"] + "</th>";
		
		var title = resultData[i]["movie_title"];
		var URL = "./SingleMovie?name="+ resultData[i]["movie_title"];

		rowHTML += "<th> <a href='"+URL+"'>" + resultData[i]["movie_title"] + "</a></th>";
		//rowHTML += "<th>" + resultData[i]["movie_title"] + "</th>";
		
		rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
		rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
		
		rowHTML += "<th> "+new_str + "</th>";
		//rowHTML += "<th>" + resultData[i]["star_name"] + "</th>";
		
		rowHTML += "<th>" + resultData[i]["genre_type"] + "</th>";
		rowHTML += "<th>" + resultData[i]["rating"] + "</th>";
		var URL2 = "./ShoppingCart?name="+ resultData[i]["movie_title"];
		rowHTML += "<th> <a href='"+URL2+"'>" + "Add to Cart" +"</a></th>";
		rowHTML += "</tr>"
		TableBodyElement.append(rowHTML);
		
	}
	
	console.log("before about ta ble");
	$(document).ready(about_table);
	console.log("after about ta ble");
	
	//window.location.replace("./advancedsearch.html");
}


function about_table()
{
	 $('#countTable').DataTable({"bFilter": false});
	 //{"bSort": false}
}
// makes the HTTP GET request and registers on success callback function handleStarResult


function split_data(obj) 
{
	
	console.log("in split data" + obj);
	var after_split = obj.split(', ');
	
	var new_str = "";
	for (var i = 0; i < after_split.length; i++) 
	{
		if (i!=after_split.length-1)
			{
				console.log("newwwwwwwww"+after_split[i]);
				new_str += "<a href='./SingleStar?name=" + after_split[i] + "'>"+after_split[i]+"</a>,"
				console.log("newwwwwwwww"+new_str);
			}
		else
			{
				new_str += "<a href='./SingleStar?name=" + after_split[i] + "'>"+after_split[i] +"</a>"
				console.log("newwwwwwwww"+new_str);
			}
	}
	
	return new_str;
	
}


function submitLoginForm(formSubmitEvent) {
	console.log("submit login form");
	
	// important: disable the default action of submitting the form
	//   which will cause the page to refresh
	//   see jQuery reference for details: https://api.jquery.com/submit/
	formSubmitEvent.preventDefault();
		
	jQuery.ajax({	  
		  dataType: "json",
		  method: "GET",
		  url: "./AdvancedSearch",  
		  data:jQuery("#input_form").serialize(),
		  success: (resultData) => handleMovieResult(resultData)
	});}

// bind the submit action of the form to a handler function
jQuery("#input_form").submit((event) => submitLoginForm(event));

