

/* A servlet to display the contents of the MySQL movieDB database */

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class BrowseByGenre extends HttpServlet
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

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>MovieDB: Found Records</TITLE></HEAD>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css\">\n" + 
        		"");
        out.println("<BODY><H1>MovieDB: Found Records</H1>");


        try
           {
              //Class.forName("org.gjt.mm.mysql.Driver");
              Class.forName("com.mysql.jdbc.Driver").newInstance();

              Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
              // Declare our statement
              Statement statement = dbcon.createStatement();

              String input = request.getParameter("name");
              out.println("get ppppparameter    "+input);
              
              String query = "SELECT movies.title, movies.year, movies.director, GROUP_CONCAT(DISTINCT stars.name ORDER BY stars.name SEPARATOR ', ') AS stars, GROUP_CONCAT(DISTINCT genres.name ORDER BY genres.name SEPARATOR ', ') AS genres, ratings.rating\n" + 
              		"FROM movies, genres, stars, stars_in_movies, genres_in_movies, ratings\n" + 
              		"WHERE movies.id=stars_in_movies.movieId AND stars_in_movies.starId=stars.id AND movies.id=genres_in_movies.movieId AND genres_in_movies.genreId=genres.id AND ratings.movieId=movies.id AND genres.name='"+input+"'\n" + 
              		"GROUP BY movies.title, movies.year, movies.director, ratings.rating\n" + 
              		"ORDER BY ratings.rating DESC\n" + 
              		"LIMIT 20;";
              
              //movies.title LIKE '%home%'
              //movies.title LIKE '%"+input+"%'\n" +

              // Perform the query
              ResultSet rs = statement.executeQuery(query);

              //out.println("<TABLE border>");
    
              out.println("<body>\n" + 
              		"  <table id=\"genre\">\n" + 
              		"    <thead>\n"); 
           
             
         
              
           // Iterate through each row of rs
              //out.println("<TABLE border style='color:#FFFFFF'>");
              out.println("<tr>" + 
              "<td>" + "Title" + "</td>" + 
            	  "<td>" + "Year" + "</td>" + 
              "<td>" + "Director" + "</td>"+ 
            	  "<td>" + "Stars" + "</td>" + 
              "<td>" + "Genres" + "</td>" + 
            	  "<td>" + "Rating" + "</td>" +
              "</tr>");

              out.println("    </thead>");

              while (rs.next())
              {
                  
                  String title = rs.getString(1);               
                  int year = rs.getInt(2) ;
                  String director = rs.getString(3);
                  String stars = rs.getString(4);
                  String url="";
                  String star;
                  Scanner s = new Scanner(stars).useDelimiter(", ");
                  if (s.hasNext()) {
                	  	star=s.next();
                	  	url="<a href=\"./BrowseByGenre?name="+star+"\\\""+">"+star+"</a>";
                  }
                  
                  while (s.hasNext()) {
                	  	star=s.next();
                	  	url=url+", <a href=\"./BrowseByGenre?name="+star+"\\\""+">"+star+"</a>";
                	  //ystem.out.println(s.next()); 
                  }
                  
                 
                  String genres = rs.getString(5);
                  //String url="<a href=\"./BrowseByGenre?name="+stars+"\\\""+">"+stars+"</a>"
                  Double rating = rs.getDouble(6);
                  out.println("<tr>" + "<td>" + "<a href=\"./SingleMovie?name="+title+"\\\""+">"+title+"</a>" + "</td>" + "<td>" + year + "</td>" + "<td>" + director + "</td>"
                          + "<td>" + url + "</td>" + "<td>" + genres + "</td>" + "<td>" + rating + "</td>" +"</tr>");
              }
      
              out.println("</TABLE>");
        
              out.println("	<script type=\"text/javascript\" src=\"http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.8.2.min.js\"></script>\n" + 
              		"    <script type=\"text/javascript\"  src=\"http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.min.js\"></script>" +
              		" \n" + 
              		"  <script>\n" + 
              		"  $(function(){\n" + 
              		"    $(\"#genre\").dataTable();\n" + 
              		" \n" + 
              		"})" + 
              		" \n" + 
              		"    </script>");
          
              
              /**
                "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>	\n" + 
              		"	<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\"></script>\n" + 
              		"	<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\"></script>\n" + 
              		"\n" + 
              		"	<script src=\"./advancedsearch.js\"></script>\n" + 
              out.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>	\n" + 
              		"	<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\"></script>\n" + 
              		"	<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\"></script>\n" + 
              		"\n" + 
              		"	<script type=\"text/javascript\" src=\"http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.8.2.min.js\"></script>\n" + 
              		"    <script type=\"text/javascript\"  src=\"http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/jquery.dataTables.min.js\"></script>");
			**/
              
              
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
