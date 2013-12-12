package mpis.database;

import java.io.File;
import java.util.Collection;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import com.db4o.ta.TransparentPersistenceSupport;

import mpis.domain.MovingObject;

public class StorageManager {

   private static final String DB_FILE = "moving-objects.db";

   private ObjectContainer db;

   public StorageManager() {
	   this.openDB();
   }

   private EmbeddedConfiguration getConfig() {
      final EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
      config.common().add(new TransparentPersistenceSupport());
      config.common().objectClass(MovingObject.class).cascadeOnUpdate(true);
      config.common().objectClass(MovingObject.class).cascadeOnDelete(true);
      return config;
   }

   public void openDB() {
      this.db = Db4oEmbedded.openFile(this.getConfig(), StorageManager.DB_FILE);
   }

   public void closeDB() {
      if (this.db != null) {
         this.db.close();
      }
   }

   public void deleteDB() {
      if (this.db != null && !this.db.ext().isClosed()) {
         this.closeDB();
      }
      new File(StorageManager.DB_FILE).delete();
   }

   public void store(final MovingObject mObject) {
      this.db.store(mObject);
   }

   public Collection<MovingObject> retrieveAll() {
      return this.db.query(MovingObject.class);
   }

   public MovingObject retrieve(final MovingObject prototype) {
      final ObjectSet<Object> result = this.db.queryByExample(prototype);
      if (result.hasNext()) {
         return (MovingObject) result.next();
      }
      return null;
   }

   public MovingObject retrieveById(final String id) {
      final ObjectSet<MovingObject> result = this.db.query(new Predicate<MovingObject>() {

         private static final long serialVersionUID = 1L;

         @Override
         public boolean match(final MovingObject candidate) {
            if (candidate.getID().equals(id)) {
               return true;
            }
            return false;
         }

      });

      if (result.hasNext()) {
         return result.next();
      }
      return null;
   }

   public void delete(final MovingObject mObject) {
      this.db.delete(mObject);
   }

   public void commit() {
      this.db.commit();
   }

   public void rollback() {
      this.db.rollback();
   }


}
