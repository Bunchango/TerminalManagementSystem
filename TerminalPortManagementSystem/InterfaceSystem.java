package TerminalPortManagementSystem;

import TerminalPortManagementSystem.User.User;
import TerminalPortManagementSystem.Utility.TerminalUtil;

import java.util.Scanner;

public class InterfaceSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Menu");
            System.out.println("1: Login");
            System.out.println("2: Terminate the program");

            String option = sc.next();
            switch (option) {
                case "1":
                    while(true) {
                        System.out.println("Enter your username:");
                        String username = sc.next();

                        System.out.println("Enter your password:");
                        String password = sc.next();

                        User user = TerminalUtil.login(username,password);

                        if (user.isAdmin()) {
                            System.out.println("Welcome admin");
                        } else if (user.isManager()) {
                            System.out.println("Welcome Port Manager");
                        } else {
                            System.out.println("The username or password is incorrect. Please try again");
                            break;
                        }
                    }
//                    break;
            }
        }
    }

}
