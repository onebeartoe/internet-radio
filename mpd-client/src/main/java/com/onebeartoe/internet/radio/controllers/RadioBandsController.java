
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
	app.setDefaultRadioBand(true);
	List<Station> stations = radioBandService.retreiveDefault();	
	app.setCurrentPlaylistAndPlay(stations);
    }
    
    /**
     * shows the new Radio Band form
     */
    public void newForm()
    {
	
    }
    
    public void personal()
    {
	app.setDefaultRadioBand(false);
	List<Station> stations = null;
	try 
	{
	    stations = radioBandService.retreivePersonal();	    
	} 
// catch a java.io.FileNotFoundException instead
        catch (Exception ex) 
	{	    
	    Logger.getLogger(RadioBandsController.class.getName()).log(Level.SEVERE, null, ex);
	}
	
	if(stations == null)
	{
	    errorMessages += "Could not load the Persoanl Radio Band";
	    stations = new ArrayList();
	}
	try 
	{
	    app.setCurrentPlaylistAndPlay(stations);
	} 
	catch (Exception ex) 
	{
	    Logger.getLogger(RadioBandsController.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
}
