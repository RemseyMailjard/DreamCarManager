package com.pluralsight;

import java.util.ArrayList;
import java.util.List;

public class Dealership {
    private String name;
    private String address;
    private String phone;
    private List<Vehicle> inventory;

    // Constructor
    public Dealership(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.inventory = new ArrayList<>();
    }

    // Getters en Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Vehicle> getAllVehicles() {
        return inventory;
    }

    // Functionaliteit: toevoegen
    public void addVehicle(Vehicle vehicle) {
        inventory.add(vehicle);
    }

    // Functionaliteit: verwijderen
    public void removeVehicle(Vehicle vehicle) {
        inventory.remove(vehicle);
    }

    // Filteren op prijs
    public List<Vehicle> getVehiclesByPrice(double min, double max) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle v : inventory) {
            if (v.getPrice() >= min && v.getPrice() <= max) {
                results.add(v);
            }
        }
        return results;
    }

    // Filteren op make/model
    public List<Vehicle> getVehiclesByMakeModel(String make, String model) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle v : inventory) {
            if (v.getMake().equalsIgnoreCase(make) && v.getModel().equalsIgnoreCase(model)) {
                results.add(v);
            }
        }
        return results;
    }

    // Filteren op bouwjaar
    public List<Vehicle> getVehiclesByYear(int minYear, int maxYear) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle v : inventory) {
            if (v.getYear() >= minYear && v.getYear() <= maxYear) {
                results.add(v);
            }
        }
        return results;
    }

    // Filteren op kleur
    public List<Vehicle> getVehiclesByColor(String color) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle v : inventory) {
            if (v.getColor().equalsIgnoreCase(color)) {
                results.add(v);
            }
        }
        return results;
    }

    // Filteren op kilometerstand
    public List<Vehicle> getVehiclesByMileage(int minMileage, int maxMileage) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle v : inventory) {
            if (v.getOdometer() >= minMileage && v.getOdometer() <= maxMileage) {
                results.add(v);
            }
        }
        return results;
    }

    // Filteren op type voertuig
    public List<Vehicle> getVehiclesByType(String type) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle v : inventory) {
            if (v.getVehicleType().equalsIgnoreCase(type)) {
                results.add(v);
            }
        }
        return results;
    }
}
