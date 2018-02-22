

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Metadata
 */
@WebServlet("/Metadata")
public class Metadata extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Metadata() {
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

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        
        try {
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // Declare our statement
            Statement statement = dbcon.createStatement();
            Statement statement2 = dbcon.createStatement();

            System.out.println(" hello in servelet ");
            String query = "SHOW TABLES FROM moviedb;";
            
            ResultSet rs = statement.executeQuery(query);
            ResultSet rs2;
            while (rs.next())
            {
            		String table_name =  rs.getString(1);
            	
            		out.println("<h3>"+table_name+"</h3>");
            		String query2 = "SHOW COLUMNS FROM "+table_name+";";
            		System.out.println("go after query  " + query2);
            		rs2 = statement2.executeQuery(query2);
            	
            		while (rs2.next())
            		{
            			System.out.println("in while loop " );
            			String field = rs2.getString(1);
            			String type = rs2.getString(2); 
            			out.println("<tr>" + 
            	            	  "<td>" + field + "</td>    " + 
            	              "<td>" + type + "</td>" + 
            	              "</tr>"
            	              + "<br>");
            		}
            		
            		rs2.close();
            		
            }
            
            rs.close();
            //
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
