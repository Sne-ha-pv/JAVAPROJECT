public class Vehicle implements Rentable {

    private int vehicleId;
    private String type;
    private String brand;
    private String model;
    private double rentalRate;
    private boolean available;

    public Vehicle(int vehicleId, String type, String brand,
                   String model, double rentalRate) {

        this.vehicleId = vehicleId;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.rentalRate = rentalRate;
        this.available = true;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getRentalRate() {
        return rentalRate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setRentalRate(double rentalRate) {
        this.rentalRate = rentalRate;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public double calculateRentalCost(int days) {
        return rentalRate * days;
    }

    @Override
    public String toString() {

        return "ID: " + vehicleId +
                " | Type: " + type +
                " | Brand: " + brand +
                " | Model: " + model +
                " | Rate/Day: Rs." + rentalRate +
                " | Available: " + available;
    }
}