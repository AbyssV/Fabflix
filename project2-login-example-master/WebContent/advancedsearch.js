
function handleMovieResult(resultData) {
	console.log("handleStarResult: populating star table from resultData" + resultData);
	
	console.log("new");
	
	console.log("genre    "+resultData[1]["genre_type"]);
	// populate the movie table
	

	jQuery("#movie_list_body").text("");
	var TableBodyElement = jQuery("#movie_list_body");
	
	
	for (var i = 0; i < 20; i++) {
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
	
	
	
	//window.location.replace("./advancedsearch.html");
}

// makes the HTTP GET request and registers on success callback function handleStarResult



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

