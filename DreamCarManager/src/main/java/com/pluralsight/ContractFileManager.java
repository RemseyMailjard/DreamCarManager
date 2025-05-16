package com.pluralsight;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ContractFileManager {

    private static final String FILE_NAME = "contracts.csv";

    public void saveContract(Contract contract) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            StringBuilder sb = new StringBuilder();

            if (contract instanceof SalesContract sc) {
                sb.append("SALE|");
                sb.append(sc.getDate()).append("|");
                sb.append(sc.getCustomerName()).append("|");
                sb.append(sc.getCustomerEmail()).append("|");
                sb.append(formatVehicle(sc.getVehicleSold())).append("|");
                sb.append(String.format("%.2f", sc.getSalesTaxAmount())).append("|");
                sb.append(String.format("%.2f", sc.getRecordingFee())).append("|");
                sb.append(String.format("%.2f", sc.getProcessingFee())).append("|");
                sb.append(String.format("%.2f", sc.getTotalPrice())).append("|");
                sb.append(sc.isFinanced() ? "YES" : "NO").append("|");
                sb.append(String.format("%.2f", sc.getMonthlyPayment()));

            } else if (contract instanceof LeaseContract lc) {
                sb.append("LEASE|");
                sb.append(lc.getDate()).append("|");
                sb.append(lc.getCustomerName()).append("|");
                sb.append(lc.getCustomerEmail()).append("|");
                sb.append(formatVehicle(lc.getVehicleSold())).append("|");
                sb.append(String.format("%.2f", lc.getExpectedEndingValue())).append("|");
                sb.append(String.format("%.2f", lc.getLeaseFee())).append("|");
                sb.append(String.format("%.2f", lc.getTotalPrice())).append("|");
                sb.append(String.format("%.2f", lc.getMonthlyPayment()));
            }

            writer.write(sb.toString());
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Error writing contract: " + e.getMessage());
        }
    }

    private String formatVehicle(Vehicle v) {
        return v.getVin() + "|" +
                v.getYear() + "|" +
                v.getMake() + "|" +
                v.getModel() + "|" +
                v.getVehicleType() + "|" +
                v.getColor() + "|" +
                v.getOdometer() + "|" +
                String.format("%.2f", v.getPrice());
    }
}
