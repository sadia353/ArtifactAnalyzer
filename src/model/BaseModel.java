package model;

import java.util.ArrayList;

import util.KeyValuePair;

/**
 * Implements the Model interface 
 * NOTE: Does not represent a specific model, so all model classes that extend BaseModel should OVERWRITE
 * analyzeFile() method by providing a specific analyzer to ensure correct behavior
 */
public class BaseModel implements Model
{
	protected ArrayList<KeyValuePair> keyValues;
	protected String filePath;
	
	public BaseModel(String path)
	{
		keyValues = new ArrayList<KeyValuePair>();
		filePath = path;		
	}	
	/*public Model clone()
	{
		Model bm = new BaseModel(this.filePath);
		for (int i = 0; i < this.getSize();i++)
		{
			String key = this.getKey(i);
			String val = this.getValue(i, key);
			KeyValuePair kv = new KeyValuePair(key, val);
			bm.addPair(kv);
		}
		return bm;		
	}*/
	/**
	 * @param index for the key
	 * @return The key at the given indexx
	 * null if list is empty 
	 */
	public String getKey(int index) 
	{
		if (getSize() == 0)
			return null;
		else
			return keyValues.get(index).getKey();		
	}
  /**
   * @param index for the key
   * @param key to which value is mapped
   * @return The value at the given index and given key
   * null if list is empty or key does not match
   */
  public String getValue(int index, String key)
  {
	if (getSize() == 0)
		return null;
	else
		return keyValues.get(index).getValue(key);			
  }
  /**
   * @return size of the list 
   */
  public int getSize()
  {
	return keyValues.size();
  }

  public void addPair(KeyValuePair pair) 
  {
		keyValues.add(pair);
  }
  /**
   * @returns Always returns -1.0 
   * NOTE: All model classes that extend BaseModel
   * must overwrite the analyzeFile(ArrayList<KeyValuePair>) method to ensure correct behavior
   */
   public double analyzeFile(ArrayList<KeyValuePair> keyTypeList)
   {
		return -1.0;
   }
}
