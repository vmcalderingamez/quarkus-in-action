package org.acme.rental;

import io.quarkus.logging.Log;
import jakarta.ws.rs.*;
import org.acme.rental.entity.Rental;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("rental")
public class RentalResource {

    private final AtomicLong id = new AtomicLong(0);

    @Path("start/{userId}/{reservationId}")
    @POST
    public Rental start(String userId, Long reservationId){
        Log.infof("Starting a rental for %s with reservation %s", userId, reservationId);
        Rental rental = new Rental();

        rental.userId = userId;
        rental.reservationId = reservationId;
        rental.startDate = LocalDate.now();
        rental.active = true;
        rental.persist();

        return rental;
    }

    @PUT
    @Path("/end/{userId}/{reservationId}")
    public Rental end(String userId, Long reservationId) {
        Log.infof("Ending rental for %s with reservation %s",
                userId, reservationId);
        Optional<Rental> optionalRental = Rental
                .findByUserAndReservationIdsOptional(userId, reservationId);
        if (optionalRental.isPresent()) {
            Rental rental = optionalRental.get();
            rental.endDate = LocalDate.now();
            rental.active = false;
            rental.update();
            return rental;
        } else {
            throw new NotFoundException("Rental not found");
        }
    }

    @GET
    public List<Rental> list() {
        return Rental.listAll();
    }

    @GET
    @Path("/active")
    public List<Rental> listActive() {
        return Rental.listActive();
    }

}
