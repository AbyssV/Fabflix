

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*; 
/**
 * Servlet implementation class CheckOut
 */
@WebServlet("/CheckOut")
public class CheckOut extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckOut() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		
		String loginUser = "root";
        String loginPasswd = "wei123456";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

		
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
		out.println("	<form id=\"input_form\">\n" + 
				"	id:<br>\n" + 
				"	\n" + 
				"	<input type=\"text\" name=\"id\">\n" + 
				"	<br><br>\n" + 
				"	\n" + 
				"	First Name:<br>\n" + 
				"    <input type=\"text\" name=\"First Name\">\n" + 
				"	<br><br>\n" + 
				"	\n" + 
				"	Last Name:<br>\n" + 
				"	<input type=\"text\" name=\"Last Name\">\n" + 
				"	<br><br>\n" + 
				"	\n" + 
				"	expiration:<br>\n" + 
				"	<input type=\"text\" name=\"expiration\">\n" + 
				"	<br><br>\n" + 
				"    \n" + 
				"    \n" + 
				"    <input type=\"submit\" value=\"Submit\">\n" + 
				"\n" + 
				"	</form>\n" + 
				"");
		
		
		
		
		
		try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // Declare our statement
            Statement statement = dbcon.createStatement();

            System.out.println(" hello in servelet ");
            String id = request.getParameter("id");
            String first_name = request.getParameter("First Name");
            String last_name = request.getParameter("Last Name");
            String expiration = request.getParameter("expiration");
            
            
            System.out.println("                 before get parameter ");
            System.out.println(" id   "+id);
            System.out.println(" first_name   "+first_name);
            System.out.println(" last_name   "+last_name);
            System.out.println(" expiration   "+expiration);
            
            
            System.out.println("               after get parameter ");
            
            String total_input =  "";
            if (id.length()>0)
            {
            		total_input += "creditcards.id ='" + id +"' ";
            }
            if (first_name.length()>0)
            {
        			total_input += "AND creditcards.firstName=" + first_name +"' ";
            }
            if (last_name.length()>0)
            {
            		total_input += "AND creditcards.lastName='" + last_name +"' ";
            }
            if (expiration.length()>0)
            {
            		total_input += "AND creditcards.expiration=" + expiration +"' ";
            }

            
            System.out.println(" total input !   "+total_input);

            
            System.out.println("before query  ");
            //genre = "drama";
            String query = "SELECT movies.title, movies.year, movies.director, GROUP_CONCAT(DISTINCT stars.name ORDER BY stars.name SEPARATOR ', ') AS stars, GROUP_CONCAT(DISTINCT genres.name ORDER BY genres.name SEPARATOR ', ') AS genres, ratings.rating\n" + 
              		"FROM movies, genres, stars, stars_in_movies, genres_in_movies, ratings\n" + 
              		"WHERE movies.id=stars_in_movies.movieId AND stars_in_movies.starId=stars.id AND movies.id=genres_in_movies.movieId AND genres_in_movies.genreId=genres.id AND ratings.movieId=movies.id AND "+ total_input +"\n" + 
              		"GROUP BY movies.title, movies.year, movies.director, ratings.rating\n" + 
              		"ORDER BY ratings.rating DESC;" ;//+ 
              		//"LIMIT 100;";
            
            System.out.println("go after query  " + query);
            //total_input[1]
            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            
            JsonArray jsonArray = new JsonArray();
            
            // Iterate through each row of rs
            while (rs.next()) {
                String movie_title = rs.getString(1);
                int movie_year = rs.getInt(2);
                String movie_director = rs.getString(3);
                String star_name = rs.getString(4);
                String genre_type = rs.getString(5);
                double rating = rs.getDouble(6);
                
                JsonObject jsonObject = new JsonObject();
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
        } catch (Exception e) {
            out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error" + "</TITLE></HEAD>\n<BODY>"
                    + "<P>SQL error in doGet: " + e.getMessage() + "</P></BODY></HTML>");
            return;
        }
        out.close();
        
	}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
