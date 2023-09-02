package TerminalPortManagementSystem.Interface;
import TerminalPortManagementSystem.Utility.TerminalUtil;

import java.io.IOException;
import java.util.Scanner;

public class AdminInterface {
    public static void run() {
        /* 4 section
        1. Announcements
        2. Create and Remove
        3. Transportation
        4. Stat Query
        */
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("1. Announcement [" + TerminalUtil.announcements.size() + "]");
        System.out.println("2. Create and Remove");
        System.out.println("3. Transportation");
        System.out.println("4. Statistics and Query");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        switch (option) {
            case "1" -> {
                System.out.println("-----------------------------------------");
                announcement();
            }

            case "2" -> {
                System.out.println("-----------------------------------------");
                createRemove();
            }

            case "3" -> {
                System.out.println("-----------------------------------------");
                transportation();
            }

            case "4" -> {
                System.out.println("-----------------------------------------");
                statQuery();
            }
        }
    }

    public static void announcement() {
        for (String announcement: TerminalUtil.announcements) {
            System.out.println(announcement);
        }
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Clear announcements? true or false: ");
            try {
                boolean clear = sc.nextBoolean();
                if (clear) {
                    TerminalUtil.clearAnnouncement();
                }
                break; // Exit the loop if input is valid

            } catch (Exception e) {
                System.out.println("Invalid input. Please enter either 'true' or 'false'.");
                sc.nextLine(); // Clear the input buffer
            }
        }

        while (true) {
            System.out.print("Return? true / false: ");
            try {
                boolean back = sc.nextBoolean();
                if (back) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter either 'true' or 'false'.");
                sc.nextLine();
            }
        }
        run();
    }

    public static void createRemove() {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Announcement [" + TerminalUtil.announcements.size() + "]");
        System.out.println("2. Create and Remove");
        System.out.println("3. Transportation");
        System.out.println("4. Statistics and Query");
        System.out.print("Enter your choice: ");
        String option = sc.nextLine();

        run();
    }

    public static void transportation() {
        run();
    }

    public static void statQuery() {
        run();
    }
}
