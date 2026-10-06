import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * RentalAdmin Class
 * Core controller / manager handling vehicle inventory, customer repository, and rental processing.
 */
public class RentalAdmin {

    private ArrayList<Vehicle> vehicles;
    private ArrayList<Customer> customers;
    private ArrayList<Rental> rentals;
    private int nextRentalId = 101;
    private int nextVehicleId = 1;
    private int nextCustomerId = 1;

    public RentalAdmin() {
        this.vehicles = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.rentals = new ArrayList<>();
        seedInitialData();
    }

    /**
     * Seeds initial fleet and customers for immediate use in both CLI and Web App.
     */
    private void seedInitialData() {
        // Sample Vehicles
        addVehicle(new Car(nextVehicleId++, "Toyota", "Camry Hybrid", 3500.0, 5, "Hybrid", "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?w=600&auto=format&fit=crop&q=80"));
        addVehicle(new Car(nextVehicleId++, "BMW", "3 Series Luxury", 6500.0, 5, "Petrol", "https://images.unsplash.com/photo-1555215695-3004980ad54e?w=600&auto=format&fit=crop&q=80"));
        addVehicle(new Car(nextVehicleId++, "Tesla", "Model 3 Electric", 5500.0, 5, "Electric", "https://images.unsplash.com/photo-1560958089-b8a1929cea89?w=600&auto=format&fit=crop&q=80"));
        addVehicle(new Car(nextVehicleId++, "Hyundai", "Creta SX", 2800.0, 5, "Diesel", "https://images.unsplash.com/photo-1583121274602-3e2820c69888?w=600&auto=format&fit=crop&q=80"));

        addVehicle(new Bike(nextVehicleId++, "Royal Enfield", "Hunter 350", 1200.0, 350, "Cruiser", "https://images.unsplash.com/photo-1558981806-ec527fa84c39?w=600&auto=format&fit=crop&q=80"));
        addVehicle(new Bike(nextVehicleId++, "Kawasaki", "Ninja 400", 2500.0, 399, "SuperSport", "https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?w=600&auto=format&fit=crop&q=80"));
        addVehicle(new Bike(nextVehicleId++, "Honda", "Activa 6G", 700.0, 110, "Scooter", "https://images.unsplash.com/photo-1591637333184-19aa84b3e01f?w=600&auto=format&fit=crop&q=80"));

        addVehicle(new Van(nextVehicleId++, "Mercedes-Benz", "Sprinter Executive", 8000.0, 12, 1200, "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=600&auto=format&fit=crop&q=80"));
        addVehicle(new Van(nextVehicleId++, "Toyota", "HiAce Commuter", 5000.0, 10, 800, "https://images.unsplash.com/photo-1563720223185-11003d516935?w=600&auto=format&fit=crop&q=80"));

        // Sample Customers
        Customer c1 = new Customer(nextCustomerId++, "Aarav Sharma", "+91 98765 43210", "aarav.sharma@example.com", "DL-0420190012");
        Customer c2 = new Customer(nextCustomerId++, "Priya Patel", "+91 91234 56789", "priya.patel@example.com", "DL-0720210084");
        Customer c3 = new Customer(nextCustomerId++, "Rohan Mehta", "+91 99887 76655", "rohan.mehta@example.com", "DL-0120220914");
        addCustomer(c1);
        addCustomer(c2);
        addCustomer(c3);

        // Pre-create an active booking for demonstration
        bookVehicle(c1.getCustomerId(), 2, LocalDate.now().minusDays(3), LocalDate.now().plusDays(2), 5000.0);
    }

    public synchronized void addVehicle(Vehicle vehicle) {
        if (vehicle.getVehicleId() <= 0) {
            vehicle.setVehicleId(nextVehicleId++);
        } else if (vehicle.getVehicleId() >= nextVehicleId) {
            nextVehicleId = vehicle.getVehicleId() + 1;
        }
        vehicles.add(vehicle);
    }

    public synchronized boolean updateVehicle(int vehicleId, String type, String brand, String model, double rentalRate) {
        Vehicle v = findVehicle(vehicleId);
        if (v == null) return false;
        v.setType(type);
        v.setBrand(brand);
        v.setModel(model);
        v.setRentalRate(rentalRate);
        return true;
    }

    public synchronized boolean removeVehicle(int vehicleId) {
        Vehicle v = findVehicle(vehicleId);
        if (v == null) return false;
        if (!v.isAvailable()) {
            return false; // Cannot remove rented vehicle
        }
        return vehicles.remove(v);
    }

    public synchronized void addCustomer(Customer customer) {
        if (customer.getCustomerId() <= 0) {
            customer.setCustomerId(nextCustomerId++);
        } else if (customer.getCustomerId() >= nextCustomerId) {
            nextCustomerId = customer.getCustomerId() + 1;
        }
        customers.add(customer);
    }

    public synchronized Vehicle findVehicle(int vehicleId) {
        for (Vehicle v : vehicles) {
            if (v.getVehicleId() == vehicleId) return v;
        }
        return null;
    }

