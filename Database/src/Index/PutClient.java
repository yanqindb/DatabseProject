package Index;
/**
 * Thread to put tuple
 * @author Yan Wang & Zishan Qin
 *
 */
public class PutClient <Key extends Comparable<Key>, Value>implements Runnable{
	   private Thread putThread;
	   private String threadName;
	   private Key key;
	   private Value value;
	   private BTree btr;
	   
	   PutClient( String name, BTree btree, Key key, Value value){
	       threadName = name;
	       this.key = key;
	       this.value=value;
	       putThread = new Thread (this, threadName);
	       putThread.start ();

	   }
	   public void run() {
			btr.put(key,value);	
        		System.out.println(threadName+"  put value"+value+" with key"+ key);
        	
	   }

}
