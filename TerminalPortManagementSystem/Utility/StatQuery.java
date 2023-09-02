package TerminalPortManagementSystem.Utility;

import TerminalPortManagementSystem.ContainerType;
import TerminalPortManagementSystem.Ports.Container;
import TerminalPortManagementSystem.Ports.Port;
import TerminalPortManagementSystem.User.Manager;
import TerminalPortManagementSystem.VehicleType;
import TerminalPortManagementSystem.Vehicles.Vehicle;
import java.util.*;

public class StatQuery {
    // For totalFuelConsumed methods, only count consumption when the trip is finished
    public static Map<Date, Double> totalFuelConsumedPerDay() {
        Map<Date, Double> dailyFuelConsumption = new HashMap<>();

        // Loop for every log
        for (Log log: TerminalUtil.occurredLogs) {
            Date arrivalDate = log.getArrivalDate();
            // Group by day
            Date dayOnly = TerminalUtil.truncateTime(arrivalDate);

            double fuelConsumed = log.getFuelConsumed();
            // Add to existing record
            dailyFuelConsumption.put(dayOnly,
                    TerminalUtil.roundToSecondDecimalPlace(dailyFuelConsumption.getOrDefault(dayOnly, 0.0) + fuelConsumed));
        }

        return dailyFuelConsumption;
    }

    public static Map<Date, Double> totalFuelConsumedPerDayOfPort(String portID) {
        Map<Date, Double> dailyFuelConsumption = new HashMap<>();

        for (Log log: TerminalUtil.occurredLogs) {
            // Filter by the given port
            if (log.getArrivalPortID().equals(portID) || log.getDeparturePortID().equals(portID)) {
                Date arrivalDate = log.getArrivalDate();
                Date dayOnly = TerminalUtil.truncateTime(arrivalDate);

                double fuelConsumed = log.getFuelConsumed();
                dailyFuelConsumption.put(dayOnly,
                        TerminalUtil.roundToSecondDecimalPlace(dailyFuelConsumption.getOrDefault(dayOnly, 0.0) + fuelConsumed));
            }
        }
        return dailyFuelConsumption;
    }

    public static double totalFuelConsumedByDay(Date dateToQuery) {
        double totalFuelConsumed = 0.0;

        for (Log log: TerminalUtil.occurredLogs) {
            // Filter by the given date object. Date with format: dd:MM:yyyy
            Date arrivalDate = log.getArrivalDate();
            Date dayOnly = TerminalUtil.truncateTime(arrivalDate);

            if (dayOnly.equals(TerminalUtil.truncateTime(dateToQuery))) {
                totalFuelConsumed += log.getFuelConsumed();
            }
        }

        return TerminalUtil.roundToSecondDecimalPlace(totalFuelConsumed);
    }

    public static double totalFuelConsumedByDayByPort(Date dateToQuery, String portID) {
        double totalFuelConsumed = 0.0;

        for (Log log: TerminalUtil.occurredLogs) {
            // Query by date and port
            Date arrivalDate = log.getArrivalDate();
            Date dayOnly = TerminalUtil.truncateTime(arrivalDate);

            if (dayOnly.equals(TerminalUtil.truncateTime(dateToQuery)) &&
                    (log.getArrivalPortID().equals(portID) || log.getDeparturePortID().equals(portID))) {
                totalFuelConsumed += log.getFuelConsumed();
            }
        }

        return TerminalUtil.roundToSecondDecimalPlace(totalFuelConsumed);
    }

    public static Map<ContainerType, Integer> getNumberOfContainerOfEachType() {
        Map<ContainerType, Integer> numberOfEachType = new HashMap<>();
        List<ContainerType> containerTypes = ContainerType.getAllContainerTypes();

        for (ContainerType containerType: containerTypes) {
            int numberOfContainer = 0;
            for (Container container: TerminalUtil.containers) {
                if (container.getContainerType() == containerType) {
                    numberOfContainer++;
                }
            }
            numberOfEachType.put(containerType, numberOfContainer);
        }
        return numberOfEachType;
    }

    public static Map<ContainerType, Integer> getNumberOfContainerOfEachTypeByPort(String portID) {
        // Count both container in port and in vehicle of the port
        Port port = TerminalUtil.searchPort(portID);
        // Null checking
        if (port == null) {
            return null;
        }

        Map<ContainerType, Integer> numberOfEachType = new HashMap<>();
        List<ContainerType> containerTypes = ContainerType.getAllContainerTypes();

        for (ContainerType containerType: containerTypes) {
            int numberOfContainer = 0;
            // Find container in port
            for (Container container: port.getPortContainers()) {
                if (container.getContainerType() == containerType) {
                    numberOfContainer++;
                }
            }
            // Find container in vehicle
            for (Vehicle vehicle: port.getPortVehicles()) {
                for (Container container: vehicle.getVehicleContainers()) {
                    if (container.getContainerType() == containerType) {
                        numberOfContainer++;
                    }
                }
            }

            numberOfEachType.put(containerType, numberOfContainer);
        }
        return numberOfEachType;
    }

