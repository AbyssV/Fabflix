

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

/**
 * Servlet implementation class InsertStar
 */
@WebServlet("/InsertStar")
public class InsertStar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertStar() {
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
            //Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            // Declare our statement
            Statement statement = dbcon.createStatement();

            System.out.println(" hello in servelet ");
            String name = request.getParameter("name");
            String birthYear = request.getParameter("birthYear");

            
            System.out.println("                 before get parameter ");
            System.out.println(" name   "+name);
            System.out.println(" year   "+birthYear);

            
            System.out.println("               after get parameter ");
            int flag=0;
            String total_input =  "";
            if (name.length()>0)
            {
            		total_input += "stars.name ='" + name +"' ";
            }
            if (birthYear.length()>0)
            {
        			total_input += "AND stars.birthYear=" + birthYear +" ";
            }
            if (name.length()==0)
            {
            		flag=-1;//show error messsage: name can't be empty
            }
        

            
            System.out.println(" total input !   "+total_input);

            
            String query_checkExist = "SELECT *"
            		+ "FROM stars"
            		+ " WHERE " + total_input +" ;";
            System.out.println(" total input !   "+query_checkExist);
            
            ResultSet rs1 = statement.executeQuery(query_checkExist);
            System.out.println("111111  ");
            if (rs1.next())
            {
            	
            		System.out.println("2222  ");
            		flag=0; //already in the database
            }
            else 
            {
            		flag=1; //insert sucessfully
            }
            
            System.out.println("before query  ");
         
            String query = "select max(id) from stars" ;
              	
            System.out.println("go after query  " + query );
      
            ResultSet rs = statement.executeQuery(query);
            
            
            System.out.println("before if loop  " );
            
            // Iterate through each row of rs
            if (rs.next() && flag!=0 ) {
            	
            		System.out.println("in if loop "  );
             	String old_id = rs.getString(1);
             	int id = Integer.parseInt(old_id.substring(2));
             	
             	System.out.println("44444"  );
             	String new_id = "nm"+Integer.toString(id+1);
             	
             	System.out.println("3333333         "+new_id  );
             	
             	String query_insertStar = "INSERT INTO stars VALUES('"+new_id+"','"+name+"',"+birthYear+");";
             	
             	System.out.println(" total input !   "+query_insertStar);
             	statement.executeUpdate(query_insertStar); 

             	
             	
             	System.out.println(" insert sucessfully"  );
            }
            
            if (flag==1) {
    			// insert success:
    			
    			
    			JsonObject responseJsonObject = new JsonObject();
    			responseJsonObject.addProperty("status", "flag1");
    			responseJsonObject.addProperty("message", "successfully insert star!");
    			
    			response.getWriter().write(responseJsonObject.toString());
    		} 
            else if (flag ==-1) 
            {
    			//name empty
    			JsonObject responseJsonObject = new JsonObject();
    			responseJsonObject.addProperty("status", "flag-1");
    			
    			responseJsonObject.addProperty("message", "star name can not be empty");
    			
    			response.getWriter().write(responseJsonObject.toString());
    		}
            
    		else //if (flag ==0) 
    		{
    			
    			JsonObject responseJsonObject = new JsonObject();
    			responseJsonObject.addProperty("status", "flag0");
    			
    			responseJsonObject.addProperty("message", "star already in the datebase");
    			
    			response.getWriter().write(responseJsonObject.toString());
    		}
            
            
            System.out.println(" after json");
            rs.close();
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
