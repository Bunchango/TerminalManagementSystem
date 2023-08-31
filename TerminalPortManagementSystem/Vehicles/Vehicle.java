package TerminalPortManagementSystem.Vehicles;
import TerminalPortManagementSystem.ContainerType;
import TerminalPortManagementSystem.Ports.Container;
import TerminalPortManagementSystem.Ports.Port;
import TerminalPortManagementSystem.Utility.Log;
import TerminalPortManagementSystem.Utility.LogManager;
import TerminalPortManagementSystem.Utility.StatQuery;
import TerminalPortManagementSystem.Utility.TerminalUtil;
import TerminalPortManagementSystem.VehicleType;

import java.io.Serializable;
import java.util.*;

public abstract class Vehicle implements Serializable {
    private final String vehicleID;
    private final VehicleType vehicleType;
    private Port currentPort;
    private double currentFuel = 0;
    private final double carryingCapacity;
    private final double fuelCapacity;
    private final List<Container> vehicleContainers = new ArrayList<>();
    Vehicle(String vehicleID, VehicleType vehicleType, Port currentPort, double carryingCapacity, double fuelCapacity) {
        // Check for illogical sets
        if (TerminalUtil.objectAlreadyExist(vehicleID)) {
            throw new IllegalArgumentException("Vehicle already exist, try another vehicleID");
        }

        if (currentPort == null) {
            throw new IllegalArgumentException("Invalid port initialization");
        }

        if (!currentPort.getLandingAbility() && vehicleType.isTruck()) {
            throw new IllegalArgumentException("Invalid port initialization, this port does not have landing ability");
        }

        this.vehicleID = vehicleID;
        this.vehicleType = vehicleType;
        this.currentPort = currentPort;
        this.carryingCapacity = carryingCapacity;
        this.fuelCapacity = fuelCapacity;
        TerminalUtil.addVehicle(this);

        // Add new vehicle to the port
        currentPort.addVehicle(this);

        // Save object
        LogManager.saveAllObjects();
        // Save id
        TerminalUtil.addId(this.vehicleID);
        LogManager.saveUsedIds();
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Port getCurrentPort() {
        return currentPort;
    }

    public double getCurrentFuel() {
        return currentFuel;
    }

    public double getCarryingCapacity() {
        return carryingCapacity;
    }

    public double getFuelCapacity() {
        return fuelCapacity;
    }

    public List<Container> getVehicleContainers() {
        return vehicleContainers;
    }

    public double getTotalCarryingWeight() {
        double totalWeight = 0;
        for (Container container: vehicleContainers) {
            totalWeight += container.getWeight();
        } return totalWeight;
    }

    public Map<ContainerType, Double> getTotalCarryingWeightByType() {
        Map<ContainerType, Double> weightByType =  new HashMap<>();
        for (ContainerType containerType: vehicleType.getAllowedContainerType()) {
            double totalWeight = 0;
            for (Container container: vehicleContainers) {
                if (container.getContainerType() == containerType) {
                    totalWeight += container.getWeight();
                }
            }
            weightByType.put(containerType, totalWeight);
        } return weightByType;
    }

    public void setCurrentPort(Port port) {
        this.currentPort = port;
    }

    public Container searchContainer(String containerID) {
        for (Container container: vehicleContainers) {
            if (container.getContainerID().equals(containerID)) {
                return container;
            }
        } return null;
    }


    //TODO: Make loadContainer and unloadContainer return a String
    public String loadContainer(Container containerToLoad) {
        if (!ableToLoadContainer(containerToLoad)) {
            return "Invalid container - This vehicle can't load this vehicle";
        }

        if (isSailAway()) {
            return "Vehicle can't load when sail away";
        }

        if (currentPort.searchContainer(containerToLoad.getContainerID()) == null) {
            return "Invalid container - Container and Vehicle must be in the same port";
        }

        vehicleContainers.add(containerToLoad);
        currentPort.unloadContainer(containerToLoad);

        // Update fuel consumed if vehicle is scheduled but has not moved
        TerminalUtil.updateLogWhenTransportContainer(this);
        // Save object
        LogManager.saveAllObjects();
        return "Container loaded";
    }

    public String unloadContainer(Container containerToUnload) {
        if (!currentPort.ableToLoadContainer(containerToUnload)) {
            return "Invalid container - This port can't load this container";
        }

        if (searchContainer(containerToUnload.getContainerID()) == null) {
            return "Invalid container - Given container does not exist on this vehicle";
        }

        if (isSailAway()) {
            return "Vehicle can't unload when sail away";
        }

        currentPort.loadContainer(containerToUnload);
        vehicleContainers.remove(containerToUnload);

        // Update fuel consumed if vehicle is scheduled but has not moved
        TerminalUtil.updateLogWhenTransportContainer(this);
        // Save object
        LogManager.saveAllObjects();

        return "Container unloaded";
    }

    public void removeContainer(Container containerToRemove) {
        vehicleContainers.remove(containerToRemove);
    }

    public boolean ableToLoadContainer(Container containerToLoad) {
        return getTotalCarryingWeight() + containerToLoad.getWeight() <= carryingCapacity && vehicleType.doesAllowContainerType(containerToLoad);
    }

    public String refuel(double gallons) { // Can only refuel in a port
        if (!isSailAway()) {
            if (currentFuel + gallons <= fuelCapacity) {
                currentFuel += gallons;
            } else {
                currentFuel = fuelCapacity;
            }
            return "Vehicle refuel successfully";
        } else {
            return vehicleType + " is not at a Port to be refueled";
        }
    }

    public double calculateFuelConsumption(Port destinationPort) {
        double fuelConsumption = 0;
        double distance = Port.calculateDistanceBetweenPort(currentPort, destinationPort);
        Map<ContainerType, Double> weightByType = getTotalCarryingWeightByType();
        for (ContainerType containerType: weightByType.keySet()) {
            if (vehicleType.isShip()) {
                // Convert from kg to ton
                fuelConsumption += containerType.getShipFuelCost() * weightByType.get(containerType) / 1000 * distance;
            } else {
                fuelConsumption += containerType.getTruckFuelCost() * weightByType.get(containerType) / 1000 * distance;
            }
        }
        return fuelConsumption;
    }

    public String moveToPort(Port destinationPort, Date departureDate, Date arrivalDate) {
        if (!TerminalUtil.passedDate(arrivalDate, departureDate)) {
            return "Invalid arrival date - arrivalDate is before DepartureDate";
        }

        if (TerminalUtil.passedDate(TerminalUtil.getNow(), departureDate)) {
            return "Invalid departure date - departureDate is before current time";
        }

        if (isScheduled()) { // Scheduled does not mean it is sail away
            return "This " + vehicleType + " is already scheduled to move to a port";
        }

        if (isSailAway()) { // If it is sail away, it means it is scheduled
            return "This " + vehicleType + " is already moving to a port";
        }

        if (destinationPort == null) {
            return "Invalid destination: Cannot move to null";
        } else if (destinationPort == currentPort) {
            return "Invalid destination: Cannot move to the same port";
        }

        double fuelCost = TerminalUtil.roundToSecondDecimalPlace(calculateFuelConsumption(destinationPort));

        if (!ableToMoveToPort(destinationPort)) {
            return "This " + vehicleType + " cannot move to this Port\n" +
                    "Destination port landing ability: " + destinationPort.getLandingAbility() + "\n" +
                    "Destination port capacity if vehicle docks: " +
                    (destinationPort.getTotalCarryingWeight() + getTotalCarryingWeight()) + "/" + destinationPort.getStoringCapacity() + "\n" +
                    "Required Gas: " + currentFuel + "/" + fuelCost;
        }

        // Save log
        Log log = new Log(vehicleID, departureDate, arrivalDate, currentPort.getPortID(), destinationPort.getPortID(), fuelCost, false);
        TerminalUtil.addOccurringLog(log);

        // Set ship to sail away
        if (TerminalUtil.passedDate(TerminalUtil.getNow(), departureDate)) {
            currentPort.departureVehicle(this);
            currentPort = null;
        }

        // Save object
        LogManager.saveAllObjects();

        return vehicleType + " moved successfully";
    }

    public String arriveToPort(Port destinationPort, double fuelCost) {
        currentPort = destinationPort;
        currentFuel = TerminalUtil.roundToSecondDecimalPlace(getCurrentFuel() - fuelCost);
        currentPort.addVehicle(this);

        return vehicleType + " arrived to Port";
    }

    public abstract boolean ableToMoveToPort(Port destinationPort);

    public boolean isSailAway() {
        return currentPort == null;
    }

    public boolean isScheduled() {
        return StatQuery.getOccurringLogByVehicle(vehicleID) != null;
    }

    @Override
    public String toString() {
        StringBuilder containersIDs = new StringBuilder();
        containersIDs.append("[");
        for (Container container : vehicleContainers) {
            if (containersIDs.length() > 1) {
                containersIDs.append(", ");
            }
            containersIDs.append(container.getContainerID());
        }
        containersIDs.append("]");

        return "Vehicle" + "{" +
                "vehicleID='" + vehicleID + '\'' +
                ", vehicleType=" + vehicleType +
                ", currentPort=" + (currentPort != null ? currentPort.getPortName() : "null") +
                ", currentFuel=" + currentFuel +
                ", carryingCapacity=" + carryingCapacity +
                ", fuelCapacity=" + fuelCapacity +
                ", portContainers=" + containersIDs.toString() +
                '}';
    }
}