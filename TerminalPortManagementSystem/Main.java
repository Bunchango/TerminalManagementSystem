package TerminalPortManagementSystem;

import TerminalPortManagementSystem.User.*;
import TerminalPortManagementSystem.Utility.TerminalUtil;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // TODO: When moving vehicle, deleting objects, creating objects, loading and unloading containers, must update files

        // Init function, always above all code
        TerminalUtil.updateLogWhenFinished();

//        new Port("1010", "Hai Phong", 50, 50, 50, true);
//        new Manager("manager-1010", "dongmanhduc", "p-1010");

        User user = TerminalUtil.login("admin", "admin12345");

        // Separate using this
        if (user != null && user.isManager()) {
            Manager manager = (Manager) user;
            System.out.println(manager.getManagePortID());
        } else if (user != null && user.isAdmin()) {
            Admin admin = (Admin) user;
            System.out.println(admin);
        }

        // Check for occurring logs every 1 minutes
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(TerminalUtil::updateLogWhenFinished, 0, 1, TimeUnit.MINUTES);
    }
}