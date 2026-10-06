import java.util.ArrayList;

public class Customer {

    private int customerId;
    private String name;
    private String contactDetails;
    private ArrayList<Rental> rentalHistory;

    public Customer(int customerId, String name,
                    String contactDetails) {

        this.customerId = customerId;
        this.name = name;
        this.contactDetails = contactDetails;
        this.rentalHistory = new ArrayList<>();
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public ArrayList<Rental> getRentalHistory() {
        return rentalHistory;
    }

    public void addRental(Rental rental) {
        rentalHistory.add(rental);
    }

    @Override
    public String toString() {

        return "Customer ID: " + customerId +
                " | Name: " + name +
                " | Contact: " + contactDetails;
    }
}