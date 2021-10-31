package it.polimi.hand;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class Controller {

    private final BookingService bookingService;
    private final ReportService reportService;

    public Controller(BookingService bookingService, ReportService reportService) {
        this.bookingService = bookingService;
        this.reportService = reportService;
    }

    @GetMapping("/users/{userID}/bookings")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingInfoUser> getBookingsUser(@PathVariable String userID) {
        System.out.println("Sono dentro getBookingUser");
        // verificare che l'utente associato all'ID passato esista
        // verificare che esistano bookings relativo a quell'user
        try {
            return bookingService.findUserBookings(userID);
        /*} catch (InvalidPathParamException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

         */
        } catch (EmptyListException e){
            System.out.println("Non qui");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

    }

    @PostMapping("/users/{userID}/reports/insert")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ReadableReportUser> storeUserReport(@PathVariable String userID, @RequestBody ReportUser reportUser){
        System.out.println("Sono entrato nello store userReport");
        System.out.println(reportUser.getReportType());
        System.out.println(reportUser.getDescription());
        System.out.println(reportUser.getRelatedBookingID());
        System.out.println(reportUser.getExitStop());
        // verificare che l'utente associato all'ID passato esista
        // eventuali verifiche su parametri passati in reportUser
        // la lista di report non può essere vuota perché uno viene creato per forza
        //try {
            reportService.registerReport(userID, reportUser);
            return reportService.findUserReports(userID);
            /*
        } catch (InvalidPathParamException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (InvalidBodyParamsException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        */

    }

    @GetMapping("/users/{userID}/reports/receive")
    @ResponseStatus(HttpStatus.OK)
    public List<ReadableReportUser> getAllReportsUser(@PathVariable String userID){
        // verificare che l'utente associato all'ID passato esista
        // verificare che esistano reports relative all'ID passato (se no code 204)
        //try {
            return reportService.findUserReports(userID);
            /*
        } catch (InvalidPathParamException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (EmptyListException e){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

             */
    }

    @GetMapping("/drivers/{driverID}/reports/receive")
    @ResponseStatus(HttpStatus.OK)
    public List<ReadableReportDriver> getAllReportsDriver(@PathVariable String driverID){
        // verificare che l'utente associato all'ID passato esista
        // verificare che esistano reports relative all'ID passato (se no code 204)
        //try {
            return reportService.findDriverReports(driverID);
            /*
        } catch (InvalidPathParamException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (EmptyListException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

             */
    }

    @PutMapping("/drivers/{driverID}/reports/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyReport(@RequestBody ProcessedReport processedReport){
        //try {
            System.out.println(processedReport.getVerificationStatus());
            reportService.verifyReport(processedReport);

            /*
        } catch (InvalidBodyParamsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

             */
    }
}
