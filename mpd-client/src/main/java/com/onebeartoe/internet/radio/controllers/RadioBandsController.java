
package com.onebeartoe.internet.radio.controllers;

import com.onebeartoe.internet.radio.Station;
import com.onebeartoe.internet.radio.services.RadioBandService;
import com.onebeartoe.io.TextFileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rmarquez
 */
public class RadioBandsController extends InternaetRadioController
{       
    
    public void loadDefault() throws Exception
    {	
	applicationContext.setDefaultRadioBand(true);
	List<Station> stations = radioBandService.retreiveDefault();
        try
        {
            applicationContext.setCurrentPlaylistAndPlay(stations);
        }
	catch(Exception e)
        {
            String message = "could not start the defauld readio band: " + e.getMessage();
            addErrorMessage(message);
            System.err.println(message);
        }
    }
    
    /**
     * shows the new Radio Band form
     */
    public void newForm()
    {
	
    }
    
    public void personal()
    {
	applicationContext.setDefaultRadioBand(false);
	List<Station> stations = null;
	try 
	{
	    stations = radioBandService.retreivePersonal();	    
	} 
// catch a java.io.FileNotFoundException instead
        catch (Exception ex) 
	{
            logger.log(Level.SEVERE, null, ex);
	}
	
	if(stations == null)
	{
	    errorMessages += "Could not load the Persoanl Radio Band";
	    stations = new ArrayList();
	}
	try 
	{
	    applicationContext.setCurrentPlaylistAndPlay(stations);
	} 
	catch (Exception ex) 
	{
	    Logger.getLogger(RadioBandsController.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
}
