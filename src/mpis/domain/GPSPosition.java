package mpis.domain;

public class GPSPosition {

	private double lat;
	private double lng;
	private double alt;
	
	public GPSPosition(double lat, double lng, double alt) {
		this.lat = lat;
		this.lng = lng;
		this.alt = alt;
	}
	
	public double getLatitude(){
		return this.lat;
	}
	
	public double getLongitude() {
		return this.lng;
	}
	
	public double getAltitude() {
		return this.alt;
	}
	
	public String toString() {
		double roundedLat = ((double)(Math.round(this.lat*10.0)))/10.0;
		double roundedLng = ((double)(Math.round(this.lng*10.0)))/10.0;
		double roundedAlt = ((double)(Math.round(this.alt*10.0)))/10.0;
		return "("+roundedLat+","+roundedLng+","+roundedAlt+")";
	}
	
	@Override
	public boolean equals(Object other) {
		if (other.getClass() != this.getClass()) return false;
		GPSPosition other1 = (GPSPosition) other;
		return this.lat==other1.lat && this.lng==other1.lng && this.alt==other1.alt;
	}
}
