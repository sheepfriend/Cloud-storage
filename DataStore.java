package mydedup;


import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.charset.StandardCharsets;


class DataStore implements Serializable{
	
	HashMap<String,FileData> fileData;
	MetaData metaData;
	
	public DataStore(){
		this.fileData=new HashMap<String,FileData>();
		this.metaData=new MetaData();
	}
	
	public boolean uploadFile(int min_chunk,int avg_chunk,int max_chunk,int d,String path,String type) throws IOException,NoSuchAlgorithmException{
		
		//file exists -> false
		if(fileData.get(path)!=null){return false;}
		
		//chunk file
		FileData file=new FileData();
		FileChunks fp=new FileChunks(path,min_chunk,avg_chunk,max_chunk,d);
		
		file.writeFile(path,type);
		
		Chunks chunk=fp.get_chunks();
		
		if(chunk==null){
			metaData.reportDedup();
			fileData.put(path,file);
			System.out.println("Empty file");
			return true;
		}
		
		
		while(chunk!=null){
			
			ChunkInfo tmp_chunk=ChunkInfo.getInstance(type);
			
			tmp_chunk.setName(new BigInteger(1,chunk.SHA1).toString());
			tmp_chunk.setChunkSize(chunk.data.length);
			
			file.addChunk(tmp_chunk);
			
			metaData.update(tmp_chunk,chunk.data);
			
			chunk=fp.get_chunks();
		}
		
		
		//dedup
		//MetaData could check whether the chunk is for Local or Azure automatically
		metaData.reportDedup();
		
		
		//update fileData
		fileData.put(path,file);
		
		return true;
	}
	
	public boolean downloadFile(String path) throws IOException{
		
		//file not exists -> false
		if(fileData.get(path)==null){return false;}
		
		fileData.get(path).downloadFile();
		
		return true;
	}
	
	public boolean deleteFile(String path){
		
		//file not exists -> false
		if(fileData.get(path)==null){return false;}
		
		metaData.delete(fileData.get(path).getChunkList());
		
		fileData.remove(path);
		
		return true;
	}
	
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.writeObject(fileData);
		out.writeObject(metaData);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		fileData=(HashMap<String,FileData>)in.readObject();
		metaData=(MetaData)in.readObject();
	}
	
	private void readObjectNoData() throws ObjectStreamException{
		fileData=new HashMap<String,FileData>();
		metaData=new MetaData();
	}
	
	
}