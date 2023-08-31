package TerminalPortManagementSystem.Ports;

import TerminalPortManagementSystem.Utility.Log;
import TerminalPortManagementSystem.Utility.LogManager;
import TerminalPortManagementSystem.Utility.TerminalUtil;
import TerminalPortManagementSystem.Vehicles.Vehicle;

import java.io.Serializable;
import java.util.*;

public class Port implements Serializable { // Might have to add a log ( history for trips )
    private final String portID;
    private final String portName;
    private final double latitude;
    private final double longitude;
    private final double storingCapacity; // Check if reached storing capacity
    private final boolean landingAbility;
    private final List<Vehicle> portVehicles = new ArrayList<>();
    private final List<Container> portContainers = new ArrayList<>(); // Store all Containers inside the port, does not include the Containers inside each Port.Vehicles in the port

    public Port(String portID, String portName, double latitude, double longitude, double storingCapacity, boolean landingAbility) {
        if (TerminalUtil.objectAlreadyExist("p-" + portID)) {
            throw new IllegalArgumentException("Port already exist, try another portID");
        }

        if (TerminalUtil.coordinateAlreadyTaken(latitude, longitude)) {
            throw new IllegalArgumentException("Coordinate already taken by a port, try another latitude or longitude");
        }

        this.portID = "p-" + portID;
        this.portName = portName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storingCapacity = storingCapacity;
        this.landingAbility = landingAbility;
        TerminalUtil.addPort(this); // Add this Port to the collection of ports

        // Save id
        TerminalUtil.addId(this.portID);
        LogManager.saveUsedIds();

        // Save object
        LogManager.saveAllObjects();

    }

    public String getPortID() {
        return portID;
    }

    public String getPortName() {
        return portName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getStoringCapacity() {
        return storingCapacity;
    }

    public boolean getLandingAbility() {
        return landingAbility;
    }

    public List<Vehicle> getPortVehicles() {
        return portVehicles;
    }

    public List<Container> getPortContainers() {
        return portContainers;
    }

    public static double calculateDistanceBetweenPort(Port target_1, Port target_2) {
        // To calculate the distance using latitude and longitude, use haversine formula
        double R = 6371; // Earth's radius in kilometers
        double dLat = Math.toRadians(target_2.latitude - target_1.latitude);
        double dLon = Math.toRadians(target_2.longitude - target_1.longitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(target_1.latitude)) * Math.cos(Math.toRadians(target_2.latitude)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public void addVehicle(Vehicle vehicleToAdd) {
        portVehicles.add(vehicleToAdd);
    }

    public void departureVehicle(Vehicle vehicleToRemove) { // Method to remove a vehicle when it moves
        portVehicles.remove(vehicleToRemove);
    }

    public void loadContainer(Container containerToLoad) {
        portContainers.add(containerToLoad);
    }

    public void unloadContainer(Container containerToUnload) {
        // Although a Port can unload its container, when moving Containers between vehicles and ports, the operation can only be called in Vehicle to ensure synchronization
        portContainers.remove(containerToUnload);
    }

    public double getTotalCarryingWeight() { // Get total weight of all containers in the port
        double totalWeight = 0;
        for (Container container: portContainers) {
            totalWeight += container.getWeight();
        } return totalWeight;
    }

    public Vehicle searchVehicle(String vehicleID) { // Search for a vehicle based on vehicleID in the port
        for (Vehicle vehicle: portVehicles) {
            if (vehicle.getVehicleID().equals(vehicleID)) {
                return vehicle;
            }
        } return null;
    }

    public Container searchContainer(String containerID) { // Search for a container based on containerID in the port
        for (Container container: portContainers) {
            if (container.getContainerID().equals(containerID)) {
                return container;
            }
        } return null;
    }

    public boolean ableToLoadContainer(Container containerToLoad) {
        return getTotalCarryingWeight() + containerToLoad.getWeight() <= storingCapacity;
    }

    public boolean isTargetPort() {
        for (Log log: TerminalUtil.occurringLogs) {
            if (Objects.equals(log.getArrivalPortID(), portID)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder vehicleIDs = new StringBuilder();
        vehicleIDs.append("[");
        for (Vehicle vehicle : portVehicles) {
            if (vehicleIDs.length() > 1) {
                vehicleIDs.append(", ");
            }
            vehicleIDs.append(vehicle.getVehicleID());
        }
        vehicleIDs.append("]");

        StringBuilder containersIDs = new StringBuilder();
        containersIDs.append("[");
        for (Container container : portContainers) {
            if (containersIDs.length() > 1) {
                containersIDs.append(", ");
            }
            containersIDs.append(container.getContainerID());
        }
        containersIDs.append("]");

        return "Port{" +
                "portID='" + portID + '\'' +
                ", portName='" + portName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", storingCapacity=" + storingCapacity +
                ", landingAbility=" + landingAbility +
                ", portVehicles=" + vehicleIDs.toString() +
                ", portContainers=" + containersIDs.toString() +
                '}';
    }
}
