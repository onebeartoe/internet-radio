
package com.onebeartoe.internet.radio.controllers;

import java.util.List;

/**
 * @author rmarquez
 */
public class VolumeController extends InternaetRadioController
{
    public void up() throws Exception
    {
	List<String> output = commandLine.execute("amixer", "set", "Master", "5+");
    }
    
    public void down() throws Exception
    {
	List<String> output = commandLine.execute("amixer", "set", "Master", "5-");
    }	    
}
