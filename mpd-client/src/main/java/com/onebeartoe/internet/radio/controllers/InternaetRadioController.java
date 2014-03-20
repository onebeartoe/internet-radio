
package com.onebeartoe.internet.radio.controllers;

import com.onebeartoe.internet.radio.PlaylistSources;
import com.onebeartoe.internet.radio.Station;

import com.onebeartoe.io.TextFileReader;

import com.onebeartoe.os.shell.BashCommandLine;
import com.onebeartoe.os.shell.CommandLine;

import java.io.InputStream;

import java.lang.reflect.Method;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.onebeartoe.radio.volume.LinuxVolumeService;
import org.onebeartoe.radio.volume.VolumeService;

public class InternaetRadioController extends SocketController 
{        
    protected CommandLine commandLine;

    protected String VOLUME_UP = "volume-up";

    protected String VOLUME_DOWN = "volume-down";    

    protected String LOAD_DEFAULT_PLAYLIST = "default";

    protected String NEXT_STATION = "nextStation";
    
    private final String CURRENT_STATION_INFO = "CURRENT_STATION_INFO";

    protected final String ERROR_MESSAGES = "ERROR_MESSAGES";
        
    protected final String NEW_RADIO_STATION_FORM = "NEW_RADIO_STATION_FORM";
    
    protected final String INTERNET_RADIO_STATIONS_TITLE = "INTERNET_RADIO_STATIONS_TITLE";
    
    protected final String STATION_INDEX = "STATION_INDEX";
    
    protected final String CURRENT_RADIO_STATIONS_LIST = "CURRENT_RADIO_STATIONS_LIST";
    
    protected final String LOAD_RADIO_BAND_HTML = "LOAD_RADIO_BAND_HTML";
    
    protected final String PLAY_STATION_FORM = "PLAY_STATION_FORM";
    
    protected final String REMOVE_STATION_FORM = "REMOVE_STATION_FORM";
    
    protected final String VOLUME_SECTION = "VOLUME_SECTION";
    
    protected final String SERVER_VOLUME = "SERVER_VOLUME";
    
    protected String currentRadioStationsHtml;
    
    protected String errorMessages;
    
    protected String internetRadioStationsTitle;
    
    private String currentStationInfo;
    
    protected String loadPersonalHtml;
    
//    protected RadioBandService radioBandService;
    
    protected VolumeService volumeService;
    
    protected Logger logger;
    
    public InternaetRadioController()
    {
        logger = Logger.getLogger(this.getClass().getName());
        
	commandLine = new BashCommandLine();
	
        errorMessages = "";
	
//        radioBandService = new RadioBandService();
        
        volumeService = new LinuxVolumeService();
    }
    
    public void addErrorMessage(String errorMessage)
    {
        errorMessages += "<br/><br/>";
        errorMessages += errorMessage;
    }
    
    private String buildVolumeSection()
    {
        StringBuilder sb = new StringBuilder();
        try 
        {
            double volume = volumeService.getVolume();
            
            String inpath = path + "volume-section.html";
            InputStream instream = getClass().getResourceAsStream(inpath);
            
            String volumeSection = TextFileReader.readText(instream);
            volumeSection = volumeSection.replace(SERVER_VOLUME, String.valueOf(volume) );
            
            sb.append(volumeSection);
        } 
        catch (Exception ex) 
        {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            
            sb.append("volume section not available");
        }
        
        return sb.toString();
    }
    
    @Override
    public void preProcess() throws Exception 
    {
	String [] substrings = request.split("/");
	if(substrings.length > 1)
	{
	    String methodName = substrings[1];
	    
	    if( methodName.contains("?") )
	    {
		int i = methodName.indexOf("?");
		methodName = methodName.substring(0, i);
	    }
	    
	    Method method = getClass().getMethod(methodName);
	    method.invoke(this);
	}
    }
    
