/**
 * Bike Class
 * Subclass of Vehicle showcasing OOP Inheritance and Polymorphism.
 */
public class Bike extends Vehicle {
    private int engineCc;
    private String bikeCategory; // e.g. "Cruiser", "Sport", "Scooter"

    public Bike(int vehicleId, String brand, String model, double rentalRate, int engineCc, String bikeCategory, String imageUrl) {
        super(vehicleId, "Bike", brand, model, rentalRate, imageUrl, engineCc + "cc Engine, " + bikeCategory + ", Helmet Provided");
        this.engineCc = engineCc;
        this.bikeCategory = bikeCategory;
    }

    public int getEngineCc() {
        return engineCc;
    }

    public String getBikeCategory() {
        return bikeCategory;
    }
}
