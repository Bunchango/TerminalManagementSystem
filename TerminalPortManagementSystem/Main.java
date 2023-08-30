package TerminalPortManagementSystem;

import TerminalPortManagementSystem.Utility.TerminalUtil;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // TODO: When moving vehicle, deleting objects, creating objects, loading and unloading containers, must update files

        // Init function, always above all code
        TerminalUtil.updateLogWhenFinished();



        // Check for occurring logs every 1 minutes
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(TerminalUtil::updateLogWhenFinished, 0, 1, TimeUnit.MINUTES);
    }
}