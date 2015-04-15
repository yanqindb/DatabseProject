package Logging;

import java.util.ArrayList;
import java.util.UUID;
/**
 * Define the Log structure
 * @author Yan Wang & Zishan Qin
 *
 */
public class Log {
	/*composed of logitem list*/
	ArrayList<LogItem> log;
	/*unique name of this log*/
	String logName="Log_"+UUID.randomUUID();
	
	/**
	 * Constructor
	 */
	public Log(){
		log=new ArrayList<LogItem>();
	}
	
	/**
	 * add a log item
	 * @param l
	 */
	public void addLog(LogItem l){
		log.add(l);
	}
	
	/**
	 * output helper function
	 */
	public String toString(){
		  String s = "";
		  for(LogItem l: log){
			  s+=l.toString();
		  }
	return s;
	}
	/**
	 * 
	 * @return log name
	 */
	public String getName(){
		return this.logName;
	}
}
/**
 * Define the structure of log item
 * @author Yan Wang & Zishan Qin
 *
 */
class LogItem {
	/*Transaction unique ID*/
	String TransactionID;
	/*which fields need to be changed
	 * for insert :null
	 * for update: location where the records need to be changed, structure: row:column1:column2:...
	 * for create table: column name of this table
	 */
	String fileds;
	/*old value: used for undo*/
	Record oldFields;
	/*new value: used for redo*/
	Record newFields;
	
	/**
	 * Constructor
	 * @param str
	 * @param f
	 * @param o
	 * @param n
	 */
	LogItem(String str, String f, Record o, Record n){
		TransactionID=str;
		fileds=f;
		oldFields=o;
		newFields=n;
	}
	/**
	 * Output helper function
	 */
	public String toString(){
		  String oldF="";
		  String newF="";
		  if(oldFields!=null){
			  oldF=oldFields.toString();
		  }
		  if(this.newFields!=null){
			  newF=newFields.toString();
		  }
		  String s = TransactionID+":"+fileds+":"+oldF+":"+newF+"\n";
	return s;
	}
}