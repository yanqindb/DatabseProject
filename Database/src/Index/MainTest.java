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
import java.util.ArrayList;

/**
 * @param <Key>: Key type, Java Generics, can be assigned a type(Integer/String/...)
 * @param <Value>: Value type, Java Generics, can be assigned a type(Integer/String/...)
 */
public class MainTest<Key extends Comparable<Key>, Value> {
	
	/**
	 *  main test procedure:
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		//test1: build index with B+ tree
		  File file = new File("DataSet.csv");
	        BufferedReader reader = null;
	        //build B+tree
	        BTree<String, Integer> st = new BTree<String, Integer>();
	        try {
	            
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
	            int line = 1;
	            while ((tempString = reader.readLine()) != null) {
	                String[] tmp=tempString.split(",");
	                //insert to the B+ tree  record by record
	                st.put(tmp[1],Integer.parseInt(tmp[0]));
	                line++;
	                
	            }
	            reader.close();
	            st.put("aSusanYan.txt", 96698);
	            int a=st.get("aSusanYan.txt");
	            st.buildLeavesList();
	            

	            //test B+ tree
	            System.out.println("############ Test B+ tree Build-up ########################");
	            System.out.println("B+ tree with height of "+st.height());
	            System.out.println("B+ tree with records of "+st.size());
	            System.out.println();
	            System.out.println("########### tree B+ tree operations ########################");
	            //test get(Key)
	            System.out.println("Test B+tree Get");
	            int value=st.get("a0000026");
	            System.out.println("Get record with Value a0000026: Record (a0000026)-"+value);
	            //test insert(Key,Value)
	            System.out.println("Test B+tree Put");
	            int testkey=1000;
	            String testValue="a33333.txt";
	            st.put(testValue,testkey);
	            System.out.println("Get record (a33333.txt)-"+st.get("a33333.txt"));
	            System.out.println("B+ tree with records of "+st.size());
	            System.out.println("Test B+tree Remove record with key 1000");
	            st.remove(1000);
	            String removeResult=st.get("a33333.txt")==null?"Not found!":"Still exist";
	            
	            System.out.println("B+ tree with records of "+st.size());
	            System.out.println("Try to get record with key a33333.txt...  "+removeResult);

	            System.out.println("############ Test Multithreads #############################");
	            @SuppressWarnings("unused")
				ArrayList<String> res=st.remove(10000);
	            new GetClient( "ThreadGet" ,st, "a0000026");
	            new GetClient( "ThreadGet" ,st, "a0000026");
	            new GetClient( "ThreadGet" ,st, "a0000026");
	            new GetClient( "ThreadGet" ,st, "a0000026");
	           
	            new RemoveClient< String,Integer>( "ThreadRemove",st, 96698);
	            new GetClient( "ThreadGet" ,st, "a0000026");
	            new GetClient( "ThreadGet" ,st, "a0000026");
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
