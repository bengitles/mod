  package mpis.domain;

public class GPSLine {
	private GPSPosition p1;
	private Long d1;
	private GPSPosition p2;
	private Long d2;
	
	public GPSLine(GPSPosition p1, long previousTime, GPSPosition p2, long newTime) {
		this.p1 = p1;
		this.d1 = previousTime;
		this.p2 = p2;
		this.d2 = newTime;
	}
	
	/**
	 * If a point is not on the line, it returns Long.MIN_VALUE.
	 * If a point is on the line, it returns the time at which it was at that point.
	 * @param p3
	 * @return
	 */
	public Long isOnLine(GPSPosition p3) {
		Vector v1 = new Vector(p1, p3);
		Vector v2 = new Vector(p2, p3);
		Vector line = new Vector(p1,p2);
		if (v1.getCosTheta(v2) == -1.0) {
			Double t = (p3.getLatitude()-p1.getLatitude())/(p2.getLatitude()-p1.getLatitude());
			if (t.isNaN()) t = (p3.getLongitude()-p1.getLongitude())/(p2.getLongitude()-p1.getLongitude());
			if (t.isNaN()) t = (p3.getAltitude()-p1.getAltitude())/(p2.getAltitude()-p1.getAltitude());
			double projectedTime = d1 + (d2-d1)*t;
			return (long) projectedTime;
		} else if (line.getCosTheta(v1) <= 0 && v1.getMagnitude() <= 3.0) {
			return this.d1;
		} else if (line.getCosTheta(v2) >= 0 && v2.getMagnitude() <= 3.0) {
			return this.d2;
		} else if (line.getCosTheta(v1) > 0 && line.getCosTheta(v2) < 0){
			double dist = line.cross(v1).getMagnitude() / line.getMagnitude();
			if (dist <= 3.0) {
				double t = v1.getMagnitude() / (v1.getMagnitude() + v2.getMagnitude());
				double projectedTime = d1 + (d2-d1)*t;
				return (long) projectedTime;
			}
		}
		return Long.MIN_VALUE;
	}
	
	public String toString() {
		return "Point 1: "+p1+"; Point2: "+p2;
	}
}