    public static Map<ContainerType, Double> getTotalWeightOfEachType() {
        // Calculate the total weight by type
        Map<ContainerType, Double> totalWeightOfEachType = new HashMap<>();

        for (ContainerType containerType: ContainerType.getAllContainerTypes()) {
            double totalWeight = 0;
            for (Container container: TerminalUtil.containers) {
                if (container.getContainerType() == containerType) {
                    totalWeight += container.getWeight();
                }
            }
            totalWeightOfEachType.put(containerType, TerminalUtil.roundToSecondDecimalPlace(totalWeight));
        }
        return totalWeightOfEachType;
    }

    public static Map<ContainerType, Double> getTotalWeightOfEachTypeByPort(String portID) {
        Port port = TerminalUtil.searchPort(portID);

        if (port == null) {
            return null;
        }

        Map<ContainerType, Double> totalWeightOfEachType = new HashMap<>();

        // Query by port
        for (ContainerType containerType: ContainerType.getAllContainerTypes()) {
            double totalWeight = 0.0;
            for (Container container: port.getPortContainers()) {
                if (container.getContainerType() == containerType) {
                    totalWeight += container.getWeight();
                }
            }

            for (Vehicle vehicle: port.getPortVehicles()) {
                for (Container container: vehicle.getVehicleContainers()) {
                    if (container.getContainerType() == containerType) {
                        totalWeight += container.getWeight();
                    }
                }
            }

            totalWeightOfEachType.put(containerType, TerminalUtil.roundToSecondDecimalPlace(totalWeight));
        }
        return totalWeightOfEachType;
    }

    public static List<Log> getTripsByArrivalDate(Date dateToQuery) {
        // Query log by date, return a list of log if arrivalDate is in the same day with given date
        // dateToQuery format: dd:MM:yyyy
        List<Log> tripsOnDate = new ArrayList<>();

        List<Log> allLogs = new ArrayList<>();
        allLogs.addAll(TerminalUtil.occurringLogs);
        allLogs.addAll(TerminalUtil.occurredLogs);

        for (Log log : allLogs) {
            Date arrivalDate = log.getArrivalDate();
            Date dayOnly = TerminalUtil.truncateTime(arrivalDate);

            // Add to list if arrivalDate
            if (dayOnly.equals(dateToQuery)) {
                tripsOnDate.add(log);
            }
        }

        return tripsOnDate;
    }

    public static List<Log> getTripsByDepartureDate(Date dateToQuery) {
        // Query log by date, return a list of log if departureDate is in the same day with given date
        // dateToQuery format: dd:MM:yyyy
        List<Log> tripsOnDate = new ArrayList<>();

        List<Log> allLogs = new ArrayList<>();
        allLogs.addAll(TerminalUtil.occurringLogs);
        allLogs.addAll(TerminalUtil.occurredLogs);

        for (Log log : allLogs) {
            Date departureDate = log.getDepartureDate();
            Date dayOnly = TerminalUtil.truncateTime(departureDate);

            // Add to list if arrivalDate
            if (dayOnly.equals(dateToQuery)) {
                tripsOnDate.add(log);
            }
        }

        return tripsOnDate;
    }

    public static List<Log> getTripsByArrivalDateOfPort(Date dateToQuery, String portID) {
        // Query log by date, return a list of log if arrivalDate is in the same day with given date and either departurePort or arrivalPort is equal to portID
        // dateToQuery format: dd:MM:yyyy
        List<Log> tripsOnDate = new ArrayList<>();

        List<Log> allLogs = new ArrayList<>();
        allLogs.addAll(TerminalUtil.occurringLogs);
        allLogs.addAll(TerminalUtil.occurredLogs);

        for (Log log : allLogs) {
            Date arrivalDate = log.getArrivalDate();
            Date dayOnly = TerminalUtil.truncateTime(arrivalDate);

            if (dayOnly.equals(dateToQuery) &&
            (log.getArrivalPortID().equals(portID) || log.getDeparturePortID().equals(portID))) {
                tripsOnDate.add(log);
            }
        }

        return tripsOnDate;
    }

    public static List<Log> getTripsByDepartureDateOfPort(Date dateToQuery, String portID) {
        // Query log by date, return a list of log if departureDate is in the same day with given date and either departurePort or arrivalPort is equal to portID
        // dateToQuery format: dd:MM:yyyy
        List<Log> tripsOnDate = new ArrayList<>();

        List<Log> allLogs = new ArrayList<>();
        allLogs.addAll(TerminalUtil.occurringLogs);
        allLogs.addAll(TerminalUtil.occurredLogs);

        for (Log log : allLogs) {
            Date departureDate = log.getDepartureDate();
            Date dayOnly = TerminalUtil.truncateTime(departureDate);

            if (dayOnly.equals(dateToQuery) &&
                    (log.getArrivalPortID().equals(portID) || log.getDeparturePortID().equals(portID))) {
                tripsOnDate.add(log);
            }
        }

        return tripsOnDate;
    }

