package TerminalPortManagementSystem.Utility;

import TerminalPortManagementSystem.Ports.*;
import TerminalPortManagementSystem.User.Admin;
import TerminalPortManagementSystem.User.Manager;
import TerminalPortManagementSystem.Vehicles.Vehicle;

import java.util.Date;
import java.util.List;

public class Prettify {
    public static void prettifyPort(Port port) {
        if(port==null){
            String format = "%-" + 6 + "s | %-" + 10 + "s | %-" + 4 + "s | %-" + 5 + "s | %-" + 10 + "s | %-13s%n";

            System.out.printf(format, "ID", "Name", "Lat", "Long", "Store", "Landing");
            System.out.printf(format, "Null", "Null", "Null", "Null", "Null", "Null");
            System.out.println();
        }else {

            StringBuilder vehicleIDs = new StringBuilder();
            for (Vehicle vehicle : port.getPortVehicles()) {
                if (vehicleIDs.length() > 0) {
                    vehicleIDs.append(", ");
                }
                vehicleIDs.append(vehicle.getVehicleID());
            }

            StringBuilder containersIDs = new StringBuilder();
            for (Container container : port.getPortContainers()) {
                if (containersIDs.length() > 0) {
                    containersIDs.append(", ");
                }
                containersIDs.append(container.getContainerID());
            }


            int IDLength = port.getPortID().length();
            int nameLength = port.getPortName().length();
            int latLength = String.valueOf(port.getLatitude()).length() + 5;
            int longLength = String.valueOf(port.getLongitude()).length() + 5;
            int storingCapacity = String.valueOf(port.getStoringCapacity()).length() * 2 + 1;

            String format = "%-" + IDLength + "s | %-" + nameLength + "s | %-" + latLength + "s | %-" + longLength + "s | %-" + storingCapacity + "s | %-13s | %-" +
                    (vehicleIDs.length() + 8) + "s | %-" + (containersIDs.length() + 8) + "s%n";

            System.out.printf(format, "ID", "Name", "Lat", "Long", "Store", "Landing", "Vehicles", "Containers");
            System.out.printf(format,
                    port.getPortID(),
                    port.getPortName(),
                    port.getLatitude(),
                    port.getLongitude(),
                    port.getTotalCarryingWeight() + "/" + port.getStoringCapacity(),
                    port.getLandingAbility(),
                    vehicleIDs,
                    containersIDs);
            System.out.println();
        }
    }

    public static void prettifyPortList(List<Port> ports) {
        if (ports.size() == 0) {
            String format = "%-6s | %-10s | %-4s | %-5s | %-10s | %-13s%n";

            System.out.printf(format, "ID", "Name", "Lat", "Long", "Store", "Landing");
            System.out.printf(format, "Null", "Null", "Null", "Null", "Null", "Null");
            System.out.println();
        } else {
            int maxIDLength = 0;
            int maxNameLength = 0;
            int maxLatLength = 0;
            int maxLongLength = 0;
            int maxStoringCapacity = 0;

            // Create dynamic printf
            for (Port port : ports) {
                int IDLength = port.getPortID().length();
                int nameLength = port.getPortName().length();
                int latLength = String.valueOf(port.getLatitude()).length() + 5;
                int longLength = String.valueOf(port.getLongitude()).length() + 5;
                int storingCapacity = String.valueOf(port.getStoringCapacity()).length() * 2 + 1;

                if (IDLength > maxIDLength) {
                    maxIDLength = IDLength;
                }
                if (nameLength > maxNameLength) {
                    maxNameLength = nameLength;
                }
                if (latLength > maxLatLength) {
                    maxLatLength = latLength;
                }
                if (longLength > maxLongLength) {
                    maxLongLength = longLength;
                }
                if (storingCapacity > maxStoringCapacity) {
                    maxStoringCapacity = storingCapacity;
                }
            }

            String format = "%-" + maxIDLength + "s | %-" + maxNameLength + "s | %-" + maxLatLength + "s | %-" + maxLongLength + "s | %-" + maxStoringCapacity + "s | %-13s%n";

            System.out.printf(format, "ID", "Name", "Lat", "Long", "Store", "Landing");

            for (Port port : ports) {
                System.out.printf(format,
                        port.getPortID(),
                        port.getPortName(),
                        port.getLatitude(),
                        port.getLongitude(),
                        port.getTotalCarryingWeight() + "/" + port.getStoringCapacity(),
                        port.getLandingAbility());
            }
            System.out.println();
        }
    }
    public static void prettifyVehicle(Vehicle vehicle){
        if(vehicle==null){
            String format = "%-8s | %-15s | %-7s | %-13s | %-15s | %-10s%n";
            System.out.printf(format, "ID", "Type", "Port", "Is Sail Away", "Fuel", "Carry");
            System.out.printf(format, "Null", "Null", "Null", "Null", "Null", "Null");
            System.out.println();
        }else{
            String format = "%-8s | %-15s | %-7s | %-13s | %-15s | %-10s%n";
            System.out.printf(format, "ID", "Type", "Port", "Is Sail Away", "Fuel", "Carry");
            System.out.printf(format,
                    vehicle.getVehicleID(),
                    vehicle.getVehicleType() + vehicle.getVehicleType().getEmoji(),
                    (vehicle.getCurrentPort() == null) ? null : vehicle.getCurrentPort().getPortID(),
                    vehicle.isSailAway(),
                    vehicle.getCurrentFuel() + "/" + vehicle.getFuelCapacity() + " gallon",
                    vehicle.getTotalCarryingWeight() + "/" + vehicle.getCarryingCapacity() + " kg");
            System.out.println();
        }
    }

