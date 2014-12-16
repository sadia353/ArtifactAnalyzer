// Some code in the method postToSolr has been obtained from SimplePostTool.java from 
// Apache Solr 
package postSolr;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class PostSolrDoc implements PostSolr
{
	//POST_HOST and POST_PORT are used in the Solr url to post data
	private final String POST_HOST = "localhost";
	private final String POST_PORT = "8080";	
	
  public PostSolrDoc(){}

  /**
   * Posts the Solr xml string into the Solr index
   * The xml string has to be formatted according to Solr requirements
   * in order to be successfully posted
   * @param: solrXmlString is the xml string containing the appropriate tags
   * @return: returns true if the post was successful,
   * 		  false if there was an error or exception
   */
 public boolean postToSolr(String solrXmlString)
 {		
  if (solrXmlString == null) return false;
		
  boolean success = true;  
  String urlStr = "http://"+POST_HOST+":"+POST_PORT+"/solr/update?commit=true"; //url to post to Solr
  String type = "text/xml";
  URL url = null;
  try {
	    url = new URL(urlStr);
		try {
			 HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			 connection.setDoOutput(true); 
			 connection.setDoInput(true);
			 try{
				 connection.setRequestMethod("POST");
				 connection.setUseCaches(false);
				 connection.setAllowUserInteraction(false);
				 connection.setRequestProperty("Content-type", type);
				 connection.setRequestProperty("charset", "utf-8");
				 connection.setRequestProperty("Content-Length", "" + Integer.toString(solrXmlString.getBytes().length));
				 try{
					 connection.connect();
					}
					catch (IOException e)
					{
						System.err.println("PostSolrDoc: cannot connect to Solr via connect() "+e.getMessage());
						return false;
					}
				//Get ready to write the xmlString to Solr
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
				//Write out the bytes to Solr
				wr.writeBytes(solrXmlString);
				if (connection.getResponseCode() >= 400) {
				      System.err.println("Solr returned an error #" + connection.getResponseCode() + 
				            " (" + connection.getResponseMessage() + ") for url: " + connection.getURL());
				}
				//Get ready to read the response from Solr
				//Some of the code below obtained from SimplePostTool.java
				String charset = "ISO-8859-1";
				final String contentType = connection.getContentType();
			    if (contentType != null)
			    {
			      int pos = contentType.indexOf("charset=");
			      if (pos != -1)
			      {
			    	 charset = contentType.substring(pos + "charset=".length());
			      }
			    }
			   // Create reader and read Solr's response as string data
			   // status of 0 means no error
			      BufferedReader r = new BufferedReader(
			              new InputStreamReader(connection.getInputStream(), charset));
			      String content = "";
			      String line;
			      while ((line = r.readLine()) != null) 
			      {
			          content += line + "\n";
			      }
				System.out.println("\n"+content);
				wr.flush();
				wr.close();
				if (connection!=null) connection.disconnect();
			}
			catch(ProtocolException e)
			{
				System.err.println("PostSolrDoc: POST not supported "+e.getMessage());
				return false;
			}
				
			} 
		    catch (IOException e) 
			{
				System.err.println("PostSolrDoc: Error connecting to Solr url "+ e.getMessage());
				return false;				
			}			
		}//end try creating url
		catch (MalformedURLException e)
        {
		  System.err.println("PostSolrDoc: Malformed URL, please re-check "+ e.getMessage());		
		  return false;			
		}
		
		return success;
	}
}//end class


