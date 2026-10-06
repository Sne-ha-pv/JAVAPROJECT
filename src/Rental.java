import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Rental {

    private int rentalId;
    private Customer customer;
    private Vehicle vehicle;
    private LocalDate startDate;
    private LocalDate endDate;
    private double rentalCost;
    private double securityDeposit;
    private double latePenalty;

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

        int days = (int) ChronoUnit.DAYS.between(
                startDate,
                endDate
        );

        if (days <= 0) {
            days = 1;
        }

        rentalCost = vehicle.calculateRentalCost(days);
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

    public LocalDate getEndDate() {
        return endDate;
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

    public void calculateLatePenalty(LocalDate returnDate) {

        if (returnDate.isAfter(endDate)) {

            long lateDays =
                    ChronoUnit.DAYS.between(
                            endDate,
                            returnDate
                    );

            latePenalty = lateDays * 200;

        } else {

            latePenalty = 0;
        }
    }

    public double getRefundAmount() {

        return Math.max(
                0,
                securityDeposit - latePenalty
        );
    }

    @Override
    public String toString() {

        return "\nRental ID: " + rentalId +
                "\nCustomer: " + customer.getName() +
                "\nVehicle: " +
                vehicle.getBrand() + " " +
                vehicle.getModel() +
                "\nStart Date: " + startDate +
                "\nEnd Date: " + endDate +
                "\nRental Cost: Rs." + rentalCost +
                "\nSecurity Deposit: Rs." + securityDeposit +
                "\nLate Penalty: Rs." + latePenalty;
    }
}