
package com.onebeartoe.internet.radio;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.onebeartoe.io.ObjectSaver;

public class DefaultPlaylistCreation 
{
    public static void main( String[] args ) throws Exception
    {
	File infile = new File("src/main/resources/com/onebeartoe/internet/radio/default-stations.text");	
	System.out.println("ab path: " + infile.getAbsolutePath() );
	System.out.println("exists: " + infile.exists() );
	Path path = infile.toPath();
	List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
	List<Station> stations = new ArrayList();
	while( !lines.isEmpty() )
	{
	    String url = lines.remove(0).trim();
	    String name = lines.remove(0).trim();
	    String blankLine = lines.remove(0).trim();
	    
	    if( url.equals("") || name.equals("") || !blankLine.equals("") )
	    {
		String message = "The file is not formated as expected:\nstation url\nstation name\n<blank line>\n<repeat";
		message += "current data: " + url + " - " + name + " - " + blankLine;
		throw new Exception(message);
	    }
	    
	    Station station = new Station();
	    station.name= name;
	    station.url = url;
	    stations.add(station);
	}
	
	System.out.println("captured stations:");
	for(Station station : stations)
	{
	    System.out.println( station.toString() );
	}
	
	File outfile = new File(infile.getParentFile(), infile.getName() + ".xml");
	ObjectSaver.encodeObject(stations, outfile);
    }
}
