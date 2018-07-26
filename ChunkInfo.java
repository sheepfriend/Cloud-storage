package mydedup;

import java.io.*;
import java.util.*;

class ChunkInfo implements Serializable{
	
	String chunkName;
	int size;
	String type_;
	
	public final static ChunkInfo getInstance(String type){
		if(type.equals("remote")){
			//System.out.println(type);
			ChunkInfoAzure result=new ChunkInfoAzure();
			return result;
		}
		else{
			//System.out.println(type);
			ChunkInfoLocal result=new ChunkInfoLocal();
			return result;
		}
	}
	
	//String or byte[]
	public void setName(String chunkname){chunkName=chunkname;}
	
	public String getChunkName(){return chunkName;}
	
	public void save(byte[] input){}; //save the chunk to harddisk
	
	public void delete(){}; //delete the chunk from harddisk
	
	public byte[] read(){return new byte[1];}; // read the chunk from harddisk
	
	public int getChunkSize(){return size;}
	
	public void setChunkSize(int chunkSize){size=chunkSize;}
	
	public String getType(){return type_;}
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.writeObject(chunkName);
		out.writeObject(size);
		out.writeObject(type_);
		//System.out.println(type_);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		chunkName=(String)in.readObject();
		size=(int)in.readObject();
		type_=(String)in.readObject();
		//System.out.println(type_);
	}
	
	private void readObjectNoData() throws ObjectStreamException{
		chunkName="";
		size=0;
		type_="";
	}
	
}