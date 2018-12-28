
package com.onebeartoe.internet.radio.controllers;

import java.io.InputStream;
import org.onebeartoe.io.TextFileReader;

/**
 * @author rmarquez
 */
public class StaticFilesController extends SocketController 
{
    
    public StaticFilesController()
    {
	mimeType = "text/css";
    }
    
    @Override
    public void preProcess() throws Exception 
    {

    }
    
    @Override
    public void process() throws Exception
    {
	String [] strs = request.split("/");
	String filename = strs[1];
	InputStream instream = getClass().getResourceAsStream(path + filename);
	String html = TextFileReader.readText(instream);
	boolean includeHeader = false;
	sendHttpResponse(html, includeHeader);	
    }        
}