    public synchronized Customer findCustomer(int customerId) {
        for (Customer c : customers) {
            if (c.getCustomerId() == customerId) return c;
        }
        return null;
    }

    public synchronized Rental findRental(int rentalId) {
        for (Rental r : rentals) {
            if (r.getRentalId() == rentalId) return r;
        }
        return null;
    }

    public synchronized List<Vehicle> searchVehicles(String type, String brand, Boolean onlyAvailable) {
        List<Vehicle> list = new ArrayList<>();
        for (Vehicle v : vehicles) {
            boolean matchesType = (type == null || type.trim().isEmpty() || type.equalsIgnoreCase("all") || v.getType().equalsIgnoreCase(type.trim()));
            boolean matchesBrand = (brand == null || brand.trim().isEmpty() || v.getBrand().toLowerCase().contains(brand.trim().toLowerCase()) || v.getModel().toLowerCase().contains(brand.trim().toLowerCase()));
            boolean matchesAvailability = (onlyAvailable == null || !onlyAvailable || v.isAvailable());

            if (matchesType && matchesBrand && matchesAvailability) {
                list.add(v);
            }
        }
        return list;
    }

    public synchronized Rental bookVehicle(int customerId, int vehicleId, LocalDate startDate, LocalDate endDate, double deposit) {
        Customer customer = findCustomer(customerId);
        Vehicle vehicle = findVehicle(vehicleId);

        if (customer == null || vehicle == null || !vehicle.isAvailable()) {
            return null;
        }

        Rental rental = new Rental(nextRentalId++, customer, vehicle, startDate, endDate, deposit);
        rentals.add(0, rental); // add to front for chronological ordering
        customer.addRental(rental);
        vehicle.setAvailable(false);
        return rental;
    }

    public synchronized Rental returnVehicle(int rentalId, LocalDate returnDate) {
        Rental rental = findRental(rentalId);
        if (rental == null || "RETURNED".equalsIgnoreCase(rental.getStatus())) {
            return null;
        }

        rental.calculateLatePenalty(returnDate);
        if (rental.getVehicle() != null) {
            rental.getVehicle().setAvailable(true);
        }
        return rental;
    }

    public synchronized ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public synchronized ArrayList<Customer> getCustomers() {
        return customers;
    }

    public synchronized ArrayList<Rental> getRentals() {
        return rentals;
    }

    // JSON serializations for Web API
    public synchronized String getVehiclesJson(String type, String brand, Boolean onlyAvailable) {
        List<Vehicle> list = searchVehicles(type, brand, onlyAvailable);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(list.get(i).toJson());
        }
        sb.append("]");
        return sb.toString();
    }

    public synchronized String getCustomersJson() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < customers.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(customers.get(i).toJson(true));
        }
        sb.append("]");
        return sb.toString();
    }

    public synchronized String getRentalsJson() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < rentals.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(rentals.get(i).toJson(true));
        }
        sb.append("]");
        return sb.toString();
    }

    public synchronized String getStatsJson() {
        int totalVehicles = vehicles.size();
        int availableVehicles = 0;
        int rentedVehicles = 0;
        for (Vehicle v : vehicles) {
            if (v.isAvailable()) availableVehicles++;
            else rentedVehicles++;
        }

        int activeRentals = 0;
        int returnedRentals = 0;
        double totalRevenue = 0;
        double activeDeposits = 0;

        for (Rental r : rentals) {
            if ("ACTIVE".equalsIgnoreCase(r.getStatus())) {
                activeRentals++;
                activeDeposits += r.getSecurityDeposit();
            } else {
                returnedRentals++;
            }
            totalRevenue += r.getRentalCost() + r.getLatePenalty();
        }

        return "{" +
                "\"totalVehicles\":" + totalVehicles + "," +
                "\"availableVehicles\":" + availableVehicles + "," +
                "\"rentedVehicles\":" + rentedVehicles + "," +
                "\"totalCustomers\":" + customers.size() + "," +
                "\"activeRentals\":" + activeRentals + "," +
                "\"returnedRentals\":" + returnedRentals + "," +
                "\"totalRevenue\":" + totalRevenue + "," +
                "\"activeDeposits\":" + activeDeposits +
                "}";
    }

    // CLI helper methods for backward compatibility
    public void displayVehicles() {
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles available.");
            return;
        }
        System.out.println("\n===== VEHICLE LIST =====");
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }
    }

    public void searchByType(String type) {
        List<Vehicle> res = searchVehicles(type, null, true);
        if (res.isEmpty()) {
            System.out.println("No available vehicle found for type: " + type);
        } else {
            for (Vehicle v : res) System.out.println(v);
        }
    }

    public void searchByBrand(String brand) {
        List<Vehicle> res = searchVehicles(null, brand, true);
        if (res.isEmpty()) {
            System.out.println("No available vehicle found for brand: " + brand);
        } else {
            for (Vehicle v : res) System.out.println(v);
        }
    }
}