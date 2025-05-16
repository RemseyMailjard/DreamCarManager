package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.*;


public class DealershipFileManager {
    private static final String FILE_NAME = "inventory.csv";

    // Laadt de dealership + voertuigen uit bestand
    public Dealership getDealership() {
        Dealership dealership = null;

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_NAME);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;

            if ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|");
                if (fields.length >= 3) {
                    String name = fields[0];
                    String address = fields[1];
                    String phone = fields[2];
                    dealership = new Dealership(name, address, phone);
                }
            }

            if (dealership == null) {
                System.out.println("Missing or invalid dealership info. Creating default.");
                dealership = new Dealership("Unknown", "Unknown", "N/A");
            }

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|");
                if (fields.length == 8) {
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
                } else {
                    System.out.println("Invalid vehicle line: " + line);
                }
            }

        } catch (IOException | NullPointerException e) {
            System.out.println("Error reading from resource file: " + e.getMessage());
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