package ValueStore;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * This file create an object of ValueStore, and test cases of put, get and remove via multiple threading
 * @author Yan Wang & Zishan Qin
 *
 */
public class Main {

	@SuppressWarnings("unchecked")
	/**
	 * If a store exists in hard drive, read in the store to generate a new object of ValueStore.
	 * @param location
	 * @return
	 */
	static ValueStore loadStore(String location) {
		ObjectInputStream ois = null;
		HashMap<Integer, byte[]> store = null;
		ValueStore valueStore = null;
		
		try {
				ois = new ObjectInputStream (new FileInputStream(location));
				store = (HashMap <Integer, byte[]>) ois.readObject();
				ois.close();
				valueStore = new ValueStore(store);
				return valueStore;
			} catch (Exception e) {
				
			} 

		 if (ois != null) {
			 try { ois.close(); } catch (IOException ioe) { }
		 }
		 valueStore = new ValueStore(store);
		 return valueStore;
	}
	
	/**
	 * Save the store in the object of ValueStore
	 * @param valueStore
	 * @param location
	 */
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
	
	/**
	 * Generate an object of ValueStore, and test
	 * @param args
	 */
	public static void main(String[] args) {
		
		final String defaultStorage = "ValueStore.storage";
		
		final ValueStore valueStore = loadStore(defaultStorage);
		

		/*
		 * Generate data. data2 is 1GB
		 */
		byte[] data0 = new byte[] { (byte)0xe0};
		System.out.println("The length is data0 is " +data0.length);
		byte[] data1 = new byte[] { (byte)0xe1};
		System.out.println("The length is data1 is " +data1.length);
		byte[] data2 = null;
		
        try {
            RandomAccessFile file = new RandomAccessFile("tmp", "rw");
            file.setLength(1024 * 1024 *1024);//
       } catch (Exception e) {
            System.err.println(e);
       }
        
		Path path = Paths.get("tmp");
	    try {
			data2 = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    /*Test 1: 1GB data*/
	    System.out.println("The length is data2 is " +data2.length);

	    /* Test 2:
	     * Generate multiple threads, to test put, get and remove.
	     */
		new GetClient( "ThreadGet", valueStore,0);
		new GetClient( "ThreadGet", valueStore,0);
	    new PutClient( "ThreadPut", valueStore,0, data0);
	    new GetClient( "ThreadGet", valueStore,0);
	    new GetClient( "ThreadGet", valueStore,0);
	    new PutClient( "ThreadPut", valueStore,0, data1);
	    new GetClient( "ThreadGet", valueStore,0);
	    new GetClient( "ThreadGet", valueStore,0);
	    new PutClient( "ThreadPut", valueStore,0, data2);
	    new GetClient( "ThreadGet", valueStore,0);
	    new GetClient( "ThreadGet", valueStore,0);
	    new RemoveClient( "ThreadRemove", valueStore, 0);
	    new GetClient( "ThreadGet", valueStore,0);
	    new GetClient( "ThreadGet", valueStore,0);
		
	    /* Test 3: Put some data, and it will be got after rerun the program */
	    new PutClient( "ThreadPut", valueStore,0, data1);
	    
		storeState(valueStore, defaultStorage);

	}

}
