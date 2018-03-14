

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

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
		//response.getWriter().append("Served at: ").append(request.getContextPath());
        out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" + 
        		"<link type=\"text/css\" rel=\"stylesheet\" href=\"path_to/simplePagination.css\"/>\n" + 
        		"<script src=\"https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css\"></script>\n" + 
        		"<link rel=\"stylesheet\" type=\"text/css\" href=\"http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/css/jquery.dataTables.css\">\n" + 
        		"<link rel=\"stylesheet\" type=\"text/css\" href=\"https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css\">");
        
        
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

        	
//        	 Class.forName("com.mysql.jdbc.Driver").newInstance();
//         Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
             // Declare our statement
        Statement statement = dbcon.createStatement();
		
    
        String movie_title = request.getParameter("name");
        //String movie_title="Action";

        
        
        out.println("<BODY><H1>Information about movie "+movie_title+"</H1>");
		//String query = "SELECT email, password from customers;";
				
		String query = "SELECT movies.id, movies.title, movies.year, movies.director, GROUP_CONCAT(DISTINCT stars.name ORDER BY stars.name SEPARATOR ', ') AS stars, GROUP_CONCAT(DISTINCT genres.name ORDER BY genres.name SEPARATOR ', ') AS genres, ratings.rating\n" + 
          		"FROM movies, genres, stars, stars_in_movies, genres_in_movies, ratings\n" + 
          		"WHERE movies.id=stars_in_movies.movieId AND stars_in_movies.starId=stars.id AND movies.id=genres_in_movies.movieId AND genres_in_movies.genreId=genres.id AND ratings.movieId=movies.id AND movies.title =\""+movie_title+"\"\n" + 
          		"GROUP BY movies.id, movies.title, movies.year, movies.director, ratings.rating\n" + 
          		"ORDER BY ratings.rating DESC\n" + 
          		"LIMIT 20;";
		System.out.println(query);
		ResultSet rs = statement.executeQuery(query);

		out.println("<TABLE border>");
        out.println("<body>\n" + 
        		"  <table id=\"title\">\n" + 
        		"    <thead>\n");     
		
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
            out.println("<tr>" + "<td>" + movie_id+ "</td>" + "<td>"+ title + "</td>" + "<td>" + year + "</td>" + "<td>" + director + "</td>"
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
