package Index;



import java.util.ArrayList;
import java.util.Queue;

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

/***
 * @param <Key>: Key type, Java Generics, can be assigned a type(Integer/String/...)
 * @param <Value>: Value type, Java Generics, can be assigned a type(Integer/String/...)
 */

public class BTree<Key extends Comparable<Key>, Value>  {
    private static final int M = 8;    // max children per B-tree node = M-1

    private Node root;             // root of the B-tree
    private int Height;                // height of the B-tree
    private int RecordsSize;                 // number of key-value pairs in the B-tree
    private ArrayList<Node> LeavsesList;	//Link all Leaves nodes together
    /**
     * 
     * Node is used for maintaining size information and entries 
     *
     */
    private static final class Node {
        private int m;                             // number of children
        private Entry[] children = new Entry[M];   // the array of children
        private Node(int k) { m = k; }             // create a node with k children
    }

    /**
     * internal nodes: only use key and next
     * external nodes: only use key and value
     *
     */
    private static class Entry {
        private Comparable key;
        private Object value;
        private Node next;     // helper field to iterate over array entries
        private Node parent;	//helper field to retrieve parent node
        public Entry(Comparable key, Object value, Node next) {
            this.key   = key;
            this.value = value;
            this.next  = next;
        }
    }

    /**
     * constructor
     */
    public BTree() { 
    	root = new Node(0);
    	LeavsesList=new ArrayList<Node>();
    }
 
    /**
     * 
     * @return number of key-value pairs in the B-tree
     */
    public int size() { return this.RecordsSize; }
 
    /**
     *
     * @return height of B-tree
     */
    public int height() { return Height; }


    
    /**
     * 
     * @param key given by caller
     * @return associated value; return null if no such key
     */
    public synchronized Value get(Key key) { return search(root, key, Height); }
   
    private Value search(Node x, Key key, int ht) {
        Entry[] children = x.children;

        // external node
        if (ht == 0) {
            for (int j = 0; j < x.m; j++) {
                if (eq(key, children[j].key)) return (Value) children[j].value;
            }
        }

        // internal node
        else {
            for (int j = 0; j < x.m; j++) {
                if (j+1 == x.m || less(key, children[j+1].key))
                    return search(children[j].next, key, ht-1);
            }
        }
        return null;
    }


