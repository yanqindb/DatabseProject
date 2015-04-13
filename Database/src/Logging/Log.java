package Logging;

import java.util.ArrayList;

public class Log {
	ArrayList<LogItem> log;
	public void addLog(LogItem l){
		log.add(l);
		}
}
class LogItem<Record> {
	String TransactionID;
	String fileds;
	Record oldFields;
	Record newFields;
	LogItem(String str, String f, Record o, Record n){
		
	}
}