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
                }
            }
        }
        occurringLogs.removeIf(Log::isFinished); // Remove all logs that is finished
        updateVehicleWhenReachDepartureDate();
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
            }
        }
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
        try {
            return formatter.parse(stringToParse);
        } catch (ParseException e) {
            return null;
        }
    }
    public static Date parseStringToDate(String stringToParse) {
        // Parse given String to date format
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
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

    public static void startScheduledTask() {
        // Run updateLogWhenFinished every 1 minute
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(TerminalUtil::updateLogWhenFinished, 0, 1, TimeUnit.MINUTES);
    }
}