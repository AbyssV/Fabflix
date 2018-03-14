

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

/**
 * Servlet implementation class Movie
 */
@WebServlet("/AdvancedSearch")
public class AdvancedSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdvancedSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
//		String loginUser = "root";
//        String loginPasswd = "wei123456";
//        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        
        System.out.println("In this servlet now~~");

        try {
        	// the following few lines are for connection pooling
            // Obtain our environment naming context

            Context initCtx = new InitialContext();
            if (initCtx == null)
                out.println("initCtx is NULL");

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");

            // the following commented lines are direct connections without pooling
            //Class.forName("org.gjt.mm.mysql.Driver");
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

            if (ds == null)
                out.println("ds is null.");

            Connection dbcon = ds.getConnection();
            if (dbcon == null)
                out.println("dbcon is null.");
//            
//            //Class.forName("org.gjt.mm.mysql.Driver");
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//
//            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // Declare our statement
            Statement statement = dbcon.createStatement();

            System.out.println(" hello in servelet ");
            String title = request.getParameter("title");
            String year = request.getParameter("year");
            String director = request.getParameter("director");
            String input_star_name = request.getParameter("star_name");
            //String year = request.getParameter("sel");
            
            
            System.out.println("                 before get parameter ");
            System.out.println(" title   "+title);
            System.out.println(" year   "+year);
            System.out.println(" director   "+director);
            System.out.println(" star_name   "+input_star_name);
            
            
            System.out.println("               after get parameter ");
            
            String total_input =  "";
            if (title.length()>0)
            {
            		total_input += "AND movies.title LIKE '%" + title +"%' ";
            }
            if (year.length()>0)
            {
        			total_input += "AND movies.year=" + year +" ";
            }
            if (director.length()>0)
            {
            		total_input += "AND movies.director LIKE '%" + director +"%' ";
            }
            if (input_star_name.length()>0)
            {
            		total_input += "AND stars.name LIKE '%" + input_star_name +"%' ";
            }

            
            System.out.println(" total input !   "+total_input);

            
            System.out.println("before query  ");
            //genre = "drama";
            String query = "SELECT movies.id, movies.title, movies.year, movies.director, GROUP_CONCAT(DISTINCT stars.name ORDER BY stars.name SEPARATOR ', ') AS stars, GROUP_CONCAT(DISTINCT genres.name ORDER BY genres.name SEPARATOR ', ') AS genres, ratings.rating\n" + 
              		"FROM movies, genres, stars, stars_in_movies, genres_in_movies, ratings\n" + 
              		"WHERE movies.id=stars_in_movies.movieId AND stars_in_movies.starId=stars.id AND movies.id=genres_in_movies.movieId AND genres_in_movies.genreId=genres.id AND ratings.movieId=movies.id  "+ total_input +"\n" + 
              		"GROUP BY movies.id, movies.title, movies.year, movies.director, ratings.rating\n" + 
              		"ORDER BY ratings.rating DESC;" ;//+ 
              		//"LIMIT 100;";
            
            System.out.println("parameterrrrrrr  "+title);
            System.out.println("go after query  " + query);
            //total_input[1]
            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            
            JsonArray jsonArray = new JsonArray();
            
            // Iterate through each row of rs
            while (rs.next()) {
             	String movie_id = rs.getString(1);
                String movie_title = rs.getString(2);
                int movie_year = rs.getInt(3);
                String movie_director = rs.getString(4);
                String star_name = rs.getString(5);
                String genre_type = rs.getString(6);
                double rating = rs.getDouble(7);
                
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
        } catch (Exception e) {
            out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error" + "</TITLE></HEAD>\n<BODY>"
                    + "<P>SQL error in doGet: " + e.getMessage() + "</P></BODY></HTML>");
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