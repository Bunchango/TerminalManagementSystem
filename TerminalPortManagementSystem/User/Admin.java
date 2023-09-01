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

import java.util.Date;
import java.util.List;
import java.util.Map;

// Don't need to save Admin since there always 1
public class Admin implements User {
    private final String username = "admin";
    private final String password = "admin12345";
    private static final Admin instance = new Admin();

    public static Admin getInstance() {
        return instance;
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
        return false;
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    //create obj
    public String createManager(String username, String password, String managePortID){
        if (TerminalUtil.objectAlreadyExist(username)) {
            return "Invalid username - This user already exist";
        }

        if (TerminalUtil.portIsManaged(managePortID)) {
            return "Invalid port - This port is already managed by a manager";
        }

        if (TerminalUtil.searchPort(managePortID) == null) {
            return "Port does not exist";
        }

        new Manager(username, password, managePortID);
        return "Manager created";
    }

    public String createPort(String portID, String portName, double latitude, double longitude, double storingCapacity,
                             boolean landingAbility){

        if (TerminalUtil.objectAlreadyExist("p-" + portID)) {
            return "Invalid portID - This port already exist";
        }

        if (TerminalUtil.coordinateAlreadyTaken(latitude, longitude)) {
            return "Invalid coordinate - latitude and longitude is already taken";
        }

        new Port(portID, portName, latitude, longitude, storingCapacity, landingAbility);
        return "Port created";
    }

    public String createVehicle(String vehicleID, String vehicleType, String portID, double carryingCapacity,
                                double fuelCapacity){
        Port currentPort = TerminalUtil.searchPort(portID);
        VehicleType type = VehicleType.fromString(vehicleType);

        if (currentPort == null) {
            return "Invalid portID - This port does not exist";
        }

        if (type == null) {
            return "Invalid vehicleType";
        }

        if (type.isShip()) {
            if (TerminalUtil.objectAlreadyExist("sh-" + vehicleID)) {
                return "Invalid vehicleID - This ship already exist";
            }
        } else if (type.isTruck()) {
            if (TerminalUtil.objectAlreadyExist("tr-" + vehicleID)) {
                return "Invalid vehicleID - This vehicle already exist";
            }
        }

        if (!currentPort.getLandingAbility() && type.isTruck()) {
            return "Invalid vehicleType - This port does not have landing ability";
        }

        if (type == VehicleType.Ship) {
            new Ship(vehicleID, currentPort, carryingCapacity, fuelCapacity);
        } else if (type == VehicleType.BasicTruck) {
            new BasicTruck(vehicleID, currentPort, carryingCapacity, fuelCapacity);
        } else if (type == VehicleType.ReeferTruck) {
            new ReeferTruck(vehicleID, currentPort, carryingCapacity, fuelCapacity);
        } else if (type == VehicleType.TankerTruck) {
            new TankerTruck(vehicleID, currentPort, carryingCapacity, fuelCapacity);
        }

        return "Vehicle created successfully";
    }

    public String createContainer(String containerID, String containerType, String portID, double weight) {
        Port port = TerminalUtil.searchPort(portID);

        if (port == null) {
            return "Invalid portID - This port does not exist";
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

    // Set manager's manage port
    public String setManagerPort(String username, String portID) {
        Manager manager = TerminalUtil.searchManager(username);
        Port port = TerminalUtil.searchPort(portID);

        if (manager == null) {
            return "Invalid username - Manager does not exist";
        }

        if (port == null) {
            return "Invalid portID - Port does not exist";
        }

        if (TerminalUtil.portIsManaged(portID)) {
            return "Invalid portID - Port is already managed by a manager";
        }

        manager.setManagePortID(portID);
        // Save
        LogManager.saveAllObjects();
        return "Managing port set successfully";
    }

    public String unsetManagerPort(String username) {
        Manager manager = TerminalUtil.searchManager(username);

        if (manager == null) {
            return "Invalid username - Manager does not exist";
        }
        if (manager.getManagePortID() == null) {
            return "Managing port is already unset";
        }

        manager.setManagePortID(null);
        return "Managing port unset successfully";
    }

    // Remove obj
    public String removeManager(String username) {
        return TerminalUtil.removeManager(username);
    }

    public String removePort(String portID) {
        return TerminalUtil.removePort(portID);
    }

    public String removeVehicle(String vehicleID) {
        return TerminalUtil.removeVehicle(vehicleID);
    }

    public String removeContainer(String containerID) {
        return TerminalUtil.removeContainer(containerID);
    }

    // Transporting
    public String loadContainer(String vehicleID, String containerID) {
        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);
        Container container = TerminalUtil.searchContainer(containerID);

        if (vehicle == null) {
            return "Invalid vehicleID - Vehicle does not exist";
        }

        if (container == null) {
            return "Invalid containerID - Container does not exist";
        }

        return vehicle.loadContainer(container);
    }

    public String unloadContainer(String vehicleID, String containerID) {
        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);
        Container container = TerminalUtil.searchContainer(containerID);

        if (vehicle == null) {
            return "Invalid vehicleID - Vehicle does not exist";
        }

        if (container == null) {
            return "Invalid containerID - Container does not exist";
        }

        return vehicle.unloadContainer(container);
    }

    public String refuelVehicle(String vehicleID, double gallons) {
        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);

        if (vehicle == null) {
            return "Invalid vehicleID - Vehicle does not exist";
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

        if (vehicle == null) {
            return "Invalid vehicleID - Vehicle does not exist";
        }

        if (destinationPort == null) {
            return "Invalid destinationPortID - Port does not exist";
        }

        return vehicle.moveToPort(destinationPort, departureDate, arrivalDate);
    }

    // Statistic
    public Map<Date, Double> totalFuelConsumedPerDay(){
        return StatQuery.totalFuelConsumedPerDay();
    }

    public Map<Date, Double> totalFuelConsumedPerDayOfPort(String portID) {
        return StatQuery.totalFuelConsumedPerDayOfPort(portID);
    }

    public double getTotalConsumedFuelByDate(String date){
        Date dateToQuery = TerminalUtil.parseStringToDateTime(date);

        if (dateToQuery == null) {
            return 0;
        }

        return StatQuery.totalFuelConsumedByDay(dateToQuery);
    }

    public double getTotalConsumedFuelByDayByPort(String portID, String date) {
        Date dateToQuery = TerminalUtil.parseStringToDateTime(date);

        if (dateToQuery == null) {
            return 0;
        }

        return StatQuery.totalFuelConsumedByDayByPort(dateToQuery, portID);
    }

    public Map<ContainerType, Double> getTotalWeightOfEachType() {
        return StatQuery.getTotalWeightOfEachType();
    }

    public Map<ContainerType, Double> getTotalWeightOfEachTypeByPort(String portID) {
        return StatQuery.getTotalWeightOfEachTypeByPort(portID);
    }

    public Map<ContainerType, Integer> getNumberOfContainerOfEachType() {
        return StatQuery.getNumberOfContainerOfEachType();
    }

    public Map<ContainerType, Integer> getNumberOfContainerOfEachTypeByPort(String portID) {
        return StatQuery.getNumberOfContainerOfEachTypeByPort(portID);
    }

    public List<Container> getListOfAllContainer() {
        return StatQuery.getListOfAllContainer();
    }

    public List<Container> getListOfContainerByPort(String portID) {
        return StatQuery.getListOfContainerByPort(portID);
    }

    public List<Vehicle> getListOfAllVehicle() {
        return StatQuery.getListOfAllVehicle();
    }

    public List<Vehicle> getListOfVehicleByType(String vehicleType){
        return StatQuery.getListOfVehicleByType(vehicleType);
    }

    public List<Vehicle> getListOFVehicleByTypeOfPort(String portID, String vehicleType) {
        return StatQuery.getListOfVehicleByTypeOfPort(portID, vehicleType);
    }

    public Map<VehicleType, Integer> getNumberOfVehicleOfEachType() {
        return StatQuery.getNumberOfVehicleOfEachType();
    }

    public Map<VehicleType, Integer> getNumberOfVehicleOfEachTypeByPort(String portID) {
        return StatQuery.getNumberOfVehicleOfEachTypeByPort(portID);
    }

    public List<Port> getListOfAllPort() {
        return StatQuery.getListOfAllPort();
    }

    public List<Manager> getListOfAllManager(){
        return StatQuery.getListOfAllManager();
    }
    // Extract log
    public List<Log> getTripsByDate(String date){
        Date dateToQuery = TerminalUtil.parseStringToDateTime(date);

        if (dateToQuery == null) {
            return null;
        }

        return StatQuery.getTripsByDate(dateToQuery);
    }

    public List<Log> getTripsByDateOfPort(String date, String portID) {
        Date dateToQuery = TerminalUtil.parseStringToDate(date);

        if (dateToQuery == null) {
            return null;
        }

        return StatQuery.getTripsByDateOfPort(dateToQuery, portID);
    }

    public List<Log> getTripsBetweenDates(String start, String end){
        Date startDate = TerminalUtil.parseStringToDate(start);
        Date endDate = TerminalUtil.parseStringToDate(end);

        if (startDate == null || endDate == null) {
            return null;
        }

        return StatQuery.getTripsBetweenDates(startDate, endDate);
    }

    public List<Log> getTripsBetweenDatesOfPort(String start, String end, String portID) {
        Date startDate = TerminalUtil.parseStringToDate(start);
        Date endDate = TerminalUtil.parseStringToDate(end);

        if (startDate == null || endDate == null) {
            return null;
        }

        return StatQuery.getTripsBetweenDatesOfPort(startDate, endDate, portID);
    }

    public double calculateDistanceBetweenPorts(String target_1, String target_2) {
        Port port_1 = TerminalUtil.searchPort(target_1);
        Port port_2 = TerminalUtil.searchPort(target_2);

        if (port_1 == null || port_2 == null) {
            return 0;
        }
        return Port.calculateDistanceBetweenPort(port_1, port_2);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}