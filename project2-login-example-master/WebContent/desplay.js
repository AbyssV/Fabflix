function handleMovieResult(resultData) {
	console.log("handleStarResult: populating star table from resultData" + resultData);
	
	console.log("new");
	// populate the movie table
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
	
	//window.location.replace("./desplayadvancedsearch.html");
}