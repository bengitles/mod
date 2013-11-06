package mpis.domain;

import java.util.Observable;
import java.util.Random;

public class GPSDevice extends Observable {
	
	private GPSPosition previousPosition;
	private GPSPosition nextPosition;
	private long timeAtPreviousPosition;
	private long timeAtNextPosition;
	private boolean running;
	private Random rand;
	
	public GPSDevice(MovingObject mo) {
		this.addObserver(mo);
		this.previousPosition = null;
		this.nextPosition = null;
		this.timeAtNextPosition = 0;
		this.timeAtPreviousPosition = 0;
		this.running = false;
		this.rand = new Random();
	}
	
	public void start(long timeRunning) {
		this.running = true;
		long endingTime = System.currentTimeMillis() + timeRunning;
		while (System.currentTimeMillis() < endingTime) {
			this.timeAtPreviousPosition = System.currentTimeMillis();
			if (this.previousPosition==null) {
				this.previousPosition = new GPSPosition(rand.nextDouble()*100,rand.nextDouble()*100,rand.nextDouble()*100);
				this.setChanged();
				this.notifyObservers(this.previousPosition);
			} else {
				this.previousPosition = this.nextPosition;
			}
			//Pick a random point within 0-100 for each coordinate.
			this.nextPosition = new GPSPosition(rand.nextDouble()*100,rand.nextDouble()*100,rand.nextDouble()*100);
			int timeToNextPosition = rand.nextInt(5)*1000+999;
			this.timeAtNextPosition = this.timeAtPreviousPosition + timeToNextPosition;
			try {
				Thread.sleep(timeToNextPosition);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.setChanged();
			this.notifyObservers(this.nextPosition);
		}
		this.stop();
	}
	
	public void stop() {
		this.running = false;
	}
	
	public GPSPosition getCurrentPosition() {
		long timeSincePreviousPosition = (System.currentTimeMillis() - this.timeAtPreviousPosition);
		double lat = this.previousPosition.getLatitude() +
				(this.nextPosition.getLatitude() - this.previousPosition.getLatitude())*
					timeSincePreviousPosition/(this.timeAtNextPosition-this.timeAtPreviousPosition);
		double lng = this.previousPosition.getLongitude() +
				(this.nextPosition.getLongitude() - this.previousPosition.getLongitude())*
					timeSincePreviousPosition/(this.timeAtNextPosition-this.timeAtPreviousPosition);
		double alt = this.previousPosition.getAltitude() +
				(this.nextPosition.getAltitude() - this.previousPosition.getAltitude())*
					timeSincePreviousPosition/(this.timeAtNextPosition-this.timeAtPreviousPosition);
		return new GPSPosition(lat,lng,alt);
	}
}
