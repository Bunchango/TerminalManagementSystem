package TerminalPortManagementSystem.Interface;

import TerminalPortManagementSystem.User.Admin;
import TerminalPortManagementSystem.User.Manager;
import TerminalPortManagementSystem.User.User;
import TerminalPortManagementSystem.Utility.TerminalUtil;

import java.util.Scanner;
import java.util.concurrent.ScheduledFuture;

public class InterfaceSystem {
    public static void run() {
        TerminalUtil.updateLogWhenFinished();
        ScheduledFuture<?> future = TerminalUtil.startScheduledTask();

        login();

        TerminalUtil.stopScheduledTask(future);
    }

    private static void login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Menu");
        System.out.println("1: Login");
        System.out.println("2: Terminate program");

        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                System.out.println("Enter username");
                String username = sc.nextLine();
                System.out.println("Enter password");
                String password = sc.nextLine();
                User user = TerminalUtil.login(username, password);

                if (user == null) {
                    System.out.println("User not found");
                    login();
                }
                if (user != null && user.isAdmin()) {
                    AdminInterface.run();
                }
                if (user != null && user.isManager()) {
                    ManagerInterface.run();
                }
            }
            case "2" -> {
                System.out.println("Exit");
                return;
            }
        }
    }
}
