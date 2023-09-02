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
        System.out.println("-----------------------------------------");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                System.out.print("Enter username: ");
                String username = sc.nextLine().replace(" ", "");
                System.out.print("Enter password: ");
                String password = sc.nextLine().replace(" ", "");
                User user = TerminalUtil.login(username, password);

                if (user == null) {
                    System.out.println("User not found");
                    System.out.println("-----------------------------------------");
                    login();
                }
                if (user != null && user.isAdmin()) {
                    AdminInterface.run();
                }
                if (user != null && user.isManager()) {
                    Manager manager = (Manager) user;
                    ManagerInterface.run(manager);
                }
            }
            case "2" -> {
                System.out.println("Exited");
            }
            default -> {
                System.out.println("Invalid input. Please enter either '1' or '2' ");
                System.out.println("-----------------------------------------");
                login();
            }
        }
    }
}
