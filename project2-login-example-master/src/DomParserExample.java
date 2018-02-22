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

public class DomParserExample {

	//No generics
	List myEmpls;
	Document dom;


	public DomParserExample(){
		//create a list to hold the employee objects
		myEmpls = new ArrayList();
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
			dom = db.parse("casts124.xml");
			

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
		NodeList nl = docEle.getElementsByTagName("dirfilms");
		System.out.println("length of casts: "+nl.getLength());
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				
				//get the employee element
				Element el = (Element)nl.item(i);
				NodeList nl2 = el.getElementsByTagName("filmc");
				System.out.println("length of dirfilms: "+nl2.getLength());				
				if(nl2 != null && nl2.getLength() > 0) {
					for(int j = 0 ; j < nl2.getLength();j++) {
						Element el2=(Element)nl2.item(j);
						Casts e = getCasts(el2);
						myEmpls.add(e);												
					}
				}
				//get the Employee object
				
				
				//add it to list
				
			}
		}
	}


	/**
	 * I take an employee element and read the values in, create
	 * an Employee object and return it
	 * @param empEl
	 * @return
	 */
	private Casts getCasts(Element empEl) {
		
		//for each <employee> element get text or int values of 
		//name ,id, age and name
		String movieId = getTextValue(empEl, "f");
		String starId= getTextValue(empEl,"a");		
		//int id = getIntValue(empEl,"Id");
		//int age = getIntValue(empEl,"Age");

		//String type = empEl.getAttribute("type");
		
		//Create a new Employee with the value read from the xml nodes
		Casts e = new Casts(starId, movieId);
		
		return e;
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
			textVal = el.getFirstChild().getNodeValue();
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
		
		System.out.println("No of stars_in_movies '" + myEmpls.size() + "'.");
		
		Iterator it = myEmpls.iterator();
		while(it.hasNext()) {
			System.out.println(it.next().toString());
		}
	}

	
	public static void main(String[] args){
		//create an instance
		DomParserExample dpe = new DomParserExample();
		
		//call run example
		dpe.runExample();
	}

}