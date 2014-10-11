
package com.onebeartoe.io;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * @deprecated use the version at https://github.com/onebeartoe/java-libraries
 * @author Roberto Marquez
 */
public class ObjectSaver
{
    public static boolean encodeObject(Object obj, File outfile) {
	boolean object_encoded = false;
	try {
	    FileOutputStream fileOutputStream = new FileOutputStream(outfile);
	    object_encoded = encodeObject(obj, fileOutputStream);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    object_encoded = false;
	    System.out.println(e.toString());
	}
	return object_encoded;
    }
    
    public static boolean encodeObject(Object obj, OutputStream outstream) {
	boolean object_encoded = false;
	XMLEncoder encoder = new XMLEncoder(outstream);
	encoder.writeObject(obj);
	encoder.close();
	object_encoded = true;
	return object_encoded;
    }
    
    public static boolean saveObject(Object obj, String file_name) {
	File f = new File(file_name);
	return saveObject(obj, f);
    }
    
    public static boolean saveObject(Object obj, String dir,
				     String file_name) {
	File f = new File(dir, file_name);
	return saveObject(obj, f);
    }
    
    public static boolean saveObject(Object obj, File f) {
	boolean object_saved = false;
	try 
	{
	    FileOutputStream fos = new FileOutputStream(f);
	    ObjectOutputStream outstream = new ObjectOutputStream(fos);
	    outstream.writeObject(obj);
	    outstream.flush();
	    outstream.close();
//	    SerializedObjects.saveObject(f, obj, true);
	    object_saved = true;
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	    object_saved = false;
	}
	return object_saved;
    }
}
