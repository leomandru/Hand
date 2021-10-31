package it.polimi.hand;

public class ProcessedReport {

    private String reportID, commentDriver;
    private String verificationStatus;

    public ProcessedReport(String reportID, String commentDriver, String verificationStatus) {
        this.reportID = reportID;
        this.commentDriver = commentDriver;
        this.verificationStatus = verificationStatus;
    }

    public String getReportID() {
        return reportID;
    }

    public String getCommentDriver() {
        return commentDriver;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }
}
