package com.pluralsight;

public class SalesContract extends Contract {
    private final double salesTaxRate = 0.05;
    private final double recordingFee = 100.00;
    private final double processingFee;
    private boolean isFinanced;

    public SalesContract(String date, String customerName, String customerEmail, Vehicle vehicleSold, boolean isFinanced) {
        super(date, customerName, customerEmail, vehicleSold);
        this.isFinanced = isFinanced;

        // Set processing fee based on vehicle price
        if (vehicleSold.getPrice() >= 10000) {
            this.processingFee = 495.00;
        } else {
            this.processingFee = 295.00;
        }
    }

    @Override
    public double getTotalPrice() {
        double price = getVehicleSold().getPrice();
        double tax = price * salesTaxRate;
        return price + tax + recordingFee + processingFee;
    }

    @Override
    public double getMonthlyPayment() {
        if (!isFinanced) {
            return 0.0;
        }

        double loanAmount = getTotalPrice();
        double interestRate;
        int termMonths;

        if (getVehicleSold().getPrice() >= 10000) {
            interestRate = 0.0425;
            termMonths = 48;
        } else {
            interestRate = 0.0525;
            termMonths = 24;
        }

        double monthlyRate = interestRate / 12.0;
        return (loanAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -termMonths));
    }

    public boolean isFinanced() {
        return isFinanced;
    }

    public double getSalesTaxAmount() {
        return getVehicleSold().getPrice() * salesTaxRate;
    }

    public double getRecordingFee() {
        return recordingFee;
    }

    public double getProcessingFee() {
        return processingFee;
    }
}
