

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
	    ArrayList previousItems = (ArrayList)session.getAttribute("previousItems");
	    if (previousItems == null) {
	      previousItems = new ArrayList();
	      session.setAttribute("previousItems", previousItems);
	    }

	    String newItem = request.getParameter("name");

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
	    
	    out.println("<div class=\"shopping-cart\">\n" + 
	    		"\n" + 
	    		"  <div class=\"column-labels\">\n" + 
	    		"    <label class=\"product-image\">Image</label>\n" + 
	    		"    <label class=\"product-details\">Product</label>\n" + 
	    		"    <label class=\"product-price\">Price</label>\n" + 
	    		"    <label class=\"product-quantity\">Quantity</label>\n" + 
	    		"    <label class=\"product-removal\">Remove</label>\n" + 
	    		"    <label class=\"product-line-price\">Total</label>\n" + 
	    		"  </div>");
	    				

	   synchronized(previousItems) {
	      if (newItem != null) {
	        previousItems.add(newItem);
	      }
	      if (previousItems.size() == 0) {
	        out.println("<I>No items</I>");
	      } else {
	        out.println("<UL>");
	        for(int i=0; i<previousItems.size(); i++) {
	          out.println("<LI>" + (String)previousItems.get(i));
	        }
	        out.println("</UL>");
	      }
	    }

	   // The following two statements show how this thread can access an
	   // object created by a thread of the ShowSession servlet
	   // Integer accessCount = (Integer)session.getAttribute("accessCount");
	   // out.println("<p>accessCount = " + accessCount);

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
