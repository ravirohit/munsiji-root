package com.munsiji.commonUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class EncryptDecryptData {
	 public String convertTextToHexString(String base64String){
	    	StringBuffer sb = new StringBuffer();
	    	try {
				byte[] base64StringByteArray = base64String.getBytes("utf-8");
				for(byte b:base64StringByteArray){
					sb.append(String.format("%02X", b));
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	    	return sb.toString();
	 }
	 public String converByteArrayToHexString(byte[] data) { 
	        StringBuffer sb = new StringBuffer();
	        for(byte b: data){
	        	sb.append(String.format("%02X", b));
	        }
	        return sb.toString();
	  } 
	
	 public String convertTextToHashedValue(String text){
		 String hashedValue = null;
		try{
	    	MessageDigest digest = MessageDigest.getInstance("SHA-1");
	    	byte[] textByte = text.getBytes();
	    	digest.update(textByte);
	    	byte[] hashedBytes = digest.digest();  // got the hashed value in byte array value
	    	System.out.println("input string:"+text+" \n hashbyte:"+hashedBytes+" \n string:"+new String(hashedBytes));
	    	hashedValue = converByteArrayToHexString(textByte); 
		}
		catch(Exception e){
			System.out.println("Exception occur while converting text to HashedValue:"+e);
		}
    	return hashedValue;
	 }
	 
}
