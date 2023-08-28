package TerminalPortManagementSystem.Utility;

import TerminalPortManagementSystem.ContainerType;
import TerminalPortManagementSystem.Ports.Container;
import TerminalPortManagementSystem.Ports.Port;
import TerminalPortManagementSystem.User.*;
import TerminalPortManagementSystem.VehicleType;
import TerminalPortManagementSystem.Vehicles.Vehicle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TerminalUtil {
    public static final List<Port> ports = LogManager.loadPorts();
    public static final List<Vehicle> vehicles = LogManager.loadVehicles();
    public static final List<Container> containers = LogManager.loadContainers();
    public static final List<Log> occurredLogs = LogManager.loadOccurredLog("Data/History/occurred.obj");
    public static final List<Log> occurringLogs = LogManager.loadOccurredLog("Data/History/occurring.obj");
    public static final List<String> usedIds = LogManager.loadUsedIds();
    public static final List<User> users = new ArrayList<>(); // TODO: More work - load and save managers

    public static double roundToSecondDecimalPlace(double number) {
        return (double)Math.round(number * 100d) / 100d;
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

    public static void addUser(User userToAdd) {
        users.add(userToAdd);
    }

    public static void removeUser(User userToRemove) {
        users.remove(userToRemove);
    }

    public static boolean objectAlreadyExist(String id) {
        return usedIds.contains(id);
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

    public static int getTotalNumberOfPorts() {
        return ports.size();
    }

    public static int getTotalNumberOfVehicles() {
        return vehicles.size();
    }

    public static int getTotalNumberOfContainers() {
        return containers.size();
    }

    public static String removePort(String portID) {
        // When remove port, must also remove all vehicles and containers
        // Only remove port that does not have any vehicles moving to it
        Port portToRemove = searchPort(portID);
        if (portToRemove == null) {
            return "Port not found";
        } else if (portToRemove.isTargetPort()) {
            return "Not allowed to remove Port that have occurring trips";
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

        // Save
        LogManager.saveAllObjects();
        return "Port found and deleted";
    }

    public static String removeVehicle(String vehicleID) {
        // When removing vehicles, must also remove all containers in the vehicle
        Vehicle vehicleToRemove = searchVehicle(vehicleID);
        if (vehicleToRemove == null) {
            return "Vehicle not found";
        } else if (vehicleToRemove.isScheduled()) {
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
        for (Log log: occurringLogs) {
            if (passedDate(getNow(), log.getArrivalDate())) {
                log.setFinished(true);
                occurredLogs.add(log);

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
        for (Log log: occurringLogs) {
            Vehicle vehicleToUpdate = searchVehicle(log.getVehicleID());
            if (passedDate(getNow(), log.getDepartureDate()) && !vehicleToUpdate.isSailAway()) {
                vehicleToUpdate.getCurrentPort().departureVehicle(vehicleToUpdate);
                vehicleToUpdate.setCurrentPort(null);
            }
        }
    }

    public static Date getNow() {
        return new Date();
    }

    public static Date parseStringToDateTime(String stringToParse) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            return formatter.parse(stringToParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseDateToString(Date dateToParse) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(dateToParse);
    }

    public static Date truncateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean passedDate(Date date1, Date date2) {
        return date1.compareTo(date2) >= 0;
    }

    public static User login(String username, String password) {
        for (User user: users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}