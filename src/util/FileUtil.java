package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class FileUtil 
{
	private String path; //file path to the file to be analyzed
	private String parentDir;
	/**
	 *  file extension
	 *  To match with config file, extension needs to match the file_ext in ClassNameMapping.xml, 
	 *  See the method getMappedFileType to make sure file extensions are mapped right
	 */
	private String fileExt = ""; 
	private String fileName; //contains extension
	public static final String ipServiceURL = "http://checkip.amazonaws.com/"; //url for web service to obtain external ip
	
	public FileUtil(String p)
	{
		path = p;
		setAttributes();
	}
	/**
	 * Sets attributes like fileName, fileExt, and parentDir 
	 * if filename has space there is an error
	 */
	private void setAttributes()
	{
		File f = new File(path);
	   parentDir = f.getParentFile().getName();
	   fileName = f.getName() ;
	   
	   int index = fileName.lastIndexOf('.') ;
	   
	   if (index == -1)
		   return;
	   fileExt = fileName.substring(index+1).toLowerCase();	//exclude '.' from file extension
	}
	/**
	 * @return file extension of the file, as obtained from the file name
	 */
	public String getFileType()
	{
		return fileExt;
	}
	public String getParentDir()
	{
		return parentDir;
	}
	/**
	 * Returns the file type used in ClassNameMapping.xml for file extensions
	 * @return .xls or .xlsx will return excel, No file extension will return unknown, 
	 * jpeg or jpg will return jpeg etc
	 * 
	 */
	 public String getMappedFileType()
	 {
		   if (fileExt.equals("")) 
			   return "unknown";
		   if (fileExt.contains("xls"))
			   return "excel";
		   else if (fileExt.contains("txt"))
			   return "text";
		   else if (fileExt.contains("jpg")||fileExt.contains("jpeg"))
			   return "jpeg";
		   else
			   return fileExt;
	  }
	 /**
	  * @return Returns the file name of the file
	  */
	public String getFileName()
	{
		return fileName;
	}
	
	/**
	 * Uses a web service to determine the external IP of the host machine. If
     * this is not available, the loop back address is used along with the host
     * name of the local machine. If the local host name cannot be ascertained
     * then a default assignment is made.
     *
     * @return A description of the host name along with the most uniquely
     * describing IP available (may or may not come from the InetAddress)
     */
	public String getHostNameAndIp()
	{
		 String localNameSpace = "";

	        try {
	            URL locator = new URL(ipServiceURL);
	            URLConnection connection = locator.openConnection();
	            InputStream is = connection.getInputStream();
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader reader = new BufferedReader(isr);
	            String str = null;
	            str = reader.readLine();
	            if (null == str) {
	                str = "127.0.0.1";
	            }
	            localNameSpace = InetAddress.getLocalHost().getHostName()
	                    + "@" + str;
	        } catch (IOException e) {
	            try {
	                localNameSpace = InetAddress.getLocalHost().getHostName()
	                        + "@" + InetAddress.getLoopbackAddress();
	            } catch (UnknownHostException ex) {
	                localNameSpace = "UnknownHostName@127.0.0.1";
	            }
	        }

	        return localNameSpace;
	}
}
