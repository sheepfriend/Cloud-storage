package mydedup;

import java.io.*;
import java.util.*;

class MetaData implements Serializable{
	
	//you mei you er weishu zu a table a QAQ
	HashMap<String,ChunkInfo> chunkListLocal;
	HashMap<String,Integer> chunkCountLocal;
	
	HashMap<String,ChunkInfo> chunkListAzure;
	HashMap<String,Integer> chunkCountAzure;
	
	
	
	public MetaData(){
		this.chunkListLocal=new HashMap<String,ChunkInfo>();
		this.chunkCountLocal=new HashMap<String,Integer>();
		
		this.chunkListAzure=new HashMap<String,ChunkInfo>();
		this.chunkCountAzure=new HashMap<String,Integer>();
	}
	
	//when uploading a file, sent returned chunkInfo ArrayList here for deduplication
	
	public void update(ChunkInfo tmp,byte[] input){
		String tmp_name=tmp.getChunkName();
		
		if(tmp.getType().equals("local")){
			if(chunkListLocal.get(tmp_name)!=null){
				chunkCountLocal.put(tmp_name,chunkCountLocal.get(tmp_name)+1);
			}
			else{
				chunkListLocal.put(tmp_name,tmp);
				chunkCountLocal.put(tmp_name,1);
				//save the unique chunk
				tmp.save(input);
			}
		}
		else{
			if(chunkListAzure.get(tmp_name)!=null){
				chunkCountAzure.put(tmp_name,chunkCountAzure.get(tmp_name)+1);
			}
			else{
				chunkListAzure.put(tmp_name,tmp);
				chunkCountAzure.put(tmp_name,1);
				//save the unique chunk
				tmp.save(input);
			}
		}
	}
	
	// when deleting a file, sent chunkInfo ArrayList here
	
	public void delete(ArrayList<ChunkInfo> chunklist){
		int num_chunk=0,num_byte=0;
		for(ChunkInfo tmp:chunklist){
			//System.out.println(tmp.getType());
			String tmp_name=tmp.getChunkName();
			
			
			if(tmp.getType().equals("local")){
				if(chunkCountLocal.get(tmp_name)==1){
					num_chunk+=1;
					num_byte+=chunkListLocal.get(tmp_name).getChunkSize();
					chunkCountLocal.remove(tmp_name);
					chunkListLocal.remove(tmp_name);
					
					//delete the unique chunk
					tmp.delete();
					
				}
				else{
					chunkCountLocal.put(tmp_name,chunkCountLocal.get(tmp_name)-1);
				}
			}
			else{
				if(chunkCountAzure.get(tmp_name)==1){
					num_chunk+=1;
					num_byte+=chunkListAzure.get(tmp_name).getChunkSize();
					chunkCountAzure.remove(tmp_name);
					chunkListAzure.remove(tmp_name);
					
					//delete the unique chunk
					tmp.delete();
					
				}
				else{
					chunkCountAzure.put(tmp_name,chunkCountAzure.get(tmp_name)-1);
				}
			}
		}
		System.out.println("Report Output:");
		System.out.println("Number of chunks deleted: "+num_chunk);
		System.out.println("Number of bytes deleted: "+num_byte);
	}
	
	public ArrayList<Integer> getSizes(){
		int sum1=0,sum2=0,tmp;
		
		int count1=0,count2=0;
		
		//count size in Azure data
		for(String tmp_name: chunkListAzure.keySet()){
			tmp=chunkListAzure.get(tmp_name).getChunkSize();
			count1++;
			count2+=chunkCountAzure.get(tmp_name);
			sum1+=tmp;
			sum2+=tmp*chunkCountAzure.get(tmp_name);
		}
		
		//count size in Local data
		for(String tmp_name: chunkListLocal.keySet()){
			tmp=chunkListLocal.get(tmp_name).getChunkSize();
			count1++;
			count2+=chunkCountLocal.get(tmp_name);
			sum1+=tmp;
			sum2+=tmp*chunkCountLocal.get(tmp_name);
		}
		
		
		ArrayList<Integer> sum=new ArrayList<Integer>();
		sum.add(sum1);
		sum.add(sum2);
		sum.add(count1);
		sum.add(count2);
		
		return sum;
	}
	
	public void reportDedup(){
		ArrayList<Integer> sizes=getSizes();
		System.out.println("Report Output:");
		System.out.println("Total number of chunks in storage: "+sizes.get(3));
		System.out.println("Number of unique chunks in storage: "+sizes.get(2));
		System.out.println("Number of bytes in storage with deduplication: "+sizes.get(0));
		System.out.println("Number of bytes in storage without deduplication: "+sizes.get(1));
		System.out.println("Space saving: "+(1-(double)sizes.get(0)/sizes.get(1)));
		
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.writeObject(chunkListLocal);
		out.writeObject(chunkCountLocal);
		out.writeObject(chunkListAzure);
		out.writeObject(chunkCountAzure);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		chunkListLocal=(HashMap<String,ChunkInfo>)in.readObject();
		chunkCountLocal=(HashMap<String,Integer>)in.readObject();
		chunkListAzure=(HashMap<String,ChunkInfo>)in.readObject();
		chunkCountAzure=(HashMap<String,Integer>)in.readObject();
	}
	
	private void readObjectNoData() throws ObjectStreamException{
		chunkListLocal=new HashMap<String,ChunkInfo>();
		chunkCountLocal=new HashMap<String,Integer>();
		chunkListAzure=new HashMap<String,ChunkInfo>();
		chunkCountAzure=new HashMap<String,Integer>();
	}
	
	
}
