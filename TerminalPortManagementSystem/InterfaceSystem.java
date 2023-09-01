package TerminalPortManagementSystem;
import TerminalPortManagementSystem.Utility.TerminalUtil;

import java.util.Scanner;
import java.sql.SQLOutput;

public class InterfaceSystem {
    public static void main(String[] args) {
        System.out.println("Menu");
        System.out.println("1:Login");
        System.out.println("2:Terminate the program");
        System.out.println("Enter your choice: ");
        Scanner menu1 = new Scanner(System.in);
        while (true) {
            int option = Integer.parseInt(menu1.next());
            switch (option) {
                case 1:
                    System.out.println("Please enter your username:");
                    String username = menu1.next();

                    System.out.println("Please enter your password:");
                    String password = menu1.next();

                    if (TerminalUtil.login(username,password).isAdmin()) {
                        System.out.println("You are Admin");
                        System.out.println("Admin's menu");

                        System.out.println("?????");
                        System.out.println("*Remove or Create Objects*");
                        System.out.println("1: Create Manager ");
                        System.out.println("2: Create Port ");
                        System.out.println("3: Create Vehicle");
                        System.out.println("4: Create Container ");
                        System.out.println("5: Remove Manager");
                        System.out.println("6: Remove Port");
                        System.out.println("7: Remove Vehicle");
                        System.out.println("8: Remove Container");

                        System.out.println("?????");
                        System.out.println("*Port's Manager Options*");
                        System.out.println("9: Set Port's Manager");
                        System.out.println("10: Unset Port's Manager");

                        System.out.println("?????");
                        System.out.println("*Transporting Options*");
                        System.out.println("11: Load Container");
                        System.out.println("12: Unload Container");
                        System.out.println("13: Refuel Vehicle");
                        System.out.println("14: Move to Port");
                        System.out.println("Choose your action: ");
                    }

            }
        }
    }
}
