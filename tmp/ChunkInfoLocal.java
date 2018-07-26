package mydedup;

import java.io.*;
import java.util.*;

class ChunkInfoLocal extends ChunkInfo{
	
	public static String localDir="data/";
	
	public ChunkInfoLocal(){
		this.type_="local";
		this.chunkName="";
		this.size=0;
	}
	
	@Override
	public void save(byte[] input){
		try{
			FileOutputStream fout=new FileOutputStream(localDir+chunkName);
//			System.out.println((new String(input)).length());
			fout.write(input);
			fout.close();
		}
		catch(Exception e){
			try {
				new File("data").mkdir();
				save(input);
			} 
			catch (Exception e1) {
				System.err.println("Fail to save chunk into local storage!");
				System.err.println("===============\nError Message:");
				e1.printStackTrace();
				
			}
		}
	}
	
	
	@Override
	public void delete(){
		try{
			File f=new File(localDir+chunkName);
			f.delete();
		}
		catch(Exception e){
			System.out.println("Fail to get chunk from local storage! Did you delete the chunk?");
			System.out.println("===============\nError Message:");
			e.printStackTrace();
		}
		//delete the chunk from harddisk
	}
	
	
	
	@Override
	public byte[] read(){
		try{
			File f=new File(localDir+chunkName);
			byte[] result=new byte[(int)f.length()];
			FileInputStream fin=new FileInputStream(f);
			fin.read(result);
			fin.close();
			return result;
		}
		catch(Exception e){
			System.out.println("Fail to read chunk from local storage! Did you delete the chunk?");
			System.out.println("===============\nError Message:");
			e.printStackTrace();
		}
		return new byte[1];
	}
}
