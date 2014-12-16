package buildSolr;
import model.DefaultModel;
import model.Model;


public class BuildSolrDoc implements BuildSolr
{	
	/**
	 * Builds a Solr XML string from the Model provided
	 * @param An object that implements the model interface
	 * @return Solr XML string containing the data from the model, null if model is null
	 */
	public String getSolrString(Model m) 
	{
		if (m== null) return null;
		StringBuilder s = new StringBuilder();
		s.append("<add>\n<doc>\n");
		s.append(buildFieldNames(m));
		s.append("</doc>\n</add>\n");
		return s.toString();
	}
	/**
	 * Builds a Solr XML string from two Model objects	 
	 * @return Solr XML string containing the data from both the models, null if models are null
	 */
	public String getSolrString(DefaultModel dm, Model m)
	{
		if ((dm == null) || (m== null)) return null;
		StringBuilder s = new StringBuilder();
		s.append("<add>\n<doc>\n");
		s.append(buildFieldNames(dm));
		s.append(buildFieldNames(m));
		s.append("</doc>\n</add>\n");
		return s.toString();
	}
	/**
	 * extracts keys and values from model
	 * @param m implements model interface
	 * @return Solr XML String 
	 */
	private String buildFieldNames(Model m)
	{
		int size = m.getSize();
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < size; i++)
		{
			String key = m.getKey(i);
			String val = m.getValue(i, key);
			s.append("\t<field name=\""+ key+"\">");
			s.append(val+"</field>"+"\n");			
		}		
		return s.toString();		
	}

}
