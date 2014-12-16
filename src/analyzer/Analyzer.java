package analyzer;

import java.util.ArrayList;

import util.KeyValuePair;

public interface Analyzer 
{
public ArrayList<KeyValuePair> extractFile(ArrayList<KeyValuePair> keyAndTypeList);
public  double validate();//returns a percentage of the keys and values found
}
