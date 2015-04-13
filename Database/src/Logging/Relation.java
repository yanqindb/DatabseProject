package Logging;

import java.util.ArrayList;

public class Relation<Record> {
	ArrayList<Record> records;
	public void addRecord(Record r){
		this.records.add(r);
	}
}
class Record {
	ArrayList value;
	ArrayList name;
	public Record(ArrayList v, ArrayList n){
		value.addAll(v);
		name.addAll(n);
	}
}
