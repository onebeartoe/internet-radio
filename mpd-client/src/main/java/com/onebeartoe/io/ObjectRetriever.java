
package com.onebeartoe.io;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author rmarquez
 */
public class ObjectRetriever 
{
    public static Object decodeObject(File infile) throws Exception
    {
        FileInputStream instream = new FileInputStream(infile);
        
        return decodeObject(instream);
    }
    
    public static Object decodeObject(InputStream instream) throws Exception
    {	
	XMLDecoder decoder = new XMLDecoder(instream);
	Object object = decoder.readObject();
	decoder.close();	
	
	return object;
    }
    
}
