package csci4180;


import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.charset.StandardCharsets;

class RFP{
	public static int min_size,q,d,dd,max_size,mask,cur_rabin,cur_size,cur_loc,cur_first,length,last_loc;
	public static byte[] first;
	public static byte[] buf;
	public static FileInputStream fin;
	
	public RFP(int min_size,int avg_size,int max_size,int d){
		this.min_size=min_size;
		this.q=avg_size;
		this.max_size=max_size;
		this.d=d;
		this.dd=(int)Math.pow((double)d,(double)min_size-1);
		this.mask=this.q-1;
		this.cur_rabin=0;
		this.cur_size=0;
		this.cur_loc=0;
		this.cur_first=0;
		this.length=0;
		this.last_loc=0;
		this.first=new byte[min_size];
		this.buf=new byte[max_size];
	}
	
	public void load_data(String path) throws FileNotFoundException,IOException{
		File f=new File(path);
		length=(int)f.length();
		fin=new FileInputStream(f);
		fin.read(first);
	}
	
	public int rabin_first(){
		int i;
		for(i=min_size-1;i>0;i--){
			cur_rabin+=d*(((int)first[i]& 0xFF)+cur_rabin);
		}
		cur_rabin=cur_rabin % q;
		cur_rabin=cur_rabin>=0?cur_rabin:cur_rabin+q;
		cur_first=(int)first[0]& 0xFF;
		System.out.println(cur_rabin+" "+cur_first+" "+dd+" "+cur_loc);
		return cur_rabin;
	}
	
	public int rabin(byte[] a){
		int i;
		//System.out.println(""+d*(cur_rabin-dd*cur_first)+" "+((int)a[0]&0xFF)+" "+(new String(a, StandardCharsets.UTF_8))+" "+cur_rabin+" "+cur_size+" "+(new String(buf, StandardCharsets.UTF_8))+" "+(cur_loc+min_size+1)+" "+cur_loc+" "+length+" "+cur_size);
		cur_rabin=(d*(cur_rabin-dd*cur_first)+((int)a[0]&0xFF)) % q;
		cur_rabin=cur_rabin>=0?cur_rabin:cur_rabin+q;
		cur_first=(int)first[0]& 0xFF;
		for(i=0;i<min_size-1;i++){
			first[i]=first[i+1];
		}
		first[min_size-1]=a[0];
		return cur_rabin;
	}
	
	public byte[] chunking() throws IOException{
		System.out.println("chunking..."+(cur_loc+min_size+1));
		int tmp=0;
		byte[] one=new byte[1];
		if(cur_loc+min_size+1>=length && cur_loc<=length){
			byte[] result=new byte[length-cur_loc];
			fin.read(result);
			cur_loc=length+1;
			return(result);
		}
		else if(cur_loc+min_size+1>=length){
			return new byte[0];
		}
		while(cur_loc+min_size+1<length){
			while(cur_size<max_size && cur_loc+min_size+1<length){
				//System.out.println(cur_rabin);
				//System.out.println(new String(one, StandardCharsets.UTF_8));
				
				if(cur_loc==0){
					tmp=rabin_first();
					cur_size=min_size;
					cur_loc=min_size;
				}
				else{
					fin.read(one);
					tmp=rabin(one);
					cur_size++;
					cur_loc++;
				}
				if((tmp & mask)==0){
					buf[cur_size-1]=one[0];
					last_loc=cur_loc+min_size;
					cur_loc=last_loc;
					byte[] result;
					int i;
					if(cur_loc+min_size<length){
						result=new byte[cur_size+min_size];
						for(i=0;i<cur_size;i++){
							result[i]=buf[i];
						}
						for(i=0;i<min_size;i++){
							fin.read(one);
							result[i+cur_size]=one[0];
						}
					}
					else{
						result=new byte[cur_size+(length-cur_loc+1)];
						for(i=0;i<cur_size;i++){
							result[i]=buf[i];
						}
						for(i=0;i<length-cur_loc+1;i++){
							fin.read(one);
							result[i+cur_size]=one[0];
						}
						
					}
					cur_size=0;
					return result;
				}
				else if(cur_loc!=min_size){
					buf[cur_size-1]=one[0];
				}
				else{
					int i=0;
					for(i=0;i<min_size;i++){
						buf[i]=first[i];
					}
				}
				if(cur_size+min_size==max_size){
					byte[] result=new byte[max_size];
					int i;
					for(i=0;i<min_size;i++){
						fin.read(one);
						buf[cur_size+i]=one[0];
						rabin(one);
						cur_loc++;
					}
					cur_size=0;
					last_loc=cur_loc;
					for(i=0;i<max_size;i++){
						result[i]=buf[i];
					}
					buf=new byte[max_size];
					return result;
				}
				if(cur_loc+min_size+1==length){
					byte[] result=new byte[cur_size];
					fin.read(result);
					return(result);
				}
			}
		}
		return new byte[0];
	}
}