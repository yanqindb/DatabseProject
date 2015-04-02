package QueryExecution;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is used to create a relation
 * @author Yan Wang & Zishan Qin
 *
 */
class Relation{
	
	/*
	 * store is the List to store tuple
	 */
	private List <String []> block;

	//Relation name
	private String relationName = null;
	
	//Length of the relation in bytes
	private int length = 0;
	
	//Path of the relation
	Path pathRelation = null;
	Charset charset = Charset.forName("UTF-8");
	
	//Get reader and write handle of the relation
	BufferedReader reader = null;
	BufferedWriter writer = null;
	
	//In the current block, point to the index of the tuple to be read
	int nextId = 0;
	
	//Suppose each block contains 10 records
	int blockSize = 10;
	

	/**
	 * Create a relation table, and store it in disk
	 * @param relationName
	 */
	public Relation(String relationName){
		
		this.relationName = relationName;
		ObjectInputStream ois = null;

		//Get the path of the relation
		pathRelation = Paths.get(relationName);
		
		//Open the relation file
		if (new File(relationName).exists()){
		try {
			reader = Files.newBufferedReader(pathRelation, charset);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
		else{
	
		//Create an empty file in disk named relationName if the file doesn't exist
			ObjectOutputStream oos = null;
			try {
	            File file = new File(relationName);
			} catch (Exception e) {
			}
		}
	}
	/**
	 * Put a tuple
	 */
	public void put(String record){
		
		try {
			//writer = Files.newBufferedWriter(new FileWriter(relationName, true), charset);
			writer = new BufferedWriter(new FileWriter(relationName, true));
			
		    writer.append(record);
		    writer.newLine();

		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Open and read in numBlocks blocks
	 * @param numBlocks Number of blocks that can be read in memory
	 * @return
	 */
	public int open(int numBlocks){
		//store the blocks that are read in
		block = new ArrayList<String []> ();
		nextId = 0;
		if(reader!=null){
			String line = null;
		    try {
		    	// According the capability of memory, we can read in numBlocks blocks
		    	for (int j = 0;j<numBlocks;j++){
		    		//Read in each block
			    	for (int i = 0;i<blockSize;i++){
						if ((line = reader.readLine()) != null) {
							//Seperate each attribute by comma, and ignore comma in quote
							String [] record = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
							block.add(record);
						}
						else 
						{
							//If reading comes to an end
							return 1;
						}
			    	}
		    	}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	/**
	 * Get the next tuple in block
	 * @return
	 */
	public String [] getNext(){
		String [] nextRecord;
		if(nextId<block.size()){
			nextRecord = block.get(nextId);
			nextId = nextId+1;
		}
		else
		{
			nextRecord=null;
		}
		
		return nextRecord;
	}
	
	/**
	 * Point to beginning of the block
	 */
	public void resetNext(){
		nextId = 0;
	}
	
	/**
	 * Point to beginning of the relation
	 */
	public void resetBlock(){
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader = Files.newBufferedReader(pathRelation, charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Close the relation
	 */
	public void close(){
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
