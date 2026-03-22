package org.acme.reservation.inventory;

public class Car {
    public Long id;
    public String licensePlateNumber;
    public String manufacturer;
    public String model;

    public Car() {
    }

    public Car(Long id, String licencePlateNumber, String manufacturer, String model) {
        this.id = id;
        this.licensePlateNumber = licencePlateNumber;
        this.manufacturer = manufacturer;
        this.model = model;
    }
}