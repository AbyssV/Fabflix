

/* A servlet to display the contents of the MySQL movieDB database */

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;

@WebServlet("/AutoComplete")
public class AutoComplete extends HttpServlet
{
	
	public static HashMap<Integer, String> movie_list = new HashMap<>();
	public static HashMap<Integer, String> star_list = new HashMap<>();
	
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutoComplete() {
        super();
    }

    
    // Use http GET
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
//        String loginUser = "root";
//        String loginPasswd = "wei123456";
//        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("application/json");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
    


        try
           {
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
              //Class.forName("org.gjt.mm.mysql.Driver");
              //Class.forName("com.mysql.jdbc.Driver").newInstance();

              //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
              // Declare our statement
              Statement statement = dbcon.createStatement();
              Statement statement2 = dbcon.createStatement();

              String input = request.getParameter("query");
              JsonArray jsonArray = new JsonArray();

              
           // return the empty json array if query is null or empty
  			if (input == null || input.trim().isEmpty()) {
  				response.getWriter().write(jsonArray.toString());
  				return;}
              
//            String result="";
//            if (input.length()<6) {
//          	  	result="(edrec(movies.title, '"+input+"', 2)=1)";
//            }
//            else if (input.length()>5) {
//          	  	result="(edrec(movies.title, '"+input+"' ,4)=1)";
//            }  
//  			
  			
  			
  			Connection conn = null;
  			String[] splited = input.split(" ");
              
              String total_input="";
              

              for (int i=0; i<splited.length; i++)
              {
            	  total_input = total_input + "MATCH (title) AGAINST ( ? IN BOOLEAN MODE) ";
            	  if (i != splited.length-1)
              	  	{
              	  		total_input+="AND ";
              	  	}
              }
             
           
              System.out.println(total_input);
              
              
              
              String query = ""
              		+ "SELECT movies.id, movies.title, movies.year, movies.director, GROUP_CONCAT(DISTINCT stars.name ORDER BY stars.name SEPARATOR ', ') AS stars, GROUP_CONCAT(DISTINCT genres.name ORDER BY genres.name SEPARATOR ', ') AS genres, ratings.rating\n" + 
                		"FROM movies, genres, stars, stars_in_movies, genres_in_movies, ratings\n" + 
                		"WHERE movies.id=stars_in_movies.movieId AND stars_in_movies.starId=stars.id AND movies.id=genres_in_movies.movieId AND genres_in_movies.genreId=genres.id AND ratings.movieId=movies.id AND " + total_input+"\n" +
                		"GROUP BY movies.id, movies.title, movies.year, movies.director, ratings.rating\n" + 
                		"ORDER BY ratings.rating DESC\n" +
                		"LIMIT 5;";
                	
             
              
              System.out.println("query = "+query);
              
              //movies.title LIKE '%home%'
              //movies.title LIKE '%"+input+"%'\n" +

              // Perform the query
 
              PreparedStatement pstmt = dbcon.prepareStatement( query );
              
              for (int i=1; i<=splited.length; i++)
              {
            	  	pstmt.setString( i,splited[i-1]+"*" );
 
              }
              
              
              
              
              // Perform the query
              long startTime2 = System.nanoTime();
              ResultSet rs = pstmt.executeQuery();
              long endTime2 = System.nanoTime();

              int key=1;
              movie_list = new HashMap<>();
              while (rs.next())
              {
                  String movie_title = rs.getString(2);
                  movie_list.put(key, movie_title);
                  key++;
                  //jsonArray.add(generateJsonObject(movie_title, "movie"));
                 
              }
           
              
//              String result2="";
//              if (input.length()<6) {
//            	  	result2="(edrec(stars.name, '"+input+"', 2)=1)";
//              }
//              else if (input.length()>5) {
//            	  	result2="(edrec(stars.name, '"+input+"' ,4)=1)";
//              }  
              
              
              System.out.println(" after json1");
              
              String total_input2="";
              

              for (int i=0; i<splited.length; i++)
              {
            	  total_input2 = total_input2 + "MATCH (name) AGAINST ( ? IN BOOLEAN MODE) ";
            	  if (i != splited.length-1)
              	  	{
              	  		total_input2+="AND ";
              	  	}
              }
             
           
              System.out.println(total_input2);
              
              
              
              String query2 = ""
              		+ "SELECT name\n" + 
                		"FROM stars\n" + 
                		"WHERE " + total_input2+"\n" +
                		"LIMIT 5;";
                	
             
              
              System.out.println("query2 = "+query2);
              
              PreparedStatement pstmt2 = dbcon.prepareStatement( query2 );
              
              for (int i=1; i<=splited.length; i++)
              {
            	  	pstmt2.setString( i,splited[i-1]+"*" );
 
              }
                  
              // Perform the query
              long startTime3 = System.nanoTime();
              rs = pstmt2.executeQuery();      
              long endTime3 = System.nanoTime();

              
              star_list = new HashMap<>();
              int key2=0;
              while (rs.next())
              {
            	  
                  String star_name = rs.getString(1);
                  System.out.println("in while loop        "+star_name);
                  star_list.put(key2, star_name);
                  key2++;
                  //jsonArray.add(generateJsonObject(movie_title, "movie"));
                 
              }
              
              for (Integer id : movie_list.keySet()) {
  				String movieName = movie_list.get(id);
  				System.out.println(movieName);
  				jsonArray.add(generateJsonObject(movieName, "movie"));
  			}
              System.out.println("        ");
              System.out.println("        ");
              System.out.println("        ");
              System.out.println("        ");
  			for (Integer id : star_list.keySet()) {
  				String starName = star_list.get(id);
  				System.out.println(starName);
  				jsonArray.add(generateJsonObject(starName, "star"));
  				 }
  			
  			  System.out.println(" after json2");
  			  out.write(jsonArray.toString());
              rs.close();
              statement.close();
              dbcon.close();
              long endTime1 = System.nanoTime();
              
              
              long TS = endTime1 - startTime1;
              long TJ = endTime2 - startTime2;
              long TJ_star = endTime3 - startTime3;
              
              System.out.println(" TS       "+TS);
              System.out.println(" TJ       "+TJ);
              System.out.println(" TJ_star       "+TJ_star);
              
//              try(FileWriter fw = new FileWriter("/Users/weijingkaihui/Downloads/p5.txt ", true);
//            		    BufferedWriter bw = new BufferedWriter(fw);
//            		    PrintWriter out2 = new PrintWriter(bw))
//            		{
//            		    out2.print(TS);
//            		    out2.print(" ");
//            		    out2.print(TJ);
//            		    out2.print(" ");
//            		    out2.println(TJ_star);
//            		    
//            		} catch (IOException e) {
//            		    //exception handling left as an exercise for the reader
//            		}
              
              
              
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
    
    
    private static JsonObject generateJsonObject(String movie_title, String categoryName) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", movie_title);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("category", categoryName);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}
 
}