    public static void prettifyVehicleList(List<Vehicle> vehicles) {
        if (vehicles.size() == 0) {
            String format = "%-8s | %-15s | %-7s | %-13s | %-15s | %-10s%n";
            System.out.printf(format, "ID", "Type", "Port", "Is Sail Away", "Fuel", "Carry");
            System.out.printf(format, "Null", "Null", "Null", "Null", "Null", "Null");
            System.out.println();
        } else {

            int maxIDLength = 0;
            int maxCurrentPortLength = 0;
            int maxFuelLength = 0;
            int maxCapacityLength = 0;

            // Create dynamic printf
            for (Vehicle vehicle : vehicles) {
                int vehicleIDLength = vehicle.getVehicleID().length();
                int currentPortLength = (vehicle.getCurrentPort() == null) ? 5 : vehicle.getCurrentPort().getPortID().length();
                int fuelLength = String.valueOf(vehicle.getFuelCapacity()).length() * 2 + 8;
                int capacityLength = String.valueOf(vehicle.getCarryingCapacity()).length() * 2 + 4;

                if (vehicleIDLength > maxIDLength) {
                    maxIDLength = vehicleIDLength;
                }
                if (currentPortLength > maxCurrentPortLength) {
                    maxCurrentPortLength = currentPortLength;
                }
                if (fuelLength > maxFuelLength) {
                    maxFuelLength = fuelLength;
                }
                if (capacityLength > maxCapacityLength) {
                    maxCapacityLength = capacityLength;
                }
            }

            System.out.printf("%-" + maxIDLength + "s | %-15s | %-" + maxCurrentPortLength + "s | %-13s | %-" + maxFuelLength + "s | %-" + maxCapacityLength + "s%n",
                    "ID", "Type", "Port", "Is Sail Away", "Fuel", "Carry");
            for (Vehicle vehicle : vehicles) {
                System.out.printf("%-" + maxIDLength + "s | %-15s | %-" + maxCurrentPortLength + "s | %-13s | %-" + maxFuelLength + "s | %-" + maxCapacityLength + "s%n",
                        vehicle.getVehicleID(),
                        vehicle.getVehicleType() + vehicle.getVehicleType().getEmoji(),
                        (vehicle.getCurrentPort() == null) ? null : vehicle.getCurrentPort().getPortID(),
                        vehicle.isSailAway(),
                        vehicle.getCurrentFuel() + "/" + vehicle.getFuelCapacity() + " gallon",
                        vehicle.getTotalCarryingWeight() + "/" + vehicle.getCarryingCapacity() + " kg");
            }
            System.out.println();
        }
    }
    public static void prettifyContainer(Container container) {
        if (container == null) {
            String format = "%-6s | %-12s | %-6s%n";
            System.out.printf(format, "ID", "Type", "Weight");
            System.out.printf(format, "Null", "Null", "Null");
            System.out.println();
        }
        else {
            String format = "%-6s | %-12s | %-6s%n";
            System.out.printf(format, "ID", "Type", "Weight");
            System.out.printf(format,
                    container.getContainerID(),
                    container.getContainerType() + container.getContainerType().getEmoji(),
                    container.getWeight() + " kg");
            System.out.println();
        }
    }
    public static void prettifyContainerList(List<Container> containers) {
        if (containers.size() == 0) {
            String format = "%-6s | %-12s | %-6s%n";
            System.out.printf(format, "ID", "Type", "Weight");
            System.out.printf(format, "Null", "Null", "Null");
            System.out.println();
        } else {

            int maxIDLength = 0;
            int maxWeightLength = 0;

            // Create dynamic printf
            for (Container container : containers) {
                int containerIDLength = container.getContainerID().length();
                int weightLength = String.valueOf(container.getWeight()).length() + 4;

                if (containerIDLength > maxIDLength) {
                    maxIDLength = containerIDLength;
                }
                if (weightLength > maxWeightLength) {
                    maxWeightLength = weightLength;
                }
            }

            System.out.printf("%-" + maxIDLength + "s | %-12s | %-" + maxWeightLength + "s%n",
                    "ID", "Type", "Weight");
            for (Container container : containers) {
                System.out.printf("%-" + maxIDLength + "s | %-12s | %-" + maxWeightLength + "s%n",
                        container.getContainerID(),
                        container.getContainerType() + container.getContainerType().getEmoji(),
                        container.getWeight() + " kg");
            }
            System.out.println();
        }
    }

