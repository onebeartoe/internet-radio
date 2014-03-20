
package org.onebeartoe.radio.volume;

/**
 * @author Roberto Marquez
 */
public interface VolumeService 
{
    void setVolume(double volume) throws Exception;
    
    double getVolume() throws Exception;
}
