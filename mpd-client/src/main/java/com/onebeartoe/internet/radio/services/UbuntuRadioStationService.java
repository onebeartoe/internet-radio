
package com.onebeartoe.internet.radio.services;

import com.onebeartoe.os.shell.BashCommandLine;
import com.onebeartoe.os.shell.CommandLine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rmarquez
 */
public class UbuntuRadioStationService implements InternetRadioStationService
{

    private CommandLine commandLine;
    
    public UbuntuRadioStationService()
    {
	commandLine = new BashCommandLine();
    }
    
    public void stopPlayback() throws Exception 
    {
	commandLine.execute("mpc", "clear");
    }

    @Override
    public boolean playStation(String url) //throws Exception 
    {
        boolean successful = false;
        try //throws Exception
        {
            stopPlayback();
            commandLine.execute("mpc",  "add", url);	    
            commandLine.execute("mpc", "play");
            successful = true;
        } 
        catch (Exception ex) 
        {            
            Logger.getLogger(UbuntuRadioStationService.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(UbuntuRadioStationService.class.getName()).log(Level.SEVERE, null, ex);
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
        boolean successful = false;
        try 
        {
            List<String> output = commandLine.execute("mpc",  "play");
            successful = true;
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(UbuntuRadioStationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return successful;
    }
    
}