    public static void prettifyLogList(List<Log> logs) {
        String format;
        if (logs.size() == 0) {
            format = "%-9s | %-20s | %-20s | %-16s | %-16s | %-16s | %-5s%n";
            System.out.printf(format, "vehicleID", "Departure Date", "Arrival Date", "Departure PortID", "Arrival PortID", "Fuel Consumed", "Finished");
            System.out.printf(format, "Null", "Null", "Null", "Null", "Null", "Null", "Null");
        }
        else {
            format = "%-9s | %-19s | %-19s | %-16s | %-14s | %-16s | %-5s%n";
            System.out.printf(format, "vehicleID", "Departure Date", "Arrival Date", "Departure PortID", "Arrival PortID", "Fuel Consumed", "Finished");
            for (Log log : logs) {
                System.out.printf(format,
                        log.getVehicleID(),
                        TerminalUtil.parseDateToString(log.getDepartureDate()),
                        TerminalUtil.parseDateToString(log.getArrivalDate()),
                        log.getDeparturePortID(),
                        log.getArrivalPortID(),
                        log.getFuelConsumed(), log.isFinished());
            }
        }
        System.out.println();
    }
    public static void prettifyLog(Log log) {
        if (log == null) {
            String format = "%-9s | %-20s | %-20s | %-16s | %-16s | %-16s | %-5s%n";
            System.out.printf(format, "vehicleID", "Departure Date", "Arrival Date", "Departure PortID", "Arrival PortID", "Fuel Consumed", "Finished");
            System.out.printf(format, "Null", "Null", "Null", "Null", "Null", "Null", "Null");
            System.out.println();
        }
        else {
            String format = "%-9s | %-19s | %-19s | %-16s | %-14s | %-16s | %-5s%n";
            System.out.printf(format, "vehicleID", "Departure Date", "Arrival Date", "Departure PortID", "Arrival PortID", "Fuel Consumed", "Finished");
            System.out.printf(format,
                    log.getVehicleID(),
                    TerminalUtil.parseDateToString(log.getDepartureDate()),
                    TerminalUtil.parseDateToString(log.getArrivalDate()),
                    log.getDeparturePortID(),
                    log.getArrivalPortID(),
                    log.getFuelConsumed(), log.isFinished());
        }
    }

    public static void prettifyManagerList(List<Manager> Managers) {
        if (Managers.size() == 0) {
            String format = "%-15s | %-15s | %-10s%n";
            System.out.printf(format, "Username", "Password", "Manage PortID");
            System.out.printf(format, "Null", "Null", "Null");
            System.out.println();
        } else {
            int maxLengthUsername = 0;
            int maxLengthPassword = 0;

            // dynamic length
            for (Manager manager: Managers) {
                int LengthUsername = manager.getUsername().length();
                int LengthPassword = manager.getPassword().length();

                if (LengthPassword > maxLengthPassword){
                    maxLengthPassword = LengthPassword;
                }
                if (LengthUsername > maxLengthUsername){
                    maxLengthUsername = LengthUsername;
                }
            }
            String format = "%-" + maxLengthUsername + "s | %-" + maxLengthPassword + "s | %-10s%n";
            System.out.printf(format, "Username", "Password", "Manage PortID");
            for(Manager manager: Managers){
                System.out.printf(format,
                        manager.getUsername(),
                        manager.getPassword(),
                        manager.getManagePortID());
            }
            System.out.println();
        }
    }

    public static void prettifyManager(Manager Manager) {
        if (Manager == null) {
            String format = "%-15s | %-15s | %-10s%n";
            System.out.printf(format, "Username", "Password", "Manage PortID");
            System.out.printf(format, "Null", "Null", "Null");
            System.out.println();
        } else {
            int maxLengthUsername = Manager.getUsername().length();
            int maxLengthPassword = Manager.getPassword().length();

            String format = "%-" + maxLengthUsername + "s | %-" + maxLengthPassword + "s | %-10s%n";
            System.out.printf(format, "Username", "Password", "Manage PortID");
            System.out.printf(format,
                    Manager.getUsername(),
                    Manager.getPassword(),
                    Manager.getManagePortID());
        }
    }

    public static void prettifyAdmin() {
        String format = "%-15s | %-15s%n";
        System.out.printf(format, "Username", "Password");
        System.out.printf(format, Admin.getInstance().getUsername(), Admin.getInstance().getPassword());
        System.out.println();
    }

}