import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;



public class DomParserExample {

    List<Movie> MovieList;
    List<Actor> ActorList;
    List<Casts> CastsList;
    Document dom;
    Document dom1;
    Document dom2;
    

    public DomParserExample() {
        //create a list to hold the employee objects
    		MovieList = new ArrayList<>();
    		ActorList = new ArrayList<>();
    		CastsList = new ArrayList<>();
    }

    public void runExample() {

        //parse the xml file and get the dom object
        parseXmlFile();

        //get each employee element and create a Employee object
        parseDocument();

        //doInsert();
        //Iterate through the list and print the data
        try {
			doInsert();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    private void parseXmlFile() {
    	
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse("mains243.xml");
            dom1 = db.parse("actors63.xml");
            dom2 = db.parse("casts124.xml");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void parseDocument() {
        //get the root elememt
        Element docEle = dom.getDocumentElement();
        Element docEle1 = dom1.getDocumentElement();
        Element docEle2 = dom2.getDocumentElement();
        

        //get a nodelist of <employee> elements
        NodeList nl = docEle.getElementsByTagName("directorfilms");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                //get the employee element
                Element el = (Element) nl.item(i);

                //get the Employee object
                Movie m = getMovie(el);

                //add it to list
                MovieList.add(m);
            }
        }
        NodeList nl1 = docEle1.getElementsByTagName("actor");
        if (nl1 != null && nl1.getLength() > 0) {
            for (int i = 0; i < nl1.getLength(); i++) {

                //get the employee element
                Element el = (Element) nl1.item(i);

                //get the Employee object
                Actor a = getStar(el);

                //add it to list
                ActorList.add(a);
            }
        }
        NodeList nl2 = docEle2.getElementsByTagName("dirfilms");
        if (nl2 != null && nl2.getLength() > 0) {
            for (int i = 0; i < nl2.getLength(); i++) {

                //get the employee element
                Element el = (Element) nl2.item(i);

                //get the Employee object
                Casts c = getCast(el);

                //add it to list
                CastsList.add(c);
            }
        }
    }

    private Casts getCast(Element el) {
    	//for each <employee> element get text or int values of 
        //name ,id, age and name
        String movie_title = getTextValue(el, "t");
        String star_name = getTextValue(el, "a");
        
        //Create a new Employee with the value read from the xml nodes
        Casts c = new Casts(movie_title, star_name);

        return c;
	}

	private Actor getStar(Element el) {
    	//for each <employee> element get text or int values of 
        //name ,id, age and name
        String name = getTextValue(el, "stagename");
        int birthYear = getIntValue(el, "dob");
        
        //Create a new Employee with the value read from the xml nodes
        Actor a = new Actor(name, birthYear);

        return a;
	}

	/**
     * I take an employee element and read the values in, create
     * an Employee object and return it
     * 
     * @param empEl
     * @return
     */
    private Movie getMovie(Element movEl) {

        //for each <employee> element get text or int values of 
        //name ,id, age and name
        String title = getTextValue(movEl, "t");
        int year = getIntValue(movEl, "year");
        String director = getTextValue(movEl, "dirn");
        String genre = getTextValue(movEl, "cat");

        //Create a new Employee with the value read from the xml nodes
        Movie m = new Movie(title, year, director, genre);

        return m;
    }

    /**
     * I take a xml element and the tag name, look for the tag and get
     * the text content
     * i.e for <employee><name>John</name></employee> xml snippet if
     * the Element points to employee node and tagName is name I will return John
     * 
     * @param ele
     * @param tagName
     * @return
     */
    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            textVal = el.getTextContent();
        }

