package it.polimi.hand;

import it.polimi.hand.BookingInfoUser;
import it.polimi.hand.ReadableReportDriver;
import it.polimi.hand.ReadableReportUser;
import it.polimi.hand.ReportUser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientControl {
    String userId = "60b34e03688c7523647a3a9d";//eventualmente da modificare
    List<String> reportIds = new ArrayList<>();
    List<BookingInfoUser> responseB= new ArrayList<>();
    List<ReadableReportUser> responseR=new ArrayList<>();
    WebClient client = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultCookie("cookieKey", "cookieValue")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
            .build();

    public void getAllReports(){
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.GET);
        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/users/" + userId + "/reports/receive");
        responseR = bodySpec.retrieve().bodyToFlux(ReadableReportUser.class).collectList().block();
        int j=0;
        for (ReadableReportUser Info:responseR) {
            System.out.println("");
            System.out.println("This is your report number: "+j);
            System.out.println("ReportType: "+Info.getReportType());

            switch (Info.getVerificationStatus()){
                case 0:
                    System.out.println("VerificationStatus: WAITING");break;
                case 1:
                    System.out.println("VerificationStatus: REJECT");break;
                case 2:
                    System.out.println("VerificationStatus: CONFIRMED");break;
            }
            System.out.println("DateReport: "+Info.getDateReport());
            System.out.println("BookedLine: "+Info.getBookedLine());
            j++;
        }
    }

    public void insertNewReport(String reportType,String description,int relatedBookingID,String exitStop){
        //WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.POST);
        ReportUser reportUser = new ReportUser(reportType, description,
                responseB.get(relatedBookingID).getBookingID(), exitStop);

        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.post();
        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/users/" + userId + "/reports/insert");
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(reportUser);
        List<ReadableReportUser> response = bodySpec.retrieve().bodyToFlux(ReadableReportUser.class).collectList().block();

    }

    public void getBookings(){
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.GET);
        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/users/" + userId + "/bookings");
        responseB = bodySpec.retrieve().bodyToFlux(BookingInfoUser.class).collectList().block();
        int i=0;
        for (BookingInfoUser Info:responseB) {
            System.out.println("");
            System.out.println("This is your booking number:  "+i);
            System.out.println("EntryStop: "+Info.getEntryStop());
            System.out.println("ExitStop: "+Info.getExitStop());
            System.out.println("VehicleType: "+Info.getVehicleType());
            System.out.println("Number: "+Info.getBookedLine());
            System.out.println("DateBooking: "+Info.getDateBooking());
            System.out.println("TimeBooked: "+Info.getTimeBooked());
            i++;
        }
    }

    public void particularReport(int report){
        System.out.println("Report Type: "+responseR.get(report).getReportType());
        System.out.println("Date of Report: "+responseR.get(report).getDateReport());
        switch (responseR.get(report).getVerificationStatus()){
            case 0:
                System.out.println("VerificationStatus: WAITING");break;
            case 1:
                System.out.println("VerificationStatus: REJECT");break;
            case 2:
                System.out.println("VerificationStatus: CONFIRMED");break;
        }
        System.out.println("EntryStop: "+responseR.get(report).getEntryStop());
        System.out.println("ExitStop: "+responseR.get(report).getExitStop());
        System.out.println("VehicleType: "+responseR.get(report).getVehicleType());
        System.out.println("BookedLine: "+responseR.get(report).getBookedLine());
        System.out.println("DateBooking: "+responseR.get(report).getDateBooking());
        System.out.println("TimeBooked: "+responseR.get(report).getTimeBooked());
        if(responseR.get(report).getCommentDriver()==null)
            System.out.println("The driver has not yet viewed your report");
        else
            System.out.println("CommentDriver: "+responseR.get(report).getCommentDriver());
        System.out.println("Description: "+responseR.get(report).getDescription());
        System.out.println("");

    }

}