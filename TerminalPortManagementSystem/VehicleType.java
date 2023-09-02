package TerminalPortManagementSystem;

import TerminalPortManagementSystem.Ports.Container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum VehicleType {
    Ship {
        @Override
        public String getEmoji() {
            return "\uD83D\uDEA2";
        }

        @Override
        public boolean isShip() {
            return true;
        }

        @Override
        public boolean isTruck() {
            return false;
        }

        @Override
        public boolean isBasicTruck() {
            return false;
        }

        @Override
        public boolean isTankerTruck() {
            return false;
        }

        @Override
        public boolean isReeferTruck() {
            return false;
        }

        @Override
        public boolean doesAllowContainerType(Container targetContainer) {
            return true;
        }

        @Override
        public List<ContainerType> getAllowedContainerType() {
            List<ContainerType> containerTypes = new ArrayList<>();
            containerTypes.add(ContainerType.DryStorage);
            containerTypes.add(ContainerType.OpenTop);
            containerTypes.add(ContainerType.OpenSide);
            containerTypes.add(ContainerType.Refrigerated);
            containerTypes.add(ContainerType.Liquid);
            return containerTypes;
        }
    },

    BasicTruck {
        @Override
        public String getEmoji() {
            return "\uD83D\uDE9A";
        }

        @Override
        public boolean isShip() {
            return false;
        }

        @Override
        public boolean isTruck() {
            return true;
        }

        @Override
        public boolean isBasicTruck() {
            return true;
        }

        @Override
        public boolean isTankerTruck() {
            return false;
        }

        @Override
        public boolean isReeferTruck() {
            return false;
        }

        @Override
        public boolean doesAllowContainerType(Container targetContainer) {
            return targetContainer.getContainerType() == ContainerType.DryStorage ||
                    targetContainer.getContainerType() == ContainerType.OpenSide ||
                    targetContainer.getContainerType() == ContainerType.OpenTop;
        }

        @Override
        public List<ContainerType> getAllowedContainerType() {
            List<ContainerType> containerTypes = new ArrayList<>();
            containerTypes.add(ContainerType.DryStorage);
            containerTypes.add(ContainerType.OpenTop);
            containerTypes.add(ContainerType.OpenSide);
            return containerTypes;
        }
    },

    TankerTruck {
        @Override
        public String getEmoji() {
            return "\uD83D\uDE9A";
        }

        @Override
        public boolean isShip() {
            return false;
        }

        @Override
        public boolean isTruck() {
            return true;
        }

        @Override
        public boolean isBasicTruck() {
            return false;
        }

        @Override
        public boolean isTankerTruck() {
            return true;
        }

        @Override
        public boolean isReeferTruck() {
            return false;
        }

        @Override
        public boolean doesAllowContainerType(Container targetContainer) {
            return targetContainer.getContainerType() == ContainerType.Liquid;
        }

        @Override
        public List<ContainerType> getAllowedContainerType() {
            List<ContainerType> containerTypes = new ArrayList<>();
            containerTypes.add(ContainerType.Liquid);
            return containerTypes;
        }
    },

    ReeferTruck {
        @Override
        public String getEmoji() {
            return "\uD83D\uDE9A";
        }
        @Override
        public boolean isShip() {
            return false;
        }

        @Override
        public boolean isTruck() {
            return true;
        }

        @Override
        public boolean isBasicTruck() {
            return false;
        }

        @Override
        public boolean isTankerTruck() {
            return false;
        }

        @Override
        public boolean isReeferTruck() {
            return true;
        }

        @Override
        public boolean doesAllowContainerType(Container targetContainer) {
            return targetContainer.getContainerType() == ContainerType.Refrigerated;
        }

        @Override
        public List<ContainerType> getAllowedContainerType() {
            List<ContainerType> containerTypes = new ArrayList<>();
            containerTypes.add(ContainerType.Refrigerated);
            return containerTypes;
        }
    };

    public abstract String getEmoji();
    public abstract boolean isShip();
    public abstract boolean isTruck();
    public abstract boolean isBasicTruck();
    public abstract boolean isTankerTruck();
    public abstract boolean isReeferTruck();
    public abstract boolean doesAllowContainerType(Container targetContainer); // Determine if the vehicle allow the type of target container
    public abstract List<ContainerType> getAllowedContainerType(); // Get a list of containers type that can be loaded onto the vehicle
    public static List<VehicleType> getAllVehicleTypes() {
        return Arrays.asList(VehicleType.values());
    } // Get all type of vehicles
    public static VehicleType fromString(String input) {
        // Return VehicleType of input String
        for (VehicleType vehicleType : VehicleType.values()) {
            if (vehicleType.name().equalsIgnoreCase(input)) {
                return vehicleType;
            }
        }
        return null;
    }
}
