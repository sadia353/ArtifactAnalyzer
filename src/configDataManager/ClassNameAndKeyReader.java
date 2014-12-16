package configDataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import util.FileUtil;
import util.KeyValuePair;

public class ClassNameAndKeyReader implements ClassNameAndKey
{
 private String mappingFile = "configFiles/classNameMapping.xml"; //config file that stores model class names 
 private String modelKeyFile = "configFiles/modelKeys.xml"; //config file for getting keys specific to a model 

   
 // Both HashMaps read and load the both config files once every execution of program  
 private HashMap<String, HashMap<String, ArrayList<String>>> classNameTable;
 private HashMap<String, ArrayList<KeyValuePair>> modelKeyTypeTable;
  	
 public ClassNameAndKeyReader()
 {	 
	  fillClassNameTable();
	  fillModelKeyTable();
 }
 
 /**
  * Read and store ClassNameMapping.xml in the HashMap so directory pattern and 
  * file extensions can be mapped to model class names
  * If unable to read config. file then table is null
  */
 private void fillClassNameTable()
 {
	 XmlParser x = new XmlParser(mappingFile); //parse this xml file to get model mapping
	 classNameTable = x.getClassNameTable();
 }
 /**
  * Read and store modelKeys.xml in the HashMap with model class names as keys and 
  * their keys and key-types as KeyValuePair values
  * If unable to read config. file then table is null
  */
 private void fillModelKeyTable()
 {
	 XmlParser x = new XmlParser(modelKeyFile);
	 modelKeyTypeTable = x.getModelKeyTypeTable();
 }
 
 /**
  * Extracts parent directory and file extension from file path so
  * they can be used to obtain list of model class names that match the file
  *  
  * @param filePath is the absolute file path to the file whose possible models need to be retrieved
  * @return Returns ArrayList<String> of classNames that match the directory pattern and file ext
  * null if error retrieving table or if no match for directory pattern or file ext 
  */
 public ArrayList<String> getClassNames(String filePath)
 {
	 if (classNameTable == null)
		 return null;	 
	 FileUtil util = new FileUtil(filePath);	 
	 String dirName = util.getParentDir();
	 String fExt = util.getMappedFileType();  
	 HashMap<String,ArrayList<String>> fTypeCNameMap;
	 //Get a set of dirPattern keys
	 Set<String> keySet = classNameTable.keySet();
	 Iterator<String> i = keySet.iterator();
	 //System.out.println("ClassReader : File Dir: "+ dirName+" full file Type: "+ fExt);
	 while(i.hasNext())
	 {
		 String dirPattern = i.next();
		 if(dirName.contains(dirPattern))
		 {
			 //get the HashMap the dirPattern is mapped to
			 fTypeCNameMap = classNameTable.get(dirPattern);
			 //Get a set of fileType keys 
			 Set<String> fTypeSet = fTypeCNameMap.keySet();
			 Iterator<String> j = fTypeSet.iterator();
			 while(j.hasNext())
			 {
				String fType = j.next();
				 //If fExt matches the fType from map, then return the list it is mapped to
				 if (fExt.equalsIgnoreCase(fType))
					 return fTypeCNameMap.get(fType);					 
			 }//end inner while
		 }
	 }//end outer while
	 //if directory and fileType are not mapped return null
	 return null;
 }
 /**
  * If the model's className is found in the table it returns a list of keys and key-types for
  * that model 
  * 
  * @param className is the model's className whose keys need to be obtained
  * @return ArrayList<KeyValuePair> of key and type that match the model
  * null if error retrieving table or if no match for className
  */
 public ArrayList<KeyValuePair> getKeysAndTypes(String className)
 {
	 if (modelKeyTypeTable == null)
		 return null;
	 //System.out.println("ClassNameR size of keyList "+ modelKeyTypeTable.size());
	 Set<String> keySet = modelKeyTypeTable.keySet();
	 Iterator<String> i = keySet.iterator();
	 //System.out.println("ClassReader : File Dir: "+ dirName+" full file Type: "+ fExt);
	 while(i.hasNext())
	 {
		 String cName = i.next();
		 //System.out.println("ClassReader : classname: "+ cName +" argument "+ className);
		 if(cName.equals(className)) //found list of keys for this className
		 {
			 return modelKeyTypeTable.get(cName);
		 }
	 }
	 return null;
 }
}
