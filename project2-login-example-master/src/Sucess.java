

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Sucess
 */
@WebServlet("/Sucess")
public class Sucess extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Sucess() {
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
	    
	    
	    out.println("<body background='http://img.ivsky.com/img/tupian/pre/201012/25/suse_zhiwen.jpg'>\n" + 
	    		"    <div class=\"my-block\">");
	    out.println("<h1> check out Success :)</h1>"
	    		+ "<a href=\"./index.html\" style = \"float = 'left';\">HomePage</a>");
	    out.println("</div>\n" + 
	    		"    </body>"
	    		+ "</HTML>");
		
	    
	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
