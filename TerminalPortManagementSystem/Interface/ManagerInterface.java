package TerminalPortManagementSystem.Interface;
import TerminalPortManagementSystem.User.Manager;
import TerminalPortManagementSystem.Utility.Prettify;
import TerminalPortManagementSystem.Utility.TerminalUtil;

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
        System.out.println("1. Announcement");
        System.out.println("2. Create and Remove");
        System.out.println("3. Transportation");
        System.out.println("4. Statistics and Query");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                System.out.println("-----------------------------------------");
                announcement(manager);
            }

            case "2" -> {
                System.out.println("-----------------------------------------");
                createRemove(manager);
            }

            case "3" -> {
                System.out.println("-----------------------------------------");
                transportation(manager);
            }

            case "4" -> {
                System.out.println("-----------------------------------------");
                statQuery(manager);
            }
        }
    }

    public static void announcement(Manager manager) {
        for (String announcement : TerminalUtil.announcements) {
            if (announcement.contains(manager.getManagePortID())) {
                System.out.println(announcement);
            }
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Clear all? true / false: ");
        boolean clear = sc.nextBoolean();

        if (clear) {
            TerminalUtil.clearAnnouncement();
        }

        while (true) {
            System.out.print("Return? true / false: ");
            boolean back = sc.nextBoolean();

            if (back) {
                break;

            }
        }
        run(manager);
    }

    public static void createRemove(Manager manager) {

        System.out.println("1. Create Container");
        System.out.println("2. Remove Container");
        System.out.println("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();

        switch (choice){
            case "1" -> {
                System.out.println("Enter Container's ID: ");
                int containerID = Integer.parseInt(sc.nextLine());
                String ID = String.valueOf(containerID);
                System.out.println("Enter Container's Type:");
                String containerType = sc.nextLine();
                System.out.println("Enter Container's Weight: ");
                double containerWeight = sc.nextDouble();

                manager.createContainer(ID,containerType,containerWeight);
            }

            case "2" -> {
                System.out.println("Enter Container's ID: ");
                int containerID = Integer.parseInt(sc.nextLine());
                String ID = String.valueOf(containerID);

                manager.removeContainer(ID);
            }
        }

    }

    public static void transportation(Manager manager) {

        System.out.println("1. Load Container");
        System.out.println("2. Unload Container");
        System.out.println("3. Refuel Vehicle");
        System.out.println("4. Move to Port");
        System.out.println("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        String choice= sc.nextLine();

        switch (choice) {

            case "1" -> {
                System.out.println("Enter Vehicle's ID: ");
                int vehicleID = Integer.parseInt(sc.nextLine());
                String veID = String.valueOf(vehicleID);
                System.out.println("Enter Container's ID: ");
                int containerID = Integer.parseInt(sc.nextLine());
              String conID = String.valueOf(containerID);
                manager.loadContainer(veID,conID);
            }

            case "2" -> {
                System.out.println("Enter Vehicle's ID: ");
                int vehicleID = Integer.parseInt(sc.nextLine());
                String veID = String.valueOf(vehicleID);
                System.out.println("Enter Container's ID: ");
                int containerID = Integer.parseInt(sc.nextLine());
                String conID = String.valueOf(containerID);
                manager.unloadContainer(veID,conID);
            }

            case "3" -> {
                System.out.println("Enter Vehicle's ID: ");
                int vehicleID = Integer.parseInt(sc.nextLine());
                String veID = String.valueOf(vehicleID);
                System.out.println("Enter number of gallons: ");
                double gallons = sc.nextDouble();

                manager.refuelVehicle(veID,gallons);
            }

            case "4" -> {
                System.out.println("Enter Vehicle's ID: ");
                int vehicleID = Integer.parseInt(sc.nextLine());
                String veID = String.valueOf(vehicleID);
                System.out.println("Enter Destination's Port ID: ");
                int destinationPortID = Integer.parseInt(sc.nextLine());
                String desID = String.valueOf(destinationPortID);
                System.out.println("Enter Departure Date:");
                String departureDate = sc.nextLine();
                System.out.println("Enter Arrival Date: ");
                String arrivalDate = sc.nextLine();

                manager.moveToPort(veID, desID,departureDate,arrivalDate);
            }
        }
    }

    public static void statQuery(Manager manager) {
/*
        1. Total/Overview Statistics
        2. Listing
        3. Get Trips
 */
        System.out.println("1. Overview Statistics");
        System.out.println("2. Listing Statistics");
        System.out.println("3. Get Trips");
        System.out.println("Enter your choice: ");
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
        }
    }

    public static void overview(Manager manager) {
        System.out.println("1. Total Fuel Consumed Per Day");
        System.out.println("2. Total Fuel Consumed by Date");
        System.out.println("3. Total Weight of Each Type of Container");
        System.out.println("4. Number of Containers of Each Type");
        System.out.println("5. Number of Vehicles of Each Type");
        System.out.println("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();

        switch (choice) {

            case "1" -> {
                Prettify.prettifyGetTotalFuelConsumedPerDay(manager.getTotalFuelConsumedPerDay());
            }

            case "2" -> {
                System.out.println("Enter The Date: ");
                String date = sc.nextLine();
                manager.getTotalConsumedFuelByDate(date);
            }

            case "3" -> {
                Prettify.prettifyGetTotalWeightOfEachType(manager.getTotalWeightOfEachType());
            }

            case "4" -> {
                Prettify.prettifyGetNumberOfContainerOfEachType(manager.getNumberOfContainerOfEachType());
            }

            case "5" -> {
                Prettify.prettifyGetNumberOfVehicleOfEachType(manager.getNumberOfVehicleOfEachType());
            }
        }
    }

    public static void listing(Manager manager) {
        System.out.println("1. List of Vehicles by Type");
        System.out.println("2. List of All Vehicles");
        System.out.println("3. List of All The Containers");
        System.out.println("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();

        switch (choice) {

            case "1" -> {
                System.out.println("Enter Vehicle's Type");
                String vehicleType = sc.nextLine();
                manager.getListOfVehicleByType(vehicleType);
            }

            case "2" -> {
                Prettify.prettifyVehicleList(manager.getListOfAllVehicles());
            }

            case "3" -> {
                Prettify.prettifyContainerList(manager.getListOfAllContainers());
            }
        }
    }

    public static void getTrips(Manager manager) {
        System.out.println("1. Get The Trips by Arrival Date");
        System.out.println("2. Get The Trips by Departure Date");
        System.out.println("3. Get The Trips That Arrives Between A to B Dates ");
        System.out.println("4. Get The Trips That Departs Between A to B Dates ");
        System.out.println("5. Get Trips Between A to B Dates ");
        System.out.println("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();

        switch (choice) {

            case "1" -> {
                System.out.println("Enter Arrival Date: ");
                String date = sc.nextLine();
                manager.getTripsByArrivalDate(date);
            }

            case "2" -> {
                System.out.println("Enter Departure Date: ");
                String date = sc.nextLine();
                manager.getTripsByDepartureDate(date);
            }

            case "3" -> {
                System.out.println("Enter Starting Date A: ");
                String startDate = sc.nextLine();
                System.out.println("Enter Ending Date B: ");
                String endDate= sc.nextLine();
                manager.getTripsBetweenArrivalDates(startDate,endDate);
            }

            case "4" -> {
                System.out.println("Enter Starting Date A: ");
                String startDate = sc.nextLine();
                System.out.println("Enter Ending Date B: ");
                String endDate= sc.nextLine();
                manager.getTripsBetweenDepartureDates(startDate,endDate);
            }

            case "5" -> {
                System.out.println("Enter Starting Date A: ");
                String startDate = sc.nextLine();
                System.out.println("Enter Ending Date B: ");
                String endDate= sc.nextLine();
                manager.getTripsInDates(startDate,endDate);
            }
        }
    }

    }