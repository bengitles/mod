/*
 * @(#)ExampleMain.java   1.0   Oct 1, 2013
 *
 * Copyright 2000-2013 ETH Zurich. All Rights Reserved.
 *
 * This software is the proprietary information of ETH Zurich.
 * Use is subject to license terms.
 *
 * @(#) $Id$
 */
package mpis;

import mpis.database.Db4MovingObjects;
import mpis.database.StorageManager;
import mpis.domain.MovingObject;


public class ExampleMain {

   public static void main(final String[] args) {

      /* Create a StorageManager instance here and use it to store and update some moving objects.
       *  Then perform some queries to validate your implementation of the time and space interpolation methods.
       */

	   Db4MovingObjects db = new Db4MovingObjects();
	   db.createObject("taxi1");
	   System.out.println("Got "+db.getObject("taxi1").getID());
	   db.createObject("taxi2");
	   System.out.println("Got "+db.getObject("taxi2").getID());
	   for (MovingObject mo : db.getObjects()) {
		   System.out.println("In the database:  "+mo.getID());
	   }
	   db.deleteObject(new MovingObject("taxi1"));
	   db.deleteObject(new MovingObject("taxi2"));
	   for (MovingObject mo : db.getObjects()) {
		   System.out.println("There's still "+mo.getID()+"!");
	   }
	   
      /*final StorageManager sm = new StorageManager();
      sm.deleteDB();
      sm.openDB();
      MovingObject taxi1 = new MovingObject("taxi1");
      sm.store(taxi1);
      System.out.println("Stored "+taxi1.getID());
      MovingObject taxi2 = new MovingObject("taxi2");
      sm.store(taxi2);
      System.out.println("Stored "+taxi2.getID());
      MovingObject taxiPrototype = new MovingObject("");
      MovingObject result = sm.retrieve(taxiPrototype);
      System.out.println("Retrieved: "+result.getID());
      sm.closeDB();*/
   }

}
