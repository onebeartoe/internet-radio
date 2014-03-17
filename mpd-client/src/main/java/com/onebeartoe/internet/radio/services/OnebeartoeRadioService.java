
package com.onebeartoe.internet.radio.services;

import com.onebeartoe.internet.radio.Station;
import com.onebeartoe.os.shell.BashCommandLine;
import com.onebeartoe.os.shell.CommandLine;
import java.util.logging.Logger;

/**
 *
 * @author Roberto Marquez
 */
public abstract class OnebeartoeRadioService implements InternetRadioStationService
{
    protected CommandLine commandLine;

    protected Station currentStation;
    
    protected Logger logger;
    
    public OnebeartoeRadioService()
    {
        logger = Logger.getLogger(this.getClass().getName());
        
        commandLine = new BashCommandLine();
        
currentStation = new Station();
currentStation.name = "nully!";
currentStation.frequency = -1.0;
    }
    
    public Station getCurrentStation() {
        return currentStation;
    }

    public void setCurrentStation(Station currentStation) {
        this.currentStation = currentStation;
    }    
}
