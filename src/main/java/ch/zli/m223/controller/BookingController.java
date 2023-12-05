package ch.zli.m223.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.model.Booking;
import ch.zli.m223.service.ApplicationUserService;
import ch.zli.m223.service.BookingService;

@Path("/bookings")
@Tag(name = "Bookings", description = "Handling of bookings")
@RolesAllowed({ "User", "Admin" })
public class BookingController {

    @Inject
    BookingService bookingService;

    @Inject
    ApplicationUserService applicationUserService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Index all bookings.", description = "Returns a list of all bookings.")
    public List<Booking> index() {
        return bookingService.findAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Creates a new booking.", description = "Creates a new booking and returns the newly added booking.")
    public Booking create(@Valid Booking booking) {
        booking.setStatus(Booking.Status.PENDING);

        String email = booking.getApplicationUser().getEmail();

        ApplicationUser user = applicationUserService.findByEmail(email).orElse(null);

        if (user != null) {
            booking.setApplicationUser(user);
            return bookingService.createBooking(booking);
        } else {
            // Handle the case when the user is not found
            // You might want to throw an exception or handle it based on your requirements
            return null;
        }
    }

    @Path("/{id}")
    @GET
    @Operation(summary = "Get a booking by its id.", description = "Returns a booking by its id.")
    public Booking getBookingById(@PathParam("id") Long id) {
        return bookingService.findBookingById(id);
    }

    @Path("/{id}")
    @PUT
    @Operation(summary = "Updates a booking by its id.", description = "Updates a booking by its id and returns the updated booking.")
    public Booking updateBooking(@PathParam("id") Long id, @Valid Booking booking) {
        return bookingService.updateBooking(id, booking);
    }

    @Path("/{id}")
    @DELETE
    @Operation(summary = "Deletes a booking by its id.", description = "Deletes a booking by its id.")
    public void deleteBooking(@PathParam("id") Long id) {
        bookingService.deleteBooking(id);
    }
}
