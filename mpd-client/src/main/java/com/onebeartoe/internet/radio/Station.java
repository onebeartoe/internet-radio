
package com.onebeartoe.internet.radio;

import java.io.Serializable;

/**
 * @author rmarquez
 */
public class Station implements Serializable
{
    public String url;
    
    public Double frequency;
    
    public String name;
    
    public RadioModes type;
    
    public Station()
    {
        name = "-None-";
        type = RadioModes.INTERNET;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name + " - " + type.name() + " - ");        
        sb.append(type == RadioModes.INTERNET ? url : frequency);
        
	return sb.toString();
    }
}
