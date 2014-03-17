
package com.onebeartoe.internet.radio.services;

import com.onebeartoe.internet.radio.Station;
import com.onebeartoe.io.ObjectRetriever;
import com.onebeartoe.io.ObjectSaver;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author rmarquez
 */
public class RadioBandService 
{
    
    private String personalInternetStationPath;
    
    public RadioBandService()
    {
	personalInternetStationPath = System.getProperty("user.home") + "/.onebeartoe/internet-radio/" + "personal-radio-station.xml";
    }
    
    public List<Station> retreiveDefault() throws Exception
    {
        final String path = "/com/onebeartoe/internet/radio/default-stations.text.xml";
        InputStream instream = getClass().getResourceAsStream(path);        	        	
	List<Station> stations = (List<Station>) ObjectRetriever.decodeObject(instream);
	
	return stations;
    }
    
    public List<Station> retreivePersonal() throws Exception
    {	
	File infile = new File(personalInternetStationPath);
	List<Station> list = (List<Station>) ObjectRetriever.decodeObject(infile);
	
	return list;
    }
    
    public void savePersonal(List<Station> stations) throws Exception
    {
	File outfile = new File(personalInternetStationPath);
	File parent = outfile.getParentFile();
	if( !parent.exists() )
	{
	    parent.mkdirs();
	}	
	ObjectSaver.encodeObject(stations, outfile);
    }
    
}
