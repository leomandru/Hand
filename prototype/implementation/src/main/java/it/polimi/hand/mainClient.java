package it.polimi.hand;
import java.util.Scanner;
import java.io.IOException;

public class mainClient {

    public static void main(String[] args) throws NullPointerException, IOException {
        String check;
        int check1,type,Related_Booking_ID,report;
        String Report_Type,Description,TroubleType;
        Scanner in=new Scanner(System.in);
        ClientControl ClientControl= new ClientControl();
        while(true) {
            System.out.println("What do you want to do?\n" + "Please insert the number related to the operation\n" + "1) My report status\n" + "2) My booking\n"+"3) Exit");
            check1 = Integer.parseInt(in.nextLine());
            switch (check1){
                case 2:
                    System.out.println("Here are your bookings:");
                    ClientControl.getBookings();
                    System.out.println("Do you want to do a report?\n Insert Y=yes or N=no");
                    check = in.nextLine();
                    if (check.toUpperCase().equals("N")) return;
                    System.out.println("Please choose your Booking number:\n" + "Please insert only the number:");
                    Related_Booking_ID=Integer.parseInt(in.nextLine());
                    System.out.println("Please insert the kind of report:\n" +"Please insert only the number\n" +
                            "1) The vehicle did not pass\n"+"2) The place reserved for me is occupied\n"+
                            "3) Missing or broken boarding board\n"+"4) The driver didn't help me get on");
                    type= Integer.parseInt(in.nextLine());
                    switch (type){
                        case 1:Report_Type="The vehicle did not pass";break;

                        case 2:Report_Type="The place reserved for me is occupied";break;

                        case 3:Report_Type="Missing or broken boarding board";break;

                        case 4:Report_Type="The driver didn't help me get on";break;

                        default:Report_Type="I'm spammer"; break;
                    }
                    System.out.println("We would be grateful if you could provide us with a small description of what happened");
                    Description=in.nextLine();
                    System.out.println("Where do you find same trouble?\n" + "Please insert only the number\n" + "0)Entry Stop\n" + "1)Exit Stop");
                    TroubleType= in.nextLine();
                    ClientControl.insertNewReport(Report_Type,Description,Related_Booking_ID,TroubleType);
                    System.out.println("Thank you very much.\nYou will soon receive feedback in the section: My report status");
                    break;
                case 1:
                    System.out.println("Hi here are your report status:");
                    ClientControl.getAllReports();
                    System.out.println("If you want more information about a certain report enter the number:\n" + "Please insert only the number");
                    report= Integer.parseInt(in.nextLine());
                    ClientControl.particularReport(report);
                    break;
                default:
                    System.out.println("Thanks see you soon");
                    return;
            }
        }
    }
}
