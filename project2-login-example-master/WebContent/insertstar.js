
function handleAddResult(resultDataJson) {
	console.log("in handle     "+resultDataJson);
	console.log("in this function");
	//resultDataJson = JSON.parse(resultData);
	
	console.log("handle login response");
	console.log(resultDataJson);
	console.log(resultDataJson["status"]);

	// if login success, redirect to index.html page
	if (resultDataJson["status"] == "flag1")
	{
		jQuery("#show_result_star").text(resultDataJson["message"]);
	} 
	else if (resultDataJson["status"] == "flag0")
	{
		console.log("show error message");
		console.log(resultDataJson["message"]);
		jQuery("#show_result_star").text(resultDataJson["message"]);
	}
	else
	{
		jQuery("#show_result_star").text(resultDataJson["message"]);
	}
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
		  url: "./InsertStar",  
		  data:jQuery("#input_form_star").serialize(),
		  success: (resultData) => handleAddResult(resultData)
//		  error: function(xhr, status, error) {
//			  var err = eval("(" + xhr.responseText + ")");
//			  alert(err.Message);
//}
           
	});
	
//	jQuery.get(
//			"/project2-login-example/InsertStar", 
//			// serialize the login form to the data sent by POST request
//			jQuery("#login_form_star").serialize(),
//			(resultDataString) => handleAddResult(resultDataString));
//	
	console.log("after  submit login form");
	
	}

// bind the submit action of the form to a handler function
jQuery("#input_form_star").submit((event) => submitLoginForm(event));

