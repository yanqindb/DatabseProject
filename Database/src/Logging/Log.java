package Logging;

import java.util.ArrayList;

public class Log {
	ArrayList<LogItem> log;
	public Log(){
		log=new ArrayList<LogItem>();
	}
	public void addLog(LogItem l){
		log.add(l);
	}
	public String toString(){
		  String s = "";
		  for(LogItem l: log){
			  s+=l.toString();
		  }
	return s;
	}
}
class LogItem {
	String TransactionID;
	String fileds;
	Record oldFields;
	Record newFields;
	LogItem(String str, String f, Record o, Record n){
		TransactionID=str;
		fileds=f;
		oldFields=o;
		newFields=n;
	}
	public String toString(){
		  String oldF="";
		  if(oldFields!=null){
			  oldF=oldFields.toString();
		  }
		  String s = TransactionID+":"+fileds+":"+oldF+":"+newFields.toString()+"\n";
	return s;
	}
}