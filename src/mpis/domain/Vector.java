package mpis.domain;

public class Vector {
	
	private GPSPosition point1;
	private GPSPosition point2;
	private double latMag;
	private double lngMag;
	private double altMag;
	
	public Vector(GPSPosition point1, GPSPosition point2) {
		this.point1 = point1;
		this.point2 = point2;
		this.latMag = this.point2.getLatitude() - this.point1.getLatitude();
		this.lngMag = this.point2.getLongitude() - this.point1.getLongitude();
		this.altMag = this.point2.getAltitude() - this.point1.getAltitude();
	}
	
	public double getCosTheta(Vector other) {
		double dotProduct = this.latMag * other.latMag
						  + this.lngMag * other.lngMag
						  + this.altMag * other.altMag;
		return dotProduct/(this.getMagnitude()*other.getMagnitude());
	}
	
	public Vector cross(Vector other) {
		GPSPosition p1 = new GPSPosition(0,0,0);
		double crossLat = this.lngMag*other.altMag - this.altMag*other.lngMag;
		double crossLng = this.altMag*other.latMag - this.latMag*other.altMag;
		double crossAlt = this.latMag*other.lngMag - this.lngMag*other.latMag;
		GPSPosition p2 = new GPSPosition(crossLat, crossLng, crossAlt);
		Vector result = new Vector(p1,p2);
		return result;
	}
	
	public double getMagnitude() {
		return Math.pow(latMag*latMag + lngMag*lngMag + altMag*altMag, 0.5);
	}
	
	public String toString() {
		return " <"+latMag+","+lngMag+","+altMag+"> ";
	}
}
