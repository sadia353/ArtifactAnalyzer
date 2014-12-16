package model;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import util.FileUtil;
import util.KeyValuePair;

public class DefaultModel extends BaseModel {

	/*private enum DefaultKeys  {ID, AUTHOR, FILE_NAME_S, SIZE_D, MACHINE_NAME_T, DATE_CREATED_S, 
							  DATE_MODIFIED_S, CONTENT_TYPE,DOCUMENT_TYPE_S, FILE_PATH_S};	
	*/
		
	ArrayList<KeyValuePair> keyAndTypeList;
	public DefaultModel (String filePath)
	{
		super(filePath);
	}
	
	public ArrayList<KeyValuePair> getAll()
	{		
		return keyValues;
	}
	/**
	 * Sets basic file attributes applicable to all file types
	 * @Returns returns a 100.0 if file exists, -1 if file is not a file or 
	 * does not exist or if KeyTypeList is null or its size is 0
	 */
	public double analyzeFile(ArrayList<KeyValuePair> kTypeList)
	{
		if (kTypeList == null || kTypeList.size() == 0)
			return -1;
		keyAndTypeList = kTypeList;
		boolean check = setAll();
		if(check)
			return 100.0;
		else
			return -1.0;
	}
	/**
	 * Attempts to set values for all the keys provided in enum DefaultKeys
	 * @return false if file does not exist or is not a file, true otherwise
	 */
	private boolean setAll()
	{
		File f = new File(filePath);
		if (!(f.isFile())) //file doesn't exist
		{
			System.err.println("DefaultModel: File does not exist or is not a file ");
			return false;
		}
		KeyValuePair keyTypePair;
		for (int i = 0; i < keyAndTypeList.size(); i++)
		{
			keyTypePair = keyAndTypeList.get(i);
			String key = keyTypePair.getKey();
			super.addPair(getPair(key, keyTypePair.getValue(key)));			
		}
		return true;
	}
	/**
	 * Extracts value for key that is passed as a parameter
	 * 
	 * @param defKey is key for which to extract value; keys are specified in the 
	 * enum DefaultKeys
	 * @return KeyValuePair containing the key and value found from file attributes, if unable to find value returns
	 * key and the value as blank (Example key= author value="")
	 */
	private KeyValuePair getPair(String defKey, String type)
	{
		String key= "";
		String value = "";
		FileUtil util = new FileUtil(filePath);			
		Path file = Paths.get(filePath);
		//boolean noError = true;
		try
		{
        BasicFileAttributes attr =
                Files.readAttributes(file, BasicFileAttributes.class);        
        				
		switch(defKey)
		{
		case "id":
			value = util.getHostNameAndIp()+"?"+util.getParentDir()+"?"+util.getFileName();
			break;
		case "author":			
			FileOwnerAttributeView ownerAttributeView = Files.getFileAttributeView(Paths.get(filePath), FileOwnerAttributeView.class);
	        UserPrincipal owner = ownerAttributeView.getOwner();
	        value = owner.getName();
			break;
		case "file_name":			
			value = util.getFileName();
			break;
		case "size_kb":			
			long temp = Math.round(attr.size()/1024.0);			
			value = Long.toString(temp);
			break;
		case "machine_name":			
			value = util.getHostNameAndIp();
			break;
		case "date_created":			
			Date dateCr = new Date((attr.creationTime()).toMillis());
			//Date formatted according to TrieDateField format in Solr schema: YYYY-MM-DDThh:mm:ssZ
			SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			value = sDate.format(dateCr);
			
			break;
		case "date_modified":			
			Date dateUp = new Date((attr.lastModifiedTime()).toMillis());
			SimpleDateFormat sDate2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			value = sDate2.format(dateUp);
			break;
		case "content_type":			
			value = util.getFileType();
			break;
		case "document_type":			
			value = util.getFileType();
			break;
		case "file_path":			
			value = filePath;
			break;
		}//end switch
		}//end try
		catch(Exception e)
		{			
			System.err.println("DefalutModel: Error extracting value "+ e.getMessage());
		}	
		if (type.equals(""))
			key = defKey;
		else
			key= defKey +"_"+type;
		KeyValuePair kv = new KeyValuePair(key, value);
		return kv;
	}	
}
