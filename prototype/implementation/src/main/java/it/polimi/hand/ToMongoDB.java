package it.polimi.hand;

import com.mongodb.*;
import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Updates.currentDate;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import javax.print.Doc;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ToMongoDB{

    public MongoClient client;
    public MongoCollection<Document> users_collection;
    public MongoDatabase database;
    public MongoCollection<Document> drivers_collection;
    public MongoCollection<Document> bookings_collection;
    public MongoCollection<Document> vehicles_collection;
    public MongoCollection<Document> reports_collection;
    public MongoCollection<Document> vehicles_status_collection;
    public MongoCollection<Document> lines_collection;

    public ToMongoDB(){

        client = MongoClients.create("mongodb://dil:dil@localhost:27017");
        database = client.getDatabase("Digital_Innovation_Lab");
        users_collection = database.getCollection("users");
        drivers_collection = database.getCollection("drivers");
        bookings_collection = database.getCollection("bookings");
        vehicles_collection = database.getCollection("vehicles");
        reports_collection = database.getCollection("reports");
        vehicles_status_collection = database.getCollection("vehicles_status");
        lines_collection = database.getCollection("lines");
    }

    public Boolean checkReportExistance(String booking_id, String user_id){

        Document temp;

        temp = (Document) reports_collection.find(and(eq("Related_Booking_ID",booking_id), eq("Reporting_User_ID", user_id))).first();

        if(temp == null)
            return false;

        return true;
    }

    public List<Document> getUserBookings(String user_id){

        return bookings_collection.find(new Document("Booking_User_ID", user_id)).into(new ArrayList<>());
    }

    public List<Document> getAssociatedBookings(List <String> bookings_ids){

        List<Document> all_bookings = new ArrayList<Document>();

        for(String id : bookings_ids){
            Document temp = (Document) (bookings_collection.find(new Document("_id",new ObjectId(id)))).first();
            all_bookings.add(temp);
        }
        return all_bookings;
    }

    public List<Document> getUserReports(String user_id){

        return reports_collection.find(new Document("Reporting_User_ID", user_id)).into(new ArrayList<>());
    }

    public void insertNewReport(String booking_id, String ReportType, String Description, String reporting_user_id, String report_exit){

        SimpleDateFormat date_formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss");
        Date today = new Date();
        Date now = new Date();
        Document temp;


        temp = (Document) bookings_collection.find(new Document("_id", new ObjectId(booking_id))).first();
        System.out.println(temp);
        String entry = temp.getString("Entry_Stop");
        String exit = temp.getString("Exit_Stop");
        String v_id = temp.getString("Vehicle_ID");

        if(report_exit.equals("0"))
            temp = (Document) vehicles_status_collection.find(and(eq("Vehicle_ID", v_id), eq("Date", date_formatter.format(today)), eq("Current_Stop", entry))).first();
        else
            temp = (Document) vehicles_status_collection.find(and(eq("Vehicle_ID", v_id), eq("Date", date_formatter.format(today)), eq("Current_Stop", exit))).first();

        String d_id = temp.getString("Current_Driver_ID");

        reports_collection.insertOne(new Document("ReportType",ReportType).append("Date", date_formatter.format(today)).append("Time_Reported", date_formatter.format(now)).append("Description", Description).append("Driver_Comment", null).append("Related_Booking_ID", booking_id).append("Verification_Status", 0).append("Verification_Date", null).append("Reporting_User_ID", reporting_user_id).append("Vehicle_ID", v_id).append("Driver_Validating_ID", d_id));
    }

    public List<Document> showUnconfirmedReports(String driver_id){

        int unconfirmed_status = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date now = new Date();

        return reports_collection.find(and(eq("Driver_Validating_ID",driver_id), eq("Verification_Status", unconfirmed_status), eq("Date", formatter.format(now)))).into(new ArrayList<>());
    }

    public void consolidateReportStatus(String report_id, String verification_result, String comment){
        //System.out.println("Pre parse: " + verification_result);
        int res = Integer.parseInt(verification_result);
        //System.out.println("Post parse: " + res);

        reports_collection.updateOne(eq("_id", new ObjectId(report_id)), combine(set("Verification_Status", res), set("Driver_Comment", comment), currentDate("Verification_Date")));
    }
}
