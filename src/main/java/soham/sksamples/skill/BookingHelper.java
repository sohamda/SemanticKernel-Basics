package soham.sksamples.skill;

import com.microsoft.semantickernel.skilldefinition.annotations.DefineSKFunction;
import com.microsoft.semantickernel.skilldefinition.annotations.SKFunctionParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class BookingHelper {
    Logger log = LoggerFactory.getLogger(BookingHelper.class);

    @DefineSKFunction(name = "getBookingDetails",
            description = "find booking details based on bookingNumber, firstName and lastName",
            returnType = "Object")
    public Mono<BookingDetails> getBookingDetails(
            @SKFunctionParameters(name = "bookingNumber", required = true) String bookingNumber,
            @SKFunctionParameters(name = "firstName", required = true) String firstName,
            @SKFunctionParameters(name = "lastName", required = true) String lastName) {
        log.debug("Invoked get flight details for BN {} FN {} LN {}", bookingNumber, firstName, lastName);
        return Mono.justOrEmpty(new BookingDetails(bookingNumber, firstName, lastName));
    }

    @DefineSKFunction(name = "changeBooking",
            description = "update or change booking details based on bookingNumber, firstName and lastName, date, from and to")
    public void changeBooking(@SKFunctionParameters(name = "bookingNumber", required = true) String bookingNumber,
                              @SKFunctionParameters(name = "firstName", required = true) String firstName,
                              @SKFunctionParameters(name = "lastName", required = true) String lastName,
                              @SKFunctionParameters(name = "date", required = true) String date,
                              @SKFunctionParameters(name = "from", required = true) String from,
                              @SKFunctionParameters(name = "to", required = true) String to) {
        log.debug("Invoked change flight details for BN {} FN {} LN {} DT {} FR {} TO {}", bookingNumber, firstName, lastName, date, from, to);
    }

    @DefineSKFunction(name = "cancelBooking",
            description = "cancel booking detail based on bookingNumber, firstName and lastName")
    public void cancelBooking(@SKFunctionParameters(name = "bookingNumber", required = true) String bookingNumber,
                              @SKFunctionParameters(name = "firstName", required = true) String firstName,
                              @SKFunctionParameters(name = "lastName", required = true) String lastName) {
        log.debug("Invoked cancel flight for BN {} FN {} LN {}", bookingNumber, firstName, lastName);
    }

}
