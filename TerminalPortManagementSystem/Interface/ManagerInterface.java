package TerminalPortManagementSystem.Interface;
import TerminalPortManagementSystem.Ports.Container;
import TerminalPortManagementSystem.User.Manager;
import TerminalPortManagementSystem.Utility.Prettify;
import TerminalPortManagementSystem.Utility.TerminalUtil;
import TerminalPortManagementSystem.Vehicles.Vehicle;

import java.sql.SQLOutput;
import java.util.Scanner;
public class ManagerInterface {
    public static void run(Manager manager) {
        /* 4 section
        1. Announcements
        2. Create and Remove
        3. Transportation
        4. Stat Query
        */
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        int announcements = 0;
        for (String announcement : TerminalUtil.announcements) {
            if (announcement.contains(manager.getManagePortID())) {
                announcements ++;
            }
        }
        System.out.println("1. Announcement[" + announcements + "]");
        System.out.println("2. Create, Remove and Update");
        System.out.println("3. Transportation");
        System.out.println("4. Statistics and Query");
        System.out.println("~. Terminate program");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                announcement(manager);
            }

            case "2" -> {
                createRemove(manager);
            }

            case "3" -> {
                transportation(manager);
            }
            case "4" -> {
                statQuery(manager);
            }
            case "~" -> {
                System.out.println("EXITED");
                System.exit(0);
            }
            default -> {
                System.out.println("Invalid input. Try again");
                run(manager);
            }
        }
    }

    public static void announcement(Manager manager) {
        System.out.println("-----------------------------------------");
        for (String announcement : TerminalUtil.announcements) {
            if (announcement.contains(manager.getManagePortID())) {
                System.out.println(announcement);
            }
        }
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("CLEAR ANNOUNCEMENTS? true or false: ");
            try {
                if (sc.nextBoolean()) {
                    TerminalUtil.clearAnnouncement();
                }
                break; // Exit the loop if input is valid

            } catch (Exception e) {
                System.out.println("Invalid input. Please enter either 'true' or 'false'.");
                System.out.println("-----------------------------------------");
                sc.nextLine(); // Clear the input buffer
            }
        }
        run(manager);

    }

    public static void createRemove(Manager manager) {
        System.out.println("-----------------------------------------");
        System.out.println("1. Create Container");
        System.out.println("2. Remove Container");
        System.out.println("3. Update Container");
        System.out.println("~. Go Back");
        System.out.print("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();

        switch (choice){
            case "1" -> {
               createContainer(manager);
            }
            case "2" -> {
                removeContainer(manager);
            }
            case "3" -> {
                updateContainer(manager);
            }
            case "~" ->{
                run(manager);
            }
            default -> {
                System.out.println("Invalid Input. ");
                createContainer(manager);
            }
        }
        run(manager);
    }

    public static void createContainer(Manager manager){
        System.out.println("-----------------------------------------");
        if(manager.getManagePortID() != null) {
            Scanner sc = new Scanner(System.in);
            //print existing ids, ids should be unique
            System.out.println("Unavailable ids: ");
            Prettify.prettifyIdsList(TerminalUtil.getContainerIds());
            System.out.println("Existing containers: ");
            Prettify.prettifyContainerList(TerminalUtil.containers);

            while (true) {
                try {
                    System.out.print("Container's ID: ");
                    String containerID = String.valueOf(sc.nextInt());
                    // Consume the newline character left in the input buffer
                    sc.nextLine();

                    System.out.println("Available container types: DryStorage | OpenTop | OpenSide | Refrigerated | Liquid");
                    System.out.print("Container's type: ");
                    String containerType = sc.nextLine();
                    System.out.print("Container's weight: ");
                    double weight = sc.nextDouble();

                    System.out.print("CONFIRM CREATION - " + containerID + " | " + containerType + " | "  +
                            weight + ". true / false: ");
                    if (sc.nextBoolean()) {
                        System.out.println(manager.createContainer(containerID, containerType, weight));
                    } else {
                        System.out.println("CREATION CANCELED");
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid Input. ");
                    System.out.println("-----------------------------------------");
                    sc.nextLine();
                }
            }
        } else {
            System.out.println("ERROR - This manager's port is Null");
        }

        createRemove(manager);
    }

    public static void removeContainer(Manager manager){
        System.out.println("-----------------------------------------");
        if(manager.getManagePortID() != null) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Existing containers: ");
            Prettify.prettifyContainerList(TerminalUtil.containers);
            System.out.print("Container's ID: ");
            String containerID = sc.nextLine().replace(" ", "");

            while (true) {
                System.out.print("CONFIRM REMOVE CONTAINER " + containerID + ". true / false: ");
                try {
                    if (sc.nextBoolean()) {
                        System.out.println(manager.removeContainer(containerID));
                    } else {
                        System.out.println("REMOVE CANCELED");
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter either 'true' or 'false'.");
                    System.out.println("-----------------------------------------");
                    sc.nextLine(); // Clear the input buffer
                }
            }
        }else {
            System.out.println("ERROR - This manager's port is Null");
        }
        createRemove(manager);
    }

    public static void updateContainer(Manager manager) {
        Scanner sc = new Scanner(System.in);
        if(manager.getManagePortID() != null){
            System.out.println("Containers in port managing: ");
            Prettify.prettifyContainerList(TerminalUtil.searchPort(manager.getManagePortID()).getPortContainers());

            System.out.print("Enter container Id: ");
            String containerID = sc.nextLine();
            System.out.print("Enter new container Id: ");
            String newContainerID = sc.nextLine();

            while (true) {
                try {
                    System.out.print("CONFIRM UPDATE CONTAINER " + containerID + " TO "+ newContainerID +". true / false: ");
                    if(sc.nextBoolean()){
                        System.out.println(TerminalUtil.updateContainerIdOfPort(manager.getManagePortID(),containerID,newContainerID));
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid input. ");
                    System.out.println("-----------------------------------------");
                    sc.nextLine(); // consume string buffer
                }
            }
        } else {
            System.out.println("ERROR - This manager's port is Null");
        }


        createRemove(manager);
    }

    public static void transportation(Manager manager) {
        System.out.println("-----------------------------------------");
        System.out.println("1. Load Container");
        System.out.println("2. Unload Container");
        System.out.println("3. Refuel Vehicle");
        System.out.println("4. Move to Port");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        String choice= sc.nextLine();

        switch (choice) {
            case "1" -> {
                loadContainer(manager);
            }
            case "2" -> {
                unloadContainer(manager);
            }
            case "3" -> {
                refuel(manager);
            }
            case "4" -> {
                moveToPort(manager);
            }
            case "~" ->{
                run(manager);
            }
            default -> {
                System.out.println("Invalid Input. ");

            }
        }
        transportation(manager);
    }

    public static void loadContainer(Manager manager) {
        System.out.println("-----------------------------------------");
        Scanner sc = new Scanner(System.in);

        if (manager.getManagePortID() != null) {
            // return list of all container available for loading
            System.out.println("Available containers for loading:");
            Prettify.prettifyContainerList(TerminalUtil.searchPort(manager.getManagePortID()).getPortContainers());

            System.out.println("Available vehicles: ");
            Prettify.prettifyVehicleList(TerminalUtil.searchPort(manager.getManagePortID()).getPortVehicles());

            System.out.print("Enter Vehicle's ID: ");
            String vehicleID = sc.nextLine().replace(" ","");
            System.out.print("Enter Container's ID: ");
            String containerID = sc.nextLine().replace(" ","");

            Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);
            if(vehicle != null && !vehicle.isSailAway()){
                while(true){
                    System.out.print("CONFIRM LOAD CONTAINER " + vehicleID + " | " + containerID + ". true / false: ");
                    try {
                        if (sc.nextBoolean()) {
                            System.out.println(manager.loadContainer(vehicleID, containerID));
                        } else {
                            System.out.println("LOAD CANCELED");
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please enter either 'true' or 'false'.");
                        System.out.println("-----------------------------------------");
                        sc.nextLine(); // Clear the input buffer
                    }
                }
            } else if(vehicle == null){
                System.out.println("Invalid vehicle - Vehicle does not exist OR Sail away");
            }
        }else if(manager.getManagePortID() == null){
            System.out.println("ERROR - This manager's port is Null");
        }
        transportation(manager);
    }

    public static void unloadContainer(Manager manager) {
        System.out.println("-----------------------------------------");
        Scanner sc = new Scanner(System.in);
        if (manager.getManagePortID() != null) {
            System.out.println("Available vehicle for unloading containers");
            // return existing vehicle to unload
            Prettify.prettifyVehicleList(TerminalUtil.searchPort(manager.getManagePortID()).getPortVehicles());

            System.out.print("Enter Vehicle's ID: ");
            String vehicleID = sc.nextLine().replace(" ","");
            Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);

            if(vehicle != null && !vehicle.isSailAway()){
                System.out.println("Available containers in vehicle:");
                Prettify.prettifyContainerList(TerminalUtil.searchVehicle(vehicleID).getVehicleContainers());

                System.out.print("Enter Container's ID: ");
                String containerID = sc.nextLine().replace(" ","");

                while (true) {
                    try{
                        System.out.print("CONFIRM UNLOAD CONTAINER " + containerID + " | " + vehicleID + ". true / false: ");
                        if(sc.nextBoolean()){
                            System.out.println(manager.unloadContainer(vehicleID,containerID));
                        }else{
                            System.out.println("UNLOAD CANCELED");
                        }break;

                    }catch(Exception e) {
                        System.out.println("Invalid input. Please enter either 'true' or 'false'.");
                        System.out.println("-----------------------------------------");
                        sc.nextLine();
                    }
                }
            }else if(vehicle == null) {
                System.out.println("Invalid vehicle - Vehicle does not exist OR Sail away");
            }
        } else {
            System.out.println("ERROR - This manager's port is Null");
        }
        transportation(manager);
    }

    public static void refuel(Manager manager) {
        System.out.println("-----------------------------------------");
        if (manager.getManagePortID() != null) {
            System.out.println("Available vehicles for refuel: ");
            Prettify.prettifyVehicleList(TerminalUtil.searchPort(manager.getManagePortID()).getPortVehicles());

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Vehicle's ID: ");
            String vehicleID = sc.nextLine().replace(" ","");
            Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID); // search for vehicle

            if(vehicle != null && !vehicle.isSailAway()){
                System.out.print("Enter number of gallons: ");
                double gallons = sc.nextDouble();
                while(true) {
                    try {
                        System.out.print("CONFIRM REFUEL VEHICLE " + vehicleID + " | " + gallons + ". true / false: ");
                        if(sc.nextBoolean()) {
                            System.out.println(manager.refuelVehicle(vehicleID,gallons));
                        }else {
                            System.out.println("REFUEL CANCELED");
                        } break;
                    }catch (Exception e){
                        System.out.println("Invalid input. Please enter either 'true' or 'false'.");
                        System.out.println("-----------------------------------------");
                        sc.nextLine();
                    }
                }
            }else if(vehicle == null) {
                System.out.println("Invalid vehicle - Vehicle does not exist OR Sail away");
            }
        }else {
            System.out.println("ERROR - This manager's port is Null");
        }
        transportation(manager);
    }

    public static void moveToPort(Manager manager) {
        System.out.println("-----------------------------------------");
        Scanner sc = new Scanner(System.in);

        if (manager.getManagePortID() != null) {
            System.out.println("Available vehicle: ");
            // current vehicles in the port
            Prettify.prettifyVehicleList(TerminalUtil.searchPort(manager.getManagePortID()).getPortVehicles());

            System.out.print("Enter Vehicle's ID: ");
            String vehicleID = sc.nextLine().replace(" ","");

            System.out.println("List of all Ports");
            Prettify.prettifyPortList(TerminalUtil.ports);

            System.out.print("Enter Destination's Port ID: ");
            String destinationPortID = sc.nextLine().replace(" ","");

            System.out.println("Date time format: dd-MM-yyyy HH:mm:ss");
            System.out.print("Departure Date and Time: ");
            String departureDate = sc.nextLine();
            System.out.print("Arrival Date and Time: ");
            String arrival = sc.nextLine();
            while(true) {
                try {
                    System.out.print("CONFIRM MOVE TO PORT:  " + vehicleID + " | " + destinationPortID + " | "+ departureDate + " | " + arrival + ". true / false: ");
                    if(sc.nextBoolean()) {
                        System.out.println(manager.moveToPort(vehicleID,destinationPortID,departureDate,arrival));
                    }else {
                        System.out.println("MOVE CANCELED");
                    } break;
                }catch (Exception e) {
                    System.out.println("Invalid input. Please enter either 'true' or 'false'.");
                    System.out.println("-----------------------------------------");
                    sc.nextLine();
                }
            }
        }else{
            System.out.println("ERROR - This manager's port is Null");
        }
        transportation(manager);
    }

    public static void statQuery(Manager manager) {
/*
        1. Total/Overview Statistics
        2. Listing
        3. Get Trips
 */
        System.out.println("-----------------------------------------");
        System.out.println("1. Overview Statistics");
        System.out.println("2. Objects Listing Statistics");
        System.out.println("3. Get Trips");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();

        switch (choice) {

            case "1" -> {
                overview(manager);
            }
            case "2" -> {
                listing(manager);
            }
            case "3" -> {
                getTrips(manager);
            }
            case "~" ->{
                run(manager);
            }
            default -> {
                System.out.println("Invalid Input. ");
            }
        }
        statQuery(manager);
    }

    public static void overview(Manager manager) {
        System.out.println("-----------------------------------------");
        System.out.println("1. Total Fuel Consumed Per Day ");
        System.out.println("2. Total Fuel Consumed by Date ");
        System.out.println("3. Total Weight of Each Type of Container ");
        System.out.println("4. Number of Containers of Each Type ");
        System.out.println("5. Number of Vehicles of Each Type ");
        System.out.println("~. Go Back");
        System.out.print("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();

        switch (choice) {

            case "1" -> {
                if(manager.getManagePortID() != null) {
                    Prettify.prettifyGetTotalFuelConsumedPerDay(manager.getTotalFuelConsumedPerDay());
                }else {
                    System.out.println("ERROR - This manager's port is Null");
                }
            }
            case "2" -> {
                if(manager.getManagePortID() != null) {
                    System.out.print("Enter The Date ( dd-MM-yyyy ): ");
                    String date = sc.nextLine().replace(" ","");
                    System.out.println(manager.getTotalConsumedFuelByDate(date));
                }else {
                    System.out.println("ERROR - This manager's port is Null");
                }

            }
            case "3" -> {
                if(manager.getManagePortID() != null) {
                    Prettify.prettifyGetTotalWeightOfEachType(manager.getTotalWeightOfEachType());
                }else {
                    System.out.println("ERROR - This manager's port is Null");
                }
            }
            case "4" -> {
                if(manager.getManagePortID() != null) {
                    Prettify.prettifyGetNumberOfContainerOfEachType(manager.getNumberOfContainerOfEachType());
                } else {
                    System.out.println("ERROR - This manager's port is Null");
                }
            }
            case "5" -> {
                if(manager.getManagePortID() != null) {
                    Prettify.prettifyGetNumberOfVehicleOfEachType(manager.getNumberOfVehicleOfEachType());
                } else {
                    System.out.println("ERROR - This manager's port is Null");
                }
            }case "~" ->{
                statQuery(manager);
            }
            default -> {
                System.out.println("Invalid Input. ");
            }
        }
        overview(manager);
    }

    public static void listing(Manager manager) {
        System.out.println("-----------------------------------------");
        System.out.println("1. List of Vehicles by Type");
        System.out.println("2. List of All Vehicles");
        System.out.println("3. List of All The Containers");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();

        switch (choice) {

            case "1" -> {
                if(manager.getManagePortID() != null) {
                    System.out.println("Available vehicle types: ReeferTruck | TankerTruck | BasicTruck | Ship ");
                    System.out.print("Enter Vehicle's Type: ");
                    String vehicleType = sc.nextLine();
                    Prettify.prettifyVehicleList(manager.getListOfVehicleByType(vehicleType));
                }else {
                    System.out.println("ERROR - This manager's port is Null");
                }
            }

            case "2" -> {
                if(manager.getManagePortID() != null) {
                    Prettify.prettifyVehicleList(manager.getListOfAllVehicles());
                } else {
                    System.out.println("ERROR - This manager's port is Null");
                }
            }

            case "3" -> {
                if(manager.getManagePortID() != null) {
                    Prettify.prettifyContainerList(manager.getListOfAllContainers());
                }else {
                    System.out.println("ERROR - This manager's port is Null");
                }
            }
            case "~" -> {
                statQuery(manager);
            }
            default -> {
                System.out.println("Invalid Input. ");
            }
        }
        listing(manager);
    }

    public static void getTrips(Manager manager) {
        System.out.println("-----------------------------------------");
        System.out.println("1. Get The Trips by Arrival Date");
        System.out.println("2. Get The Trips by Departure Date");
        System.out.println("3. Get The Trips That Arrives Between A to B Dates ");
        System.out.println("4. Get The Trips That Departs Between A to B Dates ");
        System.out.println("5. Get Trips Between A to B Dates ");
        System.out.println("~. Go back ");
        System.out.print("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();

        switch (choice) {
            case "1" -> {
                if(manager.getManagePortID() != null) {
                    System.out.println("GUIDE: get trips by arrival date.");
                    System.out.print("Enter Arrival Date ( dd-MM-yyyy ): ");
                    String date = sc.nextLine();
                    Prettify.prettifyLogList(manager.getTripsByArrivalDate(date));
                }else {
                    System.out.println("ERROR - This manager's port is Null");
                }
            }
            case "2" -> {
                if(manager.getManagePortID() != null) {
                    System.out.println("GUIDE: get trips by departure date.");
                    System.out.print("Enter Departure Date ( dd-MM-yyyy ): ");
                    String date = sc.nextLine();
                    Prettify.prettifyLogList(manager.getTripsByDepartureDate(date));
                }else {
                    System.out.println("ERROR - This manager's port is Null");
                }

            }
            case "3" -> {
                if(manager.getManagePortID() != null) {
                    System.out.println("GUIDE: get trips between 2 dates by arrival date.");
                    System.out.print("Enter Starting Date A ( dd-MM-yyyy ): ");
                    String startDate = sc.nextLine();
                    System.out.print("Enter Ending Date B ( dd-MM-yyyy ): ");
                    String endDate= sc.nextLine();
                    Prettify.prettifyLogList(manager.getTripsBetweenArrivalDates(startDate,endDate));
                }else {
                    System.out.println("ERROR - This manager's port is Null");
                }

            }
            case "4" -> {
                if(manager.getManagePortID() != null) {
                    System.out.println("GUIDE: get trips between 2 dates by departure date.");
                    System.out.print("Enter Starting Date A ( dd-MM-yyyy ): ");
                    String startDate = sc.nextLine();
                    System.out.print("Enter Ending Date B ( dd-MM-yyyy ): ");
                    String endDate= sc.nextLine();
                    Prettify.prettifyLogList(manager.getTripsBetweenDepartureDates(startDate,endDate));
                }else {
                    System.out.println("ERROR - This manager's port is Null");
                }

            }
            case "5" -> {
                if(manager.getManagePortID() != null) {
                    System.out.println("GUIDE: get trips between 2 dates.");
                    System.out.print("Enter Starting Date A ( dd-MM-yyyy ): ");
                    String startDate = sc.nextLine();
                    System.out.print("Enter Ending Date B ( dd-MM-yyyy ): ");
                    String endDate= sc.nextLine();
                    Prettify.prettifyLogList(manager.getTripsInDates(startDate,endDate));
                }else {
                    System.out.println("ERROR - This manager's port is Null");
                }
            }
            case "~" -> {
                statQuery(manager);
            }
            default -> {
                System.out.println("Invalid Input. ");
            }
        }
        getTrips(manager);
    }
}