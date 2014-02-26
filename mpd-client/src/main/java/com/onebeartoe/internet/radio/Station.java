
package com.onebeartoe.internet.radio;

/**
 * @author rmarquez
 */
public class Station 
{
    public String url;
    
    public Double frequency;
    
    public String name;
    
    @Override
    public String toString()
    {
	return url + " - " + name;
    }
}