        return textVal;
    }

    /**
     * Calls getTextValue and returns a int value
     * 
     * @param ele
     * @param tagName
     * @return
     */
    private int getIntValue(Element ele, String tagName) {
        
    	try{
    		//in production application you would catch the exception
    		return Integer.parseInt(getTextValue(ele, tagName));
    	}
    	catch(NumberFormatException e)
    	{
    		
    	}
		return 0;
        
    }

    /**
     * Iterate through the list and print the
     * content to console
     */
    private void doInsert() throws InstantiationException, IllegalAccessException, ClassNotFoundException {


        
        /*Iterator<Movie> it = myMovies.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }
        Iterator<Star> it_1 = myStars.iterator();
        while (it_1.hasNext()) {
            System.out.println(it_1.next().toString());
        }
        Iterator<Cast> it_2 = mySIM.iterator();
        while (it_2.hasNext()) {
            System.out.println(it_2.next().toString());
        }*/
    	/*System.out.println(myMovies.get(0).toString());
    	System.out.println(myMovies.get(myMovies.size() - 1).toString());
    	System.out.println(myStars.get(0).toString());
    	System.out.println(myStars.get(myStars.size() - 1).toString());
    	System.out.println(mySIM.get(0).toString());
    	System.out.println(mySIM.get(mySIM.size() - 1).toString());*/
    	 Connection conn = null;

         Class.forName("com.mysql.jdbc.Driver").newInstance();
         String jdbcURL="jdbc:mysql://localhost:3306/moviedb";

         try {
             conn = DriverManager.getConnection(jdbcURL,"root", "wei123456");
         } catch (SQLException e) {
             e.printStackTrace();
         }

         PreparedStatement psInsertRecord=null;
         String sqlInsertRecord=null;

         int[] iNoRows=null;
         
        
         

         sqlInsertRecord="insert into movies(id, title, year, director) values(?, ?,?,?)";
         
         try {
 			conn.setAutoCommit(false);

             psInsertRecord=conn.prepareStatement(sqlInsertRecord);

             int m_id = 0; //mid is the currently maximum id
             Statement statement_1 = conn.createStatement();
	  	    	 String query_1 = "SELECT CAST((SELECT SUBSTRING_INDEX((select max(id) from movies),'t',-1)) AS UNSIGNED)";
	  	    	 ResultSet rs_1 = statement_1.executeQuery(query_1);
	  	    	 if(rs_1.next())
	  	    	 {
	  	    		 m_id = rs_1.getInt(1);
	  	    	 }
	  	    	 
	  	    	System.out.println(" 1111111");
	  	    for(int i=0;i<MovieList.size();i++)
	  	    {
            	 
            	 String query_0 = "select * from movies where title = ?";
       	  	  	 PreparedStatement pstmt = conn.prepareStatement( query_0 );
       	  	  	 pstmt.setString( 1, MovieList.get(i).getTitle());
       	  	  	 
       	  	     ResultSet rs_0 = pstmt.executeQuery();
       	  	     
       	  	     if(!rs_0.next() )
       	  	     {
       	  	    	 	 m_id++;
       	  	    	 	 String m_iidd = "ttt" + m_id;
       	  	    		 psInsertRecord.setString(1, m_iidd);
       	  	    		 
       	  	    		 if(MovieList.get(i).getTitle() == null)
       	  	    		 {
       	  	    			 psInsertRecord.setString(2, "");
       	  	    		 }
       	  	    		 else
       	  	    		 {
       	  	    			 psInsertRecord.setString(2, MovieList.get(i).getTitle());
       	  	    		 }
       	  	    		 
       	  	    		 psInsertRecord.setInt(3, MovieList.get(i).getYear());
       	  	    		 
       	  	    		 if(MovieList.get(i).getDirector() == null)
       	  	    		 {
       	  	    			 psInsertRecord.setString(4, "");
       	  	    		 }
       	  	    		 else
       	  	    		 {
       	  	    			 psInsertRecord.setString(4, MovieList.get(i).getDirector());
       	                
       	  	    		 }
       	  	    		 
       	  	    		 psInsertRecord.addBatch();
       	  	    	 
       	  	    	 
       	  		    
       	  	     }
            	 
             }

 			iNoRows=psInsertRecord.executeBatch();
 			conn.commit();
         } catch (SQLException e) {
             e.printStackTrace();
         }
 			  
 			
         System.out.println(" 2222222");
 			PreparedStatement psInsertRecord_1=null;
 	        String sqlInsertRecord_1=null;

 	         int[] iNoRows_1=null;
 	         

 	         sqlInsertRecord_1="insert into genres(name) values(?)";
 	         
 	         try {
 	 			conn.setAutoCommit(false);

 	             psInsertRecord_1=conn.prepareStatement(sqlInsertRecord_1);

 	            Set<String> genres = new HashSet<>();
 	             for(int i=0;i<MovieList.size();i++)
 	             {
 	            	 genres.add(MovieList.get(i).getGenre());
 	             }
 	             
 	            Iterator<String> iterator = genres.iterator();
 	           
 	            while (iterator.hasNext()) {
 	               String g = iterator.next();
 	            	 
 	            	 String query_0 = "select * from genres where name = ?";
 	            	 PreparedStatement pstmt = conn.prepareStatement( query_0 );
 	            	pstmt.setString( 1, g); 

 	       	  	     ResultSet rs_0 = pstmt.executeQuery();
 	       	  	     
 	       	  	     if(!rs_0.next())
 	       	  	     {
 	       	  	    	 	if(g == null)
 	       	  	    	 	{
 	       	  	    	 		psInsertRecord_1.setString(1,"");
 	 	       	  	    	 
 	       	  	    	 	}
 	       	  	    	 	else
 	       	  	    	 	{
 	       	  	    	 		psInsertRecord_1.setString(1, g);
 	       	  	    	 	}
 	       	  	    		 
 	       	  	    		 
 	                
 	       	  	    		 psInsertRecord_1.addBatch();
 	       	  	    }
 	       	  	    	 
 	       	  		    
 	       	    }
 	            	 
 	             

 	 			iNoRows_1=psInsertRecord_1.executeBatch();
 	 			conn.commit();
 			
 			
 			
 			

         } catch (SQLException e) {
             e.printStackTrace();
         }
 	         
 	        System.out.println(" 3333333");
  			PreparedStatement psInsertRecord_2=null;
  	        String sqlInsertRecord_2=null;

  	         int[] iNoRows_2=null;
  	         

  	         sqlInsertRecord_2="insert into genres_in_movies(genreId, movieId) values(?, ?)";
  	         
  	         try {
  	 			 conn.setAutoCommit(false);

  	             psInsertRecord_2=conn.prepareStatement(sqlInsertRecord_2);


  	             for(int i=0;i<MovieList.size();i++)
  	             {
  	            	 int genre_id = 0;
  	            	 String query_1 = "select id from genres where name = ?";
  	            	 PreparedStatement pstmt = conn.prepareStatement( query_1 );
  	            	 pstmt.setString(1, MovieList.get(i).getGenre()); 

  	       	  	     ResultSet rs_1 = pstmt.executeQuery();
  	       	  	     if(rs_1.next())
  	       	  	     {
  	       	  	    	 genre_id = rs_1.getInt(1);
  	       	  	     }
  	       	  	    	 
  	       	  	     String movie_id = "";
  	       	  	     String query_2 = "select id from movies where title = ? and year = ? and director =?";
  	       	  	     PreparedStatement pstmt1 = conn.prepareStatement( query_2 );
  	       	  		pstmt1.setString( 1, MovieList.get(i).getTitle()); 
  	       	  		pstmt1.setInt( 2, MovieList.get(i).getYear()); 
  	       	  		pstmt1.setString( 3, MovieList.get(i).getDirector()); 
	       	  	     ResultSet rs_2 = pstmt1.executeQuery();
	       	  	     
	       	  	     if(rs_2.next())
	       	  	     {
	       	  	    	 movie_id = rs_2.getString(1);
	       	  	     }
	       	  	     if(genre_id != 0 && movie_id != "")
	       	  	     {
	       	  	    	 Statement statement_0 = conn.createStatement();
	       	  	    	 String query_0 = "select * from genres_in_movies where genreId = (select id from genres where id = '"+genre_id+"') AND movieId = (select id from movies where id = '"+movie_id+"')";
	       	  	    	 ResultSet rs_0 = statement_0.executeQuery(query_0);
  	       	  	     
	       	  	    	 if(!rs_0.next())
	       	  	    	 {
  	       	  	    	 
  	       	  	    		 psInsertRecord_2.setInt(1, genre_id);
  	       	  	    		 psInsertRecord_2.setString(2, movie_id);
	       	  	    		 psInsertRecord_2.addBatch();
	       	  	    	 }
  	       	  	    	 
	       	  	     }
	       	  	     
  	       	  		    
  	       	  	 }
  	            	 
  	             

  	 			iNoRows_2=psInsertRecord_2.executeBatch();
  	 			conn.commit();
  			
  			
  			
  			

          } catch (SQLException e) {
              e.printStackTrace();
          }
  	         
  	       System.out.println(" 44444444");
  	       PreparedStatement psInsertRecord_3=null;
 	        String sqlInsertRecord_3=null;

 	         int[] iNoRows_3=null;
 	         

 	         sqlInsertRecord_3="insert into stars(id, name, birthYear) values(?, ?, ?)";
 	         
 	         try {
 	 			 conn.setAutoCommit(false);

 	             psInsertRecord_3=conn.prepareStatement(sqlInsertRecord_3);
 	             
 	             int s_id = 0;
 	             Statement statement_1 = conn.createStatement();
		  	    	 String query_1 = "SELECT CAST((SELECT SUBSTRING_INDEX((select max(id) from stars),'m',-1)) AS UNSIGNED)";
		  	    	 ResultSet rs_1 = statement_1.executeQuery(query_1);
		  	    	 if(rs_1.next())
		  	    	 {
		  	    	 		s_id=rs_1.getInt(1);
		  	    	 }
	             for(int i=0;i<ActorList.size();i++)
	             {
	            	 
	       	  	     
	       	  	     String query_0 = "select * from stars where name = ? and birthYear = ?";
	       	  	  	 PreparedStatement pstmt = conn.prepareStatement( query_0 );
	       	  	  	 pstmt.setString( 1, ActorList.get(i).getStagename());
	       	  	  	 pstmt.setInt( 2, ActorList.get(i).getYear());
	
	       	  	     ResultSet rs_0 = pstmt.executeQuery();
	       	  	     
	       	  	     if(!rs_0.next())
	       	  	     {
	       	  	    	 	
	       	  	    	 	s_id++;
	       	  	    	 	String s_iidd = "nmm"+s_id;
	       	  	    	 	psInsertRecord_3.setString(1, s_iidd);
	       	  	    		psInsertRecord_3.setString(2, ActorList.get(i).getStagename());
	       	  	    		psInsertRecord_3.setInt(3, ActorList.get(i).getYear());
	       	  	    		 	
	       	  	    		psInsertRecord_3.addBatch();
	       	  	    	 	
	       	  	    		 
	       	  	    }
	       	  	    	 
	       	  		    
	       	  	 }
	 	            	 
 	             

 	 			iNoRows_3=psInsertRecord_3.executeBatch();
 	 			conn.commit();
 			
 			
 			
 			

         } catch (SQLException e) {
             e.printStackTrace();
         }
 	         
 	         
 	        System.out.println(" 55555555");
 	        PreparedStatement psInsertRecord_4=null;
  	        String sqlInsertRecord_4=null;

  	         int[] iNoRows_4=null;
  	         

  	         sqlInsertRecord_4="insert into stars_in_movies(starId, movieId) values(?, ?)";
  	         
  	         try {
  	 			 conn.setAutoCommit(false);

  	             psInsertRecord_4=conn.prepareStatement(sqlInsertRecord_4);

  	             //CastsList.size()
  	           ArrayList<String> sm=new ArrayList<String>(); 
  	           ArrayList<String> result=new ArrayList<String>(); 
  	             for(int i=0;i<2000;i++)
  	             {
	  	            	 String star_id = "";
	  	            	 String query_1 = "select id from stars where name = ?";
	  	            	 
	  	            	 PreparedStatement pstmt = conn.prepareStatement( query_1 );
	  	            	 pstmt.setString( 1, CastsList.get(i).getStagename()); 

  	       	  	     ResultSet rs_1 = pstmt.executeQuery();
  	       	  	     if(rs_1.next())
  	       	  	     {
  	       	  	    	 star_id = rs_1.getString(1);
  	       	  	     }
  	       	  	     //System.out.println(" 66" + star_id);
  	       	  	     String movie_id = "";
  	       	  	     String query_2 = "select id from movies where title = ?";
  	       	  	     PreparedStatement pstmt1 = conn.prepareStatement( query_2 );
  	       	  	     pstmt1.setString( 1, CastsList.get(i).getMovietitle());
  	       	  	     
  	       	  	     //System.out.println(mySIM.get(i).getmovie_title());
	       	  	     ResultSet rs_2 = pstmt1.executeQuery();
	       	  	    	 
	       	  	     if(rs_2.next())
	       	  	     {
	       	  	    	 movie_id = rs_2.getString(1);
	       	  	     }
	       	  	     
	       	  	     //System.out.println(" 77" + movie_id);
	       	  	     
	       	  	     
	       	  	     
	       	  	     if(star_id != "" && movie_id != "")
	       	  	     {
		       	  	    	 Statement statement_0 = conn.createStatement();
		     
		       	  	    	 
		       	  	    	 String query_0 = "select count(*) from stars_in_movies where starId = '"+star_id+"' AND movieId = '"+movie_id+"'";
		       	  	    	 ResultSet rs_0 = statement_0.executeQuery(query_0);
		       	  	    	 rs_0.next();
		       	  	    	 int count = rs_0.getInt(1) ;
		       	  	    	 
		       	  	    //System.out.println("count" + count); 
		       	  	    
		      
		       	  	    	 if(count==0 )
		       	  	    	 {
		       	  	    		 
		       	  	    	psInsertRecord_4.setString(1, star_id);
	    	  	    		 psInsertRecord_4.setString(2, movie_id);
	  	  	    		 psInsertRecord_4.addBatch();	       	  	 
	  	       	  	    		 
		       	  	    	 }
		       	  	    	 else
		       	  	    	 {
		       	  	    		 System.out.println("duplicate entry"); 
		       	  	    	 }
		       	  	 
		       
	       	  	    	
	       	  	    	
	       	  	    			
	       	  	    		
	       	  	    		
	       	  	    }
	       	  	     
	       	  	     }
	       	  	     
	       	  	     
  	       	  	   
  	       	  	    	 
  	       	  		    
  	       	  
  	            	 
  	             try {

  	 			iNoRows_4=psInsertRecord_4.executeBatch();
  	 			conn.commit();
  			
  	             }
  	             catch(BatchUpdateException e)
  	             {
  	            	System.out.println("duplicate entry, fail insert");  	         
  	            	//e.printStackTrace();
  	             }
  			
  			

          } catch (SQLException e) {
              e.printStackTrace();
          }
  	         
 	         

         try {
             if(psInsertRecord!=null) psInsertRecord.close();
             if(psInsertRecord_1!=null) psInsertRecord_1.close();
             if(psInsertRecord_2!=null) psInsertRecord_2.close();
             if(psInsertRecord_3!=null) psInsertRecord_3.close();
             if(psInsertRecord_4!=null) psInsertRecord_4.close();
             
             
             if(conn!=null) conn.close();
         } catch(Exception e) {
             e.printStackTrace();
         }
    	
    									
    	
        
    }

    public static void main(String[] args) {
        //create an instance
        DomParserExample dpe = new DomParserExample();

        //call run example
        dpe.runExample();
    }

}
