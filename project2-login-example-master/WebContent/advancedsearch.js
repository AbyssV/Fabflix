
function handleMovieResult(resultData) {
	console.log("handleStarResult: populating star table from resultData" + resultData);
	
	console.log("new");
	
	console.log("genre    "+resultData[1]["genre_type"]);
	// populate the movie table
	

	//jQuery("#movie_list_body").text("");
	var TableBodyElement = jQuery("#movie_list_body");
	
	
	var len =resultData.length;
	var page = Math.ceil(len/20) //获取页数
	console.log("page numebr   "+page);
	//var page_data = split_data(2, resultData)
	
	for (var i = 0; i < resultData.length; i++) {
		var rowHTML = "";
		rowHTML += "<tr>";
		rowHTML += "<th>" + resultData[i]["movie_title"] + "</th>";
		rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
		rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
		rowHTML += "<th>" + resultData[i]["star_name"] + "</th>";
		rowHTML += "<th>" + resultData[i]["genre_type"] + "</th>";
		rowHTML += "<th>" + resultData[i]["rating"] + "</th>";
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
	 $('#countTable').DataTable();
	 //{"bSort": false}
}
// makes the HTTP GET request and registers on success callback function handleStarResult


function split_data(pageNo, obj) {

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

