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

        for (Log log: TerminalUtil.occurredLogs) {
            Date arrivalDate = log.getArrivalDate();
            Date dayOnly = TerminalUtil.truncateTime(arrivalDate);

            double fuelConsumed = log.getFuelConsumed();
            dailyFuelConsumption.put(dayOnly, dailyFuelConsumption.getOrDefault(dayOnly, 0.0) + fuelConsumed);
        }

        return dailyFuelConsumption;
    }

    public static Map<Date, Double> totalFuelConsumedPerDayOfPort(String portID) {
        if (TerminalUtil.searchPort(portID) == null) {
            return null;
        }

        Map<Date, Double> dailyFuelConsumption = new HashMap<>();

        for (Log log: TerminalUtil.occurredLogs) {
            if (log.getArrivalPortID().equals(portID) || log.getDeparturePortID().equals(portID)) {
                Date arrivalDate = log.getArrivalDate();
                Date dayOnly = TerminalUtil.truncateTime(arrivalDate);

                double fuelConsumed = log.getFuelConsumed();
                dailyFuelConsumption.put(dayOnly, dailyFuelConsumption.getOrDefault(dayOnly, 0.0) + fuelConsumed);
            }
        }
        return dailyFuelConsumption;
    }

    public static double totalFuelConsumedByDay(Date dateToQuery) {
        double totalFuelConsumed = 0.0;

        for (Log log: TerminalUtil.occurredLogs) {
            Date arrivalDate = log.getArrivalDate();
            Date dayOnly = TerminalUtil.truncateTime(arrivalDate);

            if (dayOnly.equals(TerminalUtil.truncateTime(dateToQuery))) {
                totalFuelConsumed += log.getFuelConsumed();
            }
        }

        return totalFuelConsumed;
    }

    public static double totalFuelConsumedByDayByPort(Date dateToQuery, String portID) {
        if (TerminalUtil.searchPort(portID) == null) {
            return 0;
        }

        double totalFuelConsumed = 0.0;

        for (Log log: TerminalUtil.occurredLogs) {
            Date arrivalDate = log.getArrivalDate();
            Date dayOnly = TerminalUtil.truncateTime(arrivalDate);

            if (dayOnly.equals(TerminalUtil.truncateTime(dateToQuery)) &&
                    (log.getArrivalPortID().equals(portID) || log.getDeparturePortID().equals(portID))) {
                totalFuelConsumed += log.getFuelConsumed();
            }
        }

        return totalFuelConsumed;
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
        Port port = TerminalUtil.searchPort(portID);

        if (port == null) {
            return null;
        }

        Map<ContainerType, Integer> numberOfEachType = new HashMap<>();
        List<ContainerType> containerTypes = ContainerType.getAllContainerTypes();
        for (ContainerType containerType: containerTypes) {
            int numberOfContainer = 0;
            // find container in port
            for (Container container: port.getPortContainers()) {
                if (container.getContainerType() == containerType) {
                    numberOfContainer++;
                }
            }
            // find container in vehicle
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
        Map<ContainerType, Double> totalWeightOfEachType = new HashMap<>();

        for (ContainerType containerType: ContainerType.getAllContainerTypes()) {
            double totalWeight = 0;
            for (Container container: TerminalUtil.containers) {
                if (container.getContainerType() == containerType) {
                    totalWeight += container.getWeight();
                }
            }
            totalWeightOfEachType.put(containerType, totalWeight);
        }
        return totalWeightOfEachType;
    }

    public static Map<ContainerType, Double> getTotalWeightOfEachTypeByPort(String portID) {
        Port port = TerminalUtil.searchPort(portID);

        if (port == null) {
            return null;
        }

        Map<ContainerType, Double> totalWeightOfEachType = new HashMap<>();

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

            totalWeightOfEachType.put(containerType, totalWeight);
        }
        return totalWeightOfEachType;
    }

    public static List<Log> getTripsByDate(Date dateToQuery) {
        List<Log> tripsOnDate = new ArrayList<>();

        List<Log> allLogs = new ArrayList<>();
        allLogs.addAll(TerminalUtil.occurringLogs);
        allLogs.addAll(TerminalUtil.occurredLogs);

        for (Log log : allLogs) {
            Date arrivalDate = log.getArrivalDate();
            Date dayOnly = TerminalUtil.truncateTime(arrivalDate);

            if (dayOnly.equals(dateToQuery)) {
                tripsOnDate.add(log);
            }
        }

        return tripsOnDate;
    }

    public static List<Log> getTripsByDateOfPort(Date dateToQuery, String portID) {
        Port port = TerminalUtil.searchPort(portID);

        if (port == null) {
            return null;
        }

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

    public static List<Log> getTripsBetweenDates(Date startDate, Date endDate) {
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

    public static List<Log> getTripsBetweenDatesOfPort(Date startDate, Date endDate, String portID) {
        Port port = TerminalUtil.searchPort(portID);

        if (port == null) {
            return null;
        }

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

    public static Log getOccurringLogByVehicle(String vehicleID) {
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
        Port port = TerminalUtil.searchPort(portID);

        if (port == null) {
            return null;
        }

        return port.getPortVehicles();
    }

    public static List<Vehicle> getListOfVehicleByType(String vehicleType) {
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