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

        //create Object
        String port1 = Admin.getInstance().createPort("1000","Hai Phong",-42,75,400,true);
        String port2 = Admin.getInstance().createPort("1011","Quang Ninh",-41,75,350,false);
        String admin1 = Admin.getInstance().createManager("manager-1011","mike125","p-1011");
        String vehicle1 = Admin.getInstance().createVehicle("1010","Ship","p-1010",50,150);
        System.out.println(port1);
        System.out.println(port2);
        System.out.println(admin1);


        // print the data
        Prettify.prettifyAdmin();
        Prettify.prettifyManagerList(TerminalUtil.managers);
        Prettify.prettifyPortList(TerminalUtil.ports);
        Prettify.prettifyVehicleList(TerminalUtil.vehicles);
        Prettify.prettifyContainerList(TerminalUtil.containers);
        Prettify.prettifyLogList(TerminalUtil.occurringLogs);
        Prettify.prettifyLogList(TerminalUtil.occurredLogs);



        // Check for occurring logs every 1 minutes
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(TerminalUtil::updateLogWhenFinished, 0, 1, TimeUnit.MINUTES);
    }
}