    @Override
    public void process() throws Exception
    {
	List<Station> stations = applicationContext.getCurrentPlaylist();
	if(stations.size() > 0)
	{
	    int i = applicationContext.getCurrentStation();
	    if( i < stations.size() )
	    {
		Station station = stations.get( applicationContext.getCurrentStation() );
		currentStationInfo += station.name + "<br/>";
	    }	    
	}
	
	List<String> output = radioService.verboseInformation();
	for(String s : output)
	{
	    currentStationInfo += s + "<br/>";
	}

	boolean includeHeader = true;
	String uiHtmlath = path + "remote-control.html";

	InputStream instream = getClass().getResourceAsStream(uiHtmlath);				 
	String html = TextFileReader.readText(instream);

        String mode = applicationContext.getCurrentRadioMode().name();
        html = html.replace("CURRENT_RADIO_MODE", mode);
        
	if(currentStationInfo != null)
	{
	    currentStationInfo = currentStationInfo.replaceFirst("null", "");
	    html = html.replace(CURRENT_STATION_INFO, currentStationInfo);
	}
        
        String volumeSection = buildVolumeSection();
        html = html.replace(VOLUME_SECTION, volumeSection);
			
	html = html.replace(ERROR_MESSAGES, errorMessages);
	
	String inpath = path + "loadPersonal.html";
	instream = getClass().getResourceAsStream(inpath);
	String loadPersonal = TextFileReader.readText(instream);
	
	inpath = path + "loadDefault.html";
	instream = getClass().getResourceAsStream(inpath);
	String loadDefault = TextFileReader.readText(instream);
	
	inpath = path + "individualStationButtons.html";
	instream = getClass().getResourceAsStream(inpath);
	String individualStationButtonsHtml = TextFileReader.readText(instream);
	
	inpath = path + "playStationIForm.html";
	instream = getClass().getResourceAsStream(inpath);
	String playStationIForm = TextFileReader.readText(instream);
	
	inpath = path + "removeStationIForm.html";
	instream = getClass().getResourceAsStream(inpath);
	String removeStationIForm = TextFileReader.readText(instream);       
		
	String loadRadioBandsHtml = "";
	if(applicationContext.getPlaylistSource() == PlaylistSources.DEFAULT)
	{
	    internetRadioStationsTitle = "Builtin Radio Stations";
	    
	    loadRadioBandsHtml += loadDefault;
	    loadRadioBandsHtml += loadPersonal;	    	    
	}
	else
	{
	    internetRadioStationsTitle = "Personal Radio Stations";
	    	    
	    inpath = path + "newRadioBandForm.html";
	    instream = getClass().getResourceAsStream(inpath);
	    String newRadioStationForm = TextFileReader.readText(instream);
	    loadPersonal = loadPersonal.replace(NEW_RADIO_STATION_FORM, newRadioStationForm);	
	    
	    loadRadioBandsHtml += loadPersonal;
	    loadRadioBandsHtml += loadDefault;
	}
	html = html.replace(INTERNET_RADIO_STATIONS_TITLE, internetRadioStationsTitle);        
	html = html.replace(LOAD_RADIO_BAND_HTML, loadRadioBandsHtml);

	StringBuilder stationsHtml = new StringBuilder();
	int i = 0;
	for(Station s : stations)
	{
	    stationsHtml.append("<div style=\""				
				+ " padding-top: 2%;" 
				+ " padding-bottom: 2%;" 
				+ " font-size:1.875em;\">");

	    stationsHtml.append(s.name);
	    
	    stationsHtml.append("<br/>");	    
	    
	    stationsHtml.append("<a href=\"");	    
	    stationsHtml.append(s.url);
	    stationsHtml.append("\">");
	    stationsHtml.append(s.url);
	    stationsHtml.append("</a>");
	    
	    String form = playStationIForm.replace(STATION_INDEX, String.valueOf(i) );
	    String buttonsHtml = individualStationButtonsHtml.replace(PLAY_STATION_FORM, form);

	    form = removeStationIForm.replace(STATION_INDEX, String.valueOf(i) );
	    buttonsHtml = buttonsHtml.replace(REMOVE_STATION_FORM, form);
	    
	    stationsHtml.append(buttonsHtml);	    	    
	    
	    stationsHtml.append("</div>");

	    i++;
	}
	html = html.replace(CURRENT_RADIO_STATIONS_LIST, stationsHtml.toString() );
	
	sendHttpResponse(html, includeHeader);
    }	   
  
}
