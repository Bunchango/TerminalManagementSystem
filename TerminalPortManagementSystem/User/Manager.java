package TerminalPortManagementSystem.User;

import TerminalPortManagementSystem.ContainerType;
import TerminalPortManagementSystem.Ports.Container;
import TerminalPortManagementSystem.Ports.Port;
import TerminalPortManagementSystem.Utility.TerminalUtil;
import TerminalPortManagementSystem.VehicleType;
import TerminalPortManagementSystem.Vehicles.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Manager implements Serializable, User { // Update delete function later, when delete port, also delete manager
    private final String username;
    private final String password;
    public final UserType userType = UserType.Manager;
    private final String managePortID;

    public Manager(String username, String password, String managePortID) {
        this.username = username;
        this.password = password;
        this.managePortID = managePortID;
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public UserType getUserType() {
        return userType;
    }

    public String getManagePortID() {
        return managePortID;
    }

    //TODO: More work, exception handling
    public String createContainer(String containerID, String containerType, double weight) {
        Port port = TerminalUtil.searchPort(managePortID);

        if (port == null) {
            return "Manager currently does not manage any port";
        }

        if (TerminalUtil.objectAlreadyExist(containerID)) {
            return "Invalid container ID";
        }

        if (port.getTotalCarryingWeight() + weight > port.getStoringCapacity()) {
            return "Weight exceed port storing capacity";
        }

        ContainerType type = ContainerType.fromString(containerType);
        new Container(containerID, type, port, weight);

        return "Object created";
    }

    public String removeContainer(String containerID) {
        Port port = TerminalUtil.searchPort(managePortID);

        if (port == null) {
            return "Manager currently does not manage any port";
        }

        if (!TerminalUtil.objectAlreadyExist(containerID)) {
            return "Invalid container ID";
        }

        if (port.searchContainer(containerID) == null) {
            return "Container does not exist in this port";
        }

        return TerminalUtil.removeContainer(containerID);
    }

    public String loadContainer(String vehicleID, String containerID) {
        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);
        Container container = TerminalUtil.searchContainer(containerID);

        if (vehicle == null) {
            return "Vehicle does not exist";
        }

        if (!Objects.equals(vehicle.getCurrentPort().getPortID(), managePortID)) {
            return "Manager is not allowed to control this vehicle";
        }

        if (container == null) {
            return "Container does not exist";
        }

        if (vehicle.getCurrentPort().searchContainer(containerID) == null) {
            return "Container does not exist in the managing port";
        }

        vehicle.loadContainer(container);

        return "Container loaded";
    }

    public String unloadContainer(String vehicleID, String containerID) {
        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);
        Container container = TerminalUtil.searchContainer(containerID);

        if (vehicle == null) {
            return "Vehicle does not exist";
        }

        if (!Objects.equals(vehicle.getCurrentPort().getPortID(), managePortID)) {
            return "Manager is not allowed to control this vehicle";
        }

        if (container == null) {
            return "Container does not exist";
        }

        if (vehicle.searchContainer(containerID) == null) {
            return "Container does not exist on this vehicle";
        }

        vehicle.unloadContainer(container);

        return "Container unloaded";
    }

    public String refuelVehicle(String vehicleID, double gallons) {
        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);

        if (vehicle == null) {
            return "Vehicle does not exist";
        }

        if (!Objects.equals(vehicle.getCurrentPort().getPortID(), managePortID)) {
            return "Manager is not allowed to control this vehicle";
        }

        return vehicle.refuel(gallons);
    }

    public String moveToPort(String vehicleID, String destinationPortID, String departure, String arrival) {
        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);
        Port destinationPort = TerminalUtil.searchPort(destinationPortID);
        Date departureDate = TerminalUtil.parseStringToDateTime(departure);
        Date arrivalDate = TerminalUtil.parseStringToDateTime(arrival);

        if (vehicle == null) {
            return "Vehicle does not exist";
        }

        if (!Objects.equals(vehicle.getCurrentPort().getPortID(), managePortID)) {
            return "Manager is not allowed to control this vehicle";
        }

        return vehicle.moveToPort(destinationPort, departureDate, arrivalDate);
    }

    // Statistics

}
