package com.srlab.methodretriever.technique.simhash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.srlab.methodretriever.utility.CloneMethod;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.btree.BTree;
import jdbm.helper.StringComparator;
import jdbm.helper.Tuple;
import jdbm.helper.TupleBrowser;

public class SimHashCalculator {

	static String DATABASE = "methods";
	static String BTREE_NAME = "SimHashMethods";
    
    private HashMap<SimHash, String> simHashMethods;
    
    private String methodsPath;
    
    public SimHashCalculator(List<CloneMethod> cloneMethods, String methodsPath) {
    	this.methodsPath = methodsPath;
        for (int i=0; i < cloneMethods.size();i++){
            try {
                FileInputStream fisTargetFile;
                fisTargetFile = new FileInputStream(new File(methodsPath+cloneMethods.get(i).getCloneId()+"_"+cloneMethods.get(i).getCloneClassId()+".txt"));
                String methodBody = IOUtils.toString(fisTargetFile, "UTF-8");
                fisTargetFile.close();
                SimHash hash = new SimHash(methodBody, 64);
                
                simHashMethods.put(hash, cloneMethods.get(i).getCloneId()+"_"+cloneMethods.get(i).getCloneClassId());
                //addItemToBTree(hash,cloneMethods.get(i).getCloneId()+"_"+cloneMethods.get(i).getCloneClassId());
                
            }catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
       // printItemsFromTree(10);
        
    }


    private List<SimHashMethod> rankCorpus(CloneMethod testMethod){
    	List<SimHashMethod> simHashList = new ArrayList<SimHashMethod>();
    	SimHash testHash = null;
    	
    	try{
    		FileInputStream fisTargetFile;
            fisTargetFile = new FileInputStream(new File(methodsPath+testMethod.getCloneId()+"_"+testMethod.getCloneClassId()+".txt"));
            String methodBody = IOUtils.toString(fisTargetFile, "UTF-8");
            fisTargetFile.close();
        	testHash = new SimHash(methodBody, 64);
    	} catch (FileNotFoundException e){
    		e.printStackTrace();
    	} catch (IOException e) {
			e.printStackTrace();
		}
    
    	if (testHash==null) return simHashList;
    	
    	 Iterator it = simHashMethods.entrySet().iterator();
    	    while (it.hasNext()) {
    	        Map.Entry pair = (Map.Entry)it.next();
    	        int hammingDistance = testHash.hammingDistance((SimHash)pair.getKey());
    	        simHashList.add(new SimHashMethod((String)pair.getValue(),hammingDistance));
    	        it.remove(); // avoids a ConcurrentModificationException
    	    }
    	
    	Collections.sort(simHashList);
    	return simHashList;
    }

	private void printItemsFromTree(int i) {
		RecordManager recman;
		long          recid;
		Properties    props;
		Tuple         tuple = new Tuple();
	    TupleBrowser  browser;
	    BTree         tree;

        props = new Properties();
        
		 // open database and setup an object cache
        try {
			recman = RecordManagerFactory.createRecordManager( DATABASE, props );
			  // try to reload an existing B+Tree
	        recid = recman.getNamedObject( BTREE_NAME );
	        
	        if ( recid != 0 ) {
                tree = BTree.load( recman, recid );
                System.out.println( "Reloaded existing BTree with " + tree.size()
                                    + " records." );
            } else {
                // create a new B+Tree data structure
                tree = BTree.createInstance( recman, new BigIntegerComparator() );
                
                recman.setNamedObject( BTREE_NAME, tree.getRecid() );
                System.out.println( "Created a new empty BTree" );
            }
	        
	        browser = tree.browse();
            while ( browser.getNext( tuple ) ) {
            	System.out.println("KEY: " + (String) tuple.getKey() +"  VALUE: "+(String) tuple.getValue());
            }

            
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}



	private void addItemToBTree(SimHash hash, String methodId) {
		RecordManager recman;
		long          recid;
		Properties    props;
		Tuple         tuple = new Tuple();
	    TupleBrowser  browser;
	    BTree         tree;

        props = new Properties();
        
		 // open database and setup an object cache
        try {
			recman = RecordManagerFactory.createRecordManager( DATABASE, props );
			  // try to reload an existing B+Tree
	        recid = recman.getNamedObject( BTREE_NAME );
	        
	        if ( recid != 0 ) {
                tree = BTree.load( recman, recid );
                System.out.println( "Reloaded existing BTree with " + tree.size()
                                    + " records." );
            } else {
                // create a new B+Tree data structure
                tree = BTree.createInstance( recman, new BigIntegerComparator() );
                
                recman.setNamedObject( BTREE_NAME, tree.getRecid() );
                System.out.println( "Created a new empty BTree" );
            }
	        
	        tree.insert(hash, methodId, false);
	        // make the data persistent in the database
            recman.commit();
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

      
		
	}

}
