/**
 * @(#)EMRResourceBundle.java  1.0   02/02/2011
 *
 * Copyright (c) 2010 Greenclinical, Inc.
 * All rights reserved.
 *
 */
package com.linearrsa.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.configuration.PropertiesConfiguration;

public class ChurnyResourceBundle {
	private static ResourceBundle resourceBundle = null;
	
	// This is a static class. Instances of this class will not be created.
	private ChurnyResourceBundle(){}
	
	public static String getMessage(String key) {
		try{
			if(resourceBundle == null){
				Locale locale = new Locale("en","US");
				ResourceBundle bundle = ResourceBundle.getBundle("PRMessages", locale);
				resourceBundle = bundle;
			}
			
			return resourceBundle.getString(key);
		}
		catch(NullPointerException nulle){
			return "Error message key cannot be null";
		}
		catch(Exception e){
			return "Error message not found";
		}
		
	}
	
	public static String getValue(String key) {
		try{
			if(resourceBundle == null){
				Locale locale = new Locale("en","US");
				ResourceBundle bundle = ResourceBundle.getBundle("crud", locale);
				resourceBundle = bundle;
				
				
//				Properties props = new Properties();
//		        props.setProperty("ServerAddress", "serverAddr");
//		        File f = new File("crud.properties");
//		        OutputStream out = new FileOutputStream( f );
//		        props.store(out, "This is an optional header comment string");
				
				
//				PropertiesConfiguration configuration = new PropertiesConfiguration("crud.properties");
//				configuration.setProperty("colors.background", "#000000");
			}
			
			return resourceBundle.getString(key);
		}
		catch(NullPointerException nulle){
			return "Error message key cannot be null";
		}
		catch(Exception e){
			return "Error message not found";
		}
		
	}
}
