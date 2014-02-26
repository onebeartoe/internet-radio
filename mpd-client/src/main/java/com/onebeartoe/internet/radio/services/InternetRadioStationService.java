
package com.onebeartoe.internet.radio.services;

import java.util.List;

/**
 *
 * @author rmarquez
 */
public interface InternetRadioStationService 
{
    public boolean play() throws Exception;
    
    public boolean playStation(String url) throws Exception;
    
    public void stopPlayback() throws Exception;
    
    public List<String> verboseInformation() throws Exception;
}
