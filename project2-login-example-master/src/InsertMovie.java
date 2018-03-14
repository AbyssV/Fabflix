

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

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
 * Servlet implementation class InsertStar
 */
@WebServlet("/InsertMovie")
public class InsertMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertMovie() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		
		
		String loginUser = "root";
        String loginPasswd = "wei123456";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

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
            String genre = request.getParameter("genre");
            String name = request.getParameter("name");
            String birthYear = request.getParameter("birthyear");

            
            System.out.println("                 before get parameter ");
            System.out.println(" title   "+title);
            System.out.println(" year   "+year);

            
            System.out.println("               after get parameter ");
            int flag=0;
            if (title.length()==0)
            {
            		flag=-1;
            }
            if (year.length()==0)
            {
            		flag=-1;
            }
            if (director.length()==0)
            {
            		flag=-1;
            }
            if (genre.length()==0)
            {
            		flag=-1;
            }
            if (name.length()==0)
            {
            		flag=-1;
            }
            if (birthYear.length()==0)
            {
            		flag=-1;
            }
          
        

            String result = "";
            System.out.println(" before query   ");
            //call add_movie('like', 2018, 'Damon Lee', 'wjkh', 1899, 'Action');
            if(flag!=-1) {
            CallableStatement stored_procedure = dbcon.prepareCall("{call add_movie(?, ?,?, ?, ?, ?,?)}");
            
            stored_procedure.registerOutParameter(7, Types.VARCHAR);
            
            stored_procedure.setString(1, title);
            stored_procedure.setInt(2, Integer.parseInt(year));
            stored_procedure.setString(3, director);
            stored_procedure.setString(4, name);
            stored_procedure.setInt(5,Integer.parseInt(birthYear));
            stored_procedure.setString(6, genre);
            
            stored_procedure.execute();
            System.out.println(" after query   ");
            //ResultSet result = stored_procedure.getResultSet();

            System.out.println("result = "+stored_procedure.getString(7));
            result = stored_procedure.getString(7);
            //String result=stored_procedure.getString(7);
            
//            if (result==1)
//            {
//            	flag=1;//insert sucessufully
//            }
//            else if(result==0)
//            {
//            	flag=0; //already in the database
//            }}
            //String result = stored_procedure.getString(1);
            
            //String query = "call add_movie('"+title+"',"+year+",'"+director+"','"+name+"',"+birthYear+",'"+genre+"');";
            //System.out.println(" total output !   "+out1);
            
            //ResultSet rs = statement.executeQuery(query);
//            System.out.println("111111  ");
//            if (rs.next())
//            {
//            	
//            		System.out.println("2222  ");
//            		flag=1; //already in the database
//            }
      
           
      
            }
            
        
//            if (flag==1) 
//        {
//    			// insert success:
//    			
//    			
//    			JsonObject responseJsonObject = new JsonObject();
//    			responseJsonObject.addProperty("status", "flag1");
//    			responseJsonObject.addProperty("message", "successfully insert movie!");
//    			
//    			response.getWriter().write(responseJsonObject.toString());
//    		} 
            if (flag ==-1) 
        {
    			//name empty
    			JsonObject responseJsonObject = new JsonObject();
    			responseJsonObject.addProperty("status", "flag-1");
    			
    			responseJsonObject.addProperty("message", "movie info can not be empty, each field should be entered");
    			
    			response.getWriter().write(responseJsonObject.toString());
    		}
            
    		else if (flag ==0) 
    		{
    			
    			JsonObject responseJsonObject = new JsonObject();
    			responseJsonObject.addProperty("status", "flag0");
    			
    			
				responseJsonObject.addProperty("message", result);
    			
    			response.getWriter().write(responseJsonObject.toString());
    		}
            
            
            System.out.println(" after json");
            //rs.close();
            statement.close();
            dbcon.close();
	}
        catch (Exception e) {
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
