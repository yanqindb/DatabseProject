package Logging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class RecoveryBasedOnLog {
	//Log log;
	ArrayList<String> log;
	Relation relation;
	//String locationToSave;
	public RecoveryBasedOnLog(String logLocation, String relationName, Relation r){
		//log=l;
		ObjectInputStream ois = null;
		BufferedReader reader = null;
		//Get the path of the relation
		Path pathRelation = Paths.get(logLocation);
		log=new ArrayList<String>();
		
		relation = r;
		//relation.open();
		try {		 
			Charset charset=Charset.forName("UTF-8");;
			reader = Files.newBufferedReader(pathRelation, charset);
			String tempString;
			while ((tempString = reader.readLine()) != null) {
				this.log.add(tempString);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			
		}
		//locationToSave=Relationlocation;
	}
	public RecoveryBasedOnLog(String logLocation, String relationName){
		//log=l;
		ObjectInputStream ois = null;
		BufferedReader reader = null;
		//Get the path of the relation
		Path pathRelation = Paths.get(logLocation);
		log=new ArrayList<String>();
		
		//relation = new Relation(relationName);
		//relation.open();
		try {		 
			Charset charset=Charset.forName("UTF-8");;
			reader = Files.newBufferedReader(pathRelation, charset);
			String tempString;
			while ((tempString = reader.readLine()) != null) {
				this.log.add(tempString);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			
		}
		//locationToSave=Relationlocation;
	}
	public void parseLogToTransaction(String i){
		String[] fields=i.split(":");
		String Type=fields[0].split("_")[0];
		String fieldsUpdate=fields[1];
		
		
		if(Type.equals("Create")){
			String[] fieldsName=fieldsUpdate.split("/");
			ArrayList<String> fs=new ArrayList<String>();
			for(int j=1;j<fieldsName.length;j++){
				fs.add(fieldsName[j]);
			}
			CreateTableTransaction createT=new CreateTableTransaction(fs, fieldsName[0]);
			this.relation=createT.getRelation();
			relation.open();
		}
		else if(Type.equals("Insert")){
			try{
				String oldValue=fields[2];
				String newValue=fields[3];
			if(this.relation==null){
				throw new Exception("Relation doesn't exist");
			}
			InsertTransaction insertT=new InsertTransaction(relation, newValue.split("/"));
			}catch(Exception e ){
				
			}
		}
		else if(Type.equals("Update")){
			try{
				String oldValue=fields[2];
				String newValue=fields[3];
				if(this.relation==null){
					throw new Exception("Relation doesn't exist");
				}
			String[] updateLocation=fieldsUpdate.split("/");
			String[] updateValues=newValue.split("/");
			int row=Integer.parseInt(updateLocation[1]);
			ArrayList<Integer>  columns=new ArrayList<Integer>();
			for(int j=2;j<updateLocation.length;j++){
				columns.add(Integer.parseInt(updateLocation[j]));
			}
			ArrayList  values=new ArrayList();
			for(int j=0;j<columns.size();j++){
				values.add(updateValues[columns.get(j)]);
			}
			
			UpdateTransaction updateT=new UpdateTransaction(relation, columns, row, values);
			}catch(Exception e ){
				
			}
		}
	}
	public void recovery(){
		for(String i: log){
			parseLogToTransaction(i);
		}
		this.relation.writeBack();
	}
	
}
