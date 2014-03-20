
package com.onebeartoe.internet.radio.services;

import com.onebeartoe.internet.radio.Station;
import java.util.List;

/**
 *
 * @author rmarquez
 */
//
// rename this to RadioService
//
public interface RadioStationService 
{
    boolean play() throws Exception;
    
    boolean playStation(String url) throws Exception;
    
    List<Station> retreiveDefault() throws Exception;
    
    List<Station> retreivePersonal() throws Exception;
    
    void savePersonal(List<Station> stations) throws Exception;
    
    void stopPlayback() throws Exception;
    
    List<String> verboseInformation() throws Exception;
}
