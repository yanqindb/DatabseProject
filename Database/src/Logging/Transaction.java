package Logging;

import java.util.ArrayList;
import java.util.UUID;
/**
 * This is the class defining Transaction
 * Currently, we have two type of Transactions:Insert and Update
 * @author Yan Wang & Zishan Qin
 *
 */
public abstract class Transaction {
	/*Transaction Type*/
	String TransactionType;
	/*Transaction unique ID*/
	String TransactionID;
	/*ID generating: Concatenation of type and a uniqueID*/
	public void generateID(){
		this.TransactionID=this.TransactionType+"_"+UUID.randomUUID().toString();
	}

}
/**
 * Insert Transaction
 * Insert a record to specific relation
 * @author Yan Wang & Zishan Qin
 *
 */
class InsertTransaction extends Transaction{
	/**Which relation to insert*/
	Relation relationInsertedTo;
	/**the inserted record*/
	Record record;
	
	/**
	 * constructor of Insert transaction
	 * @param r: which relation to insert
	 * @param tempString: String need to be parsed to record
	 */
	public InsertTransaction(Relation r, String tempString){
		relationInsertedTo=r;
		TransactionType="Insert";
		generateID();
		ParseRecord(tempString);
		insertRecord();
	}
	/**
	 *constructor of Insert transaction
	 * @param r: which relation to insert
	 * @param fields already splitted
	 */
	public InsertTransaction(Relation relation, String[] split) {
		/* relation ready to insert*/
		relationInsertedTo=relation;
		/* Insert Type*/
		TransactionType="Insert";
		/* generating the ID for this transaction*/
		generateID();
		/*Create the record*/
		record=new Record(split);
		/*insert the record*/
		insertRecord();
	}
	/**
	 * Use Regular expression to get the fields
	 * @param tempString
	 */
	public void ParseRecord(String tempString){
		 record=new Record(tempString.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
	}
	/**
	 * insert this record to the relation
	 */
	public void insertRecord() {
		this.relationInsertedTo.addRecord(record);
		
	}
	/**
	 * 
	 * @return the record parsed
	 */
	public Record getRecord(){
		return this.record;
	}
}
/**
 * Update Transaction
 * update a record of one specific relation
 * @author Yan Wang & Zishan Qin
 *
 */
class UpdateTransaction extends Transaction{
	/*Which reation to update*/
	Relation relationUpdatedTo;
	/*which row of update*/
	int  updateRow;
	/*values of update value*/
	ArrayList updateValues;
	/*columns to update*/
	ArrayList<Integer> updateColumns;
	
	/**
	 * Constructor
	 * @param r
	 * @param updateC
	 * @param updateR
	 * @param updateV
	 */
	public UpdateTransaction(Relation r,ArrayList updateC, int updateR, ArrayList updateV){
		
		relationUpdatedTo=r;
		TransactionType="Update";
		generateID();
		this.updateColumns=updateC;
		this.updateValues=updateV;
		this.updateRow=updateR;
		updateRecord();
	}
	/**
	 * Modify corresponding fields
	 * 
	 */
	public void updateRecord() {	
		Record r=this.relationUpdatedTo.records.get(this.updateRow);
		for(int i=0;i<this.updateColumns.size();i++)
		r.setValue(this.updateColumns.get(i), this.updateValues.get(i));
		
	}
	
}
/**
 * CreateTable Transaction
 * Create a relation
 * @author Yan Wang & Zishan Qin
 *
 */
class CreateTableTransaction extends Transaction{
	
	/*Which reation to create*/
	Relation relationCreate;
	/*Relation name*/
	String name;
	/*attributes of the table*/
	ArrayList<String> attributes=new ArrayList<String>();
	
	
	/**
	 * Constructor
	 * @param att:attributes
	 */
	public CreateTableTransaction(ArrayList<String>  att, String nam){
		TransactionType="Create";
		this.generateID();
		this.attributes=att;
		this.name=nam;
		createRelation();
	}
	/**
	 * Create Relation
	 * 
	 */
	public void createRelation() {	
		this.relationCreate=new Relation(this.name);
		for(String str:this.attributes){
			this.relationCreate.fields.add(str);
		}
		
	}
	public Relation getRelation(){
		return this.relationCreate;
	}
}