package Logging;

public abstract class Transaction {
	String TransactionType;
	

}
class InsertTransaction extends Transaction{
	public void setType(String t){
		TransactionType=t;
	}
}
class UpdateTransaction extends Transaction{
	public void setType(String t){
		TransactionType=t;
	}
}