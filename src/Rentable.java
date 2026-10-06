public interface Rentable {

    double calculateRentalCost(int days);

    boolean isAvailable();

    void setAvailable(boolean available);
}