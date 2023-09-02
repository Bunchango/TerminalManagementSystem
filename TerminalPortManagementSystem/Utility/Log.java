package TerminalPortManagementSystem.Utility;
import java.io.Serializable;
import java.util.Date;

public class Log implements Serializable { // Finding a specific Log just need to find based on vehicleID and moving since at one time there can only 1 occurrence
    private final String vehicleID;
    private final Date departureDate;
    private final Date arrivalDate;
    private final String departurePortID;
    private final String arrivalPortID;
    private double fuelConsumed;
    private boolean finished;

    public Log(String vehicleID, Date departureDate, Date arrivalDate, String departurePortID, String arrivalPortID, double fuelConsumed, boolean finished) {
        this.vehicleID = vehicleID;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.departurePortID = departurePortID;
        this.arrivalPortID = arrivalPortID;
        this.fuelConsumed = fuelConsumed;
        this.finished = finished;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public String getDeparturePortID() {
        return departurePortID;
    }

    public String getArrivalPortID() {
        return arrivalPortID;
    }

    public double getFuelConsumed() {
        return fuelConsumed;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    protected void setFuelConsumed(double fuelConsumed) {
        this.fuelConsumed = fuelConsumed;
    }

    @Override
    public String toString() {
        return "Log{" +
                "vehicleID='" + vehicleID + '\'' +
                ", departureDate=" + departureDate +
                ", arrivalDate=" + arrivalDate +
                ", departurePortID='" + departurePortID + '\'' +
                ", arrivalPortID='" + arrivalPortID + '\'' +
                ", fuelConsumed=" + fuelConsumed +
                ", finished=" + finished +
                '}';
    }
}
