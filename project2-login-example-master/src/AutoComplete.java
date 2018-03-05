

/* A servlet to display the contents of the MySQL movieDB database */

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@WebServlet("/AutoComplete")
public class AutoComplete extends HttpServlet
{
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutoComplete() {
        super();
    }

    
    // Use http GET
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        String loginUser = "root";
        String loginPasswd = "wei123456";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("application/json");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
    


        try
           {
              //Class.forName("org.gjt.mm.mysql.Driver");
              Class.forName("com.mysql.jdbc.Driver").newInstance();

              Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
              // Declare our statement
              Statement statement = dbcon.createStatement();

              String input = request.getParameter("query");
              JsonArray jsonArray = new JsonArray();

              
           // return the empty json array if query is null or empty
  			if (input == null || input.trim().isEmpty()) {
  				response.getWriter().write(jsonArray.toString());
  				return;}
              
              String[] splited = input.split(" ");
              
              String total_input="";
              

              for (int i=0; i<splited.length; i++)
              {
            	  total_input = total_input + "MATCH (title) AGAINST ( '"+splited[i]+"*' IN BOOLEAN MODE) ";
            	  if (i != splited.length-1)
              	  	{
              	  		total_input+="AND ";
              	  	}
              }
             
           
              System.out.println(total_input);
              
              
              
              String query = ""
              		+ "SELECT movies.id, movies.title, movies.year, movies.director, GROUP_CONCAT(DISTINCT stars.name ORDER BY stars.name SEPARATOR ', ') AS stars, GROUP_CONCAT(DISTINCT genres.name ORDER BY genres.name SEPARATOR ', ') AS genres, ratings.rating\n" + 
                		"FROM movies, genres, stars, stars_in_movies, genres_in_movies, ratings\n" + 
                		"WHERE movies.id=stars_in_movies.movieId AND stars_in_movies.starId=stars.id AND movies.id=genres_in_movies.movieId AND genres_in_movies.genreId=genres.id AND ratings.movieId=movies.id AND " + total_input+"\n" +
                		"GROUP BY movies.id, movies.title, movies.year, movies.director, ratings.rating\n" + 
                		"ORDER BY ratings.rating DESC\n" +
                		"LIMIT 20;";
                	
             
              
              System.out.println("query = "+query);
              
              //movies.title LIKE '%home%'
              //movies.title LIKE '%"+input+"%'\n" +

              // Perform the query
              ResultSet rs = statement.executeQuery(query);


              while (rs.next())
              {
                  String movie_title = rs.getString(2);
                  jsonArray.add(generateJsonObject(movie_title, "movie"));
                 
              }
              out.write(jsonArray.toString());
              System.out.println(" after json");
              rs.close();
              statement.close();
              dbcon.close();
            }
        catch (SQLException ex) {
              while (ex != null) {
                    System.out.println ("SQL Exception:  " + ex.getMessage ());
                    ex = ex.getNextException ();
                }  // end while
            }  // end catch SQLException

        catch(java.lang.Exception ex)
            {
                out.println("<HTML>" +
                            "<HEAD><TITLE>" +
                            "MovieDB: Error" +
                            "</TITLE></HEAD>\n<BODY>" +
                            "<P>SQL error in doGet: " +
                            ex.getMessage() + "</P></BODY></HTML>");
                return;
            }
         out.close();
    }
    
    /* public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
	doGet(request, response);
	} */
    
    
    private static JsonObject generateJsonObject(String movie_title, String categoryName) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", movie_title);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("category", categoryName);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}
 
}

