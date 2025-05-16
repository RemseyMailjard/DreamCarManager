package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
public class DealershipFileManager {
public Dealership getDealership() {
    Dealership dealership = null;
    try (BufferedReader reader = new BufferedReader(new FileReader("inventory.csv"))) {
        String line;

        // Read the first line: dealership info
        if ((line = reader.readLine()) != null) {
            String[] dealershipInfo = line.split("\\|");
            String name = dealershipInfo[0];
            String address = dealershipInfo[1];
            String phone = dealershipInfo[2];
            dealership = new Dealership(name, address, phone);
        }

        // Read the rest: vehicle lines
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length < 8) continue; // skip invalid lines

            int vin = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);
            String make = parts[2];
            String model = parts[3];
            String type = parts[4];
            String color = parts[5];
            int odometer = Integer.parseInt(parts[6]);
            double price = Double.parseDouble(parts[7]);

            Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, odometer, price);
            dealership.addVehicle(vehicle);
        }

    } catch (IOException e) {
        System.out.println("Error reading inventory file: " + e.getMessage());
    }

    return dealership;
}

    public void saveDealership(Dealership dealership) {
    }
}
