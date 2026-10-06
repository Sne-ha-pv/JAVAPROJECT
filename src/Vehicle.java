/**
 * Vehicle Class
 * Base entity representing a rentable vehicle.
 * Implements Rentable interface and demonstrates Encapsulation and Polymorphism.
 */
public class Vehicle implements Rentable {

    private int vehicleId;
    private String type;         // "Car", "Bike", "Van"
    private String brand;
    private String model;
    private double rentalRate;   // Daily rate in Rs.
    private boolean available;
    private String imageUrl;     // Image/thumbnail URL for UI
    private String features;     // Feature highlights (e.g., "Automatic, 5 Seats, AC")

    public Vehicle(int vehicleId, String type, String brand, String model, double rentalRate) {
        this(vehicleId, type, brand, model, rentalRate, "", "");
    }

    public Vehicle(int vehicleId, String type, String brand, String model, double rentalRate, String imageUrl, String features) {
        this.vehicleId = vehicleId;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.rentalRate = rentalRate;
        this.available = true;
        this.imageUrl = (imageUrl == null || imageUrl.trim().isEmpty()) ? getDefaultImage(type) : imageUrl;
        this.features = (features == null || features.trim().isEmpty()) ? getDefaultFeatures(type) : features;
    }

    private static String getDefaultImage(String type) {
        if (type == null) return "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?w=600&auto=format&fit=crop&q=80";
        switch (type.toLowerCase()) {
            case "bike":
                return "https://images.unsplash.com/photo-1558981806-ec527fa84c39?w=600&auto=format&fit=crop&q=80";
            case "van":
                return "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=600&auto=format&fit=crop&q=80";
            case "car":
            default:
                return "https://images.unsplash.com/photo-1550355291-bbee04a92027?w=600&auto=format&fit=crop&q=80";
        }
    }

    private static String getDefaultFeatures(String type) {
        if (type == null) return "Standard Transmission, Air Conditioning";
        switch (type.toLowerCase()) {
            case "bike":
                return "ABS, Digital Speedometer, Helmet Included";
            case "van":
                return "8-Seater, Extra Luggage Space, Diesel, AC";
            case "car":
            default:
                return "Automatic, 5-Seater, Bluetooth, Air Conditioning";
        }
    }

    // Getters and Setters
    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(double rentalRate) {
        this.rentalRate = rentalRate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    // Interface Implementations
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
        int effectiveDays = Math.max(1, days);
        return rentalRate * effectiveDays;
    }

    /**
     * Converts vehicle object to JSON string representation
     */
    public String toJson() {
        return "{" +
                "\"vehicleId\":" + vehicleId + "," +
                "\"type\":\"" + JsonUtils.escape(type) + "\"," +
                "\"brand\":\"" + JsonUtils.escape(brand) + "\"," +
                "\"model\":\"" + JsonUtils.escape(model) + "\"," +
                "\"rentalRate\":" + rentalRate + "," +
                "\"available\":" + available + "," +
                "\"imageUrl\":\"" + JsonUtils.escape(imageUrl) + "\"," +
                "\"features\":\"" + JsonUtils.escape(features) + "\"" +
                "}";
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