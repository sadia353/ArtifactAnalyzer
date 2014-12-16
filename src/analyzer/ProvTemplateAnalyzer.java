package analyzer;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import util.KeyValuePair;

public class ProvTemplateAnalyzer implements Analyzer
{
	private String filePath;
	private double keysFound = 0.;	
	private ArrayList<KeyValuePair> keyAndTypeList; 
	public ProvTemplateAnalyzer(String path)
	{
		this.filePath = path;
	}	
	/**
	 * Extracts keys and values from an excel file
	 * File could be an xls or xlsx
	 * Keys to be extracted have been provided through the parameter
	 * @return: An ArrayList of KeyValuePairs extracted from the excel file
	 * 			null if there was an exception, list is null or size=0
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
	 * @return  An ArrayList of KeyValuePairs extracted from the excel file
	 * 			null if there was an exception
	 */
	private ArrayList<KeyValuePair> extractFile() 
	{
		Workbook wb = null;
		ArrayList<KeyValuePair> pairList;
		KeyValuePair kv ;
		int rowNum = -1;
		int keyRowNum = -1;
		
		try
		{
			wb = WorkbookFactory.create(new FileInputStream(filePath));
			Sheet sheet = wb.getSheetAt(0);
			KeyValuePair tempKeyTypePair = null;
			pairList= new ArrayList<KeyValuePair>();
			for (Row row : sheet)
			{
				//for every row create a new key-value pair
				String key = "";
				String value = "";
				String type = "";  //storing key type
				rowNum = row.getRowNum();
				boolean foundKey = false;
				cellSearch:
					for (Cell cell : row)
					{
					 switch(cell.getCellType())
					 {
						case Cell.CELL_TYPE_STRING:
							if (key.equals(""))
							{
								String temp = cell.getStringCellValue().trim();
								for (int i = 0; i < keyAndTypeList.size(); i++)
								{
									tempKeyTypePair = keyAndTypeList.get(i);
									key = tempKeyTypePair.getKey();
									String keyWithSpace = key.replaceAll("_", " "); //replace all underscores with " "
									if (keyWithSpace.equalsIgnoreCase(temp))
									{
										foundKey = true;
										keysFound += 1.;
										type = tempKeyTypePair.getValue(key);
										keyRowNum = rowNum;
										break;
									}
								}//end for keyAndTypeList iteration
								if (!foundKey)
									break cellSearch;								
							}//end if key ""
							else //we have the key and need the value
							{								
								value = cell.getStringCellValue();
							}
							break;
						case Cell.CELL_TYPE_NUMERIC:
							if (key.equals("")) 
							{
								return null; //key can't be numeric
							}
							else if (key.equalsIgnoreCase("Date"))
							{
								Date date = cell.getDateCellValue();
								if (!date.equals(null))
								{
									//Date formatted according to TrieDateField format in Solr schema: YYYY-MM-DDThh:mm:ssZ
									SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
									value = sDate.format(date);
								}
							}
							else
							{
								if (keyRowNum == rowNum)//make sure key value is from same row
								{									
								 value = String.valueOf(cell.getNumericCellValue());
								}
							}
							break;
						case Cell.CELL_TYPE_BLANK:
							if (key.equals(""))
								break cellSearch; //key can't be blank							
							break;
						case Cell.CELL_TYPE_FORMULA:
							if (key.equals(""))
								return null; //key can't be numeric, data corrupted so return
							else
							{
								if (keyRowNum == rowNum)//make sure key value is from same row
								{									
									value = String.valueOf(cell.getNumericCellValue());
								}
							}
							break;
						
					}//end switch
					 
				}//end for cell iterations per row
				if (foundKey)
				{
				  if (!type.equals(""))
					  key += "_" + type; // append the type	if type != ""			  
				  kv = new KeyValuePair(key, value);
				  pairList.add(kv);	
				}
			}//end for iterating through rows
		}
		catch (Exception e)
		{
			System.err.println("Error in ProvTemplateAnalyzer "+ e.getMessage());
			return null;
		}
		//System.out.println("In analyzer- Size of list:  "+ pairList.size());
		return pairList;
	}	

	/**
	 * Calculates the percentage of how many keys were expected and 
	 * how many were obtained from excel extraction.
	 * @return: (keysFound/totalKeys)* 100, rounded to 2 decimal places
	 * 		    0 if no keys found or if no keys specified for totalKeys
	 */
	public double validate() 
	{
		
		double size = (double)keyAndTypeList.size();
		double temp;
		//System.out.println("Num of keys: "+ size);
		if (size != 0)
		{
			temp = (keysFound/size)*100.;  // 100*(approx. value/exact value)
			return (Math.round(temp*100)/100.0); //round match percent to 2 decimal places
		}
		return 0.;
	}
}
