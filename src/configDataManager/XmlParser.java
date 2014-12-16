package configDataManager;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.ArrayList;

import org.w3c.dom.*;

import util.KeyValuePair;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XmlParser
{	
	String path; //the file path of the xml file to parse
	
	public XmlParser(String p)
	{		
		path = p;
	}
	
	/**
	 * Reads and loads the entire ClassNameMapping.xml configuration file in a HashMap
	 * @return HashMap of dir_patter as keys and values as HashMap of file_type and ArrayList of model_class_name
	 */
	public HashMap<String, HashMap<String, ArrayList<String>>> getClassNameTable()
	{
		 HashMap<String, HashMap<String, ArrayList<String>>> xmlTable = null;
		 HashMap<String, ArrayList<String>> fileTypeClassNames;
		 ArrayList<String> classNames;
		 //Create a new DocumentBuilderFactory
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder = null;
		 try
		 {
		   //Create a new DocumentBuilder 
		   builder = factory.newDocumentBuilder();
		   Document document = builder.parse(new FileInputStream(new File(path)));
		   //Normalize XML Structure
		   document.getDocumentElement().normalize();
		   
		    //Get all categories
		   NodeList catgList = document.getElementsByTagName("directory");
	       xmlTable = new HashMap<String, HashMap<String, ArrayList<String>>>();
	     
	     // iterate through child elements of category	     
	        for ( int i = 0; i < catgList.getLength(); i++) {
	            Element catg = (Element) catgList.item(i);
	            String dirPattern = catg.getAttribute("dir_pattern");//get value of dir_pattern
	            if (dirPattern == "") //empty string if unable to get value 
	            	return null;
	            fileTypeClassNames = new  HashMap<String, ArrayList<String>>();
	            //get a list of all file types in this category
	            NodeList fTypeList = catg.getElementsByTagName("file_type");
	            //iterate through child elements of file_type
	            for (int j = 0;  j < fTypeList.getLength(); j++)
	            {
	            	Element fType = (Element)fTypeList.item(j);
	            	String fileType = fType.getAttribute("value");
	            	classNames = new ArrayList<String>();
	            	NodeList classNameList = fType.getElementsByTagName("model_classname");	            			
	            	//System.out.println("Dir_Pattern: "+dirPattern +" FileType: "+ fileType);
		          //iterate through child elements of file_type with element name model_classname
		            for (int k = 0; k < classNameList.getLength(); k++ )
		            {
		            	Element cName =(Element) classNameList.item(k);
		            	String className = cName.getTextContent();
		            	//System.out.println(" model class: "+ className );
		            	
		            	//add classname to list of model_classnames
		            	classNames.add(className);		            	
		            }//end for model_classname list
		            fileTypeClassNames.put(fileType, classNames);
	            }//end for file_type iteration
	            xmlTable.put(dirPattern, fileTypeClassNames);	            
		 }//end for directory iteration 
		 }
		 catch(Exception e){
			 System.err.println("Error in XmlParser getClassNameTable() "+e.getMessage());			 
			 return null;
		 }
		 return xmlTable;
	 }
	/**
	 * Reads and loads the entire modelKeys.xml file into a HashMap
	 * @return a HashMap of model class names as keys and values as ArrayList of KeyValuePairs containing
	 * keys and their types
	 */
	public HashMap<String, ArrayList<KeyValuePair>> getModelKeyTypeTable()
	{
		HashMap<String, ArrayList<KeyValuePair>> xmlTable = null;
		ArrayList<KeyValuePair> keyAndTypeList;
		//Create a new DocumentBuilderFactory
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder = null;
		 try
		 {
		   //Create a new DocumentBuilder 
		   builder = factory.newDocumentBuilder();
		   Document document = builder.parse(new FileInputStream(new File(path)));
		   //Normalize XML Structure
		   document.getDocumentElement().normalize();
		   
		    //Get all models
		   NodeList modelList = document.getElementsByTagName("model");
		   xmlTable = new HashMap<String, ArrayList<KeyValuePair>>();
		     
		  //iterate through child elements of models	     
		   for ( int i = 0; i < modelList.getLength(); i++) {
		       Element model = (Element) modelList.item(i);
		       String className = model.getAttribute("class_name");//get value of class_name
		       if (className == "") //empty string if unable to get value 
		            return null;
		       //get ready to store the list of keys and their type for this model
		       keyAndTypeList = new ArrayList<KeyValuePair>();
		       //get a list of all keys for this model
		       NodeList keyList = model.getElementsByTagName("key");
		       //iterate through child elements of file_type
		       for (int j = 0;  j < keyList.getLength(); j++)
		       {
		            Element keyElement = (Element)keyList.item(j);
		            String type = keyElement.getAttribute("type");		
		            String key = keyElement.getTextContent();
			        //System.out.println("className: "+className +" Key Type: "+ type+" key: "+ key );
			        KeyValuePair keyTypePair = new KeyValuePair(key,type); //create keyValuePairwith key and its type
			            	
			        //add keyTypePair to list of keyAndTypeList
			        keyAndTypeList.add(keyTypePair);		            	
			   }//end for key list
			           
		       xmlTable.put(className, keyAndTypeList);	            
			 }//end for model iteration 
			 }
			 catch(Exception e){
				 System.err.println("Error in XmlParser getModelKeyTypeTable() "+e.getMessage());			 
				 return null;
			 }
		return xmlTable;
		
	}
}
