package com.pluralsight;


import java.math.BigDecimal; // For precise price representation
import java.math.RoundingMode; // For price formatting
import java.time.Year; // For year validation
import java.util.Objects; // For Objects.requireNonNull and Objects.hash

public class Vehicle {
    private final int vin;
    private final int year;
    private final String make;
    private final String model;
    private final String vehicleType;
    private final String color;

    private int odometer;
    private BigDecimal price; // Use BigDecimal for currency

    private static final int MIN_VALID_YEAR = 1886; // First car production year

    /**
     * Constructs a new Vehicle.
     *
     * @param vin         The Vehicle Identification Number.
     * @param year        The manufacturing year. Must be between MIN_VALID_YEAR and current year + 1.
     * @param make        The manufacturer. Must not be null or blank.
     * @param model       The model. Must not be null or blank.
     * @param vehicleType The type of vehicle (e.g., Sedan, SUV). Must not be null or blank.
     * @param color       The color of the vehicle. Must not be null or blank.
     * @param odometer    The current odometer reading. Must not be negative.
     * @param price       The price of the vehicle as a double. Must not be negative.
     * @throws IllegalArgumentException if any of the validation rules are violated.
     */
    public Vehicle(int vin, int year, String make, String model, String vehicleType, String color, int odometer, double price) {
        if (vin <= 0) {
            throw new IllegalArgumentException("VIN must be a positive number.");
        }
        this.vin = vin;

        int currentYear = Year.now().getValue();
        if (year < MIN_VALID_YEAR || year > currentYear + 1) {
            throw new IllegalArgumentException("Year must be between " + MIN_VALID_YEAR + " and " + (currentYear + 1) + ".");
        }
        this.year = year;

        this.make = validateStringField(make, "Make");
        this.model = validateStringField(model, "Model");
        this.vehicleType = validateStringField(vehicleType, "Vehicle Type");
        this.color = validateStringField(color, "Color");

        setOdometer(odometer); // Uses setter for its validation
        setPrice(price);       // Uses setter for its validation (double version)
    }

    private String validateStringField(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null.");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
        return value.trim();
    }

    // --- Getters for immutable fields ---
    public int getVin() { return vin; }
    public int getYear() { return year; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public String getVehicleType() { return vehicleType; }
    public String getColor() { return color; }

    // --- Getters and Setters for mutable fields ---
    public int getOdometer() { return odometer; }

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
     * Sets the price of the vehicle using a BigDecimal.
     * The price must not be null and must not be negative.
     *
     * @param price The new price as BigDecimal.
     * @throws NullPointerException if price is null.
     * @throws IllegalArgumentException if price is negative.
     */
    public void setPrice(BigDecimal price) {
        Objects.requireNonNull(price, "Price cannot be null.");
        // To check if price is negative (price < 0):
        // price.compareTo(BigDecimal.ZERO) returns:
        // -1 if price < BigDecimal.ZERO
        //  0 if price == BigDecimal.ZERO
        //  1 if price > BigDecimal.ZERO
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative. Attempted value: " + price);
        }
        this.price = price.setScale(2, RoundingMode.HALF_UP); // Ensure 2 decimal places for currency
    }

    /**
     * Sets the price of the vehicle using a double.
     * The price must not be negative.
     *
     * @param price The new price as a double.
     * @throws IllegalArgumentException if price is negative.
     */
    public void setPrice(double price) {
        if (price < 0) { // Standard double comparison is fine here
            throw new IllegalArgumentException("Price cannot be negative. Attempted value: " + price);
        }
        // Convert double to BigDecimal. Using String constructor is often preferred for precision,
        // but BigDecimal.valueOf(double) is also common and generally okay.
        this.price = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return vin == vehicle.vin &&
                year == vehicle.year &&
                odometer == vehicle.odometer &&
                Objects.equals(make, vehicle.make) &&
                Objects.equals(model, vehicle.model) &&
                Objects.equals(vehicleType, vehicle.vehicleType) &&
                Objects.equals(color, vehicle.color) &&
                // For BigDecimal, use compareTo for equality check (value equality, ignoring scale differences after setScale)
                (price != null && vehicle.price != null ? price.compareTo(vehicle.price) == 0 : price == vehicle.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vin, year, make, model, vehicleType, color, odometer, price);
    }

    @Override
    public String toString() {
        // When formatting BigDecimal as a float (%.2f), it's good practice to call .doubleValue()
        // or ensure the context handles BigDecimal appropriately. String.format can handle BigDecimal with %f.
        double priceAsDouble = (this.price != null) ? this.price.doubleValue() : 0.0;
        return String.format(
                "Vehicle{VIN=%d, Year=%d, Make='%s', Model='%s', Type='%s', Color='%s', Odometer=%d, Price=%.2f}",
                vin, year, make, model, vehicleType, color, odometer, priceAsDouble
        );
    }
}