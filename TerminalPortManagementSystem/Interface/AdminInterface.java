package TerminalPortManagementSystem.Interface;
import TerminalPortManagementSystem.User.Admin;
import TerminalPortManagementSystem.Utility.Prettify;
import TerminalPortManagementSystem.Utility.TerminalUtil;
import TerminalPortManagementSystem.Vehicles.Vehicle;
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
            case "1" -> announcement();

            case "2" -> createRemove();

            case "3" -> transportation();

            case "4" -> statQuery();
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
            case "1" -> create();
            case "2" -> remove();
            case "~" -> run();
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
            case "1" -> createManager();
            case "2" -> createPort();
            case "3" -> createVehicle();
            case "4" -> createContainer();
            case "~" -> createRemove();
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

        System.out.println("Unavailable usernames: ");
        Prettify.prettifyIdsList(TerminalUtil.getManagerIds());
        System.out.println("Existing managers: ");
        Prettify.prettifyManagerList(TerminalUtil.managers);
        System.out.println("Existing ports: ");
        Prettify.prettifyPortList(TerminalUtil.ports);

        System.out.print("Manager's username: ");
        String username = sc.nextLine().replaceAll("[^a-zA-Z0-9]", "").replace(" ", "");
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

        System.out.println("Unavailable ids: ");
        Prettify.prettifyIdsList(TerminalUtil.getPortIds());
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

                System.out.print("CONFIRM CREATION - " + "p-" + portID + " | " + portName + " | " + latitude + " | " + longitude +
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

        System.out.println("Unavailable ids: ");
        Prettify.prettifyIdsList(TerminalUtil.getVehicleIds());
        System.out.println("Existing vehicles: ");
        Prettify.prettifyVehicleList(TerminalUtil.vehicles);
        System.out.println("Existing ports: ");
        Prettify.prettifyPortList(TerminalUtil.ports);

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

        System.out.println("Unavailable ids: ");
        Prettify.prettifyIdsList(TerminalUtil.getContainerIds());
        System.out.println("Existing containers: ");
        Prettify.prettifyContainerList(TerminalUtil.containers);
        System.out.println("Existing ports: ");
        Prettify.prettifyPortList(TerminalUtil.ports);

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
                System.out.print("Container's weight: ");
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
        System.out.println("5. Set and unset manager's manage portID");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> removeManager();
            case "2" -> removePort();
            case "3" -> removeVehicle();
            case "4" -> removeContainer();
            case "5" -> setUnset();
            case "~" -> createRemove();
            default -> {
                System.out.println("Invalid input. ");
                remove();
            }
        }
    }

    public static void removeManager() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("Existing managers: ");
        Prettify.prettifyManagerList(TerminalUtil.managers);
        System.out.print("Manager's username: ");
        String username = sc.nextLine().replace(" ", "");
        System.out.println(admin.removeManager(username));

        while (true) {
            System.out.print("CONFIRM REMOVE MANAGER: " + username + ". true / false: ");
            try {
                if (sc.nextBoolean()) {
                    System.out.println(admin.removeManager(username));
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
        remove();
    }

    public static void removePort() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("Existing ports: ");
        Prettify.prettifyPortList(TerminalUtil.ports);
        System.out.print("Port's ID: ");
        String portID = sc.nextLine().replace(" ", "");

        while (true) {
            System.out.print("CONFIRM REMOVE PORT - " + "p-" + portID + ". true / false: ");
            try {
                if (sc.nextBoolean()) {
                    System.out.println(admin.removePort(portID));
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
        remove();
    }

    public static void removeVehicle() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("Existing vehicles: ");
        Prettify.prettifyVehicleList(TerminalUtil.vehicles);
        System.out.print("Vehicle's ID: ");
        String vehicleID = sc.nextLine().replace(" ", "");

        while (true) {
            System.out.print("CONFIRM REMOVE VEHICLE " + vehicleID + ". true / false: ");
            try {
                if (sc.nextBoolean()) {
                    System.out.println(admin.removeVehicle(vehicleID));
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
        remove();
    }

    public static void removeContainer() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("Existing containers: ");
        Prettify.prettifyContainerList(TerminalUtil.containers);
        System.out.print("Container's ID: ");
        String containerID = sc.nextLine().replace(" ", "");

        while (true) {
            System.out.print("CONFIRM REMOVE CONTAINER " + containerID + ". true / false: ");
            try {
                if (sc.nextBoolean()) {
                    System.out.println(admin.removeContainer(containerID));
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
        remove();
    }

    public static void setUnset() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Set Manager's Port");
        System.out.println("2. Unset Manager's Port");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                System.out.println("Existing managers: ");
                Prettify.prettifyManagerList(TerminalUtil.managers);
                System.out.println("Existing ports: ");
                Prettify.prettifyPortList(TerminalUtil.ports);
                System.out.print("Manager's username: ");
                String username = sc.nextLine().replace(" ", "");
                System.out.print("PortID: ");
                String portID = sc.nextLine().replace(" ", "");

                while (true) {
                    System.out.print("CONFIRM SET MANAGER'S PORT " + username + " | " + portID + ". true / false: ");
                    try {
                        if (sc.nextBoolean()) {
                            System.out.println(admin.setManagerPort(username, portID));
                        } else {
                            System.out.println("SET CANCELED");
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please enter either 'true' or 'false'.");
                        System.out.println("-----------------------------------------");
                        sc.nextLine(); // Clear the input buffer
                    }
                }
                remove();
            }

            case "2" -> {
                System.out.println("Existing managers: ");
                Prettify.prettifyManagerList(TerminalUtil.managers);
                System.out.print("Manager's username: ");
                String username = sc.nextLine().replace(" ", "");

                while (true) {
                    System.out.print("CONFIRM UNSET MANAGER'S PORT " + username + ". true / false: ");
                    try {
                        if (sc.nextBoolean()) {
                            System.out.println(admin.unsetManagerPort(username));
                        } else {
                            System.out.println("UNSET CANCELED");
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please enter either 'true' or 'false'.");
                        System.out.println("-----------------------------------------");
                        sc.nextLine(); // Clear the input buffer
                    }
                }
            }

            case "~" -> remove();
            default -> {
                System.out.println("Invalid input. ");
                setUnset();
            }
        }
        remove();
    }

    public static void transportation() {
        System.out.println("-----------------------------------------");
        Scanner sc = new Scanner(System.in);

        System.out.println("1. Load container");
        System.out.println("2. Unload container");
        System.out.println("3. Refuel");
        System.out.println("4. Move to port");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");

        String option = sc.nextLine();
        switch (option) {
            case "1" -> loadContainer();
            case "2" -> unloadContainer();
            case "3" -> refuel();
            case "4" -> moveToPort();
            case "~" -> run();
            default -> {
                System.out.println("Invalid input. ");
                transportation();
            }
        }
        run();
    }

    public static void loadContainer() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("Existing vehicles: ");
        Prettify.prettifyVehicleList(TerminalUtil.vehicles);
        System.out.print("Vehicle's ID: ");
        String vehicleID = sc.nextLine().replace(" ", "");

        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);

        if (vehicle != null && !vehicle.isSailAway()) {
            System.out.println("Existing containers in vehicle's port: ");
            Prettify.prettifyContainerList(vehicle.getCurrentPort().getPortContainers());
            System.out.print("Container's ID: ");
            String containerID = sc.nextLine().replace(" ", "");

            while (true) {
                System.out.print("CONFIRM LOAD CONTAINER " + vehicleID + " | " + containerID + ". true / false: ");
                try {
                    if (sc.nextBoolean()) {
                        System.out.println(admin.loadContainer(vehicleID, containerID));
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
        } else if (vehicle == null) {
            System.out.println("Invalid vehicle - Vehicle does not exist OR Sail away");
        }
        transportation();
    }

    public static void unloadContainer() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("Existing vehicles: ");
        Prettify.prettifyVehicleList(TerminalUtil.vehicles);
        System.out.print("Vehicle's ID: ");
        String vehicleID = sc.nextLine().replace(" ", "");

        Vehicle vehicle = TerminalUtil.searchVehicle(vehicleID);

        if (vehicle != null && !vehicle.isSailAway()) {
            System.out.println("Existing containers in vehicles: ");
            Prettify.prettifyContainerList(vehicle.getVehicleContainers());
            System.out.print("Container's ID: ");
            String containerID = sc.nextLine().replace(" ", "");

            while (true) {
                System.out.print("CONFIRM UNLOAD CONTAINER " + vehicleID + " | " + containerID + ". true / false: ");
                try {
                    if (sc.nextBoolean()) {
                        System.out.println(admin.unloadContainer(vehicleID, containerID));
                    } else {
                        System.out.println("UNLOAD CANCELED");
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter either 'true' or 'false'.");
                    System.out.println("-----------------------------------------");
                    sc.nextLine(); // Clear the input buffer
                }
            }
        } else if (vehicle == null) {
            System.out.println("Invalid vehicle - Vehicle does not exist OR Sail away");
        }
        transportation();
    }

    public static void refuel() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("Existing vehicles: ");
        Prettify.prettifyVehicleList(TerminalUtil.vehicles);
        System.out.print("Vehicle's ID: ");
        String vehicleID = sc.nextLine();

        while (true) {
            try {
                System.out.print("Gallons to refuel: ");
                double gallons = sc.nextDouble();

                System.out.print("CONFIRM REFUEL - " + vehicleID + " | " + gallons + ". true / false: ");
                if (sc.nextBoolean()) {
                    System.out.println(admin.refuelVehicle(vehicleID, gallons));
                } else {
                    System.out.println("REFUEL CANCELED");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
                System.out.println("-----------------------------------------");
                sc.nextLine();
            }
        }
        transportation();
    }

    public static void moveToPort() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("Existing vehicles: ");
        Prettify.prettifyVehicleList(TerminalUtil.vehicles);
        System.out.println("Existing ports: ");
        Prettify.prettifyPortList(TerminalUtil.ports);
        System.out.print("Vehicle's ID: ");
        String vehicleID = sc.nextLine();
        System.out.print("Destination Port's ID: ");
        String destinationPortID = sc.nextLine();
        System.out.println("Date Time format: dd-MM-yyyy HH:mm:ss");
        System.out.print("Departure Date and Time: ");
        String departure = sc.nextLine();
        System.out.print("Arrival Date and Time: ");
        String arrival = sc.nextLine();

        while (true) {
            try {
                System.out.print("CONFIRM MOVE - " + vehicleID + " | " + destinationPortID +
                        " | " + departure + " | " + arrival + ". true / false: ");
                if (sc.nextBoolean()) {
                    System.out.println(admin.moveToPort(vehicleID, destinationPortID, departure, arrival));
                } else {
                    System.out.println("MOVE CANCELED");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
                System.out.println("-----------------------------------------");
                sc.nextLine();
            }
        }
        transportation();
    }

    public static void statQuery() {
        System.out.println("-----------------------------------------");
        Scanner sc = new Scanner(System.in);

        System.out.println("1. Fuel");
        System.out.println("2. Containers");
        System.out.println("3. Vehicles");
        System.out.println("4. Trips");
        System.out.println("5. Other");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> getFuel();
            case "2" -> getContainers();
            case "3" -> getVehicles();
            case "4" -> getTrips();
            case "5" -> getOther();
            case "~" -> run();
            default -> {
                System.out.println("Invalid input. ");
                statQuery();
            }
        }
    }

    public static void getFuel() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("1. Fuel consumed per day");
        System.out.println("2. Fuel consumed per day of a Port");
        System.out.println("3. Fuel consumed query by date");
        System.out.println("4. Fuel consumed query by date of a port");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                Prettify.prettifyGetTotalFuelConsumedPerDay(admin.totalFuelConsumedPerDay());
                getFuel();
            }
            case "2" -> {
                System.out.println("Existing ports: ");
                Prettify.prettifyPortList(TerminalUtil.ports);
                System.out.print("PortID: ");
                String portID = sc.nextLine();
                Prettify.prettifyGetTotalFuelConsumedPerDay(admin.totalFuelConsumedPerDayOfPort(portID));
                getFuel();
            }
            case "3" -> {
                System.out.print("Date (dd-MM-yyyy): ");
                String date = sc.nextLine();
                System.out.println("Fuel consumed in " + date + " is: " + admin.getTotalConsumedFuelByDate(date));
                getFuel();
            }
            case "4" -> {
                System.out.println("Existing ports: ");
                Prettify.prettifyPortList(TerminalUtil.ports);
                System.out.print("PortID: ");
                String portID = sc.nextLine();
                System.out.print("Date (dd-MM-yyyy): ");
                String date = sc.nextLine();
                System.out.println("Fuel consumed in " + date + " of " + portID + " is: " +
                        admin.getTotalConsumedFuelByDayByPort(portID, date));
                getFuel();
            }
            case "~" -> statQuery();
            default -> {
                System.out.println("Invalid input. ");
                getFuel();
            }
        }
    }

    public static void getContainers() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("1. Total weight of each type");
        System.out.println("2. Total weight of each type of a port");
        System.out.println("3. Number of container of each type");
        System.out.println("4. Number of container of each type of a port");
        System.out.println("5. List of all containers");
        System.out.println("6. List of containers of a port");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                Prettify.prettifyGetTotalWeightOfEachType(admin.getTotalWeightOfEachType());
                getContainers();
            }
            case "2" -> {
                System.out.println("Existing ports: ");
                Prettify.prettifyPortList(TerminalUtil.ports);
                System.out.print("PortID: ");
                String portID = sc.nextLine();
                Prettify.prettifyGetTotalWeightOfEachType(admin.getTotalWeightOfEachTypeByPort(portID));
                getContainers();
            }
            case "3" -> {
                Prettify.prettifyGetNumberOfContainerOfEachType(admin.getNumberOfContainerOfEachType());
                getContainers();
            }
            case "4" -> {
                System.out.println("Existing ports: ");
                Prettify.prettifyPortList(TerminalUtil.ports);
                System.out.print("PortID: ");
                String portID = sc.nextLine();
                Prettify.prettifyGetNumberOfContainerOfEachType(admin.getNumberOfContainerOfEachTypeByPort(portID));
                getContainers();
            }
            case "5" -> {
                Prettify.prettifyContainerList(admin.getListOfAllContainer());
                getContainers();
            }
            case "6" -> {
                System.out.println("Existing ports: ");
                Prettify.prettifyPortList(TerminalUtil.ports);
                System.out.print("PortID: ");
                String portID = sc.nextLine();
                Prettify.prettifyContainerList(admin.getListOfContainerByPort(portID));
                getContainers();
            }
            case "~" -> statQuery();
            default -> {
                System.out.println("Invalid input. ");
                getContainers();
            }
        }
    }

    public static void getVehicles() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("1. List of all vehicles");
        System.out.println("2. List of vehicles by type");
        System.out.println("3. List of vehicles in port by type");
        System.out.println("4. Number of vehicle of each type");
        System.out.println("5. Number of vehicle in port of each type");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                Prettify.prettifyVehicleList(admin.getListOfAllVehicle());
                getVehicles();
            }
            case "2" -> {
                System.out.println("Available vehicle types: Ship | BasicTruck | TankerTruck | ReeferTruck");
                System.out.print("Vehicle type: ");
                String vehicleType = sc.nextLine();
                Prettify.prettifyVehicleList(admin.getListOfVehicleByType(vehicleType));
                getVehicles();
            }
            case "3" -> {
                Prettify.prettifyPortList(TerminalUtil.ports);
                System.out.print("PortID: ");
                String portID = sc.nextLine();
                System.out.println("Available vehicle types: Ship | BasicTruck | TankerTruck | ReeferTruck");
                System.out.print("Vehicle type: ");
                String vehicleType = sc.nextLine();
                Prettify.prettifyVehicleList(admin.getListOFVehicleByTypeOfPort(portID, vehicleType));
                getVehicles();
            }
            case "4" -> {
                Prettify.prettifyGetNumberOfVehicleOfEachType(admin.getNumberOfVehicleOfEachType());
                getVehicles();
            }
            case "5" -> {
                Prettify.prettifyPortList(TerminalUtil.ports);
                System.out.print("PortID: ");
                String portID = sc.nextLine();
                Prettify.prettifyGetNumberOfVehicleOfEachType(admin.getNumberOfVehicleOfEachTypeByPort(portID));
                getVehicles();
            }
            case "~" -> statQuery();
            default -> {
                System.out.println("Invalid input. ");
                getContainers();
            }
        }
    }

    public static void getTrips() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("1. Trips by arrival date");
        System.out.println("2. Trips by departure date");
        System.out.println("3. Trips of port by arrival date");
        System.out.println("4. Trips of port by departure date");
        System.out.println("5. Trips between arrival dates");
        System.out.println("6. Trips between departure dates");
        System.out.println("7. Get the trips of port that arrives between A to B dates");
        System.out.println("8. Get the trips of port that departs between A to B dates");
        System.out.println("9. Trips in dates");
        System.out.println("10. Trips of port in dates");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                System.out.print("Arrival Date ( dd-MM-yyyy ): ");
                String arrival = sc.nextLine();
                Prettify.prettifyLogList(admin.getTripsByArrivalDate(arrival));
                getTrips();
            }
            case "2" -> {
                System.out.print("Departure Date ( dd-MM-yyyy ): ");
                String departure = sc.nextLine();
                Prettify.prettifyLogList(admin.getTripsByDepartureDate(departure));
                getTrips();
            }
            case "3" -> {
                Prettify.prettifyPortList(TerminalUtil.ports);
                System.out.print("PortID: ");
                String portID = sc.nextLine();
                System.out.print("Arrival Date ( dd-MM-yyyy ): ");
                String arrival = sc.nextLine();
                Prettify.prettifyLogList(admin.getTripsByArrivalDateOfPort(arrival, portID));
                getTrips();
            }
            case "4" -> {
                Prettify.prettifyPortList(TerminalUtil.ports);
                System.out.print("PortID: ");
                String portID = sc.nextLine();
                System.out.print("Departure Date ( dd-MM-yyyy ): ");
                String departure = sc.nextLine();
                Prettify.prettifyLogList(admin.getTripsByDepartureDateOfPort(departure, portID));
                getTrips();
            }
            case "5" -> {
                System.out.println("GUIDE: Get trips where its arrival date is between 2 given dates");
                System.out.print("Start Date ( dd-MM-yyyy ): ");
                String start = sc.nextLine();
                System.out.print("End Date ( dd-MM-yyyy ): ");
                String end = sc.nextLine();
                Prettify.prettifyLogList(admin.getTripsBetweenArrivalDates(start, end));
                getTrips();
            }
            case "6" -> {
                System.out.println("GUIDE: Get trips where its departure date is between 2 given dates");
                System.out.print("Start Date ( dd-MM-yyyy ): ");
                String start = sc.nextLine();
                System.out.print("End Date ( dd-MM-yyyy ): ");
                String end = sc.nextLine();
                Prettify.prettifyLogList(admin.getTripsBetweenDepartureDates(start, end));
                getTrips();
            }
            case "7" -> {
                System.out.println("GUIDE: Get trips where its arrival date is between 2 given dates and its portID is the given portID");
                Prettify.prettifyPortList(TerminalUtil.ports);
                System.out.print("PortID: ");
                String portID = sc.nextLine();
                System.out.print("Start Date ( dd-MM-yyyy ): ");
                String start = sc.nextLine();
                System.out.print("End Date ( dd-MM-yyyy ): ");
                String end = sc.nextLine();
                Prettify.prettifyLogList(admin.getTripsBetweenArrivalDatesOfPort(start, end, portID));
                getTrips();
            }
            case "8" -> {
                System.out.println("GUIDE: Get trips where its departure date is between 2 given dates and its portID is the given portID");
                Prettify.prettifyPortList(TerminalUtil.ports);
                System.out.print("PortID: ");
                String portID = sc.nextLine();
                System.out.print("Start Date ( dd-MM-yyyy ): ");
                String start = sc.nextLine();
                System.out.print("End Date ( dd-MM-yyyy ): ");
                String end = sc.nextLine();
                Prettify.prettifyLogList(admin.getTripsBetweenDepartureDatesOfPort(start, end, portID));
                getTrips();
            }
            case "9" -> {
                System.out.println("GUIDE: Get trips where both departureDate and arrivalDate is between the 2 given dates");
                System.out.print("Start Date ( dd-MM-yyyy ): ");
                String start = sc.nextLine();
                System.out.print("End Date ( dd-MM-yyyy ): ");
                String end = sc.nextLine();
                Prettify.prettifyLogList(admin.getTripsInDates(start, end));
                getTrips();
            }
            case "10" -> {
                System.out.println("GUIDE: Get trips where both departureDate and arrivalDate is between the 2 given dates and its portID is the given portID");
                Prettify.prettifyPortList(TerminalUtil.ports);
                System.out.print("PortID: ");
                String portID = sc.nextLine();
                System.out.print("Start Date ( dd-MM-yyyy ): ");
                String start = sc.nextLine();
                System.out.print("End Date ( dd-MM-yyyy ): ");
                String end = sc.nextLine();
                Prettify.prettifyLogList(admin.getTripsInDatesOfPort(start, end, portID));
                getTrips();
            }
            case "~" -> statQuery();
            default -> {
                System.out.println("Invalid input. ");
                getTrips();
            }
        }
    }

    public static void getOther() {
        System.out.println("-----------------------------------------");
        Admin admin = Admin.getInstance();
        Scanner sc = new Scanner(System.in);

        System.out.println("1. Calculate distance between 2 ports");
        System.out.println("2. List of all ports");
        System.out.println("3. List of all logs");
        System.out.println("4. List of all managers");
        System.out.println("~. Go back");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                System.out.print("Target 1 ID: ");
                String target1 = sc.nextLine();
                System.out.print("Target 2 ID: ");
                String target2 = sc.nextLine();

                System.out.println("Distance between " + target1 + " and " + target2 + " is: " +
                        admin.calculateDistanceBetweenPorts(target1, target2));
                getOther();
            }
            case "2" -> {
                Prettify.prettifyPortList(admin.getListOfAllPort());
                getOther();
            }
            case "3" -> {
                Prettify.prettifyLogList(admin.getListOfAllLogs());
                getOther();
            }
            case "4" -> {
                Prettify.prettifyManagerList(admin.getListOfAllManager());
                getOther();
            }
            case "~" -> statQuery();
            default -> {
                System.out.println("Invalid input. ");
                getOther();
            }
        }
    }
}
