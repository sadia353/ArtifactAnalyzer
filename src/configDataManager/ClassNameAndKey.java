package configDataManager;

import java.util.ArrayList;

import util.KeyValuePair;


public interface ClassNameAndKey
{
	public ArrayList<String> getClassNames(String filePath);
	public ArrayList<KeyValuePair> getKeysAndTypes(String className);
}
