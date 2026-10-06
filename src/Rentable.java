/**
 * Rentable Interface
 * Defines the contract for all rentable assets in the system.
 */
public interface Rentable {

    /**
     * Calculates total rental cost based on the number of days.
     * @param days Number of rental days
     * @return Calculated cost
     */
    double calculateRentalCost(int days);

    /**
     * Checks if the vehicle/asset is currently available for rent.
     * @return true if available, false if rented or inactive
     */
    boolean isAvailable();

    /**
     * Sets the availability status of the vehicle/asset.
     * @param available new status
     */
    void setAvailable(boolean available);
}