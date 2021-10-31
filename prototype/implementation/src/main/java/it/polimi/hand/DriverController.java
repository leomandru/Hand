package it.polimi.hand;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DriverController {

    String driverId = "60b34e04688c7523647a3aa0";
    List<ReadableReportDriver> readableReportDrivers = new ArrayList<>();

    WebClient client = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultCookie("cookieKey", "cookieValue")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
            .build();

    public boolean getAllReports(){
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.GET);
        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/drivers/" + driverId + "/reports/receive");
        readableReportDrivers = bodySpec.retrieve().bodyToFlux(ReadableReportDriver.class).collectList().block();
        if(readableReportDrivers == null || readableReportDrivers.size()==0){
            System.out.println("No reports to show.");
            return false;}
        showFirstDetails();
        return true;
    }

    private void showFirstDetails(){
        int i = 0;
        for(ReadableReportDriver readableReportDriver: readableReportDrivers){
            System.out.println("-----------------");
            System.out.println("Report's number: " + i+1);
            System.out.println("Report type: " + readableReportDriver.getReportType());
            System.out.println("Date: " + readableReportDriver.getDateBooking());
            System.out.println("Time: " + readableReportDriver.getTimeBooked());
            System.out.println("Line: " + readableReportDriver.getBookedLine());
            System.out.println("-----------------");
            i++;
        }
    }

    public boolean showMoreDetails(int position){
        position = position - 1;
        if(position < readableReportDrivers.size()) {
            ReadableReportDriver target = readableReportDrivers.get(position);
            System.out.println("-----------------");
            System.out.println("Report's number: " + position);
            System.out.println("Report type: " + target.getReportType());
            System.out.println("Description: " + target.getDescription());
            System.out.println("Date: " + target.getDateBooking());
            System.out.println("Time: " + target.getTimeBooked());
            System.out.println("Line: " + target.getBookedLine());
            System.out.println("Entry stop: " + target.getEntryStop());
            System.out.println("Exit stop: " + target.getExitStop());
            System.out.println("-----------------");
            return true;
        }
        return false;
    }

    public boolean numberCorrectReport(int position){
        return position < readableReportDrivers.size();
    }

    public void verifyReport(int result, int report_related_position, String comment){
        ProcessedReport processedReport = new ProcessedReport(readableReportDrivers.get(report_related_position-1).getReportID(),comment,String.valueOf(result));
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.put();
        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/drivers/" + driverId + "/reports/verify");
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(processedReport);
        headersSpec.retrieve().bodyToMono(String.class).block();
    }

}
