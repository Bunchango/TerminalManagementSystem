package TerminalPortManagementSystem.Interface;
import TerminalPortManagementSystem.User.Admin;
import TerminalPortManagementSystem.Utility.Prettify;
import TerminalPortManagementSystem.Utility.TerminalUtil;

import java.io.IOException;
import java.util.Scanner;

public class AdminInterface {
    public static void run() {
        /* 4 section
        1. Announcements
        2. Create and Remove
        3. Transportation
        4. Stat Query
        */
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("1. Announcement [" + TerminalUtil.announcements.size() + "]");
        System.out.println("2. Create and Remove");
        System.out.println("3. Transportation");
        System.out.println("4. Statistics and Query");
        System.out.println("~. Terminate program");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                announcement();
            }

            case "2" -> {
                createRemove();
            }

            case "3" -> {
                transportation();
            }

            case "4" -> {
                statQuery();
            }
            case "~" -> {
                System.out.println("EXITED");
                System.exit(0);
            }
            default -> {
                System.out.println("Invalid input. Try again");
                run();
            }
        }
    }

    public static void announcement() {
        System.out.println("-----------------------------------------");
        for (String announcement: TerminalUtil.announcements) {
            System.out.println(announcement);
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
        run();
    }

    public static void createRemove() {
        System.out.println("-----------------------------------------");
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Create");
        System.out.println("2. Remove");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                create();
            }
            case "2" -> {
                remove();
            }
            case "~" -> {
                run();
            }
            default -> {
                System.out.println("Invalid input. Please enter either '1' or '2'");
                System.out.println("-----------------------------------------");
                createRemove();
            }
        }

        run();
    }

    public static void create() {
        System.out.println("-----------------------------------------");
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Create Manager");
        System.out.println("2. Create Port");
        System.out.println("3. Create Vehicle");
        System.out.println("4. Create Container");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                createManager();
            }
            case "2" -> {
                createPort();
            }
            case "3" -> {
                createVehicle();
            }
            case "4" -> {
                createContainer();
            }
            case "~" -> {
                createRemove();
            }
            default -> {
                System.out.println("Invalid input. ");
                create();
            }
        }
    }

    public static void createManager() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("Existing managers: ");
        Prettify.prettifyManagerList(TerminalUtil.managers);

        System.out.print("Manager's username: ");
        String username = sc.nextLine().replace(" ", "");
        System.out.print("Manager's password: ");
        String password = sc.nextLine().replace(" ", "");
        System.out.print("Port ID: ");
        String portID = sc.nextLine().replace(" ", "");

        while (true) {
            System.out.print("CONFIRM CREATION - " + username + " | " + password + " | " + portID + ". true / false: ");
            try {
                if (sc.nextBoolean()) {
                    System.out.println(admin.createManager(username, password, portID));
                } else {
                    System.out.println("CREATION CANCELED");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter either 'true' or 'false'.");
                System.out.println("-----------------------------------------");
                sc.nextLine(); // Clear the input buffer
            }
        }
        create();
    }

    public static void createPort() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("Existing ports: ");
        Prettify.prettifyPortList(TerminalUtil.ports);

        while (true) {
            try {
                System.out.print("Port's ID: ");
                String portID = String.valueOf(sc.nextInt());
                // Consume the newline character left in the input buffer
                sc.nextLine();

                System.out.print("Port's name: ");
                String portName = sc.nextLine();

                System.out.print("Port's latitude: ");
                double latitude = sc.nextDouble();
                System.out.print("Port's longitude: ");
                double longitude = sc.nextDouble();
                System.out.print("Port's storing capacity: ");
                double capacity = sc.nextDouble();
                System.out.print("Port's landing ability. true / false: ");
                boolean landingAbility = sc.nextBoolean();

                System.out.print("CONFIRM CREATION - " + portID + " | " + portName + " | " + latitude + " | " + longitude +
                        " | " + capacity + " | " + landingAbility + ". true / false: ");

                if (sc.nextBoolean()) {
                    System.out.println(admin.createPort(portID, portName, latitude, longitude, capacity, landingAbility));
                } else {
                    System.out.println("CREATION CANCELED");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
                System.out.println("-----------------------------------------");
                sc.nextLine(); // Consume the invalid input and newline character
            }
        }
        create();
    }

    public static void createVehicle() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("Existing vehicles: ");
        Prettify.prettifyVehicleList(TerminalUtil.vehicles);

        while (true) {
            try {
                System.out.print("Vehicle's ID: ");
                String vehicleID = String.valueOf(sc.nextInt());
                // Consume the newline character left in the input buffer
                sc.nextLine();

                System.out.println("Available vehicle types: Ship | BasicTruck | TankerTruck | ReeferTruck");
                System.out.print("Vehicle's type: ");
                String vehicleType = sc.nextLine();
                System.out.print("Vehicle's portID: ");
                String portID = sc.nextLine();
                System.out.print("Vehicle's carrying capacity: ");
                double carryingCapacity = sc.nextDouble();
                System.out.print("Vehicle's fuel capacity: ");
                double fuelCapacity = sc.nextDouble();

                System.out.print("CONFIRM CREATION - " + vehicleID + " | " + vehicleType + " | " + portID + " | " + carryingCapacity +
                        " | " + fuelCapacity + ". true / false: ");

                if (sc.nextBoolean()) {
                    System.out.println(admin.createVehicle(vehicleID, vehicleType, portID, carryingCapacity, fuelCapacity));
                } else {
                    System.out.println("CREATION CANCELED");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
                System.out.println("-----------------------------------------");
                sc.nextLine();
            }
        }
        create();
    }

    public static void createContainer() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

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
                System.out.print("Container's portID: ");
                String portID = sc.nextLine();
                System.out.print("Container's weight");
                double weight = sc.nextDouble();

                System.out.print("CONFIRM CREATION - " + containerID + " | " + containerType + " | " + portID + " | " +
                        weight + ". true / false: ");
                if (sc.nextBoolean()) {
                    System.out.println(admin.createContainer(containerID, containerType, portID, weight));
                } else {
                    System.out.println("CREATION CANCELED");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
                System.out.println("-----------------------------------------");
                sc.nextLine();
            }
        }
        create();
    }

    public static void remove() {
        System.out.println("-----------------------------------------");
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Remove Manager");
        System.out.println("2. Remove Port");
        System.out.println("3. Remove Vehicle");
        System.out.println("4. Remove Container");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                removeManager();
            }
            case "2" -> {
                removePort();
            }
            case "3" -> {
                removeVehicle();
            }
            case "4" -> {
                removeContainer();
            }
            case "~" -> {
                createRemove();
            }
            default -> {
                System.out.println("Invalid input. ");
                remove();
            }
        }
    }

    public static void removeManager() {

    }

    public static void removePort() {

    }

    public static void removeVehicle() {

    }

    public static void removeContainer() {

    }

    public static void transportation() {
        System.out.println("-----------------------------------------");
        run();
    }

    public static void statQuery() {
        System.out.println("-----------------------------------------");
        run();
    }
}
