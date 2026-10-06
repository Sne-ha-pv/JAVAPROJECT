import java.time.LocalDate;
import java.util.Scanner;

/**
 * Main Class - Entry point for the Vehicle Rental System.
 * Starts the embedded native Java Web Application on http://localhost:8080
 * and also provides the interactive console CLI fallback.
 */
public class Main {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        RentalAdmin admin = new RentalAdmin();

        // Start Web Server
        try {
            WebServer webServer = new WebServer(PORT, admin);
            webServer.start();
            System.out.println("\n🌐 Access the Web Application at: http://localhost:" + PORT);
            System.out.println("✨ Features enabled: Live Availability, Booking Engine, Late Return Calculator, Fleet Manager & Customer Profiles.");
        } catch (Exception e) {
            System.err.println("Failed to start web server: " + e.getMessage());
        }

        // Check if user requested CLI-only mode or if run without args, keep running or provide interactive CLI
        if (args.length > 0 && args[0].equalsIgnoreCase("--cli")) {
            runCli(admin);
        } else {
            System.out.println("\n💡 Tip: You can interact via the Web Interface in your browser (http://localhost:" + PORT + ") or use the interactive CLI below.");
            runCli(admin);
        }
    }

    private static void runCli(RentalAdmin admin) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====================================");
            System.out.println("    VEHICLE RENTAL SYSTEM (CLI)     ");
            System.out.println("====================================");
            System.out.println("1.  Add Vehicle");
            System.out.println("2.  Display Vehicles");
            System.out.println("3.  Search by Type");
            System.out.println("4.  Search by Brand");
            System.out.println("5.  Update Vehicle");
            System.out.println("6.  Remove Vehicle");
            System.out.println("7.  Add Customer");
            System.out.println("8.  Book Vehicle");
            System.out.println("9.  Return Vehicle");
            System.out.println("10. Exit");
            System.out.print("\nEnter choice (1-10): ");

            if (!sc.hasNextInt()) {
                break;
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Type (Car/Bike/Van): ");
                    String type = sc.nextLine();
                    System.out.print("Enter Brand: ");
                    String brand = sc.nextLine();
                    System.out.print("Enter Model: ");
                    String model = sc.nextLine();
                    System.out.print("Enter Rental Rate per Day (Rs.): ");
                    double rate = sc.nextDouble();

                    Vehicle vehicle;
                    if ("Bike".equalsIgnoreCase(type)) {
                        vehicle = new Bike(0, brand, model, rate, 250, "Standard", "");
                    } else if ("Van".equalsIgnoreCase(type)) {
                        vehicle = new Van(0, brand, model, rate, 8, 800, "");
                    } else {
                        vehicle = new Car(0, brand, model, rate, 5, "Petrol", "");
                    }
                    admin.addVehicle(vehicle);
                    System.out.println("Vehicle added successfully with ID: " + vehicle.getVehicleId());
                    break;

                case 2:
                    admin.displayVehicles();
                    break;

                case 3:
                    System.out.print("Enter vehicle type (Car/Bike/Van): ");
                    String searchType = sc.nextLine();
                    admin.searchByType(searchType);
                    break;

                case 4:
                    System.out.print("Enter vehicle brand: ");
                    String searchBrand = sc.nextLine();
                    admin.searchByBrand(searchBrand);
                    break;

                case 5:
                    System.out.print("Enter Vehicle ID to update: ");
                    int updateId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter new Type: ");
                    String newType = sc.nextLine();
                    System.out.print("Enter new Brand: ");
                    String newBrand = sc.nextLine();
                    System.out.print("Enter new Model: ");
                    String newModel = sc.nextLine();
                    System.out.print("Enter new Rental Rate: ");
                    double newRate = sc.nextDouble();

                    boolean updated = admin.updateVehicle(updateId, newType, newBrand, newModel, newRate);
                    if (updated) {
                        System.out.println("Vehicle updated successfully.");
                    } else {
                        System.out.println("Vehicle ID not found.");
                    }
                    break;

                case 6:
                    System.out.print("Enter Vehicle ID to remove: ");
                    int removeId = sc.nextInt();
                    boolean removed = admin.removeVehicle(removeId);
                    if (removed) {
                        System.out.println("Vehicle removed successfully.");
                    } else {
                        System.out.println("Cannot remove vehicle (not found or currently rented).");
                    }
                    break;

                case 7:
                    System.out.print("Enter Customer Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Contact Details (Phone): ");
                    String contact = sc.nextLine();

                    Customer customer = new Customer(0, name, contact);
                    admin.addCustomer(customer);
                    System.out.println("Customer registered successfully with ID: " + customer.getCustomerId());
                    break;

                case 8:
                    System.out.print("Enter Customer ID: ");
                    int bookingCustomerId = sc.nextInt();
                    System.out.print("Enter Vehicle ID: ");
                    int bookingVehicleId = sc.nextInt();
                    System.out.print("Enter Rental Days: ");
                    int days = sc.nextInt();
                    System.out.print("Enter Security Deposit (Rs.): ");
                    double deposit = sc.nextDouble();

                    LocalDate startDate = LocalDate.now();
                    LocalDate endDate = startDate.plusDays(days);

                    Rental rental = admin.bookVehicle(bookingCustomerId, bookingVehicleId, startDate, endDate, deposit);
                    if (rental != null) {
                        System.out.println("\nVehicle booked successfully!");
                        System.out.println(rental);
                    } else {
                        System.out.println("Booking failed. Please check customer ID, vehicle ID, or availability.");
                    }
                    break;

                case 9:
                    System.out.print("Enter Rental ID: ");
                    int rentalId = sc.nextInt();
                    System.out.print("Enter Late Days (0 if on time): ");
                    int lateDays = sc.nextInt();

                    Rental returnedRental = admin.findRental(rentalId);
                    if (returnedRental != null) {
                        LocalDate returnDate = returnedRental.getEndDate().plusDays(lateDays);
                        admin.returnVehicle(rentalId, returnDate);
                        System.out.println("\nVehicle returned successfully!");
                        System.out.println("Late Penalty: Rs." + returnedRental.getLatePenalty());
                        System.out.println("Security Deposit: Rs." + returnedRental.getSecurityDeposit());
                        System.out.println("Refund Amount: Rs." + returnedRental.getRefundAmount());
                    } else {
                        System.out.println("Rental ID not found or already returned.");
                    }
                    break;

                case 10:
                    System.out.println("\nThank you for using the Vehicle Rental System!");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 10.");
            }
        } while (choice != 10);
    }
}