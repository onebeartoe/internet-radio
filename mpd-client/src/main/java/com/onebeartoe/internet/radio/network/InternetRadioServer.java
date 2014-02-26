
package com.onebeartoe.internet.radio.network;

import com.onebeartoe.internet.radio.Station;
import com.onebeartoe.internet.radio.controllers.CurrentFmRadioBandController;
import com.onebeartoe.internet.radio.controllers.CurrentRadioBandController;
import com.onebeartoe.internet.radio.controllers.InvalidRequestController;
import com.onebeartoe.internet.radio.controllers.RadioBandsController;
import com.onebeartoe.internet.radio.controllers.StaticFilesController;
import com.onebeartoe.internet.radio.controllers.VolumeController;
import com.onebeartoe.internet.radio.controllers.SocketController;
import com.onebeartoe.internet.radio.services.InternetRadioStationService;
import com.onebeartoe.internet.radio.services.RadioBandService;
import com.onebeartoe.internet.radio.services.UbuntuRadioStationService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InternetRadioServer extends Thread
{
    private ServerSocket serverSocket;
    
    String serverConnectionClass;
    
    public int port = 1978;
    
    public String ip;
    
    private Map<String, Class> controllers;
    
    private boolean defaultRadioBand;
    
    private List<Station> currentPlaylist;
    
    private int currentStation;
    
    private RadioBandService radioBandService;
    
    private InternetRadioStationService internetRadioService;
    
    public InternetRadioServer() throws Exception
    {
	controllers = new HashMap();
	controllers.put("currentRadioBand", CurrentRadioBandController.class);
	controllers.put("radioBandsController", RadioBandsController.class);
	controllers.put("staticFiles", StaticFilesController.class);
	controllers.put("volume", VolumeController.class);
	controllers.put("currentFmRadioBand", CurrentFmRadioBandController.class);
	
	defaultRadioBand = true;
	
	radioBandService = new RadioBandService();
	currentPlaylist = radioBandService.retreiveDefault();
	currentStation = 0;
	
	internetRadioService = new UbuntuRadioStationService();
    }
    
    private SocketController connectionFor(Socket client) throws Exception
    {
        Pattern pattern = Pattern.compile("(GET|POST) /?(\\S*).*");
        
	InputStream clientInstream = client.getInputStream();
	InputStreamReader instreamReader = new InputStreamReader(clientInstream, "8859_1" );
	BufferedReader in = new BufferedReader(instreamReader);			        
        SocketController connection = null;
        
	String request = in.readLine();

        if(request == null)
        {
            System.out.println("the request is: " + request);
            connection = new InvalidRequestController();
        }
        else
        {
            Matcher httpRequestMatcher = pattern.matcher(request);            
            if ( ! httpRequestMatcher.matches() )			 
            {
                connection = new InvalidRequestController();
            }
            else
            {
                request = httpRequestMatcher.group(2);
                String [] subqueries = request.split("/");
//                Class c = null;
                if(subqueries.length > 0)
                {
                    Class c = controllers.get(subqueries[0]);
                    if(c != null)
                    {
                        Object o = c.newInstance();
                        connection = (SocketController) o;
                    }
                }	    

                if(connection == null)
                {
                    
                    connection = new InvalidRequestController();
//                    connection = new InternaetRadioController();
                }
                connection.setRequest(request);
                connection.setApp(this);
            }            
        }		
	
	return connection;
    }        
	
    public boolean playStation(String url) throws Exception
    {
	boolean successful = internetRadioService.playStation(url);
        
        return successful;        
    }
    
    @Override
    public void run() 
    {
	try 
	{
	    serverSocket = new ServerSocket(port);
	    InetAddress addr = InetAddress.getLocalHost();
	    byte[] ipAddr = addr.getAddress();
	    String ipAddrStr = "";
	    for (int i = 0; i < ipAddr.length; i++) 
	    {
		if (i > 0)
		{
		    ipAddrStr += ".";
		}
		
		ipAddrStr += String.valueOf(ipAddr[i] & 0xff);
	    }
	    ip = ipAddrStr;
	    String hostname = addr.getHostName();
	    System.out.println(ipAddr.toString() + " - " + hostname + " - " + ipAddrStr + " - " + addr.getHostAddress() 
		    + " - " + serverSocket.getInetAddress().getHostAddress() + " - " + serverSocket.getLocalSocketAddress().toString() );
	    
	    while(true)
	    {
		Socket client = serverSocket.accept();				
		SocketController nextServerConnection = connectionFor(client);		
		nextServerConnection.setClient(client);
		Thread request = new Thread(nextServerConnection);
		request.start();
	    }
	} 
	catch (Exception ioe) 
	{
	    ioe.printStackTrace();
	}
    }
    
    public void setCurrentPlaylistAndPlay(List<Station> currentPlaylist) throws Exception
    {
	stopPlayback();
	setCurrentPlaylist(currentPlaylist);	
	if(currentPlaylist.size() > 0)
	{
	    Station station = currentPlaylist.get(0);	
	    internetRadioService.playStation(station.url);		
	}
    }
    
    public void shutDown() 
    {
	try 
	{
	    serverSocket.close();
	    serverSocket = null;
	} 
	catch (IOException ioe) 
	{
	    String errorMessage = ioe.toString();
	    System.out.println(errorMessage);
	}
    }
    
    public void stopPlayback() throws Exception
    {
	internetRadioService.stopPlayback();
    }
    
    public int getCurrentStation() 
    {
	return currentStation;
    }

    public void setCurrentStation(int currentStation) 
    {
	this.currentStation = currentStation;
    }
    
    public void setPort(int port) 
    {
	this.port = port;
    }        
    
    public List<Station> getCurrentPlaylist() 
    {
	return currentPlaylist;
    }

    public void setCurrentPlaylist(List<Station> currentPlaylist) 
    {
	this.currentPlaylist = currentPlaylist;
    }

    public boolean isDefaultRadioBand() 
    {
	return defaultRadioBand;
    }

    public void setDefaultRadioBand(boolean defaultRadioBand) {
	this.defaultRadioBand = defaultRadioBand;
    }
    
}
