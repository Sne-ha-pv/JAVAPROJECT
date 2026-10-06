/**
 * Car Class
 * Subclass of Vehicle showcasing OOP Inheritance and Polymorphism.
 */
public class Car extends Vehicle {
    private int seatingCapacity;
    private String fuelType;

    public Car(int vehicleId, String brand, String model, double rentalRate, int seatingCapacity, String fuelType, String imageUrl) {
        super(vehicleId, "Car", brand, model, rentalRate, imageUrl, seatingCapacity + " Seats, " + fuelType + ", AC, Bluetooth");
        this.seatingCapacity = seatingCapacity;
        this.fuelType = fuelType;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public String getFuelType() {
        return fuelType;
    }
}
