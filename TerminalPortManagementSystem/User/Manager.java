package TerminalPortManagementSystem.User;

import TerminalPortManagementSystem.ContainerType;
import TerminalPortManagementSystem.Ports.Container;
import TerminalPortManagementSystem.Ports.Port;
import TerminalPortManagementSystem.Utility.Log;
import TerminalPortManagementSystem.Utility.StatQuery;
import TerminalPortManagementSystem.Utility.TerminalUtil;
import TerminalPortManagementSystem.VehicleType;
import TerminalPortManagementSystem.Vehicles.*;

import java.io.Serializable;
import java.util.*;

// TODO: When delete a port, reset managePortID of manager; Add null checking for managePortID

public class Manager implements Serializable, User { // Update delete function later, when delete port, also delete manager
    private final String username;
    private final String password;
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
    public int getUserType() {
        return 0;
    }

    public String getManagePortID() {
        return managePortID;
    }

    public boolean isNotManaging() {
        return managePortID == null;
    }

    //TODO: More work, exception handling, complete static query and extract log

    //Create object
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

        if (type == null) {
            return "Invalid type";
        }

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


    // Transporting
    public String loadContainer(String vehicleID, String containerID) {
        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);
        Container container = TerminalUtil.searchContainer(containerID);

        if (vehicle == null) {
            return "Vehicle does not exist";
        }

        if (isNotManaging()) {
            return "Manager currently does not manage any port";
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

        if (isNotManaging()) {
            return "Manager currently does not manage any port";
        }

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

        if (isNotManaging()) {
            return "Manager currently does not manage any port";
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

        if (departureDate == null || arrivalDate == null) {
            return "Invalid date format";
        }

        if (isNotManaging()) {
            return "Manager currently does not manage any port";
        }

        if (vehicle == null) {
            return "Vehicle does not exist";
        }

        if (!Objects.equals(vehicle.getCurrentPort().getPortID(), managePortID)) {
            return "Manager is not allowed to control this vehicle";
        }

        return vehicle.moveToPort(destinationPort, departureDate, arrivalDate);
    }

    // Statistics
    public Map<Date, Double> getTotalFuelConsumedPerDay() {
        if (isNotManaging()) {
            return null;
        }

        return StatQuery.totalFuelConsumedPerDayOfPort(managePortID);
    }

    public double getTotalConsumedFuelByDay(String date){
        Date dateToQuery = TerminalUtil.parseStringToDateTime(date);

        if (dateToQuery == null) {
            return 0;
        }

        if (isNotManaging()) {
            return 0;
        }

        return StatQuery.totalFuelConsumedByDayByPort(dateToQuery, managePortID);
    }

    public Map<ContainerType, Double> getTotalWeightOfEachType(){
        if (isNotManaging()) {
            return null;
        }

        return StatQuery.getTotalWeightOfEachTypeByPort(managePortID);
    }

    public Map<ContainerType, Integer> getNumberOfContainerOfEachType(){
        if (isNotManaging()) {
            return null;
        }

        return StatQuery.getNumberOfContainerOfEachTypeByPort(managePortID);
    }

    public List<Container> getListOfAllContainers(){
        if (isNotManaging()) {
            return null;
        }

        return StatQuery.getListOfContainerByPort(managePortID);
    }

    public Map<VehicleType, Integer> getNumberOfVehicleOfEachType() {
        if (isNotManaging()) {
            return null;
        }

        return StatQuery.getNumberOfVehicleOfEachTypeByPort(managePortID);
    }

    public List<Vehicle> getListOfVehicleByType(String vehicleType){
        if (isNotManaging()) {
            return null;
        }

        return StatQuery.getListOfVehicleByTypeOfPort(managePortID, vehicleType);
    }

    public List<Vehicle> getListOfAllVehicles() {
        if (isNotManaging()) {
            return null;
        }

        return StatQuery.getListOfVehicleByPort(managePortID);
    }

    public List<Log> getTripsByDate(String date){
        Date dateToQuery = TerminalUtil.parseStringToDateTime(date);

        if (dateToQuery == null) {
            return null;
        }

        if (isNotManaging()) {
            return null;
        }

        return StatQuery.getTripsByDateOfPort(dateToQuery, managePortID);
    }

    public List<Log> getTripsBetweenDates(String start, String end){
        Date startDate = TerminalUtil.parseStringToDateTime(start);
        Date endDate = TerminalUtil.parseStringToDateTime(end);

        if (startDate == null || endDate == null) {
            return null;
        }

        if (isNotManaging()) {
            return null;
        }

        return StatQuery.getTripsBetweenDatesOfPort(startDate, endDate, managePortID);
    }
}
