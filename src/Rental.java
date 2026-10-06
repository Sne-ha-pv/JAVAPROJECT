import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Rental Class
 * Represents a rental transaction between a Customer and a Vehicle.
 * Encapsulates duration calculation, pricing, security deposit, and late return penalty logic.
 */
public class Rental {

    private int rentalId;
    private Customer customer;
    private Vehicle vehicle;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualReturnDate;
    private double rentalCost;
    private double securityDeposit;
    private double latePenalty;
    private String status; // "ACTIVE", "RETURNED"

    public Rental(int rentalId,
                  Customer customer,
                  Vehicle vehicle,
                  LocalDate startDate,
                  LocalDate endDate,
                  double securityDeposit) {

        this.rentalId = rentalId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.securityDeposit = securityDeposit;
        this.latePenalty = 0;
        this.actualReturnDate = null;
        this.status = "ACTIVE";

        int days = (int) ChronoUnit.DAYS.between(startDate, endDate);
        if (days <= 0) {
            days = 1;
        }

        this.rentalCost = vehicle.calculateRentalCost(days);
    }

    public int getRentalId() {
        return rentalId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public double getRentalCost() {
        return rentalCost;
    }

    public double getSecurityDeposit() {
        return securityDeposit;
    }

    public double getLatePenalty() {
        return latePenalty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Calculates late-return penalty based on actual return date vs scheduled end date.
     * Penalty rate is Rs. 200/day late (or standard vehicle daily rate surcharge).
     */
    public void calculateLatePenalty(LocalDate returnDate) {
        this.actualReturnDate = returnDate;
        if (returnDate != null && returnDate.isAfter(endDate)) {
            long lateDays = ChronoUnit.DAYS.between(endDate, returnDate);
            this.latePenalty = lateDays * 200.0;
        } else {
            this.latePenalty = 0.0;
        }
        this.status = "RETURNED";
    }

    /**
     * Returns the refunded deposit amount after deducting late penalties.
     */
    public double getRefundAmount() {
        return Math.max(0.0, securityDeposit - latePenalty);
    }

    /**
     * Converts Rental to JSON
     */
    public String toJson(boolean includeCustomerFull) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"rentalId\":").append(rentalId).append(",");
        sb.append("\"customerId\":").append(customer != null ? customer.getCustomerId() : 0).append(",");
        sb.append("\"customerName\":\"").append(customer != null ? JsonUtils.escape(customer.getName()) : "").append("\",");
        sb.append("\"vehicleId\":").append(vehicle != null ? vehicle.getVehicleId() : 0).append(",");
        sb.append("\"vehicleName\":\"").append(vehicle != null ? JsonUtils.escape(vehicle.getBrand() + " " + vehicle.getModel()) : "").append("\",");
        sb.append("\"vehicleType\":\"").append(vehicle != null ? JsonUtils.escape(vehicle.getType()) : "").append("\",");
        sb.append("\"startDate\":\"").append(startDate != null ? startDate.toString() : "").append("\",");
        sb.append("\"endDate\":\"").append(endDate != null ? endDate.toString() : "").append("\",");
        sb.append("\"actualReturnDate\":\"").append(actualReturnDate != null ? actualReturnDate.toString() : "").append("\",");
        sb.append("\"rentalCost\":").append(rentalCost).append(",");
        sb.append("\"securityDeposit\":").append(securityDeposit).append(",");
        sb.append("\"latePenalty\":").append(latePenalty).append(",");
        sb.append("\"refundAmount\":").append(getRefundAmount()).append(",");
        sb.append("\"status\":\"").append(status).append("\"");

        if (includeCustomerFull && customer != null) {
            sb.append(",\"customer\":").append(customer.toJson(false));
        }
        if (vehicle != null) {
            sb.append(",\"vehicle\":").append(vehicle.toJson());
        }

        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "\nRental ID: " + rentalId +
                "\nCustomer: " + (customer != null ? customer.getName() : "Unknown") +
                "\nVehicle: " + (vehicle != null ? vehicle.getBrand() + " " + vehicle.getModel() : "Unknown") +
                "\nStart Date: " + startDate +
                "\nEnd Date: " + endDate +
                "\nStatus: " + status +
                (actualReturnDate != null ? ("\nReturn Date: " + actualReturnDate) : "") +
                "\nRental Cost: Rs." + rentalCost +
                "\nSecurity Deposit: Rs." + securityDeposit +
                "\nLate Penalty: Rs." + latePenalty +
                "\nRefund Amount: Rs." + getRefundAmount();
    }
}