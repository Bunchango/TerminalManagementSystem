package TerminalPortManagementSystem;

import java.util.Arrays;
import java.util.List;

public enum ContainerType {
    // Initiate constants for each type of containers
    DryStorage(3.5, 4.6),
    OpenTop(2.8, 3.2),
    OpenSide(2.7, 3.2),
    Refrigerated(4.5, 5.4),
    Liquid(4.8, 5.3);

    private final double shipFuelCost;
    private final double truckFuelCost;

    ContainerType(double shipFuelCost, double truckFuelCost) {
        this.shipFuelCost = shipFuelCost;
        this.truckFuelCost = truckFuelCost;
    }

    public String getEmoji() {
        return "\uD83D\uDCE6";
    }

    public double getShipFuelCost() {
        return shipFuelCost; // Returns the ship fuel cost for each enum value
    }

    public double getTruckFuelCost() {
        return truckFuelCost; // Returns the truck fuel cost for each enum value
    }

    public static List<ContainerType> getAllContainerTypes() {
        return Arrays.asList(ContainerType.values());
    }

    public static ContainerType fromString(String input) {
        // Return ContainerType from String
        for (ContainerType containerType : ContainerType.values()) {
            if (containerType.name().equalsIgnoreCase(input)) {
                return containerType;
            }
        }
        return null;
    }
}