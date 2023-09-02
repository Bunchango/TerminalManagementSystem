package TerminalPortManagementSystem;

import TerminalPortManagementSystem.Interface.InterfaceSystem;
import TerminalPortManagementSystem.User.*;
import TerminalPortManagementSystem.Utility.TerminalUtil;

public class Main {
    public static void main(String[] args) {
        InterfaceSystem.run();
//        Admin admin = Admin.getInstance();

        //create Object
//        String port1 = admin.createPort("1010","Hai Phong",-42,75,400,true);
//        String port2 = admin.createPort("1011","Quang Ninh",-41,75,350,false);
//        String manager2 = admin.createManager("manager-1010", "dongmanhduc", "p-1010");
//        String manager1 = admin.createManager("manager-1011","mike125","p-1011");
//        String vehicle1 = admin.createVehicle("1010","Ship","p-1010",50,150);
//        String vehicle2 = admin.createVehicle("1010","BasicTruck","p-1010",30,70);
//        String vehicle3 = admin.createVehicle("1011","TankerTruck","p-1010",30,70);
//
//        String container1 = admin.createContainer("1010","DryStorage","p-1010",12);
//        String container2 = admin.createContainer("1011","DryStorage","p-1010",22);
//        String container3 = admin.createContainer("1012","DryStorage","p-1010",14);
//        String container4 = admin.createContainer("1013","DryStorage","p-1010",16);
//
//        System.out.println(port1);
//        System.out.println(port2);
//        System.out.println(manager1);
//        System.out.println(manager2);
//        System.out.println(vehicle1);
//        System.out.println(vehicle2);
//        System.out.println(vehicle3);
//        System.out.println(container1);
//        System.out.println(container2);
//        System.out.println(container3);
//        System.out.println(container4);

//        Manager manager = (Manager) TerminalUtil.login("manager-1010","dongmanhduc");
//        Prettify.prettifyVehicleList(manager.getListOfAllVehicles());
//        System.out.println(admin.loadContainer("sh-1010","c-1010"));
//        System.out.println(admin.loadContainer("tr-1010","c-1011"));
//        System.out.println(admin.refuelVehicle("sh-1010",300));
//        System.out.println(admin.moveToPort("sh-1010","p-1011","02-09-2023 15:50:00","05-09-2023 15:50:00"));
//        System.out.println(admin.loadContainer("sh-1010","c-1012"));
//        System.out.println(admin.loadContainer("sh-1010","c-1013"));
//        System.out.println(admin.unloadContainer("sh-1010","c-1013"));
//        System.out.println(admin.removeVehicle("sh-1010"));

        // manager static query
//        System.out.println(manager.getListOfAllContainers());
//        Prettify.prettifyContainerList(TerminalUtil.searchVehicle("sh-1010").getVehicleContainers());
//        Prettify.prettifyContainerList(manager.getListOfAllContainers());
//        System.out.println(manager.createContainer("1015","OpenTop",13));
//        System.out.println(manager.removeContainer("c-1015"));
//        Prettify.prettifyLogList(manager.getTripsByArrivalDate("01-09-2023"));
//        System.out.println(manager.getTotalConsumedFuelByDate("01-09-2023"));
//
//        Prettify.prettifyGetTotalFuelConsumedPerDay(manager.getTotalFuelConsumedPerDay());
//        Prettify.prettifyGetTotalFuelConsumedPerDay(admin.totalFuelConsumedPerDay());
//
//        Prettify.prettifyGetTotalWeightOfEachType(manager.getTotalWeightOfEachType());
//        Prettify.prettifyGetTotalWeightOfEachType(admin.getTotalWeightOfEachType());
//
//        Prettify.prettifyGetNumberOfContainerOfEachType(manager.getNumberOfContainerOfEachType());
//        Prettify.prettifyGetNumberOfContainerOfEachType(admin.getNumberOfContainerOfEachType());
//
//        Prettify.prettifyGetNumberOfVehicleOfEachType(manager.getNumberOfVehicleOfEachType());
//        Prettify.prettifyGetNumberOfVehicleOfEachType(admin.getNumberOfVehicleOfEachType());
//        Prettify.prettifyVehicleList(manager.getListOfVehicleByType("TankerTruck"));
//        Prettify.prettifyLogList(manager.getTripsBetweenDepartureDates("01-09-2023", "06-09-2023"));
//        Prettify.prettifyLogList(manager.getTripsInDates("02-09-2023", "05-09-2023"));
//        Prettify.prettifyLogList(admin.getTripsInDates("02-09-2023", "03-09-2023"));

        // admin static
        //System.out.println(admin.setManagerPort("manager-1010","p-1010"));

//        System.out.println(admin.createContainer("1016","OpenTop","p-1011",13));
//        System.out.println(admin.removeContainer("c-1016"));
        //System.out.println(admin.createPort("1012","Da Nang",-43,75.6,400,true));
        //System.out.println(admin.removeManager("manager-1012"));
        //System.out.println(admin.createManager("manager-1012","myPassword","p-1012"));

//        Prettify.prettifyGetTotalFuelConsumedPerDay(admin.totalFuelConsumedPerDay());
//        Prettify.prettifyGetTotalFuelConsumedPerDay(admin.totalFuelConsumedPerDayOfPort("p-1011"));
//        System.out.println(admin.getTotalConsumedFuelByDate("01-09-2023"));
//        System.out.println(admin.getTotalConsumedFuelByDayByPort("p-1010","01-09-2023"));
//        Prettify.prettifyGetTotalWeightOfEachType(admin.getTotalWeightOfEachType());
//        Prettify.prettifyGetTotalWeightOfEachType(admin.getTotalWeightOfEachTypeByPort("p-1010"));
//        Prettify.prettifyGetNumberOfContainerOfEachType(admin.getNumberOfContainerOfEachType());
//        Prettify.prettifyGetNumberOfContainerOfEachType(admin.getNumberOfContainerOfEachTypeByPort("p-1011"));
//        Prettify.prettifyContainerList(admin.getListOfContainerByPort("p-1011"));
//        Prettify.prettifyVehicleList(admin.getListOfVehicleByType("TankerTruck"));
//        Prettify.prettifyVehicleList(admin.getListOFVehicleByTypeOfPort("p-1011","TankerTruck"));
//        Prettify.prettifyGetNumberOfVehicleOfEachType(admin.getNumberOfVehicleOfEachType());
//        Prettify.prettifyGetNumberOfVehicleOfEachType(admin.getNumberOfVehicleOfEachTypeByPort("p-10100"));
//        Prettify.prettifyLogList(admin.getTripsByDate("02-09-2023"));
//        Prettify.prettifyLogList(admin.getTripsByDateOfPort("01-09-2023","p-1010"));
//        Prettify.prettifyLogList(admin.getTripsBetweenDates("01-09-2023","02-09-2023"));
//        Prettify.prettifyLogList(admin.getTripsBetweenDatesOfPort("01-09-2023","02-09-2023","p-1011"));
//        System.out.println(admin.calculateDistanceBetweenPorts("p-1010", "p-1011"));



//        Prettify.prettifyManagerList(admin.getListOfAllManager());
//        Prettify.prettifyContainerList(admin.getListOfAllContainer());
//        Prettify.prettifyVehicleList(admin.getListOfAllVehicle());
//        Prettify.prettifyPortList(admin.getListOfAllPort());
//
//
//        // print the data
//        Prettify.prettifyAdmin();
//        Prettify.prettifyManagerList(TerminalUtil.managers);
//        Prettify.prettifyPortList(TerminalUtil.ports);
//        Prettify.prettifyVehicleList(TerminalUtil.vehicles);
//        Prettify.prettifyContainerList(TerminalUtil.containers);
//        Prettify.prettifyLogList(TerminalUtil.occurringLogs);
//        Prettify.prettifyLogList(TerminalUtil.occurredLogs);

    }
}