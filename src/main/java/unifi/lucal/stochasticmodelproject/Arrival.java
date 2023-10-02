package unifi.lucal.stochasticmodelproject;

public class Arrival {
	private double arrivalTime;
	
	public Arrival(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public double getWaitingTime(double serviceTime) {
		return serviceTime - arrivalTime;
	}
	
}
