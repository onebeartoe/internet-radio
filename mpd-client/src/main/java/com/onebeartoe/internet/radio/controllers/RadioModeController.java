
package com.onebeartoe.internet.radio.controllers;

import com.onebeartoe.internet.radio.RadioModes;
import java.util.List;

/**
 *
 * @author Roberto Marquez
 */
public class RadioModeController extends InternaetRadioController
{
    public void fm() throws Exception
    {
	// swithc to fem 
        applicationContext.setCurrentRadioMode(RadioModes.FM);
    }
    
    public void internet() throws Exception
    {
	// swithc to internet 
        applicationContext.setCurrentRadioMode(RadioModes.INTERNET);
    }    
}
