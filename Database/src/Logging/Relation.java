package Logging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;





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
	

	//Relation name
	public String relationName;
	
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
	
	/*records in relation*/
	ArrayList<Record> records;
	
	/*fields name*/
	ArrayList<String> fields;
	
	/**
	 * Create a relation table, and store it in disk
	 * @param relationName
	 */
	public Relation(String relationN){
		
		this.relationName = relationN.split("\\.")[0];
		this.fields=new ArrayList<String>();
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
	@SuppressWarnings("unchecked")
	public void open(){
		ObjectInputStream ois = null;

		//Get the path of the relation
		pathRelation = Paths.get(relationName);
		this.records=new ArrayList<Record>();
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
	 * Get the next tuple in block
	 * @return
	 */
	
	public Record getNext(){
			Record nextRecord;
			nextRecord =this.records.get(nextId);
			nextId = nextId+1;
			return nextRecord;
	}
	
	/**
	 * Point to beginning of the block
	 */
	public void resetNext(){
		nextId = 0;
	}
	
	
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
	/**
	 * add a record to relation
	 * @param r
	 */
	public void addRecord(Record r){
		this.records.add(r);
	}
	/**
	 * writing helper function
	 */
	public String toString(){
		String s="";
		if(this.records.isEmpty()==false){
		for(Record r:this.records){
			for(int i=0;i<r.value.size();i++){
				if(i<r.value.size()-1)
					s+=r.value.get(i)+",";
				else{
					s+=r.value.get(i);
				}
			}
			s+="\n";
		}
			
		}
		return s;
	}
	/**
	 * wrtite the relation to file system
	 */
	public void writeBack(){

		try {
		      //create a buffered reader that connects to the console, we use it so we can read lines
				FileWriter fw = new FileWriter("Recovery of "+this.relationName+".txt");
				fw.write(this.toString());
				fw.close();
		   }
		      catch(IOException e1) {
		        System.out.println("Error during reading/writing");
		   }
	}

}
/**
 * Definition Record class
 * @author Yan Wang & Zishan Qin
 *
 */
class Record {
	/*columns of this record*/
	ArrayList value;
	
	/**
	 * Constructor 1: build record directly from another record
	 * @param externalRecord
	 */
	public Record(Record externalRecord){
		value=(ArrayList) externalRecord.value.clone();
	}
	/**
	 * Constructor 2: build record from string array
	 * @param externalRecord
	 */
	public Record(String[] split) {
		// TODO Auto-generated constructor stub
		value=new ArrayList();
		for(int i=0;i<split.length;i++){
			value.add(split[i]);
		}
	}
	/**
	 * set some field value to value of o
	 * @param col: column index
	 * @param o: new value
	 */
	public void setValue(int col, Object o){
		this.value.remove(col);
		this.value.add(col,o);
	}
	/**
	 * output helper
	 */
	public String  toString(){
		String s = "";
		for(Object o: value){
			s+=o.toString()+"/";
		}
		return s;
	}
	
}
