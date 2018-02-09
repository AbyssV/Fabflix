

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class SingleMovie
 */
@WebServlet("/SingleMovie")
public class SingleMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleMovie() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginUser = "root";
        String loginPasswd = "wei123456";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("text/html");    // Response mime type
        PrintWriter out = response.getWriter();
		response.getWriter().append("Served at: ").append(request.getContextPath());
        try
        {
        	
        	 Class.forName("com.mysql.jdbc.Driver").newInstance();
         Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
             // Declare our statement
        Statement statement = dbcon.createStatement();
		
        System.out.println("connect successfully");
        //String movie_title = request.getParameter("name");
        String movie_title="Action";
		System.out.println("get parameterrrrr     "+movie_title);
		
		//String query = "SELECT email, password from customers;";
				
		String query = "SELECT movies.title, movies.year, movies.director, GROUP_CONCAT(DISTINCT stars.name ORDER BY stars.name SEPARATOR ', ') AS stars, GROUP_CONCAT(DISTINCT genres.name ORDER BY genres.name SEPARATOR ', ') AS genres, ratings.rating\n" + 
          		"FROM movies, genres, stars, stars_in_movies, genres_in_movies, ratings\n" + 
          		"WHERE movies.id=stars_in_movies.movieId AND stars_in_movies.starId=stars.id AND movies.id=genres_in_movies.movieId AND genres_in_movies.genreId=genres.id AND ratings.movieId=movies.id AND movies.title='"+movie_title+"'\n" + 
          		"GROUP BY movies.title, movies.year, movies.director, ratings.rating\n" + 
          		"ORDER BY ratings.rating DESC\n" + 
          		"LIMIT 20;";
		ResultSet rs = statement.executeQuery(query);

		
		while (rs.next())
		{
			String the_username = rs.getString(1);
			String the_password = rs.getString(2);

		}
		
			
		

        rs.close();
        statement.close();
        dbcon.close();
        }//try
        

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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
