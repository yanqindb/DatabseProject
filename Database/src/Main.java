import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;


public class Main {

	static ValueStore loadStore(String location) {
		ObjectInputStream ois = null;
		HashMap<Integer, byte[]> store = null;
		ValueStore valueStore = null;
		
		String currentWorkingDirectory = System.getProperty("user.dir");
		 
			 try {
				ois = new ObjectInputStream (new FileInputStream(location));
				store = (HashMap <Integer, byte[]>) ois.readObject();
				ois.close();
				valueStore = new ValueStore(store);
				return valueStore;
			} catch (Exception e) {
				//e.printStackTrace();
			} 

		 // close down safely
		 if (ois != null) {
			 try { ois.close(); } catch (IOException ioe) { }
		 }
		 valueStore = new ValueStore(store);
		 return valueStore;
	}
	
	public static void storeState(ValueStore valueStore, String location) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(location));
			oos.writeObject(valueStore.getStore());
		} catch (Exception e) {
			
		}
		
		if (oos != null) {
			try { oos.close(); } catch (IOException ioe) { } 
		}
		
	}
	
	public static void main(String[] args) {
		
		final String defaultStorage = "ValueStore.storage";
		
		final ValueStore valueStore = loadStore(defaultStorage);
		

		byte[] dataIn = new byte[] { (byte)0xe0};
		byte[] dataOut = new byte[2];
		//valueStore.put(1, dataIn);
		
		System.out.println(dataIn[0]);
		dataOut = valueStore.get(1);
		System.out.println(dataOut[0]);
		
		
		storeState(valueStore, defaultStorage);

		
		

	}

}
