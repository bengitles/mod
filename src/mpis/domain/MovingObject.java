package mpis.domain;

import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;


public class MovingObject implements Observer {

	private String id;
	private Map<Long, GPSPosition> locations;
	private Collection<GPSLine> lines;
	private GPSDevice device;
	private long initialTime;
	private long lastDefinedTime;
	private boolean running;
	
	/**
	 * Constructor
	 * @param id
	 */
	public MovingObject(String id) {
		this.id = id;
		this.locations = new HashMap<Long, GPSPosition>();
		this.lines = new LinkedList<GPSLine>();
		this.setGPSDevice(new GPSDevice(this));
		//Normalize all times to initialTime = 0 for display purposes
		this.initialTime = System.currentTimeMillis();
		this.lastDefinedTime = Long.MIN_VALUE;
		this.running = false;
	}
	/**
	 * Get the object's ID, i.e. its name
	 * @return
	 */
	public String getID() {
		return id;
	}
	/**
	 * Adds the new position to the map of times to location
	 * For all points added after the first one, this method also creates a GPSLine with the
	 * new position and the previous one. This is important in later methods for determining
	 * trajectories.
	 * @param newPosition
	 */
	public void setPosition(GPSPosition newPosition) {
		long newTime = System.currentTimeMillis();
		//Display the time in terms of time elapsed from the creation of the object.
		System.out.println("New Position: " + newPosition + " at " + (newTime - this.initialTime));
		
		//If there is no lastDefinedTime, don't add a line because that's the first point.
		//Otherwise, make a line between the last defined point and the new point.
		GPSPosition previousPosition = null;
		if (this.lastDefinedTime != Long.MIN_VALUE) {
			previousPosition = this.locations.get(this.lastDefinedTime);
			this.lines.add(new GPSLine(previousPosition,this.lastDefinedTime,newPosition,newTime));
		}
		this.locations.put(newTime, newPosition);
		this.lastDefinedTime = newTime;
		
	}
	
	public void setGPSDevice(GPSDevice device) {
		this.device = device;
	}
	
	/**
	 * This method is not implemented to its fullest potential. I would like to be able to call start(),
	 * allow the program to run through a loop, then call stop() at a completely separate time to break
	 * out of the loop and stop the object. My intermediate solution is to pre-specify how long the
	 * object should run for.
	 * @param timeRunning
	 */
	// TODO Figure out how to let the GPSDevice run, then calling stop() to make it stop
	public void start(long timeRunning) {
		this.running = true;
		this.device.start(timeRunning);
	}
	
	public void stop() {
		this.running = false;
	}
	
	/**
	 * This method returns the position that an object was at at the requested time,
	 * where the times are normalized to initialTime = 0.
	 * @param d
	 * @return
	 */
	public GPSPosition getPosition(Long d) {
		Long timeBefore = Long.MIN_VALUE;
		Long timeAfter = Long.MAX_VALUE;
		//If the query is given in relational time, convert it to system time, which is
		//based on milliseconds elapsed since Jan 1, 1970.
		long fortyYearsInMilliseconds = (long) (1.261*Math.pow(10, 12));
		if (d < fortyYearsInMilliseconds) d += this.initialTime;
		for (Long d1 : locations.keySet()) {
			if (d1==d) {
				return locations.get(d1);
			}
			if (d1 >= timeBefore && d1 <= d) {
				timeBefore = d1;
			} else if (d1 <= timeAfter && d1 >= d) {
				timeAfter = d1;
			}
		}
		//If query is for before the object starts moving, give the first recorded position
		if(timeBefore == Long.MIN_VALUE && timeAfter != Long.MAX_VALUE) {
			Long dMin = Long.MAX_VALUE;
			for (Long d1 : this.locations.keySet()) {
				//Compare dates to find the one with the lowest time
				if (d1 < dMin) dMin = d1;
			}
			return this.locations.get(dMin);
		}
		//If query is after the last defined location, give that last position
		else if (timeBefore != Long.MIN_VALUE && timeAfter == Long.MAX_VALUE) {
			Long dMax = Long.MIN_VALUE;
			for (Long d1 : this.locations.keySet()) {
				//Compare dates to find the one with the greatest time
				if (d1 > dMax) dMax = d1;
			}
			return this.locations.get(dMax);
		}
		GPSPosition positionBefore = this.locations.get(timeBefore);
		GPSPosition positionAfter = this.locations.get(timeAfter);
		double lat = positionBefore.getLatitude() +
				(positionAfter.getLatitude()-positionBefore.getLatitude())
				/(timeAfter-timeBefore)*(d-timeBefore);
		double lng = positionBefore.getLongitude() +
				(positionAfter.getLongitude()-positionBefore.getLongitude())
				/(timeAfter-timeBefore)*(d-timeBefore);
		double alt = positionBefore.getAltitude() +
				(positionAfter.getAltitude()-positionBefore.getAltitude())
				/(timeAfter-timeBefore)*(d-timeBefore);
		return new GPSPosition(lat, lng, alt);
	}
	/**
	 * This method determines if the requested position corresponds to one of the points
	 * that the object is recorded to have visited, if it is on the direct line trajectory
	 * between two points, or is within the 3.0 uncertainty boundary.
	 * See the isOnLine method in GPSLine for more details.
	 * Returns null if the requested point deos not meet any of these criteria.
	 * @param gp
	 * @return
	 */
	public Long getDate(GPSPosition gp) {
		if(locations.containsValue(gp)) {
			for (Long d : this.locations.keySet()) {
				if (this.locations.get(d).equals(gp)) return d;
			}
		}
		for (GPSLine l : this.lines) {
			System.out.print("Looking at line "+l+"...");
			if (l.isOnLine(gp) != Long.MIN_VALUE) {
				System.out.println("found it!");
				return l.isOnLine(gp);
			}
			System.out.println("nope");
		}
		return null;
	}
	
	public void printLines() {
		for (GPSLine l : this.lines) {
			System.out.println("Line: "+l);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		GPSPosition currentPosition = (GPSPosition) arg1;
		this.setPosition(currentPosition);
		
	}

	public Long getInitialTime() {
		return this.initialTime;
	}
}
