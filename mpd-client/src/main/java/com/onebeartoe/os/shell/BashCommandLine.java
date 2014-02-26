
package com.onebeartoe.os.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rmarquez
 */
public class BashCommandLine implements CommandLine
{

    @Override
    public List<String> execute(String ... command) throws IOException 
    {
	Runtime runtime = Runtime.getRuntime();
	Process process = runtime.exec(command);
	InputStream instream = process.getInputStream();	
	InputStreamReader instreamReader = new InputStreamReader(instream);
	BufferedReader br = new  BufferedReader(instreamReader);	
	List<String> output = new ArrayList();
	String line = br.readLine();	
	while(line != null)
	{
	    output.add(line);
	    line = br.readLine();
	}	
	br.close();
	instream.close();
	
	return output;
    }
    
}
