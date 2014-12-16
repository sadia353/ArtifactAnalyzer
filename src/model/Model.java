package model;

import java.util.ArrayList;

import util.KeyValuePair;


public interface Model
{
	public String getKey(int index);
	public String getValue(int index, String key);
	public int getSize();
	public void addPair(KeyValuePair pair);
	/**
	  * Analyzes the input file by first attempting to extract model-specific data from the file and
	  * then trying to validate it by getting the match percent of the file to the model
	  * Extraction and validation are carried out in the model's specific analyzer
	  * @param KeyAndTypeList is a list of KeyValuePair containing keys whose values need to extracted and 
	  * their type like t for text or s for string, null can be used when keys are provided in the model
	  * @return percent match as a double value, -1.0 if there was an error or exception
	  */
	public double analyzeFile(ArrayList<KeyValuePair> keyAndTypeList);	
}
