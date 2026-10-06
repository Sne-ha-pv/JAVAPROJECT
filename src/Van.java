/**
 * Van Class
 * Subclass of Vehicle showcasing OOP Inheritance and Polymorphism.
 */
public class Van extends Vehicle {
    private int cargoCapacityKg;
    private int passengerCapacity;

    public Van(int vehicleId, String brand, String model, double rentalRate, int passengerCapacity, int cargoCapacityKg, String imageUrl) {
        super(vehicleId, "Van", brand, model, rentalRate, imageUrl, passengerCapacity + " Passengers, " + cargoCapacityKg + "kg Cargo, Heavy Duty");
        this.passengerCapacity = passengerCapacity;
        this.cargoCapacityKg = cargoCapacityKg;
    }

    public int getCargoCapacityKg() {
        return cargoCapacityKg;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }
}
