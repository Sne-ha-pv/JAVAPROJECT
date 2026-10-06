import java.util.ArrayList;

/**
 * Customer Class
 * Encapsulates customer information and rental history.
 */
public class Customer {

    private int customerId;
    private String name;
    private String contactDetails;
    private String email;
    private String licenseNumber;
    private ArrayList<Rental> rentalHistory;

    public Customer(int customerId, String name, String contactDetails) {
        this(customerId, name, contactDetails, "", "");
    }

    public Customer(int customerId, String name, String contactDetails, String email, String licenseNumber) {
        this.customerId = customerId;
        this.name = name;
        this.contactDetails = contactDetails;
        this.email = (email == null || email.trim().isEmpty()) ? "customer" + customerId + "@example.com" : email;
        this.licenseNumber = (licenseNumber == null || licenseNumber.trim().isEmpty()) ? "DL-" + (10000 + customerId) : licenseNumber;
        this.rentalHistory = new ArrayList<>();
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void addRental(Rental rental) {
        rentalHistory.add(rental);
    }

    public ArrayList<Rental> getRentalHistory() {
        return rentalHistory;
    }

    /**
     * Converts customer object to JSON format
     */
    public String toJson(boolean includeHistory) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"customerId\":").append(customerId).append(",");
        sb.append("\"name\":\"").append(JsonUtils.escape(name)).append("\",");
        sb.append("\"contactDetails\":\"").append(JsonUtils.escape(contactDetails)).append("\",");
        sb.append("\"email\":\"").append(JsonUtils.escape(email)).append("\",");
        sb.append("\"licenseNumber\":\"").append(JsonUtils.escape(licenseNumber)).append("\",");
        sb.append("\"totalRentals\":").append(rentalHistory.size());

        if (includeHistory) {
            sb.append(",\"rentalHistory\":[");
            for (int i = 0; i < rentalHistory.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(rentalHistory.get(i).toJson(false));
            }
            sb.append("]");
        }

        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerId +
                " | Name: " + name +
                " | Contact: " + contactDetails +
                " | Email: " + email +
                " | License: " + licenseNumber +
                " | Total Bookings: " + rentalHistory.size();
    }
}