
package com.onebeartoe.internet.radio.controllers;

import com.onebeartoe.internet.radio.network.InternetRadioServer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class SocketController implements Runnable
{
    protected Socket client;
    
    protected final String path = "/com/onebeartoe/internet/radio/ui/";
    
    protected String request;
    
    protected String mimeType = "text/html";
    
    public abstract void preProcess() throws Exception;
    
    public abstract void process() throws Exception;
    
    protected InternetRadioServer app;
    
    @Override
    public void run() 
    {	
	try 
	{
	    preProcess();
	    process();	
	}
	catch (Exception e) 
	{
	    e.printStackTrace();
	}
        
	try 
	{
	    client.close();
	}
	catch (Exception e) 
	{
	    e.printStackTrace();
	}
    }
    
    public void setApp(InternetRadioServer app) 
    {
	this.app = app;
    }
	    
    public void setClient(Socket client) 
    {
	this.client = client;
    }
        
    protected void sendHttpResponse(String html, boolean includeHeader) throws Exception 
    {
	String heaersMessage = "HTTP/1.1 200 OK\r\nContent-Type: " + mimeType + "\r\n";
	StringBuilder sb = new StringBuilder();
	sb.append(heaersMessage);
	sb.append("\r\n");
	heaersMessage = sb.toString();
	OutputStream outs = client.getOutputStream();
	OutputStreamWriter outstreamWriter = new OutputStreamWriter(outs, "UTF-8");
	PrintWriter out = new PrintWriter(outstreamWriter, true);
	if (includeHeader)
	{
	    out.print(heaersMessage);
	}
	out.println(html);
	out.flush();
	out.close();	
    }
    
    public void setRequest(String request)
    {
	this.request = request;
    }
    
}