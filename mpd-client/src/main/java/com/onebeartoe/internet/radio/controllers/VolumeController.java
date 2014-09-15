
package com.onebeartoe.internet.radio.controllers;

import java.util.List;

/**
 * @author rmarquez
 */
public class VolumeController extends InternaetRadioController
{
    public void up() throws Exception
    {
        List<String> output = volumeService.raiseVolume(15);
        printOutput(output);
    }
    
    public void down() throws Exception
    {
	List<String> output = volumeService.lowerVolume(15);
        
        printOutput(output);
    }
    
    private void printOutput(List<String> output)
    {
        for(String line : output)
        {
            System.out.println(line);
        }
    }
}
