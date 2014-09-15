
package org.onebeartoe.radio.volume;

import java.util.List;

/**
 * @author Roberto Marquez
 */
public interface VolumeService 
{
    double getVolume() throws Exception;
    
    List<String> lowerVolume(double amount) throws Exception;
    
    /**
     * 
     * @param ammout
     * @return The standard output of the system command to raise the volume;
     * @throws Exception 
     */
    List<String> raiseVolume(double ammout) throws Exception;

    
// not sure about this one    
    void setVolume(double volume) throws Exception;    
}
