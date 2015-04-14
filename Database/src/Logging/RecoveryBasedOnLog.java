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
	String locationToSave;
	public RecoveryBasedOnLog(String logLocation, String relationName, String Relationlocation){
		//log=l;
		ObjectInputStream ois = null;
		BufferedReader reader = null;
		//Get the path of the relation
		Path pathRelation = Paths.get(logLocation);
		log=new ArrayList<String>();
		
		relation = new Relation(relationName);
		relation.open();
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
		locationToSave=Relationlocation;
	}
	public void parseLogToTransaction(String i){
		String[] fields=i.split(":");
		String Type=fields[0].split("_")[0];
		String fieldsUpdate=fields[1];
		String oldValue=fields[2];
		String newValue=fields[3];
		if(Type.equals("Insert")){
			
			InsertTransaction insertT=new InsertTransaction(relation, newValue.split("\\"));
		}
		else if(Type.equals("Update")){
			String[] updateLocation=fieldsUpdate.split("\\\\");
			String[] updateValues=newValue.split("\\\\");
			int row=Integer.parseInt(updateLocation[0]);
			ArrayList<Integer>  columns=new ArrayList<Integer>();
			for(int j=1;j<updateLocation.length;j++){
				columns.add(Integer.parseInt(updateLocation[j]));
			}
			ArrayList  values=new ArrayList();
			for(int j=0;j<columns.size();j++){
				values.add(updateValues[columns.get(j)]);
			}
			
			UpdateTransaction updateT=new UpdateTransaction(relation, columns, row, values);
		}
	}
	public void recovery(){
		for(String i: log){
			parseLogToTransaction(i);
		}
	}
	
}
