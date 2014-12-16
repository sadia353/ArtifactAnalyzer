package model;

import java.util.ArrayList;

import util.KeyValuePair;
import analyzer.ProvTemplateAnalyzer;


public class ProvTemplateExcel extends BaseModel
{
 public ProvTemplateExcel(String filePath)
 {
	 super(filePath);
 }
 /**
  * Analyzes the input file by first attempting to extract model-specific data from the file and
  * then trying to validate it by getting the match percent of the file to the model
  * Extraction and validation are carried out in the model's specific analyzer
  * 
  * @return percent match as a double value, -1.0 if there was an error or exception
  */
 public double analyzeFile(ArrayList<KeyValuePair> keyAndTypeList)
 {
	 ProvTemplateAnalyzer templAnalyze = new ProvTemplateAnalyzer(super.filePath);
	 super.keyValues = templAnalyze.extractFile(keyAndTypeList); 
	 if ( super.keyValues == null)
		 return -1.0; //error extracting from file, return -1
	 else	 
		 return templAnalyze.validate();  //return the calculated match percentage
 }
}
