package mpis.database;

import java.util.Collection;

import mpis.domain.MovingObject;

public class Db4MovingObjects {

	private StorageManager sm;
	
	public Db4MovingObjects() {
		this.sm = new StorageManager();
	}
	
	public void close() {
		this.sm.closeDB();
	}
	
	public void commit() {
		this.sm.commit();
	}
	
	public void rollback() {
		this.sm.rollback();
	}
	
	public MovingObject createObject(String id) {
		MovingObject mObject = new MovingObject(id);
		this.sm.store(mObject);
		return mObject;
	}
	
	public MovingObject getObject(String id) {
		return this.sm.retrieve(new MovingObject(id));
	}
	
	public Collection<MovingObject> getObjects() {
		return this.sm.retrieveAll();
	}
	
	public void deleteObject(MovingObject mObject) {
		this.sm.delete(mObject);
	}
}