    public static List<Log> getTripsBetweenArrivalDates(Date startDate, Date endDate) {
        // Query log between 2 dates, return a list of log if arrival date of that log is between the 2 given date
        List<Log> tripsBetweenDates = new ArrayList<>();

        List<Log> allLogs = new ArrayList<>();
        allLogs.addAll(TerminalUtil.occurringLogs);
        allLogs.addAll(TerminalUtil.occurredLogs);

        for (Log log : allLogs) {
            Date arrivalDate = log.getArrivalDate();
            Date dayOnly = TerminalUtil.truncateTime(arrivalDate);

            if (!dayOnly.before(startDate) && !dayOnly.after(endDate)) {
                tripsBetweenDates.add(log);
            }
        }

        return tripsBetweenDates;
    }

    public static List<Log> getTripsBetweenDepartureDates(Date startDate, Date endDate) {
        // Query log between 2 dates, return a list of log if departure date of that log is between the 2 given date
        List<Log> tripsBetweenDates = new ArrayList<>();

        List<Log> allLogs = new ArrayList<>();
        allLogs.addAll(TerminalUtil.occurringLogs);
        allLogs.addAll(TerminalUtil.occurredLogs);

        for (Log log : allLogs) {
            Date departureDate = log.getDepartureDate();
            Date dayOnly = TerminalUtil.truncateTime(departureDate);

            if (!dayOnly.before(startDate) && !dayOnly.after(endDate)) {
                tripsBetweenDates.add(log);
            }
        }

        return tripsBetweenDates;
    }

    public static List<Log> getTripsBetweenArrivalDatesOfPort(Date startDate, Date endDate, String portID) {
        // Query log between 2 dates, return a list of log if arrival date of that log is between the 2 given date and the given portID appear in the log
        List<Log> tripsBetweenDates = new ArrayList<>();

        List<Log> allLogs = new ArrayList<>();
        allLogs.addAll(TerminalUtil.occurringLogs);
        allLogs.addAll(TerminalUtil.occurredLogs);

        for (Log log : allLogs) {
            Date arrivalDate = log.getArrivalDate();
            Date dayOnly = TerminalUtil.truncateTime(arrivalDate);

            if (!dayOnly.before(startDate) &&
                    !dayOnly.after(endDate) &&
                    (log.getArrivalPortID().equals(portID) || log.getDeparturePortID().equals(portID))) {
                tripsBetweenDates.add(log);
            }
        }

        return tripsBetweenDates;
    }

    public static List<Log> getTripsBetweenDepartureDatesOfPort(Date startDate, Date endDate, String portID) {
        // Query log between 2 dates, return a list of log if departure date of that log is between the 2 given date and the given portID appear in the log
        List<Log> tripsBetweenDates = new ArrayList<>();

        List<Log> allLogs = new ArrayList<>();
        allLogs.addAll(TerminalUtil.occurringLogs);
        allLogs.addAll(TerminalUtil.occurredLogs);

        for (Log log : allLogs) {
            Date departureDate = log.getDepartureDate();
            Date dayOnly = TerminalUtil.truncateTime(departureDate);

            if (!dayOnly.before(startDate) &&
                    !dayOnly.after(endDate) &&
                    (log.getArrivalPortID().equals(portID) || log.getDeparturePortID().equals(portID))) {
                tripsBetweenDates.add(log);
            }
        }

        return tripsBetweenDates;
    }

    public static List<Log> getTripsInDates(Date startDate, Date endDate) {
        // Query log between 2 dates, return a list of log if departureDate and arrivalDate is between startDate and endDate
        List<Log> tripsInDate = new ArrayList<>();

        List<Log> allLogs = new ArrayList<>();
        allLogs.addAll(TerminalUtil.occurringLogs);
        allLogs.addAll(TerminalUtil.occurredLogs);

        for (Log log : allLogs) {
            Date departureDate = log.getDepartureDate();
            Date arrivalDate = log.getArrivalDate();
            Date departureDayOnly = TerminalUtil.truncateTime(departureDate);
            Date arrivalDayOnly = TerminalUtil.truncateTime(arrivalDate);

            if (startDate.before(departureDayOnly) && endDate.after(arrivalDayOnly)) {
                tripsInDate.add(log);
            }
        }

        return tripsInDate;
    }

