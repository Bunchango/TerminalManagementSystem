package TerminalPortManagementSystem.Utility;

import TerminalPortManagementSystem.Ports.Container;
import TerminalPortManagementSystem.Ports.Port;
import TerminalPortManagementSystem.User.*;
import TerminalPortManagementSystem.Vehicles.Vehicle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TerminalUtil {
    // Automatically load objects as the program runs
    public static final List<Port> ports = LogManager.loadPorts();
    public static final List<Vehicle> vehicles = LogManager.loadVehicles();
    public static final List<Container> containers = LogManager.loadContainers();
    public static final List<Log> occurredLogs = LogManager.loadOccurredLog("Data/History/occurred.obj");
    public static final List<Log> occurringLogs = LogManager.loadOccurredLog("Data/History/occurring.obj");
    public static final List<String> usedIds = LogManager.loadUsedIds();
    public static final List<Manager> managers = LogManager.loadManagers();
    public static final List<String> announcements = new ArrayList<>(); // Keep track of announcements

    public static double roundToSecondDecimalPlace(double number) {
        return (double)Math.round(number * 100.0) / 100.0;
    }

    public static void addPort(Port portToAdd) {
        ports.add(portToAdd);
    }

    public static void addVehicle(Vehicle vehicleToAdd) {
        vehicles.add(vehicleToAdd);
    }

    public static void addContainer(Container containerToAdd) {
        containers.add(containerToAdd);
    }

    public static void addOccurringLog(Log logToAdd) {
        occurringLogs.add(logToAdd);
    }

    public static void addId(String idToAdd) {
        usedIds.add(idToAdd);
    }

    public static void addManager(Manager managerToAdd) {
        managers.add(managerToAdd);
    }

    public static void clearAnnouncement() {
        announcements.clear();
    }
    public static boolean objectAlreadyExist(String id) {
        return usedIds.contains(id);
    }

    public static boolean portIsManaged(String portID) {
        for (Manager manager: managers) {
            if (Objects.equals(manager.getManagePortID(), portID)) {
                return true;
            }
        }
        return false;
    }

    public static boolean coordinateAlreadyTaken(double latitude, double longitude) {
        for (Port port: ports) {
            if (port.getLatitude() == latitude && port.getLongitude() == longitude) {
                return true;
            }
        }
        return false;
    }

    public static Manager searchManager(String username) {
        for (Manager manager: managers) {
            if (manager.getUsername().equals(username)) {
                return manager;
            }
        }
        return null;
    }

    public static Port searchPort(String portID) {
        for (Port port: ports) {
            if (port.getPortID().equals(portID)) {
                return port;
            }
        } return null;
    }

    public static Vehicle searchVehicle(String vehicleID) {
        for (Vehicle vehicle: vehicles) {
            if (vehicle.getVehicleID().equals(vehicleID)) {
                return vehicle;
            }
        } return null;
    }

    public static Container searchContainer(String containerID) {
        for (Container container: containers) {
            if (container.getContainerID().equals(containerID)) {
                return container;
            }
        } return null;
    }

    public static List<String> getVehicleIds() {
        List<String> ids = new ArrayList<>();
        for (String id: usedIds) {
            if (id.contains("sh-") || id.contains("tr-")) {
                ids.add(id);
            }
        }
        return ids;
    }

    public static List<String> getPortIds() {
        List<String> ids = new ArrayList<>();
        for (String id: usedIds) {
            if (id.contains("p-")) {
                ids.add(id);
            }
        }
        return ids;
    }

    public static List<String> getContainerIds() {
        List<String> ids = new ArrayList<>();
        for (String id: usedIds) {
            if (id.contains("c-")) {
                ids.add(id);
            }
        }
        return ids;
    }

    public static List<String> getManagerIds() {
        List<String> ids = new ArrayList<>();
        for (String id: usedIds) {
            if (!id.contains("c-") && !id.contains("p-") && !id.contains("sh-") && !id.contains("tr-")) {
                ids.add(id);
            }
        }
        return ids;
    }

    public static String removeManager(String username) {
        // Remove manager and save immediately
        Manager managerToRemove = searchManager(username);
        if (managerToRemove == null) {
            return "Manager does not exist";
        }

        managers.remove(managerToRemove);
        LogManager.saveAllObjects();
        return "Manager found and deleted";
    }

    public static String removePort(String portID) {
        Port portToRemove = searchPort(portID);
        // Prevent users from removing a port if a port is:
        if (portToRemove == null) {
            return "Port not found";
        }

        if (portToRemove.isTargetPort()) {
            return "Not allowed to remove Port that currently have vehicles going in";
        }

        if (portToRemove.isStartPort()) {
            return "Not allowed to remove Port that currently have vehicles going out";
        }

        // Remove from list of ports
        ports.remove(portToRemove);
        // Remove the port's vehicles
        for (Vehicle vehicleToRemove: portToRemove.getPortVehicles()) {
            vehicles.remove(vehicleToRemove);
            // Remove the vehicles' containers
            for (Container containerToRemove: vehicleToRemove.getVehicleContainers()) {
                // Only need to remove from list of containers since this port and its vehicles won't be accessible
                containers.remove(containerToRemove);
            }
        }
        // Remove the port's containers
        for (Container containerToRemove: portToRemove.getPortContainers()) {
            // Only need to remove from list of containers
            containers.remove(containerToRemove);
        }

        // Reset manager's managePortID
        for (Manager manager: managers) {
            if (manager.getManagePortID().equals(portID)) {
                manager.setManagePortID(null);
            }
        }

        // Save
        LogManager.saveAllObjects();
        return "Port found and deleted";
    }

    public static String removeVehicle(String vehicleID) {
        // When removing vehicles, must also remove all containers in the vehicle
        Vehicle vehicleToRemove = searchVehicle(vehicleID);
        if (vehicleToRemove == null) {
            return "Vehicle not found";
        }

        if (vehicleToRemove.isScheduled()) {
            return "Not allowed to remove vehicle that is scheduled";
        }

        // Remove vehicle from current port
        vehicleToRemove.getCurrentPort().departureVehicle(vehicleToRemove);
        // Remove vehicle from list of vehicles
        vehicles.remove(vehicleToRemove);
        // Remove its containers. Since at this point container won't be accessible, only need to remove containers from list of all containers
        for (Container containerToRemove: vehicleToRemove.getVehicleContainers()) {
            containers.remove(containerToRemove);
        }

        // Save
        LogManager.saveAllObjects();
        return "Vehicle found and deleted";
    }

    // Does not completely remove, can still reference back to the container in the Main code, but end user won't be able to access the object
    public static String removeContainer(String containerID) {
        Container containerToRemove = searchContainer(containerID);
        if (containerToRemove == null) {
            return "Container not found";
        }

        // Remove from ports
        for (Port port: ports) {
            port.unloadContainer(containerToRemove);
        }

        // Remove from vehicle that are not moving
        for (Vehicle vehicle: vehicles) {
            // If vehicle is not scheduled and container exist in the vehicle then remove
            // If vehicle is sail away but container exist then show error
            if (vehicle.searchContainer(containerID) != null && !vehicle.isScheduled()) {
                vehicle.removeContainer(containerToRemove);
            } else if (vehicle.searchContainer(containerID) != null && vehicle.isScheduled()) {
                return "Not allowed to remove container when vehicle is scheduled";
            }
        }

        containers.remove(containerToRemove);
        // Save
        LogManager.saveAllObjects();
        return "Container found and deleted";
    }

    // Updates
    public static String updateContainerId(String containerID, String newContainerID) {
        Container container = TerminalUtil.searchContainer(containerID);
        if (objectAlreadyExist(newContainerID)) {
            return "Invalid new containerID - ID already used";
        }

        if (container == null) {
            return "Invalid containerID - container does not exist";
        }

        // Delete old id
        usedIds.remove(container.getContainerID());
        // Add new id
        usedIds.add(newContainerID);
        container.setContainerID(newContainerID);
        LogManager.saveAllObjects();
        return "Container ID set";
    }

    public static String updateContainerIdOfPort(String portID, String containerID, String newContainerID) {
        Port port = TerminalUtil.searchPort(portID);
        Container container = TerminalUtil.searchContainer(containerID);

        if (objectAlreadyExist(newContainerID)) {
            return "Invalid new containerID - ID already used";
        }

        if (container == null) {
            return "Invalid containerID - container does not exist";
        }

        if (port == null) {
            return "Invalid portID - port does not exist";
        }

        // Delete old id
        usedIds.remove(container.getContainerID());
        // Add new id
        usedIds.add(newContainerID);
        container.setContainerID(newContainerID);
        LogManager.saveAllObjects();
        return "Container ID set";
    }

    public static String updateVehicleID(String vehicleID, String newVehicleID) {
        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);
        if (objectAlreadyExist(newVehicleID)) {
            return "Invalid new vehicleID - ID already used";
        }

        if (vehicle == null) {
            return "Invalid vehicleID - vehicle does not exist";
        }

        if (vehicle.isScheduled()) {
            return "Invalid vehicle - can't update vehicle if it is scheduled";
        }
        // Update log
        for (Log log: occurredLogs) {
            if (log.getVehicleID().equals(vehicle.getVehicleID())) {
                log.setVehicleID(newVehicleID);
            }
        }
        // Delete old id
        usedIds.remove(vehicle.getVehicleID());
        // Add new id
        usedIds.add(newVehicleID);
        // Update vehicle
        vehicle.setVehicleID(newVehicleID);
        LogManager.saveAllObjects();
        return "Vehicle ID set";
    }

    public static String updateVehicleCarryingCapacity(String vehicleID, double newCapacity) {
        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);

        if (vehicle == null) {
            return "Invalid vehicleID - vehicle does not exist";
        }

        if (vehicle.isScheduled()) {
            return "Invalid vehicle - can't update vehicle if it is scheduled";
        }

        if (newCapacity < vehicle.getTotalCarryingWeight()) {
            return "Invalid new carrying capacity - capacity can't handle vehicle's current weight";
        }
        vehicle.setCarryingCapacity(newCapacity);
        LogManager.saveAllObjects();
        return "Vehicle carrying capacity set";
    }

    public static String updateVehicleFuelCapacity(String vehicleID, double newCapacity) {
        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);

        if (vehicle == null) {
            return "Invalid vehicleID - vehicle does not exist";
        }

        if (vehicle.isScheduled()) {
            return "Invalid vehicle - can't update vehicle if it is scheduled";
        }
        // Set current fuel to new max if new capacity is less than original current fuel
        if (newCapacity < vehicle.getCurrentFuel()) {
            vehicle.setCurrentFuel(newCapacity);
        }
        vehicle.setFuelCapacity(newCapacity);
        LogManager.saveAllObjects();
        return "Vehicle fuel capacity set";
    }

    public static String updatePortID(String portID, String newPortID) {
        Port port = TerminalUtil.searchPort(portID);

        if (port == null) {
            return "Invalid portID - port does not exist";
        }

        if (objectAlreadyExist(newPortID)) {
            return "Invalid new portID - ID already used";
        }
        // Can only update if port has not scheduled trip
        if (port.isStartPort()) {
            return "Invalid port - can't update a started port";
        }

        if (port.isTargetPort()) {
            return "Invalid port - can't update a targeted port";
        }
        // Update log
        for (Log log: occurredLogs) {
            if (log.getArrivalPortID().equals(port.getPortID())) {
                log.setArrivalPortID(newPortID);
            } else if (log.getDeparturePortID().equals(port.getPortID())) {
                log.setDeparturePortID(newPortID);
            }
        }
        // Update manager port
        for (Manager manager : TerminalUtil.managers) {
            if (manager.getManagePortID().equals(port.getPortID())) {
                manager.setManagePortID(newPortID);
            }
        }

        // Delete old ID
        usedIds.remove(port.getPortID());
        // Add new id
        usedIds.add(newPortID);
        // Update port
        port.setPortID(newPortID);
        LogManager.saveAllObjects();
        return "Port ID set";
    }

    public static String updatePortName(String portID, String newName) {
        Port port = TerminalUtil.searchPort(portID);
        if (port == null) {
            return "Invalid portID - port does not exist";
        }

        // Can only update if port has not scheduled trip
        if (port.isStartPort()) {
            return "Invalid port - can't update a started port";
        }

        if (port.isTargetPort()) {
            return "Invalid port - can't update a targeted port";
        }
        // Update port
        port.setPortName(newName);
        LogManager.saveAllObjects();
        return "Port name set";
    }

    public static String updatePortStoringCapacity(String portID, double newStoringCapacity) {
        Port port = TerminalUtil.searchPort(portID);

        if (port == null) {
            return "Invalid portID - port does not exist";
        }

        if (newStoringCapacity < port.getTotalCarryingWeight()) {
            return "Invalid storingCapacity - port's current carrying weight is higher than new storing capacity";
        }

        // Can only update if port has not scheduled trip
        if (port.isStartPort()) {
            return "Invalid port - can't update a started port";
        }

        if (port.isTargetPort()) {
            return "Invalid port - can't update a targeted port";
        }
        // Update port
        port.setStoringCapacity(newStoringCapacity);
        LogManager.saveAllObjects();
        return "Port storing capacity set";
    }

    public static String updatePortLandingAbility(String portID, boolean newLandingAbility) {
        Port port =  TerminalUtil.searchPort(portID);

        if (port == null) {
            return "Invalid portID - port does not exist";
        }

        // Can only update if port has not scheduled trip
        if (port.isStartPort()) {
            return "Invalid port - can't update a started port";
        }

        if (port.isTargetPort()) {
            return "Invalid port - can't update a targeted port";
        }
        // If there are trucks in the port, prevent
        if (!newLandingAbility) {
            for (Vehicle vehicle : port.getPortVehicles()) {
                if (vehicle.getVehicleType().isTruck()) {
                    return "Invalid port - can't update port landing ability to false if there are trucks inside the vehicle";
                }
            }
        }
        // Update port
        port.setLandingAbility(newLandingAbility);
        LogManager.saveAllObjects();
        return "Port landing ability set";
    }

    public static String updateManagerUsername(String username, String newUsername) {
        Manager manager = TerminalUtil.searchManager(username);
        if (manager == null) {
            return "Invalid username - manager does not exist";
        }

        if (objectAlreadyExist(newUsername)) {
            return "Invalid new username - username already used";
        }
        // Remove old username
        usedIds.remove(manager.getUsername());
        // Set new username
        manager.setUsername(newUsername);
        // Add new username to list
        usedIds.add(newUsername);
        LogManager.saveAllObjects();
        return "Manager username set";
    }

    public static String updateManagerPassword(String managerUsername, String newPassword) {
        Manager manager = TerminalUtil.searchManager(managerUsername);

        if (manager == null) {
            return "Invalid username - manager does not exist";
        }

        // Set new password
        manager.setPassword(newPassword);
        LogManager.saveAllObjects();
        return "Manager password set";
    }

    public static void updateLogWhenFinished() {
        // Determine if a port is finished
        for (Log log: occurringLogs) {
            if (passedDate(getNow(), log.getArrivalDate())) {
                // Set the state of the trip and move it to a different list
                log.setFinished(true);
                occurredLogs.add(log);

                // Update the log
                Vehicle vehicle = TerminalUtil.searchVehicle(log.getVehicleID());
                Port arrivalPort = TerminalUtil.searchPort(log.getArrivalPortID());
                if (vehicle != null) {
                    vehicle.arriveToPort(arrivalPort, log.getFuelConsumed());
                    // Add announcement
                    announcements.add(log.getVehicleID() + " arrived to " + log.getArrivalPortID());
                }
            }
        }
        occurringLogs.removeIf(Log::isFinished); // Remove all logs that is finished
        updateVehicleWhenReachDepartureDate();
        deleteLogAfterSevenDays(); // Keep only logs within the last 7 days
        LogManager.saveAllObjects();
    }

    public static void updateVehicleWhenReachDepartureDate() {
        // Since vehicles can be scheduled, need to check if it is the vehicle's time to move
        for (Log log: occurringLogs) {
            Vehicle vehicleToUpdate = searchVehicle(log.getVehicleID());
            if (passedDate(getNow(), log.getDepartureDate()) && vehicleToUpdate != null && !vehicleToUpdate.isSailAway()) {
                // Move the vehicle
                vehicleToUpdate.getCurrentPort().departureVehicle(vehicleToUpdate);
                vehicleToUpdate.setCurrentPort(null);
                // Add announcement
                announcements.add(log.getVehicleID() + " start moving to " + log.getArrivalPortID());
            }
        }
    }

    public static void deleteLogAfterSevenDays() {
        Date currentDate = new Date();

        // Define the threshold date (7 days before today)
        long thresholdMillis = currentDate.getTime() - (7 * 24 * 60 * 60 * 1000);

        // Iterate through the logs and remove those with arrivalDate before the threshold
        // Remove the log if its arrivalDate is more than 7 days before today
        occurredLogs.removeIf(log -> log.getArrivalDate().getTime() <= thresholdMillis);
    }

    public static void updateLogWhenTransportContainer(Vehicle vehicle) {
        // Before moving but scheduled, vehicle can load and unload => Increase vehicle's fuel consumption => Update log
        for (Log log: occurringLogs) {
            // Search for the log and if the vehicle has not moved yet
            if (log.getVehicleID().equals(vehicle.getVehicleID()) && !vehicle.isSailAway()) {
                Port destinationPort = TerminalUtil.searchPort(log.getArrivalPortID());
                double newFuelConsumed = TerminalUtil.roundToSecondDecimalPlace(vehicle.calculateFuelConsumption(destinationPort));
                log.setFuelConsumed(newFuelConsumed);
            }
        }
    }

    public static Date getNow() {
        return new Date();
    }

    public static Date parseStringToDateTime(String stringToParse) {
        // Parse given String to date time format
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        if (!isValidDateTime(stringToParse)) {
            return null;
        }

        try {
            return formatter.parse(stringToParse);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseStringToDate(String stringToParse) {
        // Parse given String to date format
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        if (!isValidDate(stringToParse)) {
            return null;
        }

        try {
            return formatter.parse(stringToParse);
        } catch (ParseException e) {
            return null;
        }
    }
    public static String parseDateTimeToString(Date dateToParse) {
        // Parse Date (date time) to a string
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(dateToParse);
    }

    public static String parseDateToString(Date dateToParse) {
        // Parse Date (date) to a string
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(dateToParse);
    }

    public static Date truncateTime(Date date) {
        // Remove the time of a Date object with Date time format
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean passedDate(Date date1, Date date2) {
        // Determine if date1 has pass date2
        return date1.compareTo(date2) >= 0;
    }

    public static boolean isValidDateTime(String input) {
        // Define a regular expression pattern to match a valid date format
        String dateFormatPattern = "\\d{1,2}-\\d{1,2}-\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}";

        // Check if the input matches the pattern
        if (!input.matches(dateFormatPattern)) {
            return false;
        }

        // If it matches the pattern, try to parse it as a date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sdf.setLenient(false); // Disable leniency to enforce strict date parsing

        try {
            Date parsedDate = sdf.parse(input);
            // If parsing succeeds without exceptions, it's a valid date
            return true;
        } catch (ParseException e) {
            // Parsing failed, so it's an invalid date
            return false;
        }
    }

    public static boolean isValidDate(String input) {
        // Define a regular expression pattern to match a valid date format (dd-MM-yyyy)
        String dateFormatPattern = "\\d{1,2}-\\d{1,2}-\\d{4}";

        // Check if the input matches the pattern
        if (!input.matches(dateFormatPattern)) {
            return false;
        }

        // If it matches the pattern, try to parse it as a date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false); // Disable leniency to enforce strict date parsing

        try {
            Date parsedDate = sdf.parse(input);
            // If parsing succeeds without exceptions, it's a valid date
            return true;
        } catch (ParseException e) {
            // Parsing failed, so it's an invalid date
            return false;
        }
    }

    public static User login(String username, String password) {
        Admin admin = Admin.getInstance();
        if (username.equals(admin.getUsername())
                && password.equals(admin.getPassword())) {
            return admin;
        }

        for (Manager manager: managers) {
            if (manager.getUsername().equals(username) && manager.getPassword().equals(password)) {
                return manager;
            }
        }
        return null;
    }

    public static ScheduledFuture<?> startScheduledTask() {
        // Run updateLogWhenFinished every 1 minute
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        return executor.scheduleAtFixedRate(TerminalUtil::updateLogWhenFinished, 0, 1, TimeUnit.MINUTES);
    }

    public static void stopScheduledTask(ScheduledFuture<?> future) {
        future.cancel(true);
        // Stop the terminal
        System.exit(0);
    }
}