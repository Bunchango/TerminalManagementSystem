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
    // Vehicle can load and unload container as long as it is not moving (sail away)
    public void loadContainer(Container containerToLoad) {
        if (ableToLoadContainer(containerToLoad) && !isSailAway() &&
                (currentPort.searchContainer(containerToLoad.getContainerID()) != null)) {
            vehicleContainers.add(containerToLoad);
            currentPort.unloadContainer(containerToLoad);

            // Save object
            LogManager.saveAllObjects();
        } else {
            System.out.println("This " + vehicleType + " might be sail away OR given container might not exist in the port OR given container might not be suitable for this type of vehicle");
        }
    }

    public void unloadContainer(Container containerToUnload) {
        if (currentPort.ableToLoadContainer(containerToUnload) && searchContainer(containerToUnload.getContainerID()) != null && !isSailAway()) {
            currentPort.loadContainer(containerToUnload);
            vehicleContainers.remove(containerToUnload);

            // Save object
            LogManager.saveAllObjects();
        } else {
            System.out.println("Target port is not able to have more containers OR container does not exist in this " + vehicleType);
        }
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
            return "Invalid arrival date";
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