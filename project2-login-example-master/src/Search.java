

/* A servlet to display the contents of the MySQL movieDB database */

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

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
        String loginPasswd = "1065428254djdg";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>MovieDB: Found Records</TITLE></HEAD>");
        out.println("<BODY><H1>MovieDB: Found Records</H1>");


        try
           {
              //Class.forName("org.gjt.mm.mysql.Driver");
              Class.forName("com.mysql.jdbc.Driver").newInstance();

              Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
              // Declare our statement
              Statement statement = dbcon.createStatement();

              String input = request.getParameter("search");
              out.println("get ppppparameter   in search "+input);
              
              
              
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

              out.println("<TABLE border>");
              
              
              
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

              

              while (rs.next())
              {
                  
                  String title = rs.getString(1);
                  int year = rs.getInt(2) ;
                  String director = rs.getString(3);
                  String stars = rs.getString(4);
                  String genres = rs.getString(5);
                  Double rating = rs.getDouble(6);
                  out.println("<tr>" + "<td>" + title + "</td>" + "<td>" + year + "</td>" + "<td>" + director + "</td>"
                          + "<td>" + stars + "</td>" + "<td>" + genres + "</td>" + "<td>" + rating + "</td>" +"</tr>");
              }
              out.println("</TABLE>");

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
