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
    public abstract boolean doesAllowContainerType(Container targetContainer);
    public abstract List<ContainerType> getAllowedContainerType();
    public static List<VehicleType> getAllVehicleTypes() {
        return Arrays.asList(VehicleType.values());
    }
    public static VehicleType fromString(String input) {
        for (VehicleType vehicleType : VehicleType.values()) {
            if (vehicleType.name().equalsIgnoreCase(input)) {
                return vehicleType;
            }
        }
        throw new IllegalArgumentException("Unknown vehicle type: " + input);
    }
}
