package TerminalPortManagementSystem.Vehicles;

import TerminalPortManagementSystem.Ports.Port;
import TerminalPortManagementSystem.VehicleType;
import TerminalPortManagementSystem.Vehicles.Vehicle;

public class Ship extends Vehicle {
    public Ship(String vehicleID, Port currentPort, double carryingCapacity, double fuelCapacity) {
        super("sh-" + vehicleID, VehicleType.Ship, currentPort, carryingCapacity, fuelCapacity);
    }

    @Override
    public boolean ableToMoveToPort(Port destinationPort) {
        // A ship can always move to a port as long as its fuel is enough and its container weight does not exceed port capacity
        return getCurrentFuel() >= calculateFuelConsumption(destinationPort) && destinationPort.getTotalCarryingWeight() + getTotalCarryingWeight() <= destinationPort.getStoringCapacity();
    }
}
