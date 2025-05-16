package com.pluralsight;

public class LeaseContract extends Contract {
    private final double leaseFeeRate = 0.07;
    private final double residualRate = 0.50;
    private final double interestRate = 0.04;
    private final int leaseTermMonths = 36;

    public LeaseContract(String date, String customerName, String customerEmail, Vehicle vehicleSold) {
        super(date, customerName, customerEmail, vehicleSold);
    }

    @Override
    public double getTotalPrice() {
        double price = getVehicleSold().getPrice();
        double residual = price * residualRate;
        double leaseFee = price * leaseFeeRate;
        return residual + leaseFee;
    }

    @Override
    public double getMonthlyPayment() {
        double totalLeaseAmount = getTotalPrice();
        double monthlyRate = interestRate / 12.0;

        return (totalLeaseAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -leaseTermMonths));
    }

    public double getExpectedEndingValue() {
        return getVehicleSold().getPrice() * residualRate;
    }

    public double getLeaseFee() {
        return getVehicleSold().getPrice() * leaseFeeRate;
    }
}
