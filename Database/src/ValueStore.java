import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;


class ValueStore implements Serializable{
	
	
	private HashMap<Integer, byte []> store;
	
	public ValueStore(HashMap<Integer, byte[]> store){
		if(store==null){
			this.store = new HashMap<Integer, byte []>();
		}
		else{
			this.store = store;
		}
	}
	
	public synchronized void put(int key, byte [] data){
		store.put(key, data);
	}
	public byte [] get(int key){
		return store.get(key);
	} 
	public synchronized void Remove(int key){
		store.remove(key);
	}

	public HashMap<Integer, byte []> getStore() {
		// TODO Auto-generated method stub
		return store;
	}
	
	private static final long serialVersionUID = 7708517838167328419L;
}
