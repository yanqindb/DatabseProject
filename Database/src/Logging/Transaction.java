package Logging;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Transaction {
	String TransactionType;
	String TransactionID;
	public void generateID(){
		this.TransactionID=this.TransactionType+"_"+UUID.randomUUID().toString();
	}

}
class InsertTransaction extends Transaction{
	Relation relationInsertedTo;
	Record record;
	public InsertTransaction(Relation r, String tempString){
		relationInsertedTo=r;
		TransactionType="Insert";
		generateID();
		ParseRecord(tempString);
		insertRecord();
	}
	public InsertTransaction(Relation relation, String[] split) {
		// TODO Auto-generated constructor stub
		relationInsertedTo=relation;
		TransactionType="Insert";
		generateID();
		record=new Record(split);
		insertRecord();
	}
	public void ParseRecord(String tempString){
		 record=new Record(tempString.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
	}
	public void insertRecord() {
		this.relationInsertedTo.addRecord(record);
		
	}
	public Record getRecord(){
		return this.record;
	}
}
	
class UpdateTransaction extends Transaction{
	Relation relationUpdatedTo;
	//ArrayList updateColomn;
	int  updateRow;
	ArrayList updateValues;
	ArrayList<Integer> updateColumns;
	public UpdateTransaction(Relation r,ArrayList updateC, int updateR, ArrayList updateV){
		relationUpdatedTo=r;
		TransactionType="Update";
		generateID();
		this.updateColumns=updateC;
		this.updateValues=updateV;
		this.updateRow=updateR;
		updateRecord();
	}
	public void updateRecord() {	
		Record r=this.relationUpdatedTo.records.get(this.updateRow);
		for(int i=0;i<this.updateColumns.size();i++)
		r.setValue(this.updateColumns.get(i), this.updateValues.get(i));
		
	}
	
}