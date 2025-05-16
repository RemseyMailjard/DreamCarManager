package com.pluralsight;


import java.math.BigDecimal; // For precise price representation
import java.math.RoundingMode; // For price formatting
import java.time.Year; // For year validation
import java.util.Objects; // For Objects.requireNonNull and Objects.hash

public class Vehicle {
    private final int vin; // Unique Vehicle Identification Number, immutable
    private final int year; // Manufacturing year, immutable
    private final String make; // Manufacturer, immutable
    private final String model; // Model name, immutable
    private final String vehicleType; // e.g., Car, Truck, SUV, immutable
    private final String color; // immutable

    private int odometer; // Can change
    private BigDecimal price; // Can change, use BigDecimal for currency

    private static final int MIN_VALID_YEAR = 1886; // First car production year

    /**
     * Constructs a new Vehicle.
     *
     * @param vin         The Vehicle IdentificationNumber.
     * @param year        The manufacturing year. Must be between MIN_VALID_YEAR and current year + 1.
     * @param make        The manufacturer. Must not be null or blank.
     * @param model       The model. Must not be null or blank.
     * @param vehicleType The type of vehicle (e.g., Sedan, SUV). Must not be null or blank.
     * @param color       The color of the vehicle. Must not be null or blank.
     * @param odometer    The current odometer reading in miles/km. Must not be negative.
     * @param price       The price of the vehicle. Must not be negative.
     * @throws IllegalArgumentException if any of the validation rules are violated.
     */
    public Vehicle(int vin, int year, String make, String model, String vehicleType, String color, int odometer, double price) {
        // VIN is an int here, but in real-world, it's often alphanumeric and String is better.
        // For this exercise, we'll assume positive VIN.
        if (vin <= 0) {
            throw new IllegalArgumentException("VIN must be a positive number.");
        }
        this.vin = vin;

        int currentYear = Year.now().getValue();
        if (year < MIN_VALID_YEAR || year > currentYear + 1) { // Allow for next year's models
            throw new IllegalArgumentException("Year must be between " + MIN_VALID_YEAR + " and " + (currentYear + 1) + ".");
        }
        this.year = year;

        this.make = validateStringField(make, "Make");
        this.model = validateStringField(model, "Model");
        this.vehicleType = validateStringField(vehicleType, "Vehicle Type");
        this.color = validateStringField(color, "Color");

        setOdometer(odometer); // Use setter for validation
        setPrice(new BigDecimal(price)); // Use setter for validation and convert double to BigDecimal
    }

    // Helper for string validation
    private String validateStringField(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null.");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
        return value.trim();
    }

    // --- Getters for immutable fields ---
    public int getVin() {
        return vin;
    }

    public int getYear() {
        return year;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getColor() {
        return color;
    }

    // --- Getters and Setters for mutable fields ---
    public int getOdometer() {
        return odometer;
    }

    /**
     * Sets the odometer reading.
     * @param odometer The new odometer reading. Must not be negative.
     * @throws IllegalArgumentException if odometer is negative.
     */
    public void setOdometer(int odometer) {
        if (odometer < 0) {
            throw new IllegalArgumentException("Odometer reading cannot be negative.");
        }
        this.odometer = odometer;
    }

    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the price of the vehicle.
     * @param price The new price. Must not be null or negative.
     * @throws IllegalArgumentException if price is null or negative.
     */
    public void setPrice(BigDecimal price) {
        Objects.requireNonNull(price, "Price cannot be null.");
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        // Optionally, set a scale for consistent formatting, e.g., 2 decimal places for currency
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    // Overriding equals() and hashCode() is crucial for proper behavior in collections.
    // Typically, VIN should be enough for equality if it's guaranteed unique.
    // For this example, we'll consider all immutable fields for equality.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return vin == vehicle.vin &&
                year == vehicle.year &&
                odometer == vehicle.odometer && // Including mutable for completeness, though often only immutable fields are used
                Objects.equals(make, vehicle.make) &&
                Objects.equals(model, vehicle.model) &&
                Objects.equals(vehicleType, vehicle.vehicleType) &&
                Objects.equals(color, vehicle.color) &&
                (price != null && vehicle.price != null ? price.compareTo(vehicle.price) == 0 : price == vehicle.price); // BigDecimal comparison
    }

    @Override
    public int hashCode() {
        // Use Objects.hash for a convenient way to generate hash codes.
        // Include the same fields as in equals().
        return Objects.hash(vin, year, make, model, vehicleType, color, odometer, price);
    }

    /**
     * Returns a string representation of the vehicle.
     * Example: "Vehicle{VIN=12345, Year=2022, Make='Toyota', Model='Camry', Type='Sedan', Color='Red', Odometer=15000, Price=25000.00}"
     *
     * @return A string representation of the vehicle.
     */
    @Override
    public String toString() {
        return String.format(
                "Vehicle{VIN=%d, Year=%d, Make='%s', Model='%s', Type='%s', Color='%s', Odometer=%d, Price=%.2f}",
                vin, year, make, model, vehicleType, color, odometer, (price != null ? price : BigDecimal.ZERO)
        );
    }

    // --- If you need to update price from a double (e.g., from CSV parsing) ---
    /**
     * Sets the price of the vehicle from a double value.
     * @param price The new price as a double. Must not be negative.
     * @throws IllegalArgumentException if price is negative.
     */
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
    }
}