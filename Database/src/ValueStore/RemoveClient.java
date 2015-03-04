package ValueStore;

/**
 * Thread to remove tuple
 * @author Yan Wang & Zishan Qin
 *
 */
public class RemoveClient implements Runnable{
	   private Thread removeThread;
	   private String threadName;
	   private ValueStore valueStore;
	   private int key;
	   
	   RemoveClient( String name, ValueStore valueStore, int key){
	       threadName = name;
	       this.valueStore = valueStore;
	       this.key = key;
	       removeThread = new Thread (this, threadName);
	       removeThread.start ();
	   }
	   public void run() {
        	valueStore.remove(key);
	   }
	  

}

