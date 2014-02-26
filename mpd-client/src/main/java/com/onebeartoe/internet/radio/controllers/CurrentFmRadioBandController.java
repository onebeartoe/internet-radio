
package com.onebeartoe.internet.radio.controllers;

/**
 *
 * @author roberto marquez
 */
public class CurrentFmRadioBandController extends CurrentRadioBandController
{
    @Override
    public void process() throws Exception
    {
        String html = "";
        boolean includeHeader = true;
        
        sendHttpResponse(html, includeHeader);
    }    
}
