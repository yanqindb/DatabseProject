package Index;

import java.util.ArrayList;

/*************************************************************************
 * 
 *	Author: Zishan Qin & Yan Wang 
 *	
 *  Project2: Index
 *  
 *  Implementation choice: B+ tree
 *  
 *  Data source: NSF Research Awards Abstracts 1990-2003(ICS Department, School of Computer Science, UCI, Irvine CA, 92697, USA),
 *  part3-a list of words for indexing the bag-of-word data.
 *
 *************************************************************************/
/**
 * RemoveClient is used for simulating removinging operation generated by users
 * @param <Key>
 * @param <Value>
 */
public class RemoveClient <Key extends Comparable<Key>, Value>implements Runnable{
	   private Thread removeThread;
	   private String threadName;
	   private Key key;
	   private Value value;
	   private BTree btr;
	   private boolean contentBase=false;
	   
	   RemoveClient( String name, BTree btree, Value val){
		   threadName = name;
	       this.value = val;
	       this.btr=btree;
	       removeThread = new Thread (this, threadName);
	       removeThread.start ();
	       contentBase=true;
	   } 
	   RemoveClient( String name, BTree btree, Key key){
		   threadName = name;
	       this.key = key;
	       this.btr=btree;
	       removeThread = new Thread (this, threadName);
	       removeThread.start ();
	   }

	   public void run() {
		   if(contentBase==false){
		   if(btr.get(key)==null)
			   System.out.println(threadName+" try to remove the item"+" with key"+ key+"but not found");
		   else{
		   btr.remove(key);  	
       		System.out.println(threadName+"  remove the item"+" with key "+ key);
		   }
     
		   }
		   else{
			   ArrayList list=btr.remove(value);  	
			   if(list.size()==0)
				   System.out.println(threadName+" try to remove the item"+" with value "+ value+" but not found");
			   else{
				   System.out.println(threadName+" try to remove the item"+" with value, " +"the pairs are");
				   for(int i=0;i<list.size();i++){
					   System.out.print("Key: "+list.get(i).toString()+" Value: "+value+"    ");
				   }
				   System.out.println();
			   }
		   }
	   }
	  

}

