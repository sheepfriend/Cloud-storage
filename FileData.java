package mydedup;

import java.io.*;
import java.util.*;

class FileData implements Serializable{
	
	public ArrayList<ChunkInfo> chunkList;
	public String fileName;
	// 0 means local, 1 means Azure
	public int storage;

	public FileData(){
		this.chunkList=new ArrayList<ChunkInfo>();
		this.fileName="";
		this.storage = 0;
	}
	
	public FileData(String type){
		this.chunkList=new ArrayList<ChunkInfo>();
		this.fileName="";
		this.storage = 0;
	}

	
	public void downloadFile() throws IOException{
		//use chunks to form data
		FileOutputStream fout=new FileOutputStream(fileName);
		for(ChunkInfo tmp:chunkList){
			fout.write(tmp.read());
		}
		fout.close();
	}
	
	public void writeFile(String path,String type) throws IOException{
		//read a file and chunk it
		//wrong handling
		fileName=path;
		/*
		try {
			fileStream = new FileInputStream(filePath);
        }catch (FileNotFoundException e){
        	System.err.println("Error: Can not open file: " + filePath);
        	e.printStackTrace();
        	System.exit(1);
        }*/
        
	}
	
	public void addChunk(ChunkInfo a){
		chunkList.add(a);
	}
	
	public ArrayList<ChunkInfo> getChunkList(){
		return chunkList;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.writeObject(chunkList);
		out.writeObject(fileName);
		out.writeObject(storage);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		chunkList= (ArrayList<ChunkInfo>)in.readObject();
		fileName=(String)in.readObject();
		storage = (int)in.readObject();
	}
	
	private void readObjectNoData() throws ObjectStreamException{
		chunkList=new ArrayList<ChunkInfo>();
		fileName="";
		storage = 0;
	}
	
}
