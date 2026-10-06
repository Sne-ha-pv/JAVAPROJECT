import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        RentalAdmin admin = new RentalAdmin();

        int choice;

        do {

            System.out.println("\n====================================");
            System.out.println("       VEHICLE RENTAL SYSTEM");
            System.out.println("====================================");

            System.out.println("1. Add Vehicle");
            System.out.println("2. Display Vehicles");
            System.out.println("3. Search by Type");
            System.out.println("4. Search by Brand");
            System.out.println("5. Update Vehicle");
            System.out.println("6. Remove Vehicle");
            System.out.println("7. Add Customer");
            System.out.println("8. Book Vehicle");
            System.out.println("9. Return Vehicle");
            System.out.println("10. Exit");

            System.out.print("\nEnter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter Vehicle ID: ");
                    int vehicleId = sc.nextInt();
                    sc.nextLine();

                    System.out.print(
                            "Enter Type (Car/Bike/Van): "
                    );
                    String type = sc.nextLine();

                    System.out.print("Enter Brand: ");
                    String brand = sc.nextLine();

                    System.out.print("Enter Model: ");
                    String model = sc.nextLine();

                    System.out.print(
                            "Enter Rental Rate per Day: "
                    );
                    double rate = sc.nextDouble();

                    Vehicle vehicle = new Vehicle(
                            vehicleId,
                            type,
                            brand,
                            model,
                            rate
                    );

                    admin.addVehicle(vehicle);

                    break;

                case 2:

                    admin.displayVehicles();

                    break;

                case 3:

                    System.out.print(
                            "Enter vehicle type: "
                    );

                    String searchType = sc.nextLine();

                    admin.searchByType(searchType);

                    break;

                case 4:

                    System.out.print(
                            "Enter vehicle brand: "
                    );

                    String searchBrand = sc.nextLine();

                    admin.searchByBrand(searchBrand);

                    break;

                case 5:

                    System.out.print(
                            "Enter Vehicle ID to update: "
                    );

                    int updateId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter new Type: ");
                    String newType = sc.nextLine();

                    System.out.print("Enter new Brand: ");
                    String newBrand = sc.nextLine();

                    System.out.print("Enter new Model: ");
                    String newModel = sc.nextLine();

                    System.out.print(
                            "Enter new Rental Rate: "
                    );

                    double newRate = sc.nextDouble();

                    admin.updateVehicle(
                            updateId,
                            newType,
                            newBrand,
                            newModel,
                            newRate
                    );

                    break;

                case 6:

                    System.out.print(
                            "Enter Vehicle ID to remove: "
                    );

                    int removeId = sc.nextInt();

                    admin.removeVehicle(removeId);

                    break;

                case 7:

                    System.out.print(
                            "Enter Customer ID: "
                    );

                    int customerId = sc.nextInt();
                    sc.nextLine();

                    System.out.print(
                            "Enter Customer Name: "
                    );

                    String name = sc.nextLine();

                    System.out.print(
                            "Enter Contact Details: "
                    );

                    String contact = sc.nextLine();

                    Customer customer = new Customer(
                            customerId,
                            name,
                            contact
                    );

                    admin.addCustomer(customer);

                    break;

                case 8:

                    System.out.print(
                            "Enter Customer ID: "
                    );

                    int bookingCustomerId = sc.nextInt();

                    System.out.print(
                            "Enter Vehicle ID: "
                    );

                    int bookingVehicleId = sc.nextInt();

                    System.out.print(
                            "Enter Rental Days: "
                    );

                    int days = sc.nextInt();

                    System.out.print(
                            "Enter Security Deposit: "
                    );

                    double deposit = sc.nextDouble();

                    LocalDate startDate =
                            LocalDate.now();

                    LocalDate endDate =
                            startDate.plusDays(days);

                    admin.bookVehicle(
                            bookingCustomerId,
                            bookingVehicleId,
                            startDate,
                            endDate,
                            deposit
                    );

                    break;

                case 9:

                    System.out.print(
                            "Enter Rental ID: "
                    );

                    int rentalId = sc.nextInt();

                    System.out.print(
                            "Enter Late Days: "
                    );

                    int lateDays = sc.nextInt();

                    LocalDate returnDate =
                            LocalDate.now()
                                    .plusDays(lateDays);

                    admin.returnVehicle(
                            rentalId,
                            returnDate
                    );

                    break;

                case 10:

                    System.out.println(
                            "\nThank you for using Vehicle Rental System!"
                    );

                    break;

                default:

                    System.out.println(
                            "Invalid choice."
                    );
            }

        } while (choice != 10);

        sc.close();
    }
}