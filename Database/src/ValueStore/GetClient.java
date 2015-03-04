package ValueStore;
/**
 * Thread to get tuple
 * @author Yan Wang & Zishan Qin
 *
 */
public class GetClient implements Runnable{
	   private Thread getThread;
	   private String threadName;
	   private ValueStore valueStore;
	   private int key;
	   
	   GetClient( String name, ValueStore valueStore, int key){
	       threadName = name;
	       this.valueStore = valueStore;
	       this.key = key;
	       getThread = new Thread (this, threadName);
	       getThread.start ();
	   }
	   public void run() {
        	byte [] getValue = valueStore.get(key);
		}
         
}
