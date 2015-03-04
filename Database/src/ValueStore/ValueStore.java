package ValueStore;
import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * @author Yan Wang & Zishan Qin
 *
 */
class ValueStore implements Serializable{
	
	/*
	 * store is the HashMap to store tuple
	 */
	private HashMap<Integer, byte []> store;
	/*
	 * The flag to check if the method is called by the first thread
	 */
	private boolean notifyFlag = false;
	
	/**
	 * Construct based on storage in hard drive, or construct a new one
	 * @param store
	 */
	public ValueStore(HashMap<Integer, byte[]> store){
		if(store==null){
			this.store = new HashMap<Integer, byte []>();
		}
		else{
			this.store = store;
		}
		notifyFlag = false;
	}
	/**
	 * Put a tuple
	 */
	public synchronized void put(int key, byte [] data){
		/* Check if it is called firstly. If yes, no need to wait. Otherwise, wait for other threads*/
		if (notifyFlag)
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/* put in the tuple*/
		store.put(key, data);
		System.out.println("Writing key "+ key + " with first element of value as "+ data[0]);
		/* Wait for a while for other threads to be created*/
    	if(!notifyFlag)
			try {
				wait(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	notifyFlag = true;
    	/* Notify other threads*/
    	notify();
	}
	/** 
	 * Get the tuple
	 * @param key
	 * @return
	 */
	public synchronized byte [] get(int key){
		if (notifyFlag)
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		byte [] getValue = store.get(key);
    	if (getValue==null){
    		System.out.println("Reading key "+ key + " with first element of value as null");
    	}
    	else
    	{
    		System.out.println("Reading key "+ key + " with first element of value as " + getValue[0] );
    	}
    	if(!notifyFlag)
			try {
				wait(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	notifyFlag = true;
    	notify();
		return getValue;
	} 
	/**
	 * Remove the tuple
	 * @param key
	 */
	public synchronized void remove(int key){
		if (notifyFlag)
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		byte [] dataRemove = store.remove(key);
    	if (dataRemove==null){
    		System.out.println("Removing key "+ key + " with first element of value as null");
    	}
    	else{
    		System.out.println("Removing key "+ key + " with first element of value as " +dataRemove[0]);	
    	}
    	if(!notifyFlag)
			try {
				wait(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	notifyFlag = true;
    	notify();
	}

	public HashMap<Integer, byte []> getStore() {
		// TODO Auto-generated method stub
		return store;
	}
	
	private static final long serialVersionUID = 7708517838167328419L;
}
