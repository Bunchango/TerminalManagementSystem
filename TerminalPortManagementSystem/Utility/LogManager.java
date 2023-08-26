package TerminalPortManagementSystem.Utility;
import TerminalPortManagementSystem.Ports.*;
import TerminalPortManagementSystem.Vehicles.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogManager {
    public static void saveAllObjects() {
        saveVehicles();
        savePorts();
        saveOccurredLog();
        saveOccurringLog();
    }

    protected static void saveVehicles() {
        // Save only vehicles that are moving
        String filePath = "Data/Objects/vehicles.obj";
        List<Vehicle> vehiclesCurrentlyMoving = TerminalUtil.vehicles.stream()
                .filter(vehicle -> vehicle.getCurrentPort() == null).toList();

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(vehiclesCurrentlyMoving);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void savePorts() {
        // At its core, this is saving both ports and vehicles that are not moving
        String filePath = "Data/Objects/ports.obj";
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(TerminalUtil.ports);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Since containers can only exist in ports or vehicles, there is no need to save them because they will automatically be loaded along the port and vehicles

    @SuppressWarnings("unchecked")
    protected static List<Vehicle> loadVehicles() {
        // Load both moving and not moving vehicles, not moving from the port list, moving from the vehicles.obj
        List<Vehicle> loadedVehicles = new ArrayList<>();
        String filePath = "Data/Objects/vehicles.obj";
        File file = new File(filePath);

        // Read from file
        if (file.length() > 0) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {

                loadedVehicles = (List<Vehicle>) inputStream.readObject();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File empty");
        }

        // Create a new list to combine vehicles
        List<Vehicle> combinedVehicles = new ArrayList<>(loadedVehicles);

        // Add vehicles that are not moving
        for (Port port : TerminalUtil.ports) {
            combinedVehicles.addAll(port.getPortVehicles());
        }
        return combinedVehicles;
    }

    @SuppressWarnings("unchecked")
    protected static List<Port> loadPorts() {
        List<Port> loadedPorts = new ArrayList<>();
        String filePath = "Data/Objects/ports.obj";
        File file = new File(filePath);

        if (file.length() > 0) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {

                loadedPorts = (List<Port>) inputStream.readObject();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File empty");
        }

        return loadedPorts;
    }

    protected static List<Container> loadContainers() {
        List<Container> loadedContainers = new ArrayList<>();

        for (Vehicle vehicle: TerminalUtil.vehicles) {
            loadedContainers.addAll(vehicle.getVehicleContainers());
        }

        for (Port port: TerminalUtil.ports) {
            loadedContainers.addAll(port.getPortContainers());
        }

        return loadedContainers;
    }

    // Save logs
    protected static void saveOccurredLog() {
        String filePath = "Data/History/occurred.obj";
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(TerminalUtil.occurredLogs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void saveOccurringLog() {
        String filePath = "Data/History/occurring.obj";
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(TerminalUtil.occurringLogs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load logs
    @SuppressWarnings("unchecked")
    protected static List<Log> loadOccurredLog(String filePath) {
        List<Log> loadedLog = new ArrayList<>();
        File file = new File(filePath);

        if (file.length() > 0) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {

                loadedLog = (List<Log>) inputStream.readObject();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File empty");
        }

        return loadedLog;
    }

    // Save ids
    public static void saveUsedIds() {
        String filePath = "Data/used_ids.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String id: TerminalUtil.usedIds) {
                writer.write(id);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load ids
    protected static List<String> loadUsedIds() {
        List<String> loadedIds = new ArrayList<>();
        String filePath = "Data/used_ids.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                loadedIds.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedIds;
    }
}
