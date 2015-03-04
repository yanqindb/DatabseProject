package Index;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @param <Key>: Key type, Java Generics, can be assigned a type(Integer/String/...)
 * @param <Value>: Value type, Java Generics, can be assigned a type(Integer/String/...)
 */
public class MainTest<Key extends Comparable<Key>, Value> {
	 /**
	  * serach item by value(content), since we use Key to build B+ tree 
	  * so in this context, we search item line by line.
	  * @param val
	  * @return the Key with the value
	  */
	public Key get(Value val){
		 File file = new File("DataSet.csv");
	        BufferedReader reader = null;
	        BTree<String, String> st = new BTree<String, String>();
	        try {
	            
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
	            int line = 1;
	            
	            while ((tempString = reader.readLine()) != null) {
	                Object[] tmp=tempString.split(",");
	                if((tmp[1]).equals(val))
	                	return (Key) tmp[0];
	                line++;
	            	} 
	            
	        	}
	            catch (IOException e) {
		            e.printStackTrace();
		        } finally {
		            if (reader != null) {
		                try {
		                    reader.close();
		                } catch (IOException e1) {
		                }
		            }
		        }
			return null;
		 
	 }
	/**
	 *  main test procedure:
	 * @param args
	 */
	public static void main(String[] args) {
		//test1: build index with B+ tree
		  File file = new File("DataSet.csv");
	        BufferedReader reader = null;
	        //build B+tree
	        BTree<String, String> st = new BTree<String, String>();
	        try {
	            
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
	            int line = 1;
	            
	            while ((tempString = reader.readLine()) != null) {
	                String[] tmp=tempString.split(",");
	                //insert to the B+ tree  record by record
	                st.put(tmp[0], tmp[1]);
	                line++;
	            }
	            reader.close();
	            //test B+ tree
	            System.out.println("############ Test B+ tree Build-up ########################");
	            System.out.println("B+ tree with height of "+st.height());
	            System.out.println("B+ tree with records of "+st.size());
	            System.out.println();
	            System.out.println("########### tree B+ tree operations ########################");
	            //test get(Key)
	            System.out.println("Test B+tree Get");
	            String value=(String)st.get("96886");
	            System.out.println("Get record with key 96886: Record (96886)-"+value);
	            //test insert(Key,Value)
	            System.out.println("Test B+tree Put");
	            String testkey="1000";
	            String testValue="a33333.txt";
	            st.put(testkey, testValue);
	            System.out.println("Get record with key 1000: Record (1000)-"+(String)st.get("1000"));
	            System.out.println("B+ tree with records of "+st.size());
	            System.out.println("Test B+tree Remove with key 1000");
	            st.remove("1000");
	            String removeResult=(String)st.get("1000")==null?"Not found!":"Still exist";
	            
	            System.out.println("B+ tree with records of "+st.size());
	            System.out.println("Try to get record with key 1000...  "+removeResult);
	   
	            
	            //Test2: Multithreads concurrency
	            System.out.println();
	            System.out.println("############ Test Get Function based on Value ##############");
	            MainTest<String,String> main=new  MainTest<String,String>();
	            System.out.println("Test Get(Value value) function" );
	            System.out.println("Get record with Value (a0074797): Key-"+main.get("a0074797"));
	            System.out.println();
	            System.out.println("############ Test Multithreads #############################");

	            new GetClient( "ThreadGet" ,st, "96697");
	            new GetClient( "ThreadGet" ,st, "96697");
	            new GetClient( "ThreadGet" ,st, "96697");
	            new GetClient( "ThreadGet" ,st, "96697");
	            new RemoveClient( "ThreadRemove" ,st, "96697");
	            new GetClient( "ThreadGet" ,st, "96697");
	            new GetClient( "ThreadGet" ,st, "96697");
	            //Teest3: Get Function based on Value
	           
	            
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e1) {
	                }
	            }
	        }
	}

}
