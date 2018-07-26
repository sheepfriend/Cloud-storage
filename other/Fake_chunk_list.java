package csci4180;

import java.io.*;
import java.util.*;

class Fake_chunk_list{
	
	public static ArrayList<Fake_chunk> fileList;
	
	public Fake_chunk_list(String path) throws IOException{
		int i;
		fileList=new ArrayList<Fake_chunk>();
		String tmp="";
		for(i=0;i<3;i++){
			Fake_chunk chunk=new Fake_chunk(path);
			if(i==0){
				tmp=chunk.getName();
			}
			if(i==1){
				chunk.setName(tmp);
			}
			fileList.add(chunk);
		}
	}
	
	public ArrayList<Fake_chunk> getList(){return fileList;}
	
}