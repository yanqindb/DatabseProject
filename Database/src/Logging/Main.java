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

import sun.util.logging.resources.logging;

public class Main {
	/**
	 *  read the file of data, and create relation
	 * @param tableName
	 * @param fs: fields name
	 * @param log: need to write logs into this
	 * @return relation created
	 */
public  static Relation loadFile( String tableName, ArrayList<String> fs, Log log) {
		ObjectInputStream ois = null;
		BufferedReader reader = null;
		//Get the path of the relation
		Path pathRelation = Paths.get(tableName);

		//Open the relation file
		CreateTableTransaction creatT = new CreateTableTransaction(fs,tableName);
		Relation relation=creatT.getRelation();
		ArrayList<String> tableStructure=new ArrayList<String>(fs);
		tableStructure.add(0,relation.relationName);
		String structure="";
		for(String str:tableStructure ){
			structure+=str+"/";
		}
		LogItem createTtem=new LogItem(creatT.TransactionID,structure,null, null); 
		log.addLog(createTtem);
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
	/**
	 * function to multiply the population value by 1.02
	 * these are update related transactions
	 * @param relation
	 * @param FieldIndex
	 * @param log
	 */
	public static void AnnualIncreaseTrigger(Relation relation, int FieldIndex, Log log){
		System.out.println("increase population by 2%");
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
	
	public static void testBasic() {
		//information needed for table City
		String LogLacation="Log_f9428c84-b103-4080-bcec-a6dc45d35adb.txt";
		String DatabaseLocation="CityTest.csv";
		ArrayList<String> fields=new ArrayList<String>();
		  String[] fs={"Code","Name","Country Code","District","Population"};
		for(int i=0;i<fs.length;i++){
			fields.add(fs[i]);
		}
		int increaseColoumn=4;
		//Do operations:
		 // Two cases:
			 // case 1:if data in DatabaseLocation cannot be found, we can regarded this is a isolated machine and 
			 // need to build relation from log
			 // case 2: if data in DatabaseLocation can be found, we can build relation from data.
			 // 		for this case, we need to create table, insert records, update by multiplying 1.02, and write logs
			 // 		for all these operations
		System.out.println("########################## Begin Operations On CityTest #####################"); 
		oprations(LogLacation, "CityTest",DatabaseLocation, fields, increaseColoumn);
		DatabaseLocation="notavailablenow";
		oprations(LogLacation, "CityTest",DatabaseLocation, fields, increaseColoumn);
//		 
//		 
//		//information needed for table Country
//		 LogLacation="Log_8213af0e-2f72-40a0-bbf9-2201bff18424.txt";
//		 DatabaseLocation="CountryTest1.csv";
//		 fields.clear();
//		 String fs2[]={"ID","Name","Continent","Region","SurfaceArea"
//					,"IndepYear","Population","LifeExpectancy", "GNP","GNPOld","LocalName","GovernmentForm",
//					"HeadOfState","Capital","Code2"};
//		
//			for(int i=0;i<fs2.length;i++){
//				fields.add(fs2[i]);
//			}
//		 increaseColoumn=6;
//		 System.out.println("############################### Begin Operations On CountryTest ########################"); 
//		 oprations(LogLacation,"CountryTest", DatabaseLocation, fields, increaseColoumn);
		
	}
	/**
	 * Two cases:
	 * case 1:if data in DatabaseLocation cannot be found, we can regarded this is a isolated machine and 
	 * need to build relation from log
	 * case 2: if data in DatabaseLocation can be found, we can build relation from data.
	 * 		for this case, we need to create table, insert records, update by multiplying 1.02, and write logs
	 * 		for all these operations
	 * @param LogLacation
	 * @param recoverName
	 * @param DatabaseLocation
	 * @param fields
	 * @param increaseColoumn: which 
	 */
	public static void oprations(String LogLacation, String recoverName, String DatabaseLocation,
			ArrayList<String> fields, int increaseColoumn) {
		Path p=Paths.get(DatabaseLocation);
		if(Files.exists(p)){
			System.out.println("Build Relation from data");
		Log log = transactionsForTable(DatabaseLocation, fields, increaseColoumn);
		writeLog(log);
		System.out.println("Log "+log.getName()+" is generated");
		}
		//System.out.println(log.toString());
		else{
			System.out.println("Build Relation from log");
		recoverRelation(LogLacation,recoverName);
		}
	}
	/**
	 * Recover data from log
	 * @param location
	 * @param relationName
	 */
	public static void recoverRelation(String location,String relationName) {
		
		RecoveryBasedOnLog recovery=new RecoveryBasedOnLog(location, relationName);
		recovery.recovery();
		System.out.println("Relation "+recovery.relation.relationName+" is recovered successfully from "+location);
	}
	/**
	 * write the log into file system
	 * @param log
	 */
	public static void writeLog(Log log) {
		try {
		      //create a buffered reader that connects to the console, we use it so we can read lines
				FileWriter fw = new FileWriter(log.getName()+".txt");
				fw.write(log.toString());
				fw.close();
		   }
		      catch(IOException e1) {
		        System.out.println("Error during reading/writing");
		   }
	}
	/**
	 * Create a relation for data in tableLocation, and update the population field by multiplying 1.02
	 * @param tableLocation
	 * @param fields
	 * @param column
	 * @return log 
	 */
	public static Log transactionsForTable(String tableLocation,ArrayList<String> fields, int column) {
		Log log=new Log();
		Relation r=loadFile(tableLocation, fields, log);
		AnnualIncreaseTrigger(r,column,log);
		return log;
	}
	public static void main(String[] args){
		System.out.println("############################### Extra work ##################################"); 
		//testBasic();
		//String LogLacation="Log_6da543ca-7c0d-4308-87a3-4280de60abf0.txt";
		String DatabaseLocation="CityTest.csv";
		int increaseColoumn=4;
		Log log=new Log();
		ObjectInputStream ois = null;
		BufferedReader reader = null;
		//Get the path of the relation
		Path pathRelation = Paths.get(DatabaseLocation);
		Relation relation =new Relation(DatabaseLocation);		
		relation.open();
		InsertTransaction insertT;
		String tempString;
		System.out.println("############################### Begin Operations On CityTest #####################"); 
		try{
			Charset charset=Charset.forName("UTF-8");;
			reader = Files.newBufferedReader(pathRelation, charset);
			while ((tempString = reader.readLine()) != null) {
				insertT=new InsertTransaction(relation, tempString);
				
			}
			AnnualIncreaseTrigger(relation, increaseColoumn, log);
			writeLog(log);
			System.out.println("Log "+log.getName()+" is generated");
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
		}
		 DatabaseLocation="CountryTest.csv";
		 increaseColoumn=6;
		 log=new Log();
		ObjectInputStream ois1 = null;
		BufferedReader reader1 = null;
		//Get the path of the relation
		Path pathRelation1 = Paths.get(DatabaseLocation);
		Relation relation1 =new Relation(DatabaseLocation);		
		relation1.open();
		InsertTransaction insertT1;
		String tempString1;
		System.out.println("############################### Begin Operations On CountryTest #####################"); 
		try{
			Charset charset=Charset.forName("UTF-8");;
			reader1 = Files.newBufferedReader(pathRelation1, charset);
			while ((tempString1 = reader1.readLine()) != null) {
				insertT1=new InsertTransaction(relation1, tempString1);
				
			}
			AnnualIncreaseTrigger(relation1, increaseColoumn, log);
			writeLog(log);
			System.out.println("Log "+log.getName()+" is generated");
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
		}
		
	}
	
	
}
