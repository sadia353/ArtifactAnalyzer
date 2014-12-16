package util;

public class KeyValuePair 
{
	private String key;
	private String value;
	
	public KeyValuePair()
	{
		key = "";
		value = "";		
	}
	
	public KeyValuePair(String k, String v)
	{
		key = k;
		value = v;
	}
	
	public String getKey()
	{
		return key;
	}
	
	/**
	 * @param k key for which to get the value
	 * @return value obtained or "" if key does not match
	 */
	public String getValue(String k)
	{
		if (key.equalsIgnoreCase(k))
			return value;
		return "";
	}
	public void setKey(String k)
	{
		key = k;
	}
	/**
	 * Sets value only if the key matches, else value is set to ""
	 * @param k key for the value
	 * @param v value 
	 */
	public void setValue(String k, String v)
	{
		if (k.equals(key))
			value = v;	
		else 
			value = "";			
	}
}
