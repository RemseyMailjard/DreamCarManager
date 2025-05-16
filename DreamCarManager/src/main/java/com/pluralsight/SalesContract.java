package com.pluralsight;

import java.math.BigDecimal; // <<<< IMPORTANT: BigDecimal import
import java.util.Objects;

public abstract class SalesContract {
    protected String date;
    protected String customerName;
    protected String customerEmail;
    protected Vehicle vehicleSold; // The vehicle associated with this contract

    /**
     * Constructs a base Contract.
     *
     * @param date          The date of the contract. Must not be null or blank.
     * @param customerName  The name of the customer. Must not be null or blank.
     * @param customerEmail The email of the customer. Must not be null or blank.
     * @param vehicleSold   The vehicle involved in the contract. Must not be null.
     * @throws NullPointerException     if any parameter (except potentially customerEmail if allowed) is null.
     * @throws IllegalArgumentException if any string parameter is blank.
     */
    public SalesContract(String date, String customerName, String customerEmail, Vehicle vehicleSold) {
        Objects.requireNonNull(date, "Contract date cannot be null.");
        if (date.trim().isEmpty()) {
            throw new IllegalArgumentException("Contract date cannot be blank.");
        }
        this.date = date.trim();

        Objects.requireNonNull(customerName, "Customer name cannot be null.");
        if (customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be blank.");
        }
        this.customerName = customerName.trim();

        Objects.requireNonNull(customerEmail, "Customer email cannot be null.");
        if (customerEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be blank.");
        }
        this.customerEmail = customerEmail.trim();

        this.vehicleSold = Objects.requireNonNull(vehicleSold, "Vehicle sold cannot be null for a contract.");
    }

    // Getters
    public String getDate() {
        return date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public Vehicle getVehicleSold() {
        return vehicleSold;
    }

    /**
     * Calculates the total price associated with this contract.
     * This typically includes the vehicle's price plus any contract-specific fees or taxes.
     *
     * @return The total price as a BigDecimal, appropriately scaled.
     */
    public abstract BigDecimal getTotalPrice(); // <<<< CORRECTED: Returns BigDecimal

    /**
     * Calculates the monthly payment for this contract, if applicable.
     * For contracts without financing, this might return BigDecimal.ZERO.
     *
     * @return The monthly payment as a BigDecimal, appropriately scaled, or BigDecimal.ZERO.
     */
    public abstract BigDecimal getMonthlyPayment(); // <<<< CORRECTED: Returns BigDecimal

    public boolean isFinanced() {
        return true;
    }
}

