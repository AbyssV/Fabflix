
function handleMovieResult(resultData) {
	console.log("handleStarResult: populating movie table from resultData" + resultData);
	
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
	
	//doneCallback( { suggestions: jsonData } );
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


function handleLookup(query, doneCallback) {
	console.log("submit login form in handle look up");
	
	// important: disable the default action of submitting the form
	//   which will cause the page to refresh
	//   see jQuery reference for details: https://api.jquery.com/submit/
	//formSubmitEvent.preventDefault();
		
	jQuery.ajax({	  
		  dataType: "json",
		  method: "GET",
		  url: "./AutoComplete?query=" + escape(query),  
		  //data:jQuery("#input_form_search").serialize(),
		  success: (resultData) => handleLookupAjaxSuccess(resultData, query, doneCallback),
		  error: function(errorData) {
				console.log("lookup ajax error in handleLookup")
				console.log(errorData)
			}
			 
	});
	

	console.log("after  submit login form");
	}

function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	
	// parse the string into JSON
	console.log("before parse")
	//var jsonData = JSON.parse(data);
	console.log("after parse")
	console.log(data)
	
	// TODO: if you want to cache the result into a global variable you can do it here

	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	//   the "Response Format" section in documentation
	doneCallback( { suggestions: data } );
}

function submitLoginForm(formSubmitEvent,query) {
	console.log("submit login form in submitLoginForm");
	
	// important: disable the default action of submitting the form
	//   which will cause the page to refresh
	//   see jQuery reference for details: https://api.jquery.com/submit/
	formSubmitEvent.preventDefault();
		
	jQuery.ajax({	  
		  dataType: "json",
		  method: "GET",
		  url: "/project2-login-example/Search?search="+escape(query),  
		  //data:jQuery("#search_text").serialize(),
		  success: (resultData) => handleMovieResult(resultData),
//		  success:function(data) {
//				// pass the data, query, and doneCallback function into the success handler
//			  handleMovieResult(data) 
//			},
		  error: function(errorData) {
				console.log("lookup ajax error in submitLoginForm")
				console.log(errorData)
			}
           
	});
	
	console.log("after submit login form in submitLoginForm");}

// bind the submit action of the form to a handler function
//jQuery("#input_form_search").submit((event) => submitLoginForm(event));



/*
 * This function is the select suggestion hanlder function. 
 * When a suggestion is selected, this function is called by the library.
 * 
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion
	
	console.log("you select " + suggestion["value"])
	if (suggestion["data"]["category"] == "movie")
		{
		var url = "/project2-login-example/SingleMovie?name=" + suggestion["value"]
		window.location.replace(url);
		console.log(url)
		}
	
	
	else
		{
		var url = "/project2-login-example/SingleStar?name=" + suggestion["value"]
		window.location.replace(url);
		console.log(url)
		}

	
}





$('#search_text').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set the groupby name in the response json data field
    groupBy: "category",
    // set delay time
    deferRequestBy: 300,
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as mininum characters
});

/*
 * do normal full text search if no suggestion is selected 
 */


// bind pressing enter key to a hanlder function
$('#search_text').keypress(function(event) {
	// keyCode 13 is the enter key
	console.log("enter is using ");
	if (event.keyCode == 13) {
		// pass the value of the input box to the hanlder function
		submitLoginForm(event,$('#search_text').val())
	}
	console.log("go to submit login form ");
})



