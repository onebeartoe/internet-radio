
package com.onebeartoe.internet.radio.controllers;

import com.onebeartoe.internet.radio.RadioModes;
import com.onebeartoe.internet.radio.Station;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rmarquez
 */
public class CurrentRadioBandController extends InternaetRadioController
{    
    public void addStationToPersonal() throws Exception
    {
	Station s = new Station();
	String queryString = null;
	if( request.contains("?") )
	{
	    int i = request.indexOf("?");
	    queryString = request.substring(i+1);
	    String [] pairs = queryString.split("&");
	    for(String str : pairs)
	    {
		String [] nameValue = str.split("=");
		if(nameValue.length > 1)
		{
		    if( nameValue[0].equals("name") )
		    {
			s.name = URLDecoder.decode(nameValue[1], "UTF-8");			
		    }
		    else if( nameValue[0].equals("url") )
		    {
			s.url = URLDecoder.decode(nameValue[1], "UTF-8");
		    }
		}
	    }
	}
	
	List<Station> stations = applicationContext.getCurrentPlaylist();	
	stations.add(s);
	
	if( applicationContext.isDefaultRadioBand() )
	{
            addErrorMessage("Radio stations added to the default list will not be saved.");
	}
	else
	{
	    radioBandService.savePersonal(stations);
	}
    }	    
	    
    public void nextStation() throws Exception 
    {
	int index = applicationContext.getCurrentStation();
	index++;
	List<Station> stations = applicationContext.getCurrentPlaylist();
	if( index >= stations.size() )
	{
	    index = 0;
	}
	if(stations.size() == 0)
	{
	    errorMessages += "There are no station loaded.  Try loading the default radio band or adding to your personal radio band.";
	}
	else
	{
	    applicationContext.setCurrentStation(index);
	    Station station = stations.get(index);
            System.out.println("next station: " + station.url);
                    
//	    List<String> output = commandLine.execute("mpc",  "clear");
//	    output = commandLine.execute("mpc",  "add", station.url);
//	    output = commandLine.execute("mpc",  "play");
            boolean successful = radioService.playStation(station.url);            
            if(!successful)
            {
                addErrorMessage("Playing the next station failed");
            }            
	}	
    }
    
    public void play() throws Exception 
    {
//        applicationContext.setCurrentRadioMode(RadioModes.INTERNET);
        
	String [] strs = request.split("/");
	if(strs.length > 2)
	{
	    String s = strs[2];
	    int i = Integer.valueOf(s);
	    List<Station> stations = applicationContext.getCurrentPlaylist();
	    if( i < stations.size() )
	    {
		applicationContext.setCurrentStation(i);
		Station station = stations.get(i);
		boolean successful = applicationContext.playStation(station.url);
                if(!successful)
                {
                    addErrorMessage("The Media Player play command failed for: " + station.url);
                }
	    }
	    else
	    {
		addErrorMessage("The specified station (" + i + ") could not be loaded.");
	    }
	}
	else
	{	    
            boolean successful = radioService.play();
            if(!successful)
            {
                addErrorMessage("The play command failed on the Media Player.");
            }
	}
    }
    
    public void remove() throws Exception 
    {
	String [] strs = request.split("/");
	if(strs.length > 2)
	{
	    String s = strs[2];
	    int i = Integer.valueOf(s);
	    List<Station> stations = applicationContext.getCurrentPlaylist();
	    if( 0 <= i && i < stations.size() )
	    {
		stations.remove(i);
		if( !applicationContext.isDefaultRadioBand() )
		{
		    radioBandService.savePersonal(stations);
		}
	    }
	    else
	    {
		errorMessages += "The specified station (" + i + ") could not be removed.";
	    }
	}
	else
	{
	    errorMessages += "A valid index is requird for this action.";
	}
    }
    
    public void stop() throws Exception 
    {
        try 
        {
            radioService.stopPlayback();
        } 
        catch (Exception ex) 
        {
            logger.log(Level.SEVERE, null, ex);
            errorMessages += "There was a problem stopping the Media Player.";
        }
    }    
        
}
