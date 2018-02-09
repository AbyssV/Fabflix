

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();

		HttpSession session = request.getSession();
		//session.invalidate();
		
	    HashMap<String,Integer> previousItems = (HashMap<String,Integer>)session.getAttribute("previousItems");
	    
	    if (previousItems == null) 
	    {
	    		out.println("<h1>" +"Nothing in shopping cart" +"</h1>");
		}
	    else
	    {
	    	
	    	out.println("<h1>" +"Moveis you want to check out:" +"</h1>");
	    	for(String key: previousItems.keySet()) {
        		out.println("<h3>" +key+"  ");
        		out.println(previousItems.get(key)+" </h3>");
        		
        }
        
	    }
		
		String loginUser = "root";
        String loginPasswd = "wei123456";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

		
		out.println("	<form id=\"input_form\">\n" + 
				"	credit card number:<br>\n" + 
				"	\n" + 
				"	<input type=\"text\" name=\"credit\">\n" + 
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
            String credit = request.getParameter("credit");
            String first_name = request.getParameter("First Name");
            String last_name = request.getParameter("Last Name");
            String expiration = request.getParameter("expiration");
            
            
            System.out.println("                 before get parameter ");
            System.out.println(" credit   "+credit);
            System.out.println(" first_name   "+first_name);
            System.out.println(" last_name   "+last_name);
            System.out.println(" expiration   "+expiration);
            
            
            System.out.println("               after get parameter ");
            
            String total_input =  "";
            if (credit.length()>0)
            {
            		total_input += "creditcards.id ='" + credit +"' ";
            }
            if (first_name.length()>0)
            {
        			total_input += "AND creditcards.firstName='" + first_name +"' ";
            }
            if (last_name.length()>0)
            {
            		total_input += "AND creditcards.lastName='" + last_name +"' ";
            }
            if (expiration.length()>0)
            {
            		total_input += "AND creditcards.expiration='" + expiration +"' ";
            }

            
            System.out.println(" total input !   "+total_input);

            
            System.out.println("before query  ");
            //genre = "drama";
            String query = "SELECT creditcards.id,  creditcards.firstName, creditcards.lastName, creditcards.expiration\n" + 
            		"FROM creditcards\n" + 
            		"WHERE "+ total_input;
            
            System.out.println("go after query  " + query);
            //total_input[1]
            // Perform the query
            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            //out.println("<TABLE border>");
            
            
            
         // Iterate through each row of rs
            //out.println("<TABLE border style='color:#FFFFFF'>");
          

            //156017
            //Wendy
            //Hu
            //2007/09/30

            if (rs.next())
            {
                
                //String result_credir = rs.getString(1);
                //String result_firstName = rs.getString(2) ;
                //String result_lastName = rs.getString(3);
                //String resultExpiration = rs.getString(4);
            	
            		response.sendRedirect("./Sucess");
                //out.println("<h1>" +"check out sucess!" +"</h1>");
            }
            
            else
            {
            			out.println("<h3>" +"check out failed! :(" +"</h3>");
            			out.println("<h3>" +"You need to enter again :(" +"</h3>");
            			//response.sendRedirect("./CheckOut");
            		//out.println("<h1>" +"check out failed! :(" +"</h1>");
            }
            //out.println("</TABLE>");

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
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
