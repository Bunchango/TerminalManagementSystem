package TerminalPortManagementSystem;

import TerminalPortManagementSystem.User.*;
import TerminalPortManagementSystem.Utility.Prettify;
import TerminalPortManagementSystem.Utility.TerminalUtil;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // Init function, always above all code
        TerminalUtil.updateLogWhenFinished();

//        new Port("1010", "Hai Phong", 50, 50, 50, true);
//        new Manager("manager-1010", "dongmanhduc", "p-1010");

        //User user = TerminalUtil.login("admin", "admin12345");

        // Separate using this
//        if (user != null && user.isManager()) {
//            Manager manager = (Manager) user;
//            Prettify.prettifyManager(manager);
//        } else if (user != null && user.isAdmin()) {
//            Admin admin = (Admin) user;
//            Prettify.prettifyAdmin();
//        }
        Admin admin = Admin.getInstance();

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
        Manager manager  = (Manager) TerminalUtil.login("manager-1010","dongmanhduc");
//
//        System.out.println(admin.loadContainer("sh-1010","c-1010"));
//        System.out.println(admin.loadContainer("tr-1010","c-1011"));
//        System.out.println(admin.refuelVehicle("sh-1010",300));
//        System.out.println(admin.moveToPort("sh-1010","p-1011","31-08-2023 17:50:00","31-08-2023 18:15:00"));
//        System.out.println(admin.loadContainer("sh-1010","c-1012"));
//        System.out.println(admin.loadContainer("sh-1010","c-1013"));
//
//        // manager static query
        System.out.println(manager.getListOfAllContainers());
        //Prettify.prettifyContainerList(TerminalUtil.searchVehicle("sh-1010").getVehicleContainers());
        Prettify.prettifyContainerList(manager.getListOfAllContainers());
        System.out.println(manager.createContainer("1014","OpenTop",13));
        System.out.println(manager.removeContainer("c-1014"));

        Prettify.prettifyContainerList(admin.getListOfAllContainer());
        Prettify.prettifyVehicleList(admin.getListOfAllVehicle());



        // print the data
        Prettify.prettifyAdmin();
        Prettify.prettifyManagerList(TerminalUtil.managers);
        Prettify.prettifyPortList(TerminalUtil.ports);
        Prettify.prettifyVehicleList(TerminalUtil.vehicles);
        Prettify.prettifyContainerList(TerminalUtil.containers);
        Prettify.prettifyLogList(TerminalUtil.occurringLogs);
        Prettify.prettifyLogList(TerminalUtil.occurredLogs);

        //em chau

        // Check for occurring logs every 1 minutes
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(TerminalUtil::updateLogWhenFinished, 0, 1, TimeUnit.MINUTES);
    }
}