    /**
     * insert key-value pair
     * @param key
     * @param value
     */
    public synchronized void put(Key key, Value value) {
        Node u = insert(root, key, value, Height); 
        this.RecordsSize++;
        if (u == null) return;

        // need to split root
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[0].parent=t;
        t.children[1] = new Entry(u.children[0].key, null, u);
        t.children[1].parent=t;
        root = t;
        
        Height++;
    }

    
    /**
     * insert helper method
     * @param h: node being inserted
     * @param key
     * @param value
     * @param ht: height of tree
     * @return
     */
    private Node insert(Node h, Key key, Value value, int height) {
        int j;
        Entry t = new Entry(key, value, null);

        // external node: when height ==0
        if (height == 0) {
            for (j = 0; j < h.m; j++) {
            	//we find the place we need to insert record, and it is leaves layer,okay to insert
                if (less(key, h.children[j].key)) break;
            }
        }

        // internal node
        else {
            for (j = 0; j < h.m; j++) {
            	//check each key in this node's entries
                if ((j+1 == h.m) || less(key, h.children[j+1].key)) {
                	//key is less than the j+1 entry, so we need to insert to the place under j entry
                	//recursively find the appropriate entry layer by layer
                	//until hit the leaves 
                    Node u = insert(h.children[j++].next, key, value, height-1);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }
        //we need to shift records which keys value are bigger than key to right with one spot
        for (int i = h.m; i > j; i--) h.children[i] = h.children[i-1];
        h.children[j] = t;
        t.parent=h;
        h.m++;
       
        //the node isn't full,done!
        if (h.m < M) return null;
        //node is full, need to split into two nodes
        else         return split(h);
    }

     
    /**
     * split node in half
     * @param h: the node h is splited into 2 parts
     * @return new node with half elements of h
     */
    private Node split(Node h) {
        Node t = new Node(M/2);
        h.m = M/2;
        for (int j = 0; j < M/2; j++)
            t.children[j] = h.children[M/2+j]; 
        return t;    
    }
     
    /**
     * remove key-value pair according to Key
     * @param key
     */
    synchronized void remove(Key key){
    	//first locate that entry, then judge: if node.m>=M/2, then shift; else has to merge with neibours.
    	 Node u = delete(root, key,  Height); 
    	 this.RecordsSize--;
    	 if (u == null) return;
    	 //need to merge root
    	 else 
    	 {
    		 Node t=new Node(1);
    		t.children[0]=new Entry(root.children[0].key, null, root.children[0].next);
    		root=t;
    	 }

    }
/**
 * remove according to Value, because we have only one clustterd index on key,
 * if the attribute is not key, we have to iterate on all tuples
 * We keep the leaves list in the tree
 * @param val
 * @return
 */
	public synchronized ArrayList<Key> remove(Value val) {
    	ArrayList<Key> result=new ArrayList<Key>();
    	int size=this.LeavsesList.size();
    	 //iterate all leaf nodes, compare each record's value with val
    	for(int i=0;i<size;i++){
    		Node current=this.LeavsesList.get(i);
    		int m=current.m;
    		for(int j=0;j<m;j++){
    			if(current.children[j].value.equals(val)){
    				
    				
    				result.add((Key) current.children[j].key);
    				
    			}
    		}
    	}
    	for(int i=0;i<result.size();i++){
    		this.remove(result.get(i));
    	}
    	return result;
    }
    /**
     * remove helper function
     * @param h
     * @param key
     * @param ht
     * @return
     */
    private Node delete(Node h, Key key, int ht){
    	 Entry[] children = h.children;
    	 int j;
         // external node
         if (ht == 0) {
             for (j = 0; j < h.m; j++) {
                 if (eq(key, children[j].key))
                	 break;
             }
         }

         // internal node
         else {
             for (j = 0; j < h.m; j++) {
                 if (j+1 == h.m || less(key, children[j+1].key))
                      {
                	 	Node u=delete(children[j].next, key,  ht-1);
                        if(u==null) return null;
                        else {
                        	if(u.m<M/2){
                        	//try to borrow from neighbours
                        	// j is next to u
                        	if(children[j+1]!=null&&children[j+1].next.m>M/2){
                        		//borrow from neighbour, move the first item from next child and add to this child
                        		
                        		children[j].next.children[children[j].next.m]=children[j+1].next.children[0];
                        		children[j].next.m++;
                        		for(int i=1;i<children[j+1].next.m;i++){
                        			children[j+1].next.children[i-1]=children[j+1].next.children[i];
                        		}
                        		children[j+1].next.m--;
                        		children[j+1].key=children[j+1].next.children[0].key;
                        		return null;
                        	}
                        	//borrowing fails, try to merge j and j-1, return merge node j
                        	//first delete that element

                        	if(j-1>=0&&children[j-1].next.m>M/2){
                        		//borrow from neighbour
                        		Entry e=children[j-1].next.children[children[j-1].next.m-1];//borrowed
                        		for(int i=children[j].next.m-1;i>=0;i--){
                        			children[j].next.children[i]=children[j].next.children[i+1];
                        		}
                        		children[j].next.children[0]=e;
                        		children[j].next.m++;
                        		children[j].key=e.key;
                        		children[j-1].next.m--;
                        		return null;
                        	}
                        	//borrowing fails, try to merge j and j+1, return merge node j
                        	//first delete that element
                        	if(children[j+1]!=null&&children[j+1].next.m<=M/2)
                        	{Node v=merge( children[j],children[j+1]);
	                        	for(int i=h.m-1;i>j+1;i--){
	                    			children[i-1]=children[i];
	                    		}
	                    		h.m--;
                        	return v;
                        	}
                        	if(children[j+1]==null&&children[j-1].next.m<=M/2)//j is the last entry
                        	{
                        		Node v=merge( children[j-1],children[j]);
                        		h.m--;
                        	return v;
                        	}
                        	break;
                        }
                        else{
                        	//at this point, the B+tree features are satisfied
                        	return null;
                        }
                        	
                        }
                        
                    }
             }
         }
        
    	//shift items and decrement the size
        for (int i = j; i <h.m-1; i++)
         	h.children[i] = h.children[i+1];
         	h.m--;
        if (h.m >= M/2) 	
        	return null;
        	
        else  return h;
    }
    
    /**
     * remove helper function, merge two entries children and children23
     * @param children
     * @param children2
     * @return
     */
    private Node merge( Entry children, Entry children2){
    	int i;  
    	for(i=0;i<children2.next.m;i++){
    		  children.next.children[children.next.m+i]=children2.next.children[i];	  
    	  }
    	  children.next.m+=i;
    	  return children.next;
    	  
    }
    
	/**
	 * for debugging
	 */
    public String toString() {
        return toString(root, Height, "") + "\n";
    }
    /**
	 *  debugging helper function
	 */
    private String toString(Node h, int ht, String indent) {
        String s = "";
        Entry[] children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.m; j++) {
                s += indent + children[j].key + " " + children[j].value + "\n";
            }
        }
        else {
            for (int j = 0; j < h.m; j++) {
                if (j > 0) s += indent + "(" + children[j].key + ")\n";
                s += toString(children[j].next, ht-1, indent + "     ");
            }
        }
        return s;
    }


     
    /**
     * comparison functions - make Comparable instead of Key to avoid casts
     * @param k1
     * @param k2
     * @return k1 less than k2
     */
    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }
    /**
     * used for search and delete
     * @param k1
     * @param k2
     * @return k1 equals to k2
     */
    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }
     void buildLeavesList(){
//    	//search for the leftmost Leaf-node: 
//    	//1.Ht=0
//    	//2.key is smallest
//    	int ht=this.Height;
//    	Node n=this.root;
//    	while(ht>0){
//    		n=n.children[0].next;
//    		ht--;
//    	}
//    	this.LeavsesList=new ArrayList<Node>();
//    	this.LeavsesList.add(n);
    	//use 2 rolling queues and store each layer in idle queue, when ht==0, we can begin to add those nodes into list
    	
		ArrayList<Node> currentNodeList=new ArrayList<Node>();
		ArrayList<Node> parentNodeList=new ArrayList<Node>();
		parentNodeList.add(root);
		int ht=this.Height;
		while(ht>0){
			for(int i=0;i<parentNodeList.size();i++){
				for(int j=0;j<parentNodeList.get(i).m;j++){
					currentNodeList.add(parentNodeList.get(i).children[j].next);
				}
			}
			ht--;
			if(ht==0) break;
			ArrayList<Node> temp;
			temp=new ArrayList<Node>(currentNodeList);
			parentNodeList=temp;
			currentNodeList.clear();		
		}
    	this.LeavsesList=currentNodeList;
    	
    }
