
package com.onebeartoe.internet.radio.network;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Roberto Marquez
 */
public class InternetRadioServerSpecification
{
    InternetRadioServer implementation;
    
    @BeforeMethod
    public void setUpMethod() throws Exception
    {
        implementation = new InternetRadioServer();
    }    
    
    @Test(expectedExceptions = {NullPointerException.class})
    public void playStation_fails_nullStation() throws Exception
    {
        String url = null;
        
        implementation.playStation(url);
    }
}
