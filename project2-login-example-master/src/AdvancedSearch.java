

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

import java.util.*;
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
        	
        		long startTime1 = System.nanoTime();
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
            
            
            System.out.println("                 before get parameter ");
            System.out.println(" title   "+title);
            System.out.println(" year   "+year);
            System.out.println(" director   "+director);
            System.out.println(" star_name   "+input_star_name);
            
            
            System.out.println("               after get parameter ");
            
            
            
            Connection conn = null;
            HashMap<String,String> hm=new HashMap<String,String>();
            
            String total_input =  "";
            if (title.length()>0)
            {
            		total_input += "AND movies.title LIKE ? ";
            		hm.put("title", title);
            }
            if (year.length()>0)
            {
        			total_input += "AND movies.year=? ";
        			hm.put("year", year);
            }
            if (director.length()>0)
            {
            		total_input += "AND movies.director LIKE ? ";
            		hm.put("director", director);
            }
            if (input_star_name.length()>0)
            {
            		total_input += "AND stars.name LIKE ? ";
            		hm.put("input_star_name", input_star_name);
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
            PreparedStatement pstmt = dbcon.prepareStatement( query );
            //pstmt.setString( 1, MovieList.get(i).getTitle());
            
            int index=1;
            
            if(hm.get("title")!=null)
            {
            		pstmt.setString( index, "%"+title+"%");
            		index++;
            }
            if(hm.get("year")!=null)
            {
            		pstmt.setString( index, year);
            		index++;
            }
            if(hm.get("director")!=null)
            {
            		pstmt.setString( index, "%"+director+"%");
            		index++;
            }
            if(hm.get("input_star_name")!=null)
            {
            		pstmt.setString( index, "%"+input_star_name+"%");
            		index++;
            }
            
            
            
            
            
            long startTime2 = System.nanoTime();
            ResultSet rs = pstmt.executeQuery();
            long endTime2 = System.nanoTime();
            
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
            long endTime1 = System.nanoTime();
            
            long TS = endTime1 - startTime1;
            long TJ = endTime2 - startTime2;
            
            System.out.println(" TS       "+TS);
            System.out.println(" TJ       "+TJ);
            
            
//            try(FileWriter fw = new FileWriter("/Users/weijingkaihui/Downloads/p5.txt ", true);
//          		    BufferedWriter bw = new BufferedWriter(fw);
//          		    PrintWriter out2 = new PrintWriter(bw))
//          		{
//          		    out2.print(TS);
//          		    out2.print(" ");
//          		    out2.println(TJ);
//          		    
//          		} catch (IOException e) {
//          		    //exception handling left as an exercise for the reader
//          		}
            
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