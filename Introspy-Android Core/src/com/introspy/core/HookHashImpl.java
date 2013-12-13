package com.introspy.core;

import java.lang.reflect.Method;

import android.util.Log;


class Intro_HASH extends IntroHook { 
	public void execute(Object... args) {
		
	    StackTraceElement[] ste = Thread.currentThread().getStackTrace();
	    // this is called within apps and is super noisy so not displaying it
	    if (ste[7].toString().contains("com.crashlytics."))
	    	return;
	    // the above code may only work on android 4.2.2
	    // replace with the code below if so
	    /* for (int i = 0; i < ste.length; i++)
	        if (ste[i].toString().contains("com.crashlytics."))
	        	return; */
	    
		if (args[0] != null) {
            _l.logBasicInfo();
            String input = StringHelper.getReadableByteArr((byte[])args[0]);            
            
            byte[] output = null;
            String s_output = "";
            try {
                    // execution the method to calculate the digest
                    // using reflection to call digest from the object's instance
                    try {
                            Class<?> cls = Class.forName("java.security.MessageDigest");
                            Object obj =_resources;
                            Class<?> noparams[] = {};
                            Method xmethod = cls.getDeclaredMethod("digest", noparams);
                            output = (byte[]) xmethod.invoke(obj);
                            s_output = StringHelper.getReadableByteArr(output);
                    }
                    catch (Exception e) {
                    	Log.w(_TAG_ERROR, "Error in Hash func: " + e);
                    }
            }
            catch (Throwable e) {
            	Log.w(_TAG_ERROR, "Error in Hash func: " + e);
            }

            // use reflection to call a method from this instance
            String algoName = null;
            try {
                    Class<?> cls = Class.forName("java.security.MessageDigest");
                    Object obj =_resources;
                    Class<?> noparams[] = {};
                    Method xmethod = cls.getDeclaredMethod("getAlgorithm", noparams);
                    algoName = (String) xmethod.invoke(obj);
            }
            catch (Exception e) {
                    algoName = "error: " + e;        
            }
            
            _l.logLine("-> Hash of : [" + input + "] is: [" + 
                            s_output +"] , Algo: [" + algoName + "]");
            
            _l.logParameter("Input", input);
            _l.logParameter("Output", s_output);
            _l.logParameter("Algo", algoName);
            
            if (algoName.contains("MD5")) {
                    _l.logFlush_W("MD5 used, this hashing algo " +
                    		"is broken and should not be used");
            }
            else
                    _l.logFlush_I();
		}
	}
}
