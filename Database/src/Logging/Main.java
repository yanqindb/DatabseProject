package Logging;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
public  static Relation loadFile( String tabelName, Log log) {
		ObjectInputStream ois = null;
		BufferedReader reader = null;
		//Get the path of the relation
		Path pathRelation = Paths.get(tabelName);

		//Open the relation file
		Relation relation = new Relation(tabelName);
		try {		 
			Charset charset=Charset.forName("UTF-8");;
			reader = Files.newBufferedReader(pathRelation, charset);
			
			relation.open();
			InsertTransaction insertT;
			String tempString;
			while ((tempString = reader.readLine()) != null) {
				insertT=new InsertTransaction(relation, tempString);
				LogItem item=new LogItem(insertT.TransactionID,"",null,insertT.getRecord() ); 
				log.addLog(item);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			return null;
		}
		
		
		return relation;
		
	}
	public static boolean loadLog() {
		// TODO Auto-generated method stub
		return true;
	}
	public void AnnualIncreaseTrigger(Relation relation, int FieldIndex){
	
		
		
	}
	public static void main(String[] args) {
		
		//Relation city
		Log log=new Log();
		Relation city = null ;
		city=loadFile("City.csv", log);
		if(city==null){
			loadLog();
		}
		System.out.println(log);
		
	}
	

}
