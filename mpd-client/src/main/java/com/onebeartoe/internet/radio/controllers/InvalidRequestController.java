
package com.onebeartoe.internet.radio.controllers;

/**
 *
 * @author rmarquez
 */
public class InvalidRequestController  extends SocketController 
{
    
    @Override
    public void preProcess() throws Exception 
    {

    }
    
    @Override
    public void process() throws Exception
    {
	String message = invalidRequest(request);
	sendHttpResponse(message, true);
//	client.close();
//	System.out.println("closing Socket for: " + request);
    }    
    
    protected String invalidRequest(String request) 
    {
	StringBuilder out = new StringBuilder();
	out.append(new StringBuilder().append("Request Recieved Busta: ")
		       .append
		       (request).toString());
	out.append(new StringBuilder().append("<br /><br />").append
		       (this.getClass().getSimpleName()).append
		       (" accepts requests like \"GET filename.html\"<br />")
		       .toString());
	out.append("<br /><br />or something like \"filename.zip\"<br />");
	return out.toString();
    }
}
