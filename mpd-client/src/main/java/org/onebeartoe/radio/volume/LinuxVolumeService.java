
package org.onebeartoe.radio.volume;

import com.onebeartoe.os.shell.BashCommandLine;
import com.onebeartoe.os.shell.CommandLine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.onebeartoe.platforms.HardwarePlatforms;

/**
 * @author Roberto Marquez
 */
public class LinuxVolumeService implements VolumeService
{

    protected CommandLine commandLine;
    
    protected Logger logger = 
            Logger.getLogger(LinuxVolumeService.class.getName());
    
    protected final HardwarePlatforms platform = HardwarePlatforms.BeagleBoneBlack;
    
    protected final String audioDeviceName = (platform  == HardwarePlatforms.BeagleBoneBlack ) ? "Speaker" : "Master";
    
    public LinuxVolumeService()
    {
        commandLine = new BashCommandLine();
    }
    
    private List<String> changeVolumne(double amount, boolean increase)
    {
	List<String> output;
        try 
        {
            String operator = increase ? "+" : "-";
            
            // someting like "5+" or "10.5-"
            String arg = String.valueOf(amount) + operator;
            
            output = commandLine.execute("amixer", "set", audioDeviceName, arg);
        } 
        catch (IOException ex) 
        {
            output = new ArrayList();
            output.add(ex.getMessage());
            
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return output;        
    }

    @Override
    public double getVolume() 
    {
        String volumeCommand = "amixer get " + audioDeviceName +  " | grep % | awk '{print $5}'| sed 's/[^0-9\\%]//g'";
//        String volumeCommand = "amixer get " + audioDeviceName + " | awk '$0~/%/{print $4}' | tr -d '[]'";
        String line;
        List<String> output;
        try 
        {
            String[] cmds = 
                    {
                        "/bin/sh",
                        "-c",
                        volumeCommand
                    };
            output = commandLine.execute(cmds);
//            output = commandLine.execute(volumeCommand);
            line = output.get(0);
            line = line.replace("%", "");
        } 
        catch (IOException ex) 
        {
            line = "-1.23";
            logger.log(Level.SEVERE, null, ex);
        }
                
        Double d = Double.valueOf(line);
        
        return d.doubleValue();
    }

    @Override
    public List<String> lowerVolume(double amount) 
    {
	List<String> output = changeVolumne(amount, false);

        return output;
    }

    @Override
    public List<String> raiseVolume(double ammout)
    {   
	List<String> output = changeVolumne(ammout, true);

        return output;
    }
    
    @Override
    public void setVolume(double volume) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
