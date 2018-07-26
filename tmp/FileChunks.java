package mydedup;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Math.*;

class FileChunks{
	public String filePath;
	private FileInputStream fileStream;
	private int min_size;
	private int max_size;
	private int avg_size;
	private int d;
	private int offset;
	private int last_anchor_point;
	private int fingerPrint;
	BufferedInputStream byteFile;
	//public HashMap<> chunks;

	public FileChunks(){
		this.filePath="";
		this.fileStream = null;
		this.offset = 0;
		//this.chunks = new HashMap<>

	}

	public FileChunks(String inputName,int min, int avg, int max, int d){
		this.filePath = inputName;
		this.fileStream = null;
		this.min_size = min;
		this.max_size = max;
		this.avg_size = avg;
		this.d = d;
		this.offset = 0;
		this.last_anchor_point = 0;
		this.fingerPrint = 0;
		try {
			this.fileStream = new FileInputStream(this.filePath);
			this.byteFile = new BufferedInputStream(this.fileStream);
		}
		catch (FileNotFoundException e){
			System.err.println("Error: Can not open file: " + filePath);
			System.err.println("========================");
			e.printStackTrace();
			System.exit(1);
		}
		//System.out.println(new String(b, 0));	
	}

	public Chunks get_chunks(){
		int rfp = 0;
		ArrayList storage = new ArrayList();
		int index = 0;
		int maxPowMode = modExpOpt(this.d, this.min_size-1, this.avg_size);
		int q = this.avg_size;
		int mask = this.avg_size-1;

		try{
			int b;
			while((b=byteFile.read())!=-1){
				//System.out.println(b);
				if(index < this.min_size){
					storage.add((byte)b);
					int tmp = ((b%q)*modExpOpt(this.d, this.min_size-index-1,q))%q;
					rfp = (tmp+rfp)%q;
					index ++;
					//System.out.println("index: "+ index);
				}
				else if(index >= this.min_size && (rfp & mask) != 0){
					storage.add((byte)b);
					int tmp = (rfp - (maxPowMode*((byte)storage.get(index-this.min_size)%q)))%q;

					rfp = (((d%q)*tmp)%q+(byte)storage.get(index)%q)%q;
					index ++;
				}

				if (index >= this.min_size && (rfp & mask) == 0){
					byte[] chunks = new byte[storage.size()];
					for(int i = 0; i < storage.size(); i++) {
						chunks[i] = (byte)storage.get(i);
					}
					//System.out.println(new String(chunks, 0));
					//System.out.println("\n");
					Chunks chunk = new Chunks(chunks);
					return chunk;
				}
				else if(index == this.max_size){
					byte[] chunks = new byte[storage.size()];
					for(int i = 0; i < storage.size(); i++) {
						chunks[i] = (byte)storage.get(i);
					}
					//System.out.println(new String(chunks, 0));
					//System.out.println("\n");
					Chunks chunk = new Chunks(chunks);
					return chunk;
				}
			}

			//
			if (storage.size() > 0){	
				byte[] chunks = new byte[storage.size()];
				for(int i = 0; i < storage.size(); i++) {
					chunks[i] = (byte)storage.get(i);
				}
				//System.out.println(new String(chunks, 0));
				Chunks chunk = new Chunks(chunks);
				return chunk;
			}
		}
		catch(Exception e){
			System.out.println("Error:"+e);
		}
		return null;
	}
	public static int modExpOpt( int d, int e, int q ) {
		// Compute d^e mod q
		int result = 1;
		while( e > 0 ) {
			if ( ( e & 1 ) == 1 )
				result = ( result * d ) % q;
			e = ( e - ( e & 1 ) ) >> 1;
			d = ( d * d ) % q;
		}
		return result;
	}
}