package com.munsiji.commonUtil;

import java.util.Random;

public class GeneratePassword {
	static final int PWDLEN = 8;
	public String getPassword() 
    { 
        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
        String Small_chars = "abcdefghijklmnopqrstuvwxyz"; 
        String numbers = "0123456789"; 
        String symbols = "!@#$%^&*_=+-/.?<>)"; 
        String values = Capital_chars + Small_chars + 
                        numbers + symbols; 
        Random rndm_method = new Random(); 
        char[] password = new char[PWDLEN]; 
        for (int i = 0; i < PWDLEN; i++) 
        { 
            password[i] = 
            values.charAt(rndm_method.nextInt(values.length())); 
  
        } 
        System.out.println("Your new password is : "+new String(password)); 
       return new String(password); 
    } 
}
