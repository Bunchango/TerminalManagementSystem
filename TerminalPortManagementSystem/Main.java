package TerminalPortManagementSystem;

import TerminalPortManagementSystem.Ports.*;
import TerminalPortManagementSystem.Utility.*;
import TerminalPortManagementSystem.Utility.TerminalUtil;
import TerminalPortManagementSystem.Vehicles.*;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // TODO: When moving vehicle, deleting objects, creating objects, loading and unloading containers, must update files

        // Init function, always above all code
        TerminalUtil.updateLogWhenFinished();


        //Create object
//        new Port("1012", "Quang Ninh", 42, -73.5, 300, false);
//        new Ship("1011",TerminalUtil.searchPort("p-1012"),100,150);
//        new Ship("1015",TerminalUtil.searchPort("p-1012"),100,150);
//
//        new Port("1013", "Khanh Hoa", 42, -73.3, 250, true);
//        new Ship("1012",TerminalUtil.searchPort("p-1013"),160,200);
//        new BasicTruck("1013",TerminalUtil.searchPort("p-1013"),50,100);
//
//        new Port("1014", "Ca Mau", 32, -73.5, 250, true);
//        new ReeferTruck("1014",TerminalUtil.searchPort("p-1014"),50,100);
//        new TankerTruck("1016",TerminalUtil.searchPort("p-1014"),50,100);
//
//        new Container("1011", ContainerType.DryStorage, TerminalUtil.searchPort("p-1012"), 10.5);
//        new Container("1014", ContainerType.OpenTop, TerminalUtil.searchPort("p-1013"), 10.5);
//        new Container("1111", ContainerType.Liquid, TerminalUtil.searchPort("p-1012"), 15.5);
//        new Container("1012", ContainerType.OpenSide,TerminalUtil.searchPort("p-1012"),12.0);
//        new Container("1013", ContainerType.Refrigerated,TerminalUtil.searchPort("p-1012"),20.0);

        //--------------------------------------
        // all of the object
        Prettify.prettifyVehicleList(TerminalUtil.vehicles);
        Prettify.prettifyContainerList(TerminalUtil.containers);
        Prettify.prettifyPortList(TerminalUtil.ports);
        Prettify.prettifyLogList(TerminalUtil.occurredLogs);
        Prettify.prettifyLogList(TerminalUtil.occurringLogs);

        //2 ship but 4 container
        System.out.println("Container and vehicle in p-1012");
        Prettify.prettifyVehicleList(TerminalUtil.searchPort("p-1012").getPortVehicles());
        Prettify.prettifyContainerList(TerminalUtil.searchPort("p-1012").getPortContainers());
        System.out.println("container in ship sh-1011");
        Prettify.prettifyContainerList(TerminalUtil.searchVehicle("sh-1011").getVehicleContainers());
        System.out.println("container in ship sh-1015");
        Prettify.prettifyContainerList(TerminalUtil.searchVehicle("sh-1015").getVehicleContainers());


        System.out.println("Container and vehicle in p-1013");
        Prettify.prettifyVehicleList(TerminalUtil.searchPort("p-1013").getPortVehicles());
        Prettify.prettifyContainerList(TerminalUtil.searchPort("p-1013").getPortContainers());
        System.out.println("container in ship sh-1012");
        Prettify.prettifyContainerList(TerminalUtil.searchVehicle("sh-1012").getVehicleContainers());
        System.out.println("container in truck basic tr-1013");
        Prettify.prettifyContainerList(TerminalUtil.searchVehicle("tr-1013").getVehicleContainers());


        System.out.println("Container and vehicle in p-1014");
        Prettify.prettifyVehicleList(TerminalUtil.searchPort("p-1014").getPortVehicles());
        Prettify.prettifyContainerList(TerminalUtil.searchPort("p-1014").getPortContainers());
        System.out.println("container in ship tr-1014");
        Prettify.prettifyContainerList(TerminalUtil.searchVehicle("tr-1014").getVehicleContainers());
        System.out.println("container in truck basic tr-1016");
        Prettify.prettifyContainerList(TerminalUtil.searchVehicle("tr-1016").getVehicleContainers());



        //TerminalUtil.searchVehicle("sh-1011").loadContainer(TerminalUtil.searchContainer("c-1011"));
        //TerminalUtil.searchVehicle("sh-1011").refuel(200);
        //TerminalUtil.searchVehicle("sh-1015").loadContainer(TerminalUtil.searchContainer("c-1013"));
        //TerminalUtil.searchVehicle("sh-1015").refuel(200);

        System.out.println(TerminalUtil.searchVehicle("sh-1011").moveToPort(TerminalUtil.searchPort("p-1013"),TerminalUtil.getNow(),TerminalUtil.parseStringToDateTime("26-08-2023 16:00:00")));
        //depart date later
        System.out.println(TerminalUtil.searchVehicle("sh-1015").moveToPort(TerminalUtil.searchPort("p-1014"),TerminalUtil.parseStringToDateTime("26-08-2023 15:00:00"),TerminalUtil.parseStringToDateTime("26-08-2023 16:00:00")));
        //TerminalUtil.searchVehicle("sh-1015").loadContainer(TerminalUtil.searchContainer("c-1012"));
        //TerminalUtil.searchVehicle("sh-1015").unloadContainer(TerminalUtil.searchContainer("c-1012"));

        // Check for occurring logs every 1 minutes
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(TerminalUtil::updateLogWhenFinished, 0, 1, TimeUnit.MINUTES);
    }
}