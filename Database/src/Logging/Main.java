package Logging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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
	public static void AnnualIncreaseTrigger(Relation relation, int FieldIndex, Log log){
		double factor=1.02;
		int recordsSize=relation.records.size();
		int count=0;
		Record r;
		UpdateTransaction updateT;
		while(count<recordsSize){
			
			r=relation.records.get(count);
			Record oldRec=new Record(r);
			String oldValue= ((String) r.value.get(FieldIndex)).replaceAll("/", "");
			double old=Double.parseDouble(oldValue);
			int newV=(int) Math.round(old*factor);
			String newValue=""+newV+"";
			ArrayList updateColumns=new ArrayList();
			updateColumns.add(FieldIndex);
			ArrayList updateValues=new ArrayList();
			updateValues.add(newValue);
			updateT=new UpdateTransaction(relation,updateColumns ,count, updateValues);
			LogItem item=new LogItem(updateT.TransactionID,relation.relationName+"/"
					+count+"/"+FieldIndex,oldRec,r); 
			log.addLog(item);
			count++;
		}
		
		
	}
	public static void main(String[] args) {
		
		//Relation city
		Log log=new Log();
		Relation city = null ;
		city=loadFile("CityTest.csv", log);
		if(city==null){
			loadLog();
		}
		
		
		AnnualIncreaseTrigger(city,4,log);
		
		//System.out.println(log.toString());
		try {
		      //create a buffered reader that connects to the console, we use it so we can read lines
				FileWriter fw = new FileWriter("file.txt");
				fw.write(log.toString());
				fw.close();
		   }
		      catch(IOException e1) {
		        System.out.println("Error during reading/writing");
		   }
		RecoveryBasedOnLog recovery=new RecoveryBasedOnLog("file.txt", "CityRecovery", "CityRecovery1");
		recovery.recovery();
		
	}
	

}
