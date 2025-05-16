package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DealershipFileManager {
    private static final String FILE_NAME = "inventory.csv";

    // Laadt de dealership + voertuigen uit bestand
    public Dealership getDealership() {
        Dealership dealership = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            // Eerste regel: dealership info
            if ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|");
                String name = fields[0];
                String address = fields[1];
                String phone = fields[2];

                dealership = new Dealership(name, address, phone);
            }

            // Volgende regels: voertuigen
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|");

                int vin = Integer.parseInt(fields[0]);
                int year = Integer.parseInt(fields[1]);
                String make = fields[2];
                String model = fields[3];
                String type = fields[4];
                String color = fields[5];
                int odometer = Integer.parseInt(fields[6]);
                double price = Double.parseDouble(fields[7]);

                Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, odometer, price);
                dealership.addVehicle(vehicle);
            }

        } catch (IOException e) {
            System.out.println("Fout bij lezen van bestand: " + e.getMessage());
        }

        return dealership;
    }

    // Schrijft de volledige dealership + voertuigen terug naar bestand
    public void saveDealership(Dealership dealership) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            // Eerste regel: dealership info
            writer.write(dealership.getName() + "|" + dealership.getAddress() + "|" + dealership.getPhone());
            writer.newLine();

            // Elke voertuig op nieuwe regel
            for (Vehicle v : dealership.getAllVehicles()) {
                String line = v.getVin() + "|" + v.getYear() + "|" + v.getMake() + "|" + v.getModel() + "|" +
                        v.getVehicleType() + "|" + v.getColor() + "|" + v.getOdometer() + "|" + v.getPrice();
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Fout bij schrijven naar bestand: " + e.getMessage());
        }
    }
}