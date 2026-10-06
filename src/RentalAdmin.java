 import java.time.LocalDate;
import java.util.ArrayList;

public class RentalAdmin {

    private ArrayList<Vehicle> vehicles;
    private ArrayList<Customer> customers;
    private ArrayList<Rental> rentals;

    private int nextRentalId = 1;

    public RentalAdmin() {

        vehicles = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {

        vehicles.add(vehicle);

        System.out.println(
                "Vehicle added successfully."
        );
    }

    public void displayVehicles() {

        if (vehicles.isEmpty()) {

            System.out.println(
                    "No vehicles available."
            );

            return;
        }

        System.out.println("\n===== VEHICLE LIST =====");

        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }
    }

    public void searchByType(String type) {

        boolean found = false;

        for (Vehicle vehicle : vehicles) {

            if (vehicle.getType().equalsIgnoreCase(type)
                    && vehicle.isAvailable()) {

                System.out.println(vehicle);
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                    "No available vehicle found."
            );
        }
    }

    public void searchByBrand(String brand) {

        boolean found = false;

        for (Vehicle vehicle : vehicles) {

            if (vehicle.getBrand().equalsIgnoreCase(brand)
                    && vehicle.isAvailable()) {

                System.out.println(vehicle);
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                    "No available vehicle found."
            );
        }
    }

    public void updateVehicle(int vehicleId,
                              String type,
                              String brand,
                              String model,
                              double rentalRate) {

        Vehicle vehicle = findVehicle(vehicleId);

        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        vehicle.setType(type);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setRentalRate(rentalRate);

        System.out.println(
                "Vehicle updated successfully."
        );
    }

    public void removeVehicle(int vehicleId) {

        Vehicle vehicle = findVehicle(vehicleId);

        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        if (!vehicle.isAvailable()) {
            System.out.println(
                    "Cannot remove a rented vehicle."
            );
            return;
        }

        vehicles.remove(vehicle);

        System.out.println(
                "Vehicle removed successfully."
        );
    }

    public void addCustomer(Customer customer) {

        customers.add(customer);

        System.out.println(
                "Customer added successfully."
        );
    }

    public Customer findCustomer(int customerId) {

        for (Customer customer : customers) {

            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }

        return null;
    }

    public Vehicle findVehicle(int vehicleId) {

        for (Vehicle vehicle : vehicles) {

            if (vehicle.getVehicleId() == vehicleId) {
                return vehicle;
            }
        }

        return null;
    }

    public void bookVehicle(int customerId,
                            int vehicleId,
                            LocalDate startDate,
                            LocalDate endDate,
                            double deposit) {

        Customer customer = findCustomer(customerId);
        Vehicle vehicle = findVehicle(vehicleId);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        if (!vehicle.isAvailable()) {
            System.out.println(
                    "Vehicle is already rented."
            );
            return;
        }

        Rental rental = new Rental(
                nextRentalId++,
                customer,
                vehicle,
                startDate,
                endDate,
                deposit
        );

        rentals.add(rental);
        customer.addRental(rental);
        vehicle.setAvailable(false);

        System.out.println(
                "\nVehicle booked successfully."
        );

        System.out.println(rental);
    }

    public void returnVehicle(int rentalId,
                              LocalDate returnDate) {

        for (Rental rental : rentals) {

            if (rental.getRentalId() == rentalId) {

                rental.calculateLatePenalty(returnDate);

                rental.getVehicle().setAvailable(true);

                System.out.println(
                        "\nVehicle returned successfully."
                );

                System.out.println(
                        "Late Penalty: Rs." +
                        rental.getLatePenalty()
                );

                System.out.println(
                        "Security Deposit: Rs." +
                        rental.getSecurityDeposit()
                );

                System.out.println(
                        "Refund Amount: Rs." +
                        rental.getRefundAmount()
                );

                return;
            }
        }

        System.out.println(
                "Rental ID not found."
        );
    }
}