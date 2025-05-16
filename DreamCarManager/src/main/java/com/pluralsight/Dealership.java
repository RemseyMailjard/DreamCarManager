package com.pluralsight;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Dealership {
    // Fields for Dealership properties
    private String name;
    private String address;
    private String phone;

    // The inventory list. Marked final so the list reference itself cannot be changed.
    // Contents of the list can still be modified through methods.
    private final List<Vehicle> inventory;

    /**
     * Constructs a new Dealership.
     *
     * @param name    The name of the dealership. Must not be null or blank.
     * @param address The address of the dealership. Must not be null or blank.
     * @param phone   The phone number of the dealership. Must not be null or blank.
     * @throws IllegalArgumentException if any parameter is null or blank.
     */
    public Dealership(String name, String address, String phone) {
        this.name = validateStringParameter(name, "Dealership name");
        this.address = validateStringParameter(address, "Dealership address");
        this.phone = validateStringParameter(phone, "Dealership phone");
        this.inventory = new ArrayList<>(); // Initialize with an empty, modifiable list
    }

    // --- Helper for validating string parameters ---
    private String validateStringParameter(String value, String paramName) {
        Objects.requireNonNull(value, paramName + " cannot be null.");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(paramName + " cannot be blank.");
        }
        return value.trim();
    }

    // --- Getters and Setters for Dealership properties ---
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = validateStringParameter(name, "Dealership name");
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = validateStringParameter(address, "Dealership address");
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = validateStringParameter(phone, "Dealership phone");
    }

    // --- Inventory Management ---

    /**
     * Returns an unmodifiable view of all vehicles in the inventory.
     * Prevents external modification of the internal list.
     *
     * @return An unmodifiable list of all vehicles.
     */
    public List<Vehicle> getAllVehicles() {
        return Collections.unmodifiableList(inventory);
        // Alternatively, to return a mutable copy:
        // return new ArrayList<>(inventory);
    }

    /**
     * Adds a vehicle to the inventory.
     *
     * @param vehicle The vehicle to add. Must not be null.
     * @throws NullPointerException if the vehicle is null.
     */
    public void addVehicle(Vehicle vehicle) {
        Objects.requireNonNull(vehicle, "Cannot add a null vehicle to inventory.");
        // Optional: Check for duplicates based on VIN if desired
        // if (inventory.stream().anyMatch(v -> v.getVin() == vehicle.getVin())) {
        //     throw new IllegalArgumentException("Vehicle with VIN " + vehicle.getVin() + " already exists in inventory.");
        // }
        inventory.add(vehicle);
    }

    /**
     * Removes a specific vehicle from the inventory.
     * Relies on {@link Vehicle#equals(Object)} for matching.
     *
     * @param vehicle The vehicle to remove. Must not be null.
     * @return true if the vehicle was found and removed, false otherwise.
     * @throws NullPointerException if the vehicle is null.
     */
    public boolean removeVehicle(Vehicle vehicle) {
        Objects.requireNonNull(vehicle, "Cannot remove a null vehicle from inventory.");
        return inventory.remove(vehicle);
    }

    /**
     * Finds a vehicle by its VIN.
     *
     * @param vin The VIN to search for.
     * @return An {@link Optional} containing the vehicle if found, or an empty Optional otherwise.
     */
    public Optional<Vehicle> findVehicleByVin(int vin) {
        return inventory.stream()
                .filter(v -> v.getVin() == vin)
                .findFirst();
    }

    // --- Generic Search Method using Predicates ---

    /**
     * Searches for vehicles matching a given predicate.
     * This is a flexible way to implement various search criteria.
     *
     * @param predicate The condition vehicles must satisfy. Must not be null.
     * @return A new list of vehicles matching the predicate.
     * @throws NullPointerException if the predicate is null.
     */
    public List<Vehicle> searchVehicles(Predicate<Vehicle> predicate) {
        Objects.requireNonNull(predicate, "Search predicate cannot be null.");
        return inventory.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    // --- Specific Search Methods (delegating to the generic search or using streams directly) ---

    /**
     * Searches for vehicles within a given price range.
     *
     * @param minPrice The minimum price (inclusive). Must not be negative.
     * @param maxPrice The maximum price (inclusive). Must not be negative and not less than minPrice.
     * @return A list of vehicles within the price range.
     * @throws IllegalArgumentException if price parameters are invalid.
     */
    public List<Vehicle> getVehiclesByPrice(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Price values cannot be negative.");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price.");
        }

        // Convert double to BigDecimal for accurate comparison with Vehicle's price
        BigDecimal minBdPrice = BigDecimal.valueOf(minPrice);
        BigDecimal maxBdPrice = BigDecimal.valueOf(maxPrice);

        return searchVehicles(v -> {
            BigDecimal vehiclePrice = v.getPrice(); // Vehicle.getPrice() should return BigDecimal
            return vehiclePrice.compareTo(minBdPrice) >= 0 && vehiclePrice.compareTo(maxBdPrice) <= 0;
        });
    }

    /**
     * Searches for vehicles by make and model (case-insensitive).
     *
     * @param make  The make to search for. Must not be null or blank.
     * @param model The model to search for. Must not be null or blank.
     * @return A list of vehicles matching the make and model.
     * @throws IllegalArgumentException if make or model is null or blank.
     */
    public List<Vehicle> getVehiclesByMakeModel(String make, String model) {
        String searchMake = validateStringParameter(make, "Search make");
        String searchModel = validateStringParameter(model, "Search model");
        return searchVehicles(v -> v.getMake().equalsIgnoreCase(searchMake) &&
                v.getModel().equalsIgnoreCase(searchModel));
    }

    /**
     * Searches for vehicles within a given manufacturing year range.
     *
     * @param minYear The minimum year (inclusive).
     * @param maxYear The maximum year (inclusive). Must not be less than minYear.
     * @return A list of vehicles within the year range.
     * @throws IllegalArgumentException if year parameters are invalid.
     */
    public List<Vehicle> getVehiclesByYear(int minYear, int maxYear) {
        if (minYear > maxYear) {
            throw new IllegalArgumentException("Minimum year cannot be greater than maximum year.");
        }
        // Additional validation for year range sensibility could be added (e.g., not before 1886)
        return searchVehicles(v -> v.getYear() >= minYear && v.getYear() <= maxYear);
    }

    /**
     * Searches for vehicles by color (case-insensitive).
     *
     * @param color The color to search for. Must not be null or blank.
     * @return A list of vehicles matching the color.
     * @throws IllegalArgumentException if color is null or blank.
     */
    public List<Vehicle> getVehiclesByColor(String color) {
        String searchColor = validateStringParameter(color, "Search color");
        return searchVehicles(v -> v.getColor().equalsIgnoreCase(searchColor));
    }

    /**
     * Searches for vehicles within a given mileage range.
     *
     * @param minMileage The minimum mileage (inclusive). Must not be negative.
     * @param maxMileage The maximum mileage (inclusive). Must not be negative and not less than minMileage.
     * @return A list of vehicles within the mileage range.
     * @throws IllegalArgumentException if mileage parameters are invalid.
     */
    public List<Vehicle> getVehiclesByMileage(int minMileage, int maxMileage) {
        if (minMileage < 0 || maxMileage < 0) {
            throw new IllegalArgumentException("Mileage values cannot be negative.");
        }
        if (minMileage > maxMileage) {
            throw new IllegalArgumentException("Minimum mileage cannot be greater than maximum mileage.");
        }
        return searchVehicles(v -> v.getOdometer() >= minMileage && v.getOdometer() <= maxMileage);
    }

    /**
     * Searches for vehicles by type (case-insensitive).
     *
     * @param type The vehicle type to search for. Must not be null or blank.
     * @return A list of vehicles matching the type.
     * @throws IllegalArgumentException if type is null or blank.
     */
    public List<Vehicle> getVehiclesByType(String type) {
        String searchType = validateStringParameter(type, "Search type");
        return searchVehicles(v -> v.getVehicleType().equalsIgnoreCase(searchType));
    }


    // --- Optional: Advanced Search with Criteria Object ---
    // This shows a more extensible way to handle complex searches.

    public static class VehicleSearchCriteria {
        // Using Optional for criteria fields allows us to distinguish between
        // "not specified" and "specified as null/empty" (though we'd typically prevent the latter).
        private Optional<String> make = Optional.empty();
        private Optional<String> model = Optional.empty();
        private Optional<String> color = Optional.empty();
        private Optional<String> vehicleType = Optional.empty();
        private Optional<Integer> minYear = Optional.empty();
        private Optional<Integer> maxYear = Optional.empty();
        private Optional<BigDecimal> minPrice = Optional.empty();
        private Optional<BigDecimal> maxPrice = Optional.empty();
        private Optional<Integer> minMileage = Optional.empty();
        private Optional<Integer> maxMileage = Optional.empty();

        // Private constructor for builder pattern
        private VehicleSearchCriteria() {}

        // Getters for criteria
        public Optional<String> getMake() { return make; }
        public Optional<String> getModel() { return model; }
        public Optional<String> getColor() { return color; }
        public Optional<String> getVehicleType() { return vehicleType; }
        public Optional<Integer> getMinYear() { return minYear; }
        public Optional<Integer> getMaxYear() { return maxYear; }
        public Optional<BigDecimal> getMinPrice() { return minPrice; }
        public Optional<BigDecimal> getMaxPrice() { return maxPrice; }
        public Optional<Integer> getMinMileage() { return minMileage; }
        public Optional<Integer> getMaxMileage() { return maxMileage; }


        public static class Builder {
            private VehicleSearchCriteria criteria = new VehicleSearchCriteria();

            public Builder withMake(String make) {
                if (make != null && !make.trim().isEmpty()) criteria.make = Optional.of(make.trim());
                return this;
            }
            public Builder withModel(String model) {
                if (model != null && !model.trim().isEmpty()) criteria.model = Optional.of(model.trim());
                return this;
            }
            public Builder withColor(String color) {
                if (color != null && !color.trim().isEmpty()) criteria.color = Optional.of(color.trim());
                return this;
            }
            public Builder withVehicleType(String type) {
                if (type != null && !type.trim().isEmpty()) criteria.vehicleType = Optional.of(type.trim());
                return this;
            }
            public Builder withMinYear(int year) { criteria.minYear = Optional.of(year); return this; }
            public Builder withMaxYear(int year) { criteria.maxYear = Optional.of(year); return this; }
            public Builder withMinPrice(BigDecimal price) {
                if (price != null && price.compareTo(BigDecimal.ZERO) >= 0) criteria.minPrice = Optional.of(price);
                return this;
            }
            public Builder withMaxPrice(BigDecimal price) {
                if (price != null && price.compareTo(BigDecimal.ZERO) >= 0) criteria.maxPrice = Optional.of(price);
                return this;
            }
            public Builder withMinMileage(int mileage) {
                if (mileage >= 0) criteria.minMileage = Optional.of(mileage);
                return this;
            }
            public Builder withMaxMileage(int mileage) {
                if (mileage >= 0) criteria.maxMileage = Optional.of(mileage);
                return this;
            }
            // Overloads for price as double
            public Builder withMinPrice(double price) { return withMinPrice(BigDecimal.valueOf(price)); }
            public Builder withMaxPrice(double price) { return withMaxPrice(BigDecimal.valueOf(price)); }


            public VehicleSearchCriteria build() {
                // Add validation for ranges (e.g., minPrice <= maxPrice) if needed here
                if (criteria.minPrice.isPresent() && criteria.maxPrice.isPresent() &&
                        criteria.minPrice.get().compareTo(criteria.maxPrice.get()) > 0) {
                    throw new IllegalArgumentException("Min price cannot be greater than max price in criteria.");
                }
                // Similar checks for year and mileage ranges
                if (criteria.minYear.isPresent() && criteria.maxYear.isPresent() &&
                        criteria.minYear.get() > criteria.maxYear.get()) {
                    throw new IllegalArgumentException("Min year cannot be greater than max year in criteria.");
                }
                if (criteria.minMileage.isPresent() && criteria.maxMileage.isPresent() &&
                        criteria.minMileage.get() > criteria.maxMileage.get()) {
                    throw new IllegalArgumentException("Min mileage cannot be greater than max mileage in criteria.");
                }
                return criteria;
            }
        }
    }

    /**
     * Performs a search based on multiple criteria provided by a VehicleSearchCriteria object.
     *
     * @param criteria The search criteria. Must not be null.
     * @return A list of vehicles matching all specified criteria.
     * @throws NullPointerException if criteria is null.
     */
    public List<Vehicle> searchVehicles(VehicleSearchCriteria criteria) {
        Objects.requireNonNull(criteria, "Search criteria cannot be null.");

        return inventory.stream()
                .filter(v -> criteria.getMake().map(make -> v.getMake().equalsIgnoreCase(make)).orElse(true))
                .filter(v -> criteria.getModel().map(model -> v.getModel().equalsIgnoreCase(model)).orElse(true))
                .filter(v -> criteria.getColor().map(color -> v.getColor().equalsIgnoreCase(color)).orElse(true))
                .filter(v -> criteria.getVehicleType().map(type -> v.getVehicleType().equalsIgnoreCase(type)).orElse(true))
                .filter(v -> criteria.getMinYear().map(minY -> v.getYear() >= minY).orElse(true))
                .filter(v -> criteria.getMaxYear().map(maxY -> v.getYear() <= maxY).orElse(true))
                .filter(v -> criteria.getMinPrice().map(minP -> v.getPrice().compareTo(minP) >= 0).orElse(true))
                .filter(v -> criteria.getMaxPrice().map(maxP -> v.getPrice().compareTo(maxP) <= 0).orElse(true))
                .filter(v -> criteria.getMinMileage().map(minM -> v.getOdometer() >= minM).orElse(true))
                .filter(v -> criteria.getMaxMileage().map(maxM -> v.getOdometer() <= maxM).orElse(true))
                .collect(Collectors.toList());
    }


    // --- Standard Object Methods ---

    @Override
    public String toString() {
        return "Dealership{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", inventoryCount=" + inventory.size() +
                '}';
    }

    // equals() and hashCode() could be implemented if Dealership objects
    // need to be compared or used in hash-based collections.
    // Typically, this might be based on a unique dealership ID or name/address combination.
    // For this example, we'll omit them for brevity unless specifically required.
}
