
package com.onebeartoe.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextFileReader
{
    public static String readText(InputStream instream) 
    {
	String string;
	try {
	    InputStreamReader reader = new InputStreamReader(instream);
	    BufferedReader input = new BufferedReader(reader);
	    StringBuffer text = new StringBuffer();
	    String line = input.readLine();
	    if (line == null)
		System.out.println
		    ("TextFileReader attempts to read empty text file.");
	    for (/**/; line != null; line = input.readLine()) {
		text.append(line);
		text.append(System.getProperty("line.separator"));
	    }
	    input.close();
	    string = text.toString();
	} catch (FileNotFoundException fnf) {
	    System.out.println
		("FileNotFoundException within TextFileReader.readText");
	    return null;
	} catch (IOException ioe) {
	    System.out.println("IOException within TextFileReader.readText");
	    return null;
	}
	return string;
    }
    
    public static String readText(String filename) {
	File file = new File(filename);
	if (file.exists())
	    return readText(file);
	return null;
    }
    
    public static String readText(File f) {
	String string;
	try {
	    FileInputStream instream = new FileInputStream(f);
	    string = readText(instream);
	} catch (FileNotFoundException fnfe) {
	    return null;
	}
	return string;
    }
}
