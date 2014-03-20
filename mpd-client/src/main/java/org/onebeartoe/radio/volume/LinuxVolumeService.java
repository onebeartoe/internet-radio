
package org.onebeartoe.radio.volume;

/**
 * @author Roberto Marquez
 */
public class LinuxVolumeService implements VolumeService
{

    @Override
    public void setVolume(double volume) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getVolume() 
    {
        String volumeCommand = "amixer get Master | awk '$0~/%/{print $4}' | tr -d '[]'";
        
        return -1;
    }
    
}
