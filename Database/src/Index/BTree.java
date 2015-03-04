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

/***
 * @param <Key>: Key type, Java Generics, can be assigned a type(Integer/String/...)
 * @param <Value>: Value type, Java Generics, can be assigned a type(Integer/String/...)
 */

public class BTree<Key extends Comparable<Key>, Value>  {
    private static final int M = 10;    // max children per B-tree node = M-1

    private Node root;             // root of the B-tree
    private int HT;                // height of the B-tree
    private int N;                 // number of key-value pairs in the B-tree
    private boolean notifyFlag = false;
    // helper B-tree node data type
    private static final class Node {
        private int m;                             // number of children
        private Entry[] children = new Entry[M];   // the array of children
        private Node(int k) { m = k; }             // create a node with k children
    }

    // internal nodes: only use key and next
    // external nodes: only use key and value
    private static class Entry {
        private Comparable key;
        private Object value;
        private Node next;     // helper field to iterate over array entries
        public Entry(Comparable key, Object value, Node next) {
            this.key   = key;
            this.value = value;
            this.next  = next;
        }
    }

    /**
     * constructor
     */
    public BTree() { root = new Node(0); }
 
    /**
     * 
     * @return number of key-value pairs in the B-tree
     */
    public int size() { return N; }
 
    /**
     *
     * @return height of B-tree
     */
    public int height() { return HT; }


    
    /**
     * 
     * @param key given by caller
     * @return associated value; return null if no such key
     */
    public synchronized Value get(Key key) { return search(root, key, HT); }
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
        Node u = insert(root, key, value, HT); 
        N++;
        if (u == null) return;

        // need to split root
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        HT++;
    }

    
    /**
     * insert helper method
     * @param h: node being inserted
     * @param key
     * @param value
     * @param ht: height of tree
     * @return
     */
    private Node insert(Node h, Key key, Value value, int ht) {
        int j;
        Entry t = new Entry(key, value, null);

        // external node
        if (ht == 0) {
            for (j = 0; j < h.m; j++) {
                if (less(key, h.children[j].key)) break;
            }
        }

        // internal node
        else {
            for (j = 0; j < h.m; j++) {
                if ((j+1 == h.m) || less(key, h.children[j+1].key)) {
                    Node u = insert(h.children[j++].next, key, value, ht-1);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }

        for (int i = h.m; i > j; i--) h.children[i] = h.children[i-1];
        h.children[j] = t;
        h.m++;
        if (h.m < M) return null;
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
    	 Node u = delete(root, key,  HT); 
    	 N--;
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
        return toString(root, HT, "") + "\n";
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
}