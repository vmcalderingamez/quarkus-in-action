package org.acme.inventory.grcp;

import io.quarkus.grpc.GrpcService;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.acme.inventory.database.CarInventory;
import org.acme.inventory.model.*;

import java.util.Optional;

@GrpcService
public class GrpcInventoryService implements InventoryService {

    @Inject
    CarInventory inventory;

    @Override
    public Uni<CarResponse> add(InsertCarRequest request) {
        Car car = new Car();
        car.licensePlateNumber = request.getLicensePlateNumber();
        car.manufacturer = request.getManufacturer();
        car.model = request.getModel();
        car.id = CarInventory.ids.incrementAndGet();
        Log.info("Persisting " + car);
        inventory.getCars().add(car);

        return Uni.createFrom().item(CarResponse.newBuilder()
                .setLicensePlateNumber(car.licensePlateNumber)
                .setManufacturer(car.manufacturer)
                .setModel(car.model)
                .setId(car.id)
                .build());
    }

    @Override
    public Uni<CarResponse> remove(RemoveCarRequest request) {
        Optional<Car> optionalCar = inventory.getCars().stream()
                .filter(car -> request.getLicensePlateNumber()
                        .equals(car.licensePlateNumber))
                .findFirst();

        if (optionalCar.isPresent()) {
            Car removedCar = optionalCar.get();
            inventory.getCars().remove(removedCar);
            return Uni.createFrom().item(CarResponse.newBuilder()
                    .setLicensePlateNumber(removedCar.licensePlateNumber)
                    .setManufacturer(removedCar.manufacturer)
                    .setModel(removedCar.model)
                    .setId(removedCar.id)
                    .build());
        }
        return Uni.createFrom().nullItem();
    }

    /*@Override
    public Multi<CarResponse> add(Multi<InsertCarRequest> requests) {
        return requests
                .map(request -> {
                    Car car = new Car();
                    car.licensePlateNumber = request.getLicensePlateNumber();
                    car.manufacturer = request.getManufacturer();
                    car.model = request.getModel();
                    car.id = CarInventory.ids.incrementAndGet();
                    return car;
                }). onItem().invoke(car -> {
                    Log.info("Persisting " + car);
                    inventory.getCars().add(car);
                }).map(car -> CarResponse.newBuilder()
                        .setLicensePlateNumber(car.licensePlateNumber)
                        .setManufacturer(car.manufacturer)
                        .setModel(car.model)
                        .setId(car.id)
                        .build());
    }*/

}
