

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*; 


/**
 * Servlet implementation class ShoppingCart
 */
@WebServlet("/ShoppingCart")
public class ShoppingCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingCart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		HttpSession session = request.getSession();
		//session.invalidate();
		
	    HashMap<String,Integer> previousItems = (HashMap<String,Integer>)session.getAttribute("previousItems");
	    if (previousItems == null) {
	      previousItems = new HashMap<String,Integer>();
	      session.setAttribute("previousItems", previousItems);
	    }

	    
	    
	    String newItem = request.getParameter("name");
	    
	    
	    System.out.println("get ppppparameter for newitem   "+ newItem);
	    
	    
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    String title = "Items Purchased";
	    String docType =
	      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
	      "Transitional//EN\">\n";
	    out.println(docType +
	                "<HTML>\n" +
	                "<HEAD><TITLE>" + title + "</TITLE></HEAD>\n" +
	                "<BODY BGCOLOR=\"#FDF5E6\">\n" +
	                "<H1>" + title + "</H1>");
	    
	
	    				
	    
	    
	 
	   synchronized(previousItems) {
		   
//		   String remove_index = request.getParameter("name");
//	       System.out.println("get ppppparameter for remove    "+remove_index);
//	       if(remove_index != null)
//	       {previousItems.remove(remove_index);}
//		    
	      if (newItem != null) {
	    	  	if (previousItems.containsKey(newItem))
	    	  	{
	    	  		previousItems.put(newItem, (previousItems.get(newItem) + 1));
	    	  	}
	    	  	else
	    	  	{
	    	  		previousItems.put(newItem,1);
	    	  	}
	      }
	      if (previousItems.size() == 0) {
	        out.println("<I>No items</I>");
	      } 
	      else {
	    	  
	    	  	
	        out.println("<UL>");
	        for(String key: previousItems.keySet()) {
	        		System.out.println(key);
	        		System.out.println(previousItems.get(key));
	        		
	        }
	        
	        
	        String big_movie_name = "";
	        for(String key: previousItems.keySet()) {
	        	  Integer value = previousItems.get(key);
	          out.println("<LI>" + key);
	          
	          out.println("</BR>");
	          
	         
	          
	          out.println("<form id=\"myForm\" action='./ShoppingCart\'>\n" + 
	        		  	//"<label> " + key + "</label>" +
		        		"  <input type='hidden' name='movie title' value='"+key+"'>\n" +     
		        		"  Quatity: <input type='text' name='quatity' value='"+value+"'>\n" + 
		        		"  <input type=\"submit\"  value=\"Change Quantity\">\n" + 
		        		"</form>\n" 
		        		);
		        
		        String movie_title = request.getParameter("movie title");
		        String quatity = request.getParameter("quatity");
			    
			    if (quatity != null &&Integer.parseInt(quatity)!=value) 
			    {
				    previousItems.put(movie_title, Integer.parseInt(quatity));
				    response.sendRedirect("./ShoppingCart");

			    }
		   
	    
	          out.println("<form id=\"myForm2\" action='./ShoppingCart\'>\n" + 
		        		"  <input type='hidden' name='movie name' value='"+key+"'>\n" + 
		        		"  <input type=\"submit\"  value=\"Delete\">\n" + 
		        		"</form>\n" 
		        		);
	          
	          
		        String movie_name = request.getParameter("movie name");
		        if (movie_name!=null)
		        {
		        	big_movie_name=movie_name;
		        		
		        }

	        }
	        
	        
	        if (big_movie_name.length()!=0)
	        {previousItems.remove(big_movie_name);
	        big_movie_name="";
	        response.sendRedirect("./ShoppingCart");}
	        
	        out.println("</UL>");
	      }
	      
	      
	      
	    }
	   
	   
	   
	   for(String key: previousItems.keySet()) {
     	  Integer value = previousItems.get(key);
     	  System.out.println( key+value);
	   }

	   // The following two statements show how this thread can access an
	   // object created by a thread of the ShowSession servlet
	   // Integer accessCount = (Integer)session.getAttribute("accessCount");
	   // out.println("<p>accessCount = " + accessCount);

	   
	   out.println("<a href='./CheckOut'>Check Out</a>");
	   out.println("<a href='./index.html'>HomePage</a>");
	   out.println("</BODY></HTML>");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
