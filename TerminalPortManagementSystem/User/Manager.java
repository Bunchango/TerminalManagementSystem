package TerminalPortManagementSystem.User;

import TerminalPortManagementSystem.ContainerType;
import TerminalPortManagementSystem.Ports.Container;
import TerminalPortManagementSystem.Ports.Port;
import TerminalPortManagementSystem.Utility.Log;
import TerminalPortManagementSystem.Utility.LogManager;
import TerminalPortManagementSystem.Utility.StatQuery;
import TerminalPortManagementSystem.Utility.TerminalUtil;
import TerminalPortManagementSystem.VehicleType;
import TerminalPortManagementSystem.Vehicles.*;

import java.io.Serializable;
import java.util.*;

public class Manager implements Serializable, User {
    private final String username;
    private final String password;
    private String managePortID;

    public Manager(String username, String password, String managePortID) {
        // Prevent illogical object creation
        if (TerminalUtil.objectAlreadyExist(username)) {
            throw new IllegalArgumentException("User already exist");
        }

        if (TerminalUtil.portIsManaged(managePortID)) {
            throw new IllegalArgumentException("Port already managed by a manager");
        }

        if (TerminalUtil.searchPort(managePortID) == null) {
            throw new IllegalArgumentException("Port does not exist");
        }

        this.username = username;
        this.password = password;
        this.managePortID = managePortID;

        // Save
        TerminalUtil.addId(this.username);
        TerminalUtil.addManager(this);
        LogManager.saveAllObjects();
        LogManager.saveUsedIds();
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
    public boolean isManager() {
        return true;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    public String setManagePortID(String portID) {
        // Can only set portID if the target port is not already manage
        if (TerminalUtil.portIsManaged(portID)) {
            return "Invalid portID. Port already managed by a Manager";
        }

        managePortID = portID;
        return "Set manager's port successfully";
    }

    public String getManagePortID() {
        return managePortID;
    }

    public boolean isNotManaging() {
        // Prevent manager from doing anything if that manager does not manage any port
        return managePortID == null;
    }

    // Create object
    public String createContainer(String containerID, String containerType, double weight) {
        Port port = TerminalUtil.searchPort(managePortID);

        // Null checking
        if (port == null) {
            return "Manager currently does not manage any port";
        }

        if (TerminalUtil.objectAlreadyExist("c-" + containerID)) {
            return "Invalid container ID - Container already exist";
        }

        if (port.getTotalCarryingWeight() + weight > port.getStoringCapacity()) {
            return "Invalid container weight - Weight exceed port storing capacity";
        }

        ContainerType type = ContainerType.fromString(containerType);

        if (type == null) {
            return "Invalid type";
        }

        new Container(containerID, type, port, weight);

        return "Container created";
    }

    public String removeContainer(String containerID) {
        Port port = TerminalUtil.searchPort(managePortID);

        // Null checking
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

        // Null checking
        if (vehicle == null) {
            return "Invalid vehicleID - Vehicle does not exist";
        }

        if (isNotManaging()) {
            return "Manager currently does not manage any port";
        }

        if (!Objects.equals(vehicle.getCurrentPort().getPortID(), managePortID)) {
            return "Invalid vehicleID - Manager is not allowed to control this vehicle";
        }

        if (container == null) {
            return "Invalid containerID - Container does not exist";
        }

        return vehicle.loadContainer(container);
    }

    public String unloadContainer(String vehicleID, String containerID) {
        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);
        Container container = TerminalUtil.searchContainer(containerID);

        // Null checking
        if (isNotManaging()) {
            return "Manager currently does not manage any port";
        }

        if (vehicle == null) {
            return "Invalid vehicleID - Vehicle does not exist";
        }

        if (!Objects.equals(vehicle.getCurrentPort().getPortID(), managePortID)) {
            return "Manager is not allowed to control this vehicle";
        }

        if (container == null) {
            return "Invalid containerID - Container does not exist";
        }

        return vehicle.unloadContainer(container);
    }

    public String refuelVehicle(String vehicleID, double gallons) {
        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);

        // Null checking
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

        // Null checking
        if (departureDate == null || arrivalDate == null) {
            return "Invalid date format";
        }

        if (isNotManaging()) {
            return "Manager currently does not manage any port";
        }

        if (vehicle == null) {
            return "Invalid vehicleID - Vehicle does not exist";
        }

        if (!Objects.equals(vehicle.getCurrentPort().getPortID(), managePortID)) {
            return "Invalid vehicleID - Manager is not allowed to control this vehicle";
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

    public double getTotalConsumedFuelByDate(String date){
        Date dateToQuery = TerminalUtil.parseStringToDate(date);

        // Null checking
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

    public List<Log> getTripsByArrivalDate(String date){
        Date dateToQuery = TerminalUtil.parseStringToDate(date);

        // Null checking
        if (dateToQuery == null) {
            return null;
        }

        if (isNotManaging()) {
            return null;
        }

        return StatQuery.getTripsByArrivalDateOfPort(dateToQuery, managePortID);
    }

    public List<Log> getTripsByDepartureDate(String date) {
        Date dateToQuery = TerminalUtil.parseStringToDate(date);

        // Null checking
        if (dateToQuery == null) {
            return null;
        }

        if (isNotManaging()) {
            return null;
        }

        return StatQuery.getTripsByDepartureDateOfPort(dateToQuery, managePortID);
    }

    public List<Log> getTripsBetweenArrivalDates(String start, String end){
        Date startDate = TerminalUtil.parseStringToDate(start);
        Date endDate = TerminalUtil.parseStringToDate(end);

        // Null checking
        if (startDate == null || endDate == null) {
            return null;
        }

        if (isNotManaging()) {
            return null;
        }

        return StatQuery.getTripsBetweenArrivalDatesOfPort(startDate, endDate, managePortID);
    }

    public List<Log> getTripsBetweenDepartureDates(String start, String end) {
        Date startDate = TerminalUtil.parseStringToDate(start);
        Date endDate = TerminalUtil.parseStringToDate(end);

        // Null checking
        if (startDate == null || endDate == null) {
            return null;
        }

        if (isNotManaging()) {
            return null;
        }

        return StatQuery.getTripsBetweenDepartureDatesOfPort(startDate, endDate, managePortID);
    }

    public List<Log> getTripsInDatesOfPort(String start, String end) {
        Date startDate = TerminalUtil.parseStringToDate(start);
        Date endDate = TerminalUtil.parseStringToDate(end);

        // Null checking
        if (startDate == null || endDate == null) {
            return null;
        }

        if (isNotManaging()) {
            return null;
        }

        return StatQuery.getTripsInDatesOfPort(startDate, endDate, managePortID);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", managePortID='" + managePortID + '\'' +
                '}';
    }
}
