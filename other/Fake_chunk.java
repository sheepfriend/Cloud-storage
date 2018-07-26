package csci4180;

import java.io.*;
import java.util.*;

class Fake_chunk{
	
	String name;
	byte[] data;
	int size;
	
	
	public Fake_chunk(String path) throws IOException{
		this.name=""+System.nanoTime();
		File f=new File(path);
		this.size=(int)f.length();
		this.data=new byte[this.size];
		FileInputStream fin=new FileInputStream(f);
		fin.read(this.data);
	}
	
	public String getName(){return name;}
	public byte[] getData(){return data;}
	public int getSize(){return size;}
	public void setName(String name_){name=name_;}
	
}