    public static List<Log> getTripsInDatesOfPort(Date startDate, Date endDate, String portID) {
        // Query log between 2 dates, return a list of log if departure date and arrival Date of that log is between the 2 given date and the given portID appear in the log
        List<Log> tripsBetweenDates = new ArrayList<>();

        List<Log> allLogs = new ArrayList<>();
        allLogs.addAll(TerminalUtil.occurringLogs);
        allLogs.addAll(TerminalUtil.occurredLogs);

        for (Log log : allLogs) {
            Date departureDate = log.getDepartureDate();
            Date arrivalDate = log.getArrivalDate();
            Date departureDayOnly = TerminalUtil.truncateTime(departureDate);
            Date arrivalDayOnly = TerminalUtil.truncateTime(arrivalDate);

            if (startDate.before(departureDayOnly) && endDate.after(arrivalDayOnly) &&
                    (log.getArrivalPortID().equals(portID) || log.getDeparturePortID().equals(portID))) {
                tripsBetweenDates.add(log);
            }
        }

        return tripsBetweenDates;
    }

    public static Log getOccurringLogByVehicleID(String vehicleID) {
        // Search for occurring log based on vehicle ID. Always return 1 since at any given time, there can only be 1 vehicle moving
        for (Log log: TerminalUtil.occurringLogs) {
            if (log.getVehicleID().equals(vehicleID)) {
                return log;
            }
        }
        return null;
    }

    public static List<Container> getListOfAllContainer() {
        return TerminalUtil.containers;
    }

    public static List<Container> getListOfContainerByPort(String portID) {
        // Get a list of containers from portID. Also returns containers in vehicles that are in the port
        Port port = TerminalUtil.searchPort(portID);

        if (port == null) {
            return null;
        }

        List<Container> containersInPort = new ArrayList<>(List.copyOf(port.getPortContainers()));
        for (Vehicle vehicle: port.getPortVehicles()) {
            containersInPort.addAll(vehicle.getVehicleContainers());
        }

        return containersInPort;
    }

    public static List<Vehicle> getListOfAllVehicle() {
        return TerminalUtil.vehicles;
    }

    public static List<Vehicle> getListOfVehicleByPort(String portID) {
        // Get a list of vehicles from portID
        Port port = TerminalUtil.searchPort(portID);

        if (port == null) {
            return null;
        }

        return port.getPortVehicles();
    }

    public static List<Vehicle> getListOfVehicleByType(String vehicleType) {
        // Get a list of vehicles filter by type
        VehicleType type = VehicleType.fromString(vehicleType);

        if (type == null) {
            return null;
        }

        List<Vehicle> vehicles = new ArrayList<>();
        for (Vehicle vehicle: TerminalUtil.vehicles) {
            if (vehicle.getVehicleType() == type) {
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }

    public static List<Vehicle> getListOfVehicleByTypeOfPort(String portID, String vehicleType) {
        // Get a list of vehicles filter by type from a port
        Port port = TerminalUtil.searchPort(portID);
        VehicleType type = VehicleType.fromString(vehicleType);

        if (port == null) {
            return null;
        }

        if (type == null) {
            return null;
        }

        List<Vehicle> vehicles = new ArrayList<>();
        for (Vehicle vehicle: port.getPortVehicles()) {
            if (vehicle.getVehicleType() == type) {
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }

    public static Map<VehicleType, Integer> getNumberOfVehicleOfEachType() {
        // Get the number of each type of vehicles
        Map<VehicleType, Integer> numberOfEachType = new HashMap<>();
        List<VehicleType> vehicleTypes = VehicleType.getAllVehicleTypes();
        for (VehicleType vehicleType: vehicleTypes) {
            int numberOfVehicle = 0;
            for (Vehicle vehicle: TerminalUtil.vehicles) {
                if (vehicle.getVehicleType() == vehicleType) {
                    numberOfVehicle++;
                }
            }
            numberOfEachType.put(vehicleType, numberOfVehicle);
        }
        return numberOfEachType;
    }

    public static Map<VehicleType, Integer> getNumberOfVehicleOfEachTypeByPort(String portID) {
        // Get the number of each type of vehicles of a port
        Port port = TerminalUtil.searchPort(portID);

        if (port == null) {
            return null;
        }

        Map<VehicleType, Integer> numberOfEachType = new HashMap<>();
        List<VehicleType> vehicleTypes = VehicleType.getAllVehicleTypes();
        for (VehicleType vehicleType: vehicleTypes) {
            int numberOfVehicle = 0;
            for (Vehicle vehicle: port.getPortVehicles()) {
                if (vehicle.getVehicleType() == vehicleType) {
                    numberOfVehicle++;
                }
            }
            numberOfEachType.put(vehicleType, numberOfVehicle);
        }
        return numberOfEachType;
    }

    public static List<Port> getListOfAllPort() {
        return TerminalUtil.ports;
    }

    public static List<Manager> getListOfAllManager(){
        return TerminalUtil.managers;
    }
}