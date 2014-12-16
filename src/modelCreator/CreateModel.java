package modelCreator;

import java.lang.reflect.Constructor;

public class CreateModel implements ModelCreator
{

	/**
	 * Uses Java Reflection to create objects that implement the Model interface
	 * from the fully qualified class names of model classes
	 * 
	 * @param modelName should be the fully qualified class name like package.classname
	 * @param filePath is the absolute filePath to the file that has to be analyzed
	 * @return Object implementing Model interface, null if it cannot locate the class associated with the class name 
	 * or if there is an exception
	 */	
	public Object getModel(String modelName, String filePath) 
	{
		Object obj = null;
		try
		{
		  Class<?> objClass = Class.forName(modelName); //<?> class type is unknown
		  Constructor<?> constructor = objClass.getConstructor(new Class[]{String.class});
		  //create instance of model  object
		  obj = constructor.newInstance(filePath);		
		}
		catch(Exception e)
		{System.err.println(" Error in CreateModel "+ e.getMessage());
		 return null;
		}
		return obj;
	}
}