//    public static void main(String[] args) {
//        BTree<String, String> st = new BTree<String, String>();
//
////      st.put("www.cs.princeton.edu", "128.112.136.12");
//        st.put("www.cs.princeton.edu", "128.112.136.11");
//        st.put("www.princeton.edu",    "128.112.128.15");
//        st.put("www.yale.edu",         "130.132.143.21");
//        st.put("www.simpsons.com",     "209.052.165.60");
//        st.put("www.apple.com",        "17.112.152.32");
//        st.put("www.amazon.com",       "207.171.182.16");
//        st.put("www.ebay.com",         "66.135.192.87");
//        st.put("www.cnn.com",          "64.236.16.20");
//        st.put("www.google.com",       "216.239.41.99");
//        st.put("www.nytimes.com",      "199.239.136.200");
//        st.put("www.microsoft.com",    "207.126.99.140");
//        st.put("www.dell.com",         "143.166.224.230");
//        st.put("www.slashdot.org",     "66.35.250.151");
//        st.put("www.espn.com",         "199.181.135.201");
//        st.put("www.weather.com",      "63.111.66.11");
//        st.put("www.yahoo.com",        "216.109.118.65");
//        st.put("www.cs.princeton.edu", "128.112.136.11");
//        st.put("www.princeton.edu",    "128.112.128.15");
//        st.put("www.yale.edu",         "130.132.143.21");
//        st.put("www.simpsons.com",     "209.052.165.60");
//        st.put("www.apple.com",        "17.112.152.32");
//        st.put("www.amazon.com",       "207.171.182.16");
//        st.put("www.ebay.com",         "66.135.192.87");
//        st.put("www.cnn.com",          "64.236.16.20");
//        st.put("www.google.com",       "216.239.41.99");
//        st.put("www.nytimes.com",      "199.239.136.200");
//        st.put("www.microsoft.com",    "207.126.99.140");
//        st.put("www.dell.com",         "143.166.224.230");
//        st.put("www.slashdot.org",     "66.35.250.151");
//        st.put("www.espn.com",         "199.181.135.201");
//        st.put("www.weather.com",      "63.111.66.11");
//        st.put("www.yahoo.com",        "216.109.118.65");
//        st.buildLeavesList();
//
//        System.out.println("cs.princeton.edu:  " + st.get("www.cs.princeton.edu"));
//        System.out.println("hardvardsucks.com: " + st.get("www.harvardsucks.com"));
//        System.out.println("simpsons.com:      " + st.get("www.simpsons.com"));
//        System.out.println("apple.com:         " + st.get("www.apple.com"));
//        System.out.println("ebay.com:          " + st.get("www.ebay.com"));
//        System.out.println("dell.com:          " + st.get("www.dell.com"));
//        System.out.println();
//
//        System.out.println("size:    " + st.size());
//        System.out.println("height:  " + st.height());
//        System.out.println(st);
//        System.out.println();
//    }
}