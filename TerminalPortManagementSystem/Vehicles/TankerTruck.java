package TerminalPortManagementSystem.Vehicles;

import TerminalPortManagementSystem.Ports.Port;
import TerminalPortManagementSystem.VehicleType;
import TerminalPortManagementSystem.Vehicles.Vehicle;

public class TankerTruck extends Vehicle {
    public TankerTruck(String vehicleID, Port currentPort, double carryingCapacity, double fuelCapacity) {
        super("tr-" + vehicleID, VehicleType.TankerTruck, currentPort, carryingCapacity, fuelCapacity);
    }

    @Override
    public boolean ableToMoveToPort(Port destinationPort) {
        // Condition similar as in Ship but the Port also needs to have landing = true
        return getCurrentFuel() >= calculateFuelConsumption(destinationPort) && destinationPort.getLandingAbility() &&
                destinationPort.getTotalCarryingWeight() + getTotalCarryingWeight() <= destinationPort.getStoringCapacity();
    }
}
