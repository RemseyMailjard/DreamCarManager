package com.pluralsight;

import java.io.*;
import java.nio.file.Files;       // NIO for more modern file handling
import java.nio.file.Paths;       // NIO
import java.util.ArrayList;       // Needed if Dealership internally uses a List
import java.util.List;          // Needed if Dealership internally uses a List

public class DealershipFileManager {
    private static final String FILE_NAME = "inventory.csv";
    private static final String DELIMITER_REGEX = "\\|"; // Regex for split
    private static final String DELIMITER_OUTPUT = "|";  // Char for output

    // Indices for dealership header (optional, but makes it clearer)
    private static final int DEALERSHIP_NAME_IDX = 0;
    private static final int DEALERSHIP_ADDRESS_IDX = 1;
    private static final int DEALERSHIP_PHONE_IDX = 2;

    // Indices for vehicle data (optional)
    private static final int VEHICLE_VIN_IDX = 0;
    private static final int VEHICLE_YEAR_IDX = 1;
    private static final int VEHICLE_MAKE_IDX = 2;
    private static final int VEHICLE_MODEL_IDX = 3;
    private static final int VEHICLE_TYPE_IDX = 4;
    private static final int VEHICLE_COLOR_IDX = 5;
    private static final int VEHICLE_ODOMETER_IDX = 6;
    private static final int VEHICLE_PRICE_IDX = 7;

    /**
     * Loads the dealership and vehicles from the CSV file.
     * If the file does not exist, a new, empty dealership with default values is returned.
     * If the dealership information in the file is corrupt, a default dealership is used.
     * Corrupt vehicle lines are skipped with an error message.
     *
     * @return The loaded or a default Dealership. Never null.
     */
    public Dealership getDealership() {
        Dealership dealership = null;

        // Use java.nio.file.Files and Paths for more modern file I/O
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE_NAME))) {
            String line;

            // 1. Read the dealership information (first line)
            if ((line = reader.readLine()) != null) {
                dealership = parseDealershipHeader(line);
            }

            // If dealership info is missing or corrupt in an existing file, create a default.
            if (dealership == null) {
                System.err.println("Dealership information is missing or corrupt in '" + FILE_NAME + "'. Using default dealership.");
                dealership = createDefaultDealership();
            }

            // 2. Read the vehicles (remaining lines)
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines

                Vehicle vehicle = parseVehicleData(line);
                if (vehicle != null) {
                    dealership.addVehicle(vehicle);
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("Inventory file '" + FILE_NAME + "' not found. A new dealership will be created.");
            dealership = createDefaultDealership(); // Create a new, empty dealership
        } catch (IOException e) {
            System.err.println("Error reading inventory file '" + FILE_NAME + "': " + e.getMessage());
            // In case of a serious I/O error, it might be better to return a default dealership
            // or rethrow the exception, depending on application requirements.
            // Here, we choose a default as a fallback.
            if (dealership == null) { // Ensure we always return a dealership
                dealership = createDefaultDealership();
            }
        }

        // Ensure there's always a dealership object, even if everything fails.
        // This check is an extra safeguard; most paths above already create a default.
        if (dealership == null) {
            System.err.println("Unexpected situation: no dealership loaded or created. Creating a last resort default.");
            dealership = createDefaultDealership();
        }

        return dealership;
    }

    private Dealership parseDealershipHeader(String line) {
        String[] fields = line.split(DELIMITER_REGEX);
        if (fields.length >= 3) { // Previously >= 3, now exactly 3 or more to be flexible with extra fields
            String name = fields[DEALERSHIP_NAME_IDX];
            String address = fields[DEALERSHIP_ADDRESS_IDX];
            String phone = fields[DEALERSHIP_PHONE_IDX];
            return new Dealership(name, address, phone);
        }
        System.err.println("Invalid dealership header line: " + line);
        return null;
    }

    private Vehicle parseVehicleData(String line) {
        String[] fields = line.split(DELIMITER_REGEX);
        if (fields.length == 8) { // Exactly 8 fields expected
            try {
                int vin = Integer.parseInt(fields[VEHICLE_VIN_IDX].trim());
                int year = Integer.parseInt(fields[VEHICLE_YEAR_IDX].trim());
                String make = fields[VEHICLE_MAKE_IDX].trim();
                String model = fields[VEHICLE_MODEL_IDX].trim();
                String type = fields[VEHICLE_TYPE_IDX].trim();
                String color = fields[VEHICLE_COLOR_IDX].trim();
                int odometer = Integer.parseInt(fields[VEHICLE_ODOMETER_IDX].trim());
                double price = Double.parseDouble(fields[VEHICLE_PRICE_IDX].trim());

                return new Vehicle(vin, year, make, model, type, color, odometer, price);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format in vehicle line: '" + line + "'. Error: " + e.getMessage());
                return null; // Skip this vehicle
            }
        } else {
            System.err.println("Invalid number of fields for vehicle line: '" + line + "'. Expected 8 fields, found " + fields.length);
            return null; // Skip this vehicle
        }
    }

    private Dealership createDefaultDealership() {
        return new Dealership("Default Dealership", "Unknown Address", "N/A");
    }

    /**
     * Writes the complete dealership (information and vehicles) to the CSV file.
     * Overwrites the existing file.
     *
     * @param dealership The Dealership to be saved. Must not be null.
     */
    public void saveDealership(Dealership dealership) {
        if (dealership == null) {
            System.err.println("Cannot save a null dealership.");
            return;
        }

        // Use java.nio.file.Files and Paths
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_NAME))) {
            // First line: dealership info
            writer.write(String.join(DELIMITER_OUTPUT,
                    dealership.getName(),
                    dealership.getAddress(),
                    dealership.getPhone()));
            writer.newLine();

            // Each vehicle on a new line
            if (dealership.getAllVehicles() != null) {
                for (Vehicle v : dealership.getAllVehicles()) {
                    writer.write(String.join(DELIMITER_OUTPUT,
                            String.valueOf(v.getVin()),
                            String.valueOf(v.getYear()),
                            v.getMake(),
                            v.getModel(),
                            v.getVehicleType(), // Ensure Vehicle.getVehicleType() exists
                            v.getColor(),
                            String.valueOf(v.getOdometer()),
                            String.valueOf(v.getPrice())));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file '" + FILE_NAME + "': " + e.getMessage());
        }
    }
}