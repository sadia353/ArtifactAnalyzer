package buildSolr;
import model.DefaultModel;
import model.Model;

public interface BuildSolr 
{
	public String getSolrString(Model m);
	public String getSolrString(DefaultModel dm, Model m);
}
