

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
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

//		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
//		System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
//		// Verify CAPTCHA.
//		boolean valid = VerifyUtils.verify(gRecaptchaResponse);
//		if (!valid) {
//		    //errorString = "Captcha invalid!";
//		    out.println("<HTML>" +
//				"<HEAD><TITLE>" +
//				"MovieDB: Error" +
//				"</TITLE></HEAD>\n<BODY>" +
//				"<P>Recaptcha WRONG!!!! </P></BODY></HTML>");
//		    return;
//		}
		
		String loginUser = "root";
        String loginPasswd = "wei123456";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        
//        HttpSession session = request.getSession();
//        ArrayList customerInfo = null;
//	    session.setAttribute("customerInfo", customerInfo);
//	    
//        
//        
//        
        
        response.setContentType("text/html");    // Response mime type
        //String username = request.getParameter("username");
		//String password = request.getParameter("password");
        
   
        try
        {
        	
        	 Class.forName("com.mysql.jdbc.Driver").newInstance();
         Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
             // Declare our statement
        Statement statement = dbcon.createStatement();
		
        String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println("why can't I read");
		
		//String query = "SELECT email, password from customers;";
				
		String query = "SELECT email, password from customers where email = \"" + username + "\"" + " and password = \"" + password + "\"\n";
				//+"LIMIT 20;";
		ResultSet rs = statement.executeQuery(query);
		ArrayList<String> usernames = new ArrayList<String>();
		ArrayList<String> passwords = new ArrayList<String>();
		
		while (rs.next())
		{
			String the_username = rs.getString(1);
			String the_password = rs.getString(2);
			//System.out.println(the_username);
			//System.out.println(the_password);
			usernames.add(the_username);
			passwords.add(the_password);
		}
		
		int flag=0;
		int flag2=0;
		int index=0;
		for (int i=0;i<usernames.size();i++) {
			if (usernames.get(i).equals(username)) {
				flag=1;
				index=i;
				break;
			}				
		}
		if (flag==1) {
			if (passwords.get(index).equals(password)) {
				flag2=1;
			}
		}
			
		
		// this example only allows username/password to be test/test
		// in the real project, you should talk to the database to verify username/password
		if (flag==1&&flag2==1) {
			// login success:
			// set this user into the session
			
			request.getSession().setAttribute("user", new User(username));
			
			JsonObject responseJsonObject = new JsonObject();
			responseJsonObject.addProperty("status", "success");
			responseJsonObject.addProperty("message", "success");
			
			response.getWriter().write(responseJsonObject.toString());
		} else {
			// login fail
			request.getSession().setAttribute("user", new User(username));
			
			JsonObject responseJsonObject = new JsonObject();
			responseJsonObject.addProperty("status", "fail");
			if (flag==0) {
				responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
			} else if (flag2==0) {
				responseJsonObject.addProperty("message", "incorrect password");
			}
			
			response.getWriter().write(responseJsonObject.toString());
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