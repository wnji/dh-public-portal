package com.blkchainsolutions.common;

import java.util.regex.Pattern;

/**
 *
 * @Description : 邮箱验证
 * @Author : aven
 * @Date : 2020/2/9 2:06 下午
*/
public final class CheckVariable {

	public static boolean isValidEmail(String email)
	{
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                "[a-zA-Z0-9_+&*-]+)*@" + 
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                "A-Z]{2,7}$"; 
                  
		Pattern pat = Pattern.compile(emailRegex); 
		if (email == null) 
			return false; 
		
		return pat.matcher(email).matches(); 
	}
	
	public static boolean isStringInt(String s)
	{
	    try
	    {
	        Long.parseLong(s);
	        return true;
	    } catch (NumberFormatException ex)
	    {
	        return false;
	    }
	}
}