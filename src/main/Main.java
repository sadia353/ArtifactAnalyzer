package main;
import java.util.ArrayList;





import configDataManager.ClassNameAndKeyReader;
import postSolr.PostSolrDoc;
import util.KeyValuePair;
import model.DefaultModel;
import model.Model;
import modelCreator.CreateModel;
import buildSolr.BuildSolrDoc;


public class Main 
{
	/** 
	 *The main method provides the entry point
	 *to the application 
	 *It attempts to index the input files into the Solr index by first attempting to 
	 *convert them into Solr XML Strings
	 *@param args absolute file path(s) to files that have to be indexed
	 */
	public static void main(String args[]) 
	{
		int size = args.length;
		String filePath;
		if (size == 0) //no file paths 
		{
			System.err.println("Main: No file paths specified");
			return;
		}
		BuildSolrDoc buildSolr = new BuildSolrDoc();
		PostSolrDoc postSolr = new PostSolrDoc();
		ClassNameAndKeyReader cNameReader = new ClassNameAndKeyReader(); //load ClassNameMapping.xml only once	
		ArrayList<String> classNames;
		ArrayList<KeyValuePair> keyAndTypeList;
		//Start iterating through file paths provided
		for (int i = 0; i < size; i++ )
		{
			filePath = args[i];			
			
			double matchPercent = -1.0; //to store matchPercent of a model
			Model finalModel = null;		
			
			//Get a list of model class names that match this file			 
			classNames = cNameReader.getClassNames(filePath);
			 if (classNames != null)
			 {
				 for(int j = 0 ; j < classNames.size();j++ ) //iterate through all possible model class names
				 {
					 String modelClassName = classNames.get(j);
					 CreateModel cm = new CreateModel();
					 Model m = (Model) cm.getModel(modelClassName, filePath);					 
					 if (m == null)
					 {
						 System.err.println("Main: Model class not found");	
						 continue; //get next model class name
					 }
					 //System.out.println("class name: "+ m.getClass().getName());
					 keyAndTypeList = cNameReader.getKeysAndTypes(m.getClass().getName());					
				     double temp = m.analyzeFile(keyAndTypeList);
				     //Keep track  of best match model				 
				     if (temp > matchPercent)
				     {
				    	 matchPercent = temp;
				    	 finalModel = m;					
				     }
			      }//end inner for
			  }//end if className list not null 
			 
			 //Create DefaultModel for the file
			 DefaultModel dm = new DefaultModel(filePath);
			 keyAndTypeList = cNameReader.getKeysAndTypes(dm.getClass().getName());
			 double checkFileExists = dm.analyzeFile(keyAndTypeList); //make sure file still exists
			 if (checkFileExists < 0.)
			 {
				 continue; //skip indexing this file and go to next file
			 }
			 
			 String xmlString = "";
			 if (matchPercent < 0.)//use default model only
			 {
				xmlString = buildSolr.getSolrString(dm);
				//System.out.println("default model "+ xmlString);
			 }
			 else//use default model and finalModel key values 
			 {
				 xmlString = buildSolr.getSolrString(dm, finalModel);
				 //System.out.println(" 2 model else: "+ xmlString); 				 
			 }			 
			 if (xmlString == null) continue;
			 postSolr.postToSolr(xmlString);
			 		
		}//end for looping through string args[]		
		
		System.out.println("End Program");
		return;
	}	
} //end class
