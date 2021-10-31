package it.polimi.hand;

import java.util.List;
import java.util.Scanner;

public class mainDriver {

    public static void main(String[] args) {
        DriverController driverController = new DriverController();
        System.out.println();

        Scanner in = new Scanner(System.in);

        boolean exit = false;

        while(!exit){
            System.out.println("Hi there, what do you want to do now?\nSelect the number to choose an action");
            System.out.println("1 - Show the unconfirmed reports");
            System.out.println("2 - Exit");

            String value = in.nextLine();
            try {
                int n = Integer.parseInt(value);

                if(n == 1){
                    showUnconfirmedReports(driverController, in);
                }else if(n ==2)
                    break;
                else
                    exit = true;
            }catch(NumberFormatException e){
                System.out.println("Put only a number, not somewhat else");
                System.out.println("\n");
            }
        }
    }

    private static void showUnconfirmedReports(DriverController driverController, Scanner in) {
        boolean moreThanOne = driverController.getAllReports();
        if(moreThanOne) {
            System.out.println("\nType with a \"+\" before the number of report you want to see the details");
            System.out.println("Otherwise, if you want to confirm or reject a report, type only the corresponding network");
            boolean exit = false;
            while (!exit) {
                String value = in.nextLine();
                try {
                    if (value.contains("+")) {
                        int n = Integer.parseInt(value.substring(1));
                        if (!driverController.showMoreDetails(n))
                            System.out.println("Wrong number! Select a showed number before");
                        else {
                            confirmSelectedReport(driverController, in, n);
                            exit = true;
                        }
                    } else {
                        int n = Integer.parseInt(value);
                        if (driverController.numberCorrectReport(n - 1)) {
                            confirmSelectedReport(driverController, in, n);
                            exit = true;
                        } else
                            System.out.println("Wrong number! Select a showed number before");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Put only a number, not somewhat else");
                    System.out.println("\n");
                }
            }
        }
    }

    private static void confirmSelectedReport(DriverController driverController, Scanner in, int n){
        System.out.println("Do you want to confirm it?");
        System.out.println("1 - Reject it");
        System.out.println("2 - Confirm it");
        System.out.println("3 - Go back to other reports");
        boolean exit = false;
        while(!exit) {
            String value = in.nextLine();
            try {
                int choice = Integer.parseInt(value);
                if (choice == 1 || choice == 2) {
                    System.out.println("Insert a comment: ");
                    String comment = in.nextLine();
                    driverController.verifyReport(choice, n, comment);
                    exit = true;
                }else if(choice == 3)
                    exit = true;
            } catch (NumberFormatException e) {
                System.out.println("Put only a number, not somewhat else");
                System.out.println("\n");
            }
        }

    }
}
