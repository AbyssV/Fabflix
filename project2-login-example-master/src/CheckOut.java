

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
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();

	    out.println("<HTML><HEAD><TITLE>confirmation</TITLE>"
	    		+ ""
	    		+ "<style>\n" + 
	    		"\n" + 
	    		"        html, body {\n" + 
	    		"            height: 100%;\n" + 
	    		"            margin: 0;\n" + 
	    		"            padding: 0;\n" + 
	    		"            width: 100%;\n" + 
	    		"        }\n" + 
	    		"\n" + 
	    		"        body {\n" + 
	    		"            display: table;\n" + 
	    		"        }\n" + 
	    		"\n" + 
	    		"        .my-block {\n" + 
	    		"            text-align: center;\n" + 
	    		"            display: table-cell;\n" + 
	    		"            vertical-align: middle;\n" + 
	    		"        }\n" + 
	    		"        </style>"
	    		+ ""
	    		+ ""
	    		+ "</HEAD>");
		HttpSession session = request.getSession();
		//session.invalidate();
		
	    HashMap<String,Integer> previousItems = (HashMap<String,Integer>)session.getAttribute("previousItems");
	    User user = (User)session.getAttribute("user");
	    
	    if (previousItems.isEmpty() == true) 
	    {
	    	
	    		out.println("<body background='http://pic.qiantucdn.com/00/94/60/08bOOOPICc3.jpg!qt780'>");
	    		out.println("<h1>" +"Nothing in shopping cart" +"</h1>");
	    		out.println("<a href='./index.html'>HomePage</a>");
	    		out.println("</body>\n" + 
	    				"</html>");
		}
	    else
	    {
    		out.println("<body background='http://pic.qiantucdn.com/00/94/60/08bOOOPICc3.jpg!qt780'>");

	    	out.println(" <div class='my-block'>"
	    			+ "<h3>" +"Moveis you want to check out:" +"</h3>");
	    	for(String key: previousItems.keySet()) {
        		out.println("<LI>" +key+"  ");
        		out.println(previousItems.get(key));
        		out.println("</LI>");
        }
        
	    
		
		String loginUser = "root";
        String loginPasswd = "wei123456";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

		
		out.println("	<br><br>"
				+ "<br>" +
				"<h3> Please enter your information:</h3>" +
				"<form id=\"input_form\">\n" + 
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
				"</div>");
		
		
		
		
		
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
                

            	
            	String userID = "SELECT customers.id \n" + 
            			"from customers \n" + 
            			"where customers.firstName='"+first_name+"' and customers.lastName='"+last_name +"' and customers.ccId='"+credit+"';\n" + 
            			"";
            		
            	System.out.println("go after query  " + userID);
            	
            	
            	System.out.println("this is the error 1");
            	//ResultSet rs = statement.executeQuery(query);
            	ResultSet rs2 = statement.executeQuery(userID);   
            	int userid = 0;
            	if (rs2.next())
                {
            		userid = rs2.getInt(1);
                }
            		
            		
            		
            
        		System.out.println("this is the error 2");
             for(String key: previousItems.keySet()) {
         		
            	 System.out.println("this is the error 3  inseide for loop      " + key);
            	
	            	 	String movieID = "SELECT movies.id \n" + 
	            			"from movies \n" + 
	            			"where movies.title='"+key+"' ;";
            			
	            	 	System.out.println("go after query  " + movieID);
	            	 	
	            	 	System.out.println("this is the error 4");
	            	 	
	            	 	ResultSet rs3 = statement.executeQuery(movieID); 
	            	 	rs3.next();
	            	 	String movieid= rs3.getString(1);
	            	 	
	            	 	System.out.println("this is the error 5      "+movieid);
              
	            	 	String insertSale = "\n" + 
              		"INSERT INTO sales(customerId,movieId,saleDate) VALUES('"+userid+"','"+movieid+"', curdate());";
	            	 	
	            	 	System.out.println("this is the error 5      "+insertSale);
	            	 	
	            	 	
	            	 
               statement.executeUpdate(insertSale);
               
               
               
               String querynnnn = "SELECT sales.saleDate \n" + 
           			"from sales \n" + 
           			"where sales.customerId='"+userid+"' and sales.movieId='"+movieid +"' ;\n" + 
           			"";
               ResultSet rsn = statement.executeQuery(querynnnn);   
           	if (rsn.next())
               {
           		System.out.println("sucessfully insert     " + rsn.getDate(1));               }
             	}
             
             previousItems.clear();
              response.sendRedirect("./Sucess");
              
            		
       
            }
            
            else
            {
            	
            			out.println("<div class='my-block'>"
            					+ "<h3>check out failed! :(</h3>"+
            			"<h3>You need to enter again :(</h3>"+
            					"</div>");
            			//response.sendRedirect("./CheckOut");
            		//out.println("<h1>" +"check out failed! :(" +"</h1>");
            }
            //out.println("</TABLE>");
            //out.println("</body>\n" + 
    				//"</html>");
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
//                out.println("<HTML>" +
//                            "<HEAD><TITLE>" +
//                            "MovieDB: Error" +
//                            "</TITLE></HEAD>\n<BODY>" +
//                            "<P>SQL error in doGet: " +
//                            ex.getMessage() + "</P></BODY></HTML>");
                return;
            }
         out.close();}
        
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
