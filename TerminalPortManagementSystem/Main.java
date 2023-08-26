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

        /*
        Port haiphong = new Port("1010", "Hai Phong", 75, 75, 50, true);
        Port danang = new Port("1110", "Da Nang", 75, 75.5, 50, true);
        Ship ship = new Ship("1010", danang, 50, 50);
        Container container = new Container("1010", ContainerType.DryStorage, danang, 15);
        ship.loadContainer(container);
        ship.refuel(50);

        Date departDate = TerminalUtil.getToday();
        Date arrivalDate = TerminalUtil.parseStringToDateTime("24-8-2023 18:00:00"); // Should return an error
        System.out.println(ship.moveToPort(haiphong, departDate, arrivalDate));

        */

        Prettify.prettifyVehicleList(TerminalUtil.vehicles);
        Vehicle vehicle = TerminalUtil.searchVehicle("sh-1010");
        Port port1 = TerminalUtil.searchPort("p-1010");
        Port port2 = TerminalUtil.searchPort("p-1110");
        Prettify.prettifyVehicleList(TerminalUtil.vehicles);
        Prettify.prettifyPortList(TerminalUtil.ports);

        /*
        Date departDate = TerminalUtil.getToday();
        Date arrivalDate = TerminalUtil.parseStringToDateTime("24-8-2023 22:00:00"); // Should not return any error. However, if the vehicle is already moving it return error
        if (vehicle != null && port1 != null) {
            System.out.println(vehicle.moveToPort(port1, departDate, arrivalDate));
        }
        */


        // Check for occurring logs every 1 minutes
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(TerminalUtil::updateLogWhenFinished, 0, 1, TimeUnit.MINUTES);

        Prettify.prettifyLogList(TerminalUtil.occurredLogs);
        Prettify.prettifyLogList(TerminalUtil.occurringLogs);
       //hello em
        //new changing for branch
    }
}