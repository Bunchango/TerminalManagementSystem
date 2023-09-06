package TerminalPortManagementSystem.Interface;

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
        System.exit(0);
    }

    private static void login() {
        Scanner sc = new Scanner(System.in);

        // Welcome screen
        System.out.println("COSC2081 GROUP ASSIGNMENT");
        System.out.println("CONTAINER PORT MANAGEMENT SYSTEM");
        System.out.println("Instructor: Mr. Minh Vu & Dr. Phong Ngo");
        System.out.println("Group: Team 4");
        System.out.println("s3978290, Dong Manh Duc");
        System.out.println("s3977747, Le Nguyen My Chau");
        System.out.println("s3927777, Do Thuy Linh");
        System.out.println("s3978506, Nguyen Ba Duc Manh");
        System.out.println();
        System.out.println("-----------------------------------------");

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
                System.out.println("EXITED");
                System.exit(0);
            }
            default -> {
                System.out.println("Invalid input. Please enter either '1' or '2' ");
                System.out.println("-----------------------------------------");
                login();
            }
        }
    }
}
