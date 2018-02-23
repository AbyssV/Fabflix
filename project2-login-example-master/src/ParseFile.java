


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ParseFile {

	//No generics
	List myMovies;
	Document dom;


	public ParseFile(){
		//create a list to hold the employee objects
		myMovies = new ArrayList();
	}

	public void runExample() {
		
		//parse the xml file and get the dom object
		parseXmlFile();
		
		//get each employee element and create a Employee object
		parseDocument();
		
		//Iterate through the list and print the data
		printData();
		
	}
	
	
	private void parseXmlFile(){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			//parse using builder to get DOM representation of the XML file
			dom = db.parse("mains243.xml");
			

		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	
	private void parseDocument(){
		//get the root elememt
		Element docEle = dom.getDocumentElement();
		
		//get a nodelist of <employee> elements
		NodeList nl = docEle.getElementsByTagName("directorfilms");
		System.out.println("nl1  length " + nl.getLength());
		if(nl != null && nl.getLength() > 0) 
		{
			
			//nl.getLength()
			for(int i = 0 ; i < 10;i++) 
			{
				System.out.println("                         index is   "+i);
				Element el = (Element)nl.item(i);
				System.out.println("el is   "+el);
				
				String director = getDirector(el);
				System.out.println("director is   "+director);
				
				
				NodeList nl2 = el.getElementsByTagName("films");
				//get the film element
				Element el2 = (Element)nl2.item(0);
				Element el22 = (Element)nl2.item(1);
				
				System.out.println("el2 is   "+el2);
				System.out.println("nl2 length " + nl2.getLength());
				
				
				NodeList nl3 = el2.getElementsByTagName("film");
				System.out.println("nl3  length " + nl3.getLength());
				
				for(int j = 0 ; j < nl3.getLength();j++) 
				{
				
					Element el3 = (Element)nl3.item(j);
					System.out.println("inside  second for loop   "+j);
					
					
					//////////////////////////////////////////////////////////////////////////////////
					
////////////////					
					
					//get the Movie object
					Movie m = getMovie(el3, director);
					
					//add it to list
					myMovies.add(m);
				

				}
			}
		}
	}


	/**
	 * I take an employee element and read the values in, create
	 * an Employee object and return it
	 * @param empEl
	 * @return
	 */
	private Movie getMovie(Element empEl, String director) {
		
		//for each <employee> element get text or int values of 
		//name ,id, age and name
		String id = getTextValue(empEl,"fid");
		String title = getTextValue(empEl,"t");
		
		int year = getIntValue(empEl,"year");

		String genre = getTextValue(empEl,"cat");
		
		//Create a new Employee with the value read from the xml nodes
		Movie e = new Movie(id,title,year,director,genre);
		
		return e;
	}
	
	private String getDirector(Element empEl) {
		
		//for each <employee> element get text or int values of 
		//name ,id, age and name
		String director = getTextValue(empEl,"dirname");
		
		return director;
	}
	


	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content 
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is name I will return John  
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getTextContent();
		}

		return textVal;
	}

	
	/**
	 * Calls getTextValue and returns a int value
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}
	
	/**
	 * Iterate through the list and print the 
	 * content to console
	 */
	private void printData(){
		
		System.out.println("No of Movies '" + myMovies.size() + "'.");
		
		Iterator it = myMovies.iterator();
		while(it.hasNext()) {
			System.out.println(it.next().toString());
		}
	}


	public static void main(String[] args){
		//create an instance
		ParseFile dpe = new ParseFile();
		
		//call run example
		dpe.runExample();
	}

}


