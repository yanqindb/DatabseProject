package ValueStore;
/**
 * Thread to put tuple
 * @author Yan Wang & Zishan Qin
 *
 */
public class PutClient implements Runnable{
	   private Thread putThread;
	   private String threadName;
	   private ValueStore valueStore;
	   private int key;
	   private byte [] value;
	   
	   PutClient( String name, ValueStore valueStore, int key, byte [] value){
	       threadName = name;
	       this.valueStore = valueStore;
	       this.key = key;
	       this.value = value;
	       putThread = new Thread (this, threadName);
	       putThread.start ();

	   }
	   public void run() {
        	valueStore.put(key, value);
	   }

}
