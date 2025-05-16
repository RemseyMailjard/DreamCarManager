package com.pluralsight;


public abstract class Contract {
    private String date;
    private String customerName;
    private String customerEmail;
    private Vehicle vehicleSold;

    public Contract(String date, String customerName, String customerEmail, Vehicle vehicleSold) {
        this.date = date;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.vehicleSold = vehicleSold;
    }

    // Abstract methods to be implemented in subclasses
    public abstract double getTotalPrice();
    public abstract double getMonthlyPayment();

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

    // Optional: String format for displaying
    @Override
    public String toString() {
        return "Contract Date: " + date +
                "\nCustomer: " + customerName +
                "\nEmail: " + customerEmail +
                "\nVehicle: " + vehicleSold;
    }
}
