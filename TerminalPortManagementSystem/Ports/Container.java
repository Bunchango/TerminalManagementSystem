package TerminalPortManagementSystem.Ports;

import TerminalPortManagementSystem.ContainerType;
import TerminalPortManagementSystem.Utility.LogManager;
import TerminalPortManagementSystem.Utility.TerminalUtil;
import TerminalPortManagementSystem.Vehicles.Vehicle;

import java.io.Serializable;

public class Container implements Serializable {
    private String containerID;
    private final ContainerType containerType;
    private final double weight;

    public Container(String containerID, ContainerType containertype, Port port, double weight) {
        // Prevent illogical object creation
        if (TerminalUtil.objectAlreadyExist("c-" + containerID)) {
            throw new IllegalArgumentException("Container already exist, try another containerID");
        }

        if (port == null) {
            throw new IllegalArgumentException("Container must be in a Port when initialized");
        } else {
            if (port.getTotalCarryingWeight() + weight > port.getStoringCapacity()) {
                throw new IllegalArgumentException("Weight exceed port storing capacity");
            } else {
                // Add this Container to the port
                port.loadContainer(this);
            }
        }
        // Create object
        this.containerID = "c-" + containerID;
        this.containerType = containertype;
        this.weight = weight;
        TerminalUtil.addContainer(this);

        // Save id
        TerminalUtil.addId(this.containerID);
        LogManager.saveUsedIds();

        // Save object
        LogManager.saveAllObjects();
    }

    // Getters
    public String getContainerID() {
        return containerID;
    }

    public ContainerType getContainerType() {
        return containerType;
    }

    public double getWeight() {
        return weight;
    }

    // Setter
    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    @Override
    public String toString() {
        return "Container{" +
                "containerID='" + containerID + '\'' +
                ", containerType=" + containerType +
                ", weight=" + weight +
                '}';
    }
}