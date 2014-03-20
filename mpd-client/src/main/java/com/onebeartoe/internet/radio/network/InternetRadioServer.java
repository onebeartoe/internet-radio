
package com.onebeartoe.internet.radio.network;

import com.onebeartoe.internet.radio.PlaylistSources;
import com.onebeartoe.internet.radio.RadioModes;
import static com.onebeartoe.internet.radio.RadioModes.FM;
import com.onebeartoe.internet.radio.Station;
import com.onebeartoe.internet.radio.controllers.CurrentRadioBandController;
import com.onebeartoe.internet.radio.controllers.InvalidRequestController;
import com.onebeartoe.internet.radio.controllers.RadioBandsController;
import com.onebeartoe.internet.radio.controllers.RadioModeController;
import com.onebeartoe.internet.radio.controllers.StaticFilesController;
import com.onebeartoe.internet.radio.controllers.VolumeController;
import com.onebeartoe.internet.radio.controllers.SocketController;
import com.onebeartoe.internet.radio.services.RadioStationService;
import com.onebeartoe.internet.radio.services.RtlSdrAntennaRadioService;
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
  
    private PlaylistSources playlistSource;
//    private boolean defaultRadioBand;    
    
    private List<Station> currentPlaylist;
    
    private int currentStation;
    
    private RadioModes currentRadioMode;
    
//    private RadioBandService radioBandService;
    
    private RadioStationService internetRadioService;
       
    private RadioStationService antennaRadioService;
    
    protected RadioStationService radioService;
    
    public InternetRadioServer() throws Exception
    {
	controllers = new HashMap();
//	controllers.put("currentFmRadioBand", CurrentFmRadioBandController.class);        
	controllers.put("currentRadioBand", CurrentRadioBandController.class);
	controllers.put("radioBandsController", RadioBandsController.class);
	controllers.put("radioMode", RadioModeController.class);
        controllers.put("staticFiles", StaticFilesController.class);
	controllers.put("volume", VolumeController.class);
// what about a URL mapping for "/"?

        internetRadioService = new UbuntuRadioStationService();
        antennaRadioService = new RtlSdrAntennaRadioService();
        
	playlistSource = PlaylistSources.DEFAULT;
        currentRadioMode = RadioModes.INTERNET;

        radioService = internetRadioService;
        
//	radioBandService = new RadioBandService();
	currentPlaylist = radioService.retreiveDefault();
//	currentPlaylist = radioBandService.retreiveDefault();
        
	currentStation = 0;
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
                }
                connection.setRequest(request);
                connection.setApplicationContext(this);
            }            
        }		
	
	return connection;
    }        

    public List<Station> getCurrentPlaylist() throws Exception
    {
//        List<Station> stations = null;
//        
//        switch (playlistSource)
//        {
//            case PERSONAL:
//            {
//                stations = radioService.retreivePersonal();
//                
//                break;
//            }
//            default:
//            {
//                stations = radioService.retreiveDefault();
//            }
//        };
  
        return currentPlaylist;
//	return stations;
    }

    public RadioModes getCurrentRadioMode() 
    {
        return currentRadioMode;
    }

    public int getCurrentStation() 
    {
	return currentStation;
    }    

    public PlaylistSources getPlaylistSource() 
    {
        return playlistSource;
    }    
    
    public boolean playStation(String url) throws Exception
    {
	boolean successful = internetRadioService.playStation(url);
        
        return successful;        
    }
    
    private RadioStationService radioService()
    {
        RadioStationService service = null;
        
        switch(currentRadioMode)
        {
            case FM:
            {
                service = antennaRadioService;
                break;
            }
            default:
            {
                service = internetRadioService;
            }                
        };
        
        return service;
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
                nextServerConnection.setApplicationContext(this);
		nextServerConnection.setClient(client);
                
                RadioStationService radioService = radioService();                
                nextServerConnection.setRadioService(radioService);
                
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
    
    private void stopPlayback() throws Exception
    {
        switch(currentRadioMode)
        {
            case FM:
            {
                try
                {
                    antennaRadioService.stopPlayback();
                }
                catch(Exception e)
                {
                    String message = "could not stop the Internet radio: " + e.getMessage();
                    System.err.println(message);
                }
                
                break;
            }
            default:
            {
                try
                {
                    internetRadioService.stopPlayback();
                }
                catch(Exception e)
                {
                    String message = "could not stop the antenna radio: " + e.getMessage();
                    System.err.println(message);
                }                
            }
        };        
    }
    
    private void switchPlaylist() throws Exception
    {
        switch(playlistSource)
        {
            case PERSONAL:
            {
                currentPlaylist = radioService.retreivePersonal();
                
                break;
            }
            default:
            {
                currentPlaylist = radioService.retreiveDefault();
            }
        };
    }
    
    private void switchRadioService(RadioModes radioMode)
    {
        switch(radioMode)
        {
            case FM:
            {
                radioService = antennaRadioService;
                
                break;
            }
            default:
            {
                radioService = internetRadioService;
            }
        };        
    }
    

    
    public void setCurrentRadioMode(RadioModes radioMode) throws Exception
    {
        stopPlayback();
        switchRadioService(radioMode);
        switchPlaylist();
                
        this.currentRadioMode = radioMode;
    }
    
    public void setCurrentStation(int currentStation) 
    {
	this.currentStation = currentStation;
    }
    
    public void setPort(int port) 
    {
	this.port = port;
    }    
    
    public void setPlaylistSource(PlaylistSources playlistSource) {
        this.playlistSource = playlistSource;
    }
    

    
    public void setCurrentPlaylist(List<Station> currentPlaylist) 
    {
	this.currentPlaylist = currentPlaylist;
    }

//    public boolean isDefaultRadioBand() 
//    {
//	return defaultRadioBand;
//    }
//
//    public void setDefaultRadioBand(boolean defaultRadioBand) 
//    {
//	this.defaultRadioBand = defaultRadioBand;
//    }
    
}
