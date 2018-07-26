package mydedup;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Math.*;

class Chunks{
	public byte[] SHA1;
	public byte[] data;

	public Chunks(byte[] input){
		try{
			this.data = input;
			MessageDigest md =MessageDigest.getInstance("SHA-1");
			md.update(this.data,0, this.data.length);
			this.SHA1 = md.digest();
			//System.out.println(new String(data, 0));
			
		}
		catch(Exception e){
			System.out.println("Error:"+e);
		}
		
	}
	
}