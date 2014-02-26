
package com.onebeartoe.internet.radio;

import com.onebeartoe.internet.radio.network.InternetRadioServer;

/**
 * @author rmarquez
 */
public class InternetRadio 
{
    public static void main(String [] args)
    {
	try
	{
	    InternetRadioServer nextSongServer = new InternetRadioServer();
	    nextSongServer.start();				
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	}
    }
}
