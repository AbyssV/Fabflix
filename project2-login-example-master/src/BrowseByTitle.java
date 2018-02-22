

/* A servlet to display the contents of the MySQL movieDB database */

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class BrowseByTitle extends HttpServlet
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
        String loginPasswd = "1065428254djdg";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" + 
        		"<link type=\"text/css\" rel=\"stylesheet\" href=\"path_to/simplePagination.css\"/>\n" + 
        		"<script src=\"https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css\"></script>\n" + 
        		"<link rel=\"stylesheet\" type=\"text/css\" href=\"http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/css/jquery.dataTables.css\">\n" + 
        		"<link rel=\"stylesheet\" type=\"text/css\" href=\"https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css\">");
        out.println("<HTML><HEAD><TITLE>MovieDB: Found Records</TITLE>");
        
        
        
        out.println("</HEAD>");
        out.println("<BODY><H1>MovieDB: Found Records</H1>");


        try
           {
              //Class.forName("org.gjt.mm.mysql.Driver");
              Class.forName("com.mysql.jdbc.Driver").newInstance();

              Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
              // Declare our statement
              Statement statement = dbcon.createStatement();

              String input = request.getParameter("name");
              
              String query = "SELECT movies.id, movies.title, movies.year, movies.director, GROUP_CONCAT(DISTINCT stars.name ORDER BY stars.name SEPARATOR ', ') AS stars, GROUP_CONCAT(DISTINCT genres.name ORDER BY genres.name SEPARATOR ', ') AS genres, ratings.rating\n" + 
              		"FROM movies, genres, stars, stars_in_movies, genres_in_movies, ratings\n" + 
              		"WHERE movies.id=stars_in_movies.movieId AND stars_in_movies.starId=stars.id AND movies.id=genres_in_movies.movieId AND genres_in_movies.genreId=genres.id AND ratings.movieId=movies.id AND movies.title LIKE '"+input+"%'\n" + 
              		"GROUP BY movies.id, movies.title, movies.year, movies.director, ratings.rating\n" + 
              		"ORDER BY ratings.rating DESC\n" + 
              		"LIMIT 20;";
              
              //movies.title LIKE '%home%'
              //movies.title LIKE '%"+input+"%'\n" +

              // Perform the query
              ResultSet rs = statement.executeQuery(query);

              out.println("<TABLE border>");
             
              out.println("<body>\n" + 
                		"  <table id=\"title\">\n" + 
                		"    <thead>\n");               
              
              
           // Iterate through each row of rs
              //out.println("<TABLE border style='color:#FFFFFF'>");
              out.println("<tr>" + 
            	  "<td>" + "Movie Id" + "</td>" + 
              "<td>" + "Title" + "</td>" + 
            	  "<td>" + "Year" + "</td>" + 
              "<td>" + "Director" + "</td>"+ 
            	  "<td>" + "Stars" + "</td>" + 
              "<td>" + "Genres" + "</td>" + 
            	  "<td>" + "Rating" + "</td>" +
            	  "<td>" + "Add to Cart" + "</td>" +
              "</tr>");
              
              
              out.println("    </thead>");
              while (rs.next())
              {
                  String movie_id = rs.getString(1);
                  String title = rs.getString(2);
                  int year = rs.getInt(3) ;
                  String director = rs.getString(4);
                  String stars = rs.getString(5);
                  
                  String url="";
                  String star;
                  Scanner s = new Scanner(stars).useDelimiter(", ");
                  if (s.hasNext()) {
                	  	star=s.next();
                	  	url="<a href=\"./SingleStar?name="+star+"\""+">"+star+"</a>";
                  }
                  
                  while (s.hasNext()) {
                	  	star=s.next();
                	  	url=url+", <a href=\"./SingleStar?name="+star+"\""+">"+star+"</a>";
                	  //ystem.out.println(s.next()); 
                  }
                  
                  String genres = rs.getString(6);
                  Double rating = rs.getDouble(7);
                  out.println("<tr>" + "<td>" + movie_id+ "</td>" + "<td>"+"<a href=\"./SingleMovie?name="+title+"\""+">"+title+"</a>" + "</td>" + "<td>" + year + "</td>" + "<td>" + director + "</td>"
                          + "<td>" + url + "</td>" + "<td>" + genres + "</td>" + "<td>" + rating + "</td>" +"<td>"+"<a href=\"./ShoppingCart?name="+title+"\""+">add to cart</a>"+"</td>"+"</tr>");
              }
              out.println("</TABLE>");
              out.println("<a href='./ShoppingCart'>ShoppingCart</a>");
              
              out.println("	<script type=\"text/javascript\" src=\"http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.8.2.min.js\"></script>\n" + 
                		"    <script type=\"text/javascript\"  src=\"http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.min.js\"></script>" +
                		" \n" + 
                		"  <script>\n" + 
                		"  $(function(){\n" + 
                		"    $(\"#title\").dataTable();\n" + 
                		" \n" + 
                		"})" + 
                		" \n" + 
                		"    </script>");              

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
}
