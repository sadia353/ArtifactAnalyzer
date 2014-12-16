package analyzer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import util.KeyValuePair;

public class ShotNumberPngAnalyzer implements Analyzer
{
  private String filePath; //filePath of the file to be extracted
  private double keysFound = 0.0; //keys found in file  
  private ArrayList<KeyValuePair> keyAndTypeList;  
		
  public ShotNumberPngAnalyzer(String path)
  {
	    	this.filePath = path;
  } 
  /**
	 * Extracts keys and values from a PNG file
	 * Keys to be extracted are provided through the parameter
	 * @param kTypeList is a list of KeyValuePair containing keys and their types
	 * @return: An ArrayList of KeyValuePairs extracted from the PNG file
	 * 			null if there was an exception, or list of keys is null or 0
	 */
 public ArrayList<KeyValuePair> extractFile(ArrayList<KeyValuePair> kTypeList)
 {
	  if ((kTypeList == null) ||(kTypeList.size() == 0))
		  return null;
	  keyAndTypeList = kTypeList;
	  return extractFile();
 }
 /**
	 * Used for actual extraction by extractFile(ArrayList<KeyValuePair> kTypeList)
	 * @return  An ArrayList of KeyValuePairs extracted from the PNG file
	 * 			null if there was an exception
	 */
 private ArrayList<KeyValuePair> extractFile() 
 {
	  BufferedImage buffImage;
	  KeyValuePair kv;
	  ArrayList<KeyValuePair> pairList;
	  KeyValuePair keyTypePair;
	  try
	  {
		File file;
		if ((file = new File(filePath)).isFile())
		{
			buffImage = ImageIO.read(file);
			pairList = new ArrayList<KeyValuePair>();
			for (int i = 0; i < keyAndTypeList.size();i++  )
			{
				keyTypePair = keyAndTypeList.get(i);
				String tempKey = keyTypePair.getKey();
				String type = keyTypePair.getValue(tempKey);
				String key = "";
				String value = "";
			 switch(tempKey)
			 {
				case "image_height":
					key = tempKey;
					value = Integer.toString(buffImage.getHeight()); //in pixels
					keysFound++;
					break;
				case "image_width":
					key = tempKey;
					value = Integer.toString(buffImage.getWidth()); //in pixels
					keysFound++;
					break;				
			 }//end switch
			 if (!type.equals(""))
					key += "_"+ type;   // append the type if type != ""	
				kv = new KeyValuePair(key, value);
				pairList.add(kv);
		}//end for
	 }//end if isFile is true
	else
	{
		System.err.println("PngAnalyzer: File does not exist or is not a file");
		return null;
	}
	}//end try
	catch(Exception e)
	{
		System.err.println("PngAnalyzer: Error reading image file "+ e.getMessage());
		return null;
	}	
	return pairList;
}

 /**
  * Calculates the percentage of how many keys were expected and 
  * how many were obtained from image extraction.
  * @return: (keysFound/totalKeys)* 100, rounded to 2 decimal places
  * 		 0 if no keys found or if no keys specified for totalKeys
  */
public double validate()
{
	double totalKeys = (double)keyAndTypeList.size();
	if (totalKeys == 0.0)
		return 0.;
	else
	{
	  double temp = (keysFound/totalKeys)*100.;
	  return (Math.round(temp*100)/100.0); //round match percent to 2 decimal places
	}
 }
}


