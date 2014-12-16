package analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import util.KeyValuePair;

public class TextFileKeyValueAnalyzer implements Analyzer
{
  private String filePath;
  private double keysFound = 0.;
  private double totalKeys = 0.0; //equals the number of lines minus 1 for the first line
  public TextFileKeyValueAnalyzer(String path)
  {
	  this.filePath = path;
  }
 /**Analyzes a text file and extracts key value pairs from the file 
  *if the key value pairs are stored in the following expected formats-
  * Expected Formats: Tab-separated, comma separated, space separated
  * 				  colon-separated. Also, each line is expected to contain
  * 				  only one set of key value pairs. The number of keys can vary from file
  * 				  to file. The first line is used for determining the format but its
  * 				   String values are ignored.
  * @returns:ArrayList of KeyValue pairs from the file, null if there is an error or 
  * 		 separator not recognized
  */
  public ArrayList<KeyValuePair> extractFile(ArrayList<KeyValuePair> kTypeList) 
  {
		ArrayList<KeyValuePair> kvList =new ArrayList<KeyValuePair>();
		KeyValuePair kvPair = null;
		Scanner in = null;
		String currLine = null;
		String delimiter = null;
		
		try{
			in = new Scanner(new File(filePath));				
		
		//Analyze first line
		if (in.hasNext())
		{
			currLine = in.nextLine();
			if (currLine.contains(","))
			{
				delimiter = ",";
			}
			else if(currLine.contains(":"))
			{
			delimiter = ":";	
			}
			else if(currLine.contains("\t"))
			{
				delimiter = "\t";
			}
			else
			{
				delimiter = " ";
			}		
		}
		//System.out.println("Delimiter: "+ delimiter);
		while (in.hasNext())
		{
			currLine = in.nextLine();
			String[] splitLine = currLine.split(delimiter);
			//System.out.println("Length of line: "+ splitLine.length+"\n Current line "+currLine);
			
			if (splitLine.length >= 1)
			{
				String key, value;
				if (splitLine.length == 1) //only  key expected
				{
					key = splitLine[0]+"_t";//get key and add _t for dynamic indexing as text
					value = "";
					totalKeys++;
					keysFound =  keysFound + .5; //only half of the total value
				}
				else
				{
					//System.out.println("[0] "+ splitLine[0]+" [1] "+ splitLine[1]);
					key = splitLine[0]+"_t";//get key
					value = splitLine[1].trim(); //get value
					totalKeys++;
					keysFound++;
				}
				kvPair = new KeyValuePair(key,value);
				kvList.add(kvPair);				
			}			
		}//end while
	 }//end try
	 catch(FileNotFoundException e)
	 {
			System.err.println("TextFileKeyValueAnalyzer: File not found");
			return null;
	 }
	 catch (Exception e)
	 {
		 System.err.println("TextFileKeyValueAnalyzer: Error "+ e.getMessage());
         in.close();
         return null;
	 }
		in.close();
		System.out.println("In text analyzer: total keys "+ totalKeys+ " keys found "+ keysFound);
		return kvList;
  }

  /**
   * Calculates the percentage of how many keys were expected and 
   * how many were obtained from image extraction.
   * @return: (keysFound/totalKeys)* 100, rounded to 2 decimal places
   * 			0 if no keys found or if no keys specified for totalKeys
   */
  public double validate()
  {
	  double temp;
		if (totalKeys == 0.0)
			return 0.;
		else
		{
			temp = (keysFound/totalKeys)*100.;
			return (Math.round(temp*100)/100.0); //round match percent to 2 decimal places
		}
  }

}
