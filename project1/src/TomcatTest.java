
/* A servlet to display the contents of the MySQL movieDB database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TomcatTest extends HttpServlet {
    /**
	 * 
	 */

	public String getServletInfo() {
        return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Use http GET

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String loginUser = "root";
        String loginPasswd = "Jkh7956937159";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("text/html"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>MovieDB</TITLE><style>html{background-image: url(http://img3.imgtn.bdimg.com/it/u=3172796408,99931213&fm=27&gp=0.jpg);background-repeat:repeat;background-position:top left;}</style></HEAD>");
        
        out.println("<BODY><H1 style='color:#FFFFFF'>Top 20 Movies</H1>");

        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // Declare our statement
            Statement statement = dbcon.createStatement();

            String query = "SELECT movies.title, movies.year, movies.director, GROUP_CONCAT(DISTINCT stars.name ORDER BY stars.name SEPARATOR ', ') AS stars, GROUP_CONCAT(DISTINCT genres.name ORDER BY genres.name SEPARATOR ', ') AS genres, ratings.rating\n" + 
            		"FROM movies, genres, stars, stars_in_movies, genres_in_movies, ratings\n" + 
            		"WHERE movies.id=stars_in_movies.movieId AND stars_in_movies.starId=stars.id AND movies.id=genres_in_movies.movieId AND genres_in_movies.genreId=genres.id AND ratings.movieId=movies.id\n" + 
            		"GROUP BY movies.title, movies.year, movies.director, ratings.rating\n" + 
            		"ORDER BY ratings.rating DESC\n" + 
            		"LIMIT 20;";

            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            out.println("<TABLE border style='color:#FFFFFF'>");
            out.println("<tr>" + "<td>" + "Title" + "</td>" + "<td>" + "Year" + "</td>" + "<td>" + "Director" + "</td>"
                    + "<td>" + "Stars" + "</td>" + "<td>" + "Genres" + "</td>" + "<td>" + "Rating" + "</td>" +"</tr>");
            // Iterate through each row of rs
            while (rs.next()) {
            		
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
        } catch (SQLException ex) {
            while (ex != null) {
                System.out.println("SQL Exception:  " + ex.getMessage());
                ex = ex.getNextException();
            } // end while
        } // end catch SQLException

        catch (java.lang.Exception ex) {
            out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error" + "</TITLE></HEAD>\n<BODY>"
                    + "<P>SQL error in doGet: " + ex.getMessage() + "</P></BODY></HTML>");
            return;
        }
        out.close();
    }
}