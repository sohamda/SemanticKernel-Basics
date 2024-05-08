package soham.sksamples.skill;

public class BookingDetails {
    String bookingNumber;
    String firstName;
    String lastName;

    @Override
    public String toString() {
        return "BookingDetails{" +
                "bookingNumber='" + bookingNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public BookingDetails(String bookingNumber, String firstName, String lastName) {
        this.bookingNumber = bookingNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
