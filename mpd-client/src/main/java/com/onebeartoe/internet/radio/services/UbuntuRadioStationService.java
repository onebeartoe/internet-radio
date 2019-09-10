
package com.onebeartoe.internet.radio.services;

import com.onebeartoe.internet.radio.Station;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.onebeartoe.io.ObjectRetriever;
import org.onebeartoe.io.ObjectSaver;

/**
 * @author rmarquez
 */
public class UbuntuRadioStationService extends OnebeartoeRadioService // implements InternetRadioStationService
{

    private Logger logger;
    
    private final String personalInternetStationPath;
    
    public UbuntuRadioStationService()
    {
        logger = Logger.getLogger(UbuntuRadioStationService.class.getName());
        
        personalInternetStationPath = System.getProperty("user.home") + "/.onebeartoe/radio/" + "personal-stations.xml";
    }

    @Override
    public List<Station> retreiveDefault() throws Exception
    {
        final String path = "/com/onebeartoe/internet/radio/default-stations.text.xml";
        InputStream instream = getClass().getResourceAsStream(path);        	        	
	List<Station> stations = (List<Station>) ObjectRetriever.decodeObject(instream);
	
	return stations;
    }
    

    @Override
    public List<Station> retreivePersonal() throws Exception
    {	
	File infile = new File(personalInternetStationPath);
	List<Station> list = (List<Station>) ObjectRetriever.decodeObject(infile);
	
	return list;
    }
    
    @Override
    public void savePersonal(List<Station> stations) throws Exception
    {
	File outfile = new File(personalInternetStationPath);
	File parent = outfile.getParentFile();
	if( !parent.exists() )
	{
	    parent.mkdirs();
	}	
	ObjectSaver.encodeObject(stations, outfile);
    }
    
    /**
     *
     * @throws Exception
     */
    @Override
    public void stopPlayback() throws Exception 
    {
        commandLine.execute("mpc",  "stop");
	commandLine.execute("mpc", "clear");
    }

    @Override
    public boolean playStation(String url)
    {
        if(url == null)
        {
            throw new NullPointerException();
        }
        
        boolean successful = false;
        
        try
        {
            stopPlayback();
            commandLine.execute("mpc",  "add", url);	    
            commandLine.execute("mpc", "play");
            successful = true;
        } 
        catch (Exception ex) 
        {            
            logger.log(Level.SEVERE, null, ex);
        }
        
        return successful;
    }

    @Override
    public List<String> verboseInformation() //throws Exception 
    {
        List<String> output = null;
        try 
        {
            output = commandLine.execute("mpc",  "-v");
        } 
        catch (IOException ex) 
        {
            logger.log(Level.SEVERE, ex.getMessage());
        }
        
        if(output == null)
        {
            output = new ArrayList();
            output.add("Information lookup for Media Player Client failed.");
        }
        
        return output;
    }

    @Override
    public boolean play() //throws Exception 
    {
        System.out.println("play from ubuntu service");
        
        boolean successful = false;
        try 
        {
            List<String> output = commandLine.execute("mpc",  "play");
            successful = true;
        } 
        catch (IOException ex) 
        {
            String message = ex.getMessage();
            logger.log(Level.SEVERE, message, ex);
        }
        
        return successful;
    }
 
    
    
    
    
    
    

    
}
