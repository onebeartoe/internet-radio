
package com.onebeartoe.internet.radio.services;

import com.onebeartoe.internet.radio.Station;
import com.onebeartoe.internet.radio.network.InternetRadioServer;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Roberto Marquez
 */
public class UbuntuRadioStationServiceSpecification
{
    UbuntuRadioStationService implementation;
    
    @BeforeMethod
    public void setUpMethod() throws Exception
    {
        implementation = new UbuntuRadioStationService();
    }

    /**
     * Test of retreiveDefault method, of class UbuntuRadioStationService.
     */
    @Test
    public void testRetreiveDefault() throws Exception
    {
        List<Station> defaults = implementation.retreiveDefault();
        
        assertNotNull(defaults);
    }    
}
