package it.polimi.hand;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReportService {

    @Autowired
    ToMongoDB databaseService;

    public void registerReport(String userId, ReportUser reportUser){
        System.out.println(reportUser.getRelatedBookingID() + reportUser.getReportType() + reportUser.getDescription() + userId);
        System.out.println(!databaseService.checkReportExistance(reportUser.getRelatedBookingID(), userId));
        if (!databaseService.checkReportExistance(reportUser.getRelatedBookingID(), userId))
            databaseService.insertNewReport(reportUser.getRelatedBookingID(), reportUser.getReportType(), reportUser.getDescription(), userId, reportUser.getExitStop());
    }

    public List<ReadableReportUser> findUserReports(String userId){
        List<ReadableReportUser> readableReportUserList = new ArrayList<>();
        List<Document> documentReports = databaseService.getUserReports(userId);

        List<Document> documentBooking = databaseService.getAssociatedBookings(findReportId(documentReports));

        //System.out.println(documentBooking.size());
        //System.out.println(documentReports.size());

        for(int i = 0; i < documentReports.size(); i++){
            Document report = documentReports.get(i);
            Document booking = documentBooking.get(i);




            String reportType = (String) report.get("ReportType");
            //System.out.println(reportType);
            String description = (String) report.get("Description");
            int verificationStatus = (int) report.get("Verification_Status");
            String dateReport = (String) report.get("Date");
            String commentDriver = (String) report.get("Comment_Driver");

            String bookedLine = (String) booking.get("BookedLine");
            String timeBooked = (String) booking.get("Arrival_At_Entry_Time");
            String dateBooking = (String) booking.get("Date");
            String entryStop = (String) booking.get("Entry_Stop");
            String exitStop = (String) booking.get("Exit_Stop");
            String vehicleType = (String) booking.get("VehicleType");

            readableReportUserList.add(new ReadableReportUser(reportType, bookedLine, vehicleType, description,
                    entryStop, exitStop, commentDriver, verificationStatus, dateReport, dateBooking, timeBooked));
        }

        return readableReportUserList;

    }

    private List<String> findReportId(List<Document> documents){
        List<String> reportIds = new ArrayList<>();
        for(Document d: documents){
            reportIds.add((String) d.getString("Related_Booking_ID").toString());
            //System.out.println(d.getObjectId("_id").toString());
        }
        return reportIds;
    }

    public List<ReadableReportDriver> findDriverReports(String driverId){
        List<ReadableReportDriver> readableReportDrivers = new ArrayList<>();
        List<Document> driverReports = databaseService.showUnconfirmedReports(driverId);
        List<Document> associatedBookings = databaseService.getAssociatedBookings(findReportId(driverReports));

        System.out.println(driverReports.size());
        System.out.println(associatedBookings.size());

        for(int i = 0; i < driverReports.size(); i++){
            Document driverReport = driverReports.get(0);
            Document booking = associatedBookings.get(0);
            String reportId = driverReport.getObjectId("_id").toString();
            String reportType = driverReport.getString("ReportType");
            String description = driverReport.getString("Description");
            String dateBooking = booking.getString("Date");
            String timeBooked = booking.getString("Arrival_At_Entry_Time");
            String bookedLine = booking.getString("BookedLine");
            String entryStop = booking.getString("Entry_Stop");
            String exitStop = booking.getString("Exit_Stop");

            readableReportDrivers.add(new ReadableReportDriver(reportId, reportType, description, bookedLine, entryStop,
                    exitStop, dateBooking, timeBooked));
        }

        return readableReportDrivers;
    }

    public void verifyReport(ProcessedReport processedReport){
        System.out.println("Here");
        System.out.println(processedReport.getVerificationStatus());
        databaseService.consolidateReportStatus(processedReport.getReportID(), processedReport.getVerificationStatus()
                ,processedReport.getCommentDriver());
    }

}
