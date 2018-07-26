package mydedup;

import java.io.*;
import java.util.*;


public class MyDedup{
	
	public static DataStore dataStore;
	public static String metaFileName="mydedup.meta";
	public static String metaPath="data";
	
	
	public static void loadMetaData(){
		try{
			FileInputStream fin=new FileInputStream(metaFileName);
			ObjectInputStream oin=new ObjectInputStream(fin);
			dataStore=(DataStore)oin.readObject();
			oin.close();fin.close();
		}
		catch(Exception e){
			System.out.println("Error in loading meta data. Created a new one.");
			//e.printStackTrace();
			dataStore=new DataStore();
			saveMetaData();
		}
	}
	
	public static void saveMetaData(){
		try{
			FileOutputStream fout=new FileOutputStream(metaFileName);
			ObjectOutputStream oout=new ObjectOutputStream(fout);
			oout.writeObject(dataStore);
			oout.close();fout.close();
		}
		catch(Exception e){
			//e.printStackTrace();
			System.out.println("Error in saving meta data.");
		}
	}
	
	public static void main(String[] args) throws IOException{
		System.setProperty("http.proxyHost","proxy.cse.cuhk.edu.hk");
		System.setProperty("http.proxyPort","8000");
		
		
		if(args.length==7 && args[0].equals("upload")){
			
			try{
				int min_chunk=Integer.parseInt(args[1]);
				int avg_chunk=Integer.parseInt(args[2]);
				int max_chunk=Integer.parseInt(args[3]);
				int d=Integer.parseInt(args[4]);
				String filename=args[5];
				String type=args[6];
			
			loadMetaData();
			
			boolean result=dataStore.uploadFile(min_chunk,avg_chunk,max_chunk,d,filename,type);
			
			if(!result){
				System.out.println("File already exists!");
			}
			else{
				saveMetaData();
			}
			
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("Usage:\njava Mydedup upload <min_chunk> <avg_chunk> <max_chunk> <d> <file_to_upload> <local|remote>\njava Mydedup download <file_to_download>\njava Mydedup delete <file_to_delete>");
				System.exit(0);
			}
			
		}
		else if(args.length==2 && args[0].equals("download")){
			
			String filename=args[1];
			
			loadMetaData();
			
			boolean result=dataStore.downloadFile(filename);
			
			if(!result){
				System.out.println("File does not exist!");
			}
			else{
				saveMetaData();
			}
			
		}
		else if(args.length==2 && args[0].equals("delete")){
			
			String filename=args[1];
			
			loadMetaData();
			
			boolean result=dataStore.deleteFile(filename);
			
			if(!result){
				System.out.println("File does not exist!");
			}
			else{
				saveMetaData();
			}
		}
		else{
			System.out.println("Usage:\njava Mydedup upload <min_chunk> <avg_chunk> <max_chunk> <d> <file_to_upload> <local|remote>\njava Mydedup download <file_to_download>\njava Mydedup delete <file_to_delete>");
			
		}
	}
}
