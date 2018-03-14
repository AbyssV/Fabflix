

/* A servlet to display the contents of the MySQL movieDB database */

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

public class Search extends HttpServlet
{
    public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Use http GET

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        String loginUser = "root";
        String loginPasswd = "wei123456";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("application/json");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
    
        System.out.println("in the servelet SEARCH now");

        try
           {
        		// the following few lines are for connection pooling
            // Obtain our environment naming context

            Context initCtx = new InitialContext();
            if (initCtx == null)
                out.println("initCtx is NULL");

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");
        	
        	
        	
              //Class.forName("org.gjt.mm.mysql.Driver");
              //Class.forName("com.mysql.jdbc.Driver").newInstance();

              //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            
	            if (ds == null)
	                out.println("ds is null.");
	
	            Connection dbcon = ds.getConnection();
	            if (dbcon == null)
	                out.println("dbcon is null.");
	            
              // Declare our statement
              Statement statement = dbcon.createStatement();

              String input = request.getParameter("search");
              System.out.println("user input " + input);
//              String result="";
//      
//              if (input.length()<6) {
//            	  	result="(edrec(movies.title, '"+input+"', 2)=1)";
//              }
//              else if (input.length()>5) {
//            	  	result="(edrec(movies.title, '"+input+"' ,4)=1)";
//              } 
//              
              
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
              
              
              
//              String query = ""
//              		+ "SELECT movies.id, movies.title, movies.year, movies.director, GROUP_CONCAT(DISTINCT stars.name ORDER BY stars.name SEPARATOR ', ') AS stars, GROUP_CONCAT(DISTINCT genres.name ORDER BY genres.name SEPARATOR ', ') AS genres, ratings.rating\n" + 
//                		"FROM movies, genres, stars, stars_in_movies, genres_in_movies, ratings\n" + 
//                		"WHERE movies.id=stars_in_movies.movieId AND stars_in_movies.starId=stars.id AND movies.id=genres_in_movies.movieId AND genres_in_movies.genreId=genres.id AND ratings.movieId=movies.id AND (" + total_input+"OR "+result+")\n" +
//                		"GROUP BY movies.id, movies.title, movies.year, movies.director, ratings.rating\n" + 
//                		"ORDER BY ratings.rating DESC;" ;
//                	
             
              String query = ""
                		+ "SELECT movies.id, movies.title, movies.year, movies.director, GROUP_CONCAT(DISTINCT stars.name ORDER BY stars.name SEPARATOR ', ') AS stars, GROUP_CONCAT(DISTINCT genres.name ORDER BY genres.name SEPARATOR ', ') AS genres, ratings.rating\n" + 
                  		"FROM movies, genres, stars, stars_in_movies, genres_in_movies, ratings\n" + 
                  		"WHERE movies.id=stars_in_movies.movieId AND stars_in_movies.starId=stars.id AND movies.id=genres_in_movies.movieId AND genres_in_movies.genreId=genres.id AND ratings.movieId=movies.id AND " + total_input+"\n" +
                  		"GROUP BY movies.id, movies.title, movies.year, movies.director, ratings.rating\n" + 
                  		"ORDER BY ratings.rating DESC;" ;
                  	
                             
              System.out.println("query = "+query);
              
              //movies.title LIKE '%home%'
              //movies.title LIKE '%"+input+"%'\n" +

              // Perform the query
              ResultSet rs = statement.executeQuery(query);


         
              
              JsonArray jsonArray = new JsonArray();
              while (rs.next())
              {
            	  	  String movie_id = rs.getString(1);
                  String movie_title = rs.getString(2);
                  int movie_year = rs.getInt(3);
                  String movie_director = rs.getString(4);
                  String star_name = rs.getString(5);
                  String genre_type = rs.getString(6);
                  double rating = rs.getDouble(7);
                  
                  System.out.print(movie_id);
                  System.out.print(movie_title);
                  System.out.print(movie_year);
                  System.out.print(movie_director);
                  System.out.print(star_name);
                  System.out.print(genre_type);
                  System.out.println(rating);
                  
                  
                  JsonObject jsonObject = new JsonObject();
                  jsonObject.addProperty("movie_id", movie_id);
                  jsonObject.addProperty("movie_title", movie_title);
                  jsonObject.addProperty("movie_year", movie_year);
                  jsonObject.addProperty("movie_director", movie_director);
                  jsonObject.addProperty("star_name", star_name);
                  jsonObject.addProperty("genre_type", genre_type);
                  jsonObject.addProperty("rating", rating);
                  
                  jsonArray.add(jsonObject);
                  
                  
              }
              out.write(jsonArray.toString());
              System.out.println(" after json");
              rs.close();
              statement.close();
              dbcon.close();
              
              System.out.println(" go back to js");
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
 
}

