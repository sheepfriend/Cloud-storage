package mydedup;

import java.io.*;
import java.util.*;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.microsoft.windowsazure.services.core.storage.*;
import com.microsoft.windowsazure.services.blob.client.*;


class ChunkInfoAzure extends ChunkInfo{
	public static final String storageConnectionString = 
	    	    "DefaultEndpointsProtocol=http;" + 
	    	    "AccountName=lzt4180;" + 
	    	    "AccountKey=eFc/OFsxgb4u6J0wd/K+oGpkcf0Q+6mWstwojhv8K/dKsJ5/r4Aak5vFsMZenaWEMKMNG4jC8ty4h7PDRzbJwQ==";



	public ChunkInfoAzure(){
		this.type_="remote";
		this.chunkName="";
		this.size=0;
	}
	
	@Override
	public void save(byte[] input){
		try{
		CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
		CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
		CloudBlobContainer container = blobClient.getContainerReference("data");
		container.createIfNotExist();
		
		CloudBlockBlob blob = container.getBlockBlobReference(chunkName);
		
		String tmp_name=chunkName+System.nanoTime();
		
		//create a tmpfile and upload it...
		File img = new File(tmp_name);
		
		FileOutputStream tmp_f=new FileOutputStream(img);
		tmp_f.write(input);
		tmp_f.close();
		
		blob.upload(new FileInputStream(img),img.length());
		
		img.delete();}catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (StorageException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					System.err.println("Fail to save temperary file into local!");
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("Fail to save temperary file into local!");
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	}
	
	@Override
	public void delete(){
		try{
		CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
		CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
		CloudBlobContainer container = blobClient.getContainerReference("data");
		container.createIfNotExist();
		//System.out.println("deleting...");
		CloudBlockBlob blob = container.getBlockBlobReference(chunkName);
		blob.delete();}catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (StorageException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
	}
	
	@Override
	public byte[] read(){
		try{
		CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
		CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
		CloudBlobContainer container = blobClient.getContainerReference("data");
		container.createIfNotExist();
		
		CloudBlockBlob blob = container.getBlockBlobReference(chunkName);
		
		String tmp_name=chunkName+System.nanoTime();
		
		blob.download(new FileOutputStream(tmp_name,false));
		
		File img = new File(tmp_name);
		FileInputStream tmp_f=new FileInputStream(img);
		byte[] result=new byte[(int)img.length()];
		tmp_f.read(result);
		tmp_f.close();
		
		img.delete();
		return result;
		}catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (StorageException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					System.out.println("Fail to read file from Azure!");
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("Fail to save temperary file into local when reading from Azure!");
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		return new byte[1];
	}
}
