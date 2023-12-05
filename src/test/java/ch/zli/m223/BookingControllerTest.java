package ch.zli.m223;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.zli.m223.model.ApplicationUser;
import ch.zli.m223.model.ApplicationUser.UserRole;
import ch.zli.m223.model.Booking;
import io.quarkus.test.h2.H2DatabaseTestResource;

import java.time.LocalDate;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(OrderAnnotation.class)
public class BookingControllerTest {

    @Inject
    EntityManager entityManager;

    @Test
    @Transactional
    @TestSecurity(user = "test@example.com", roles = "User")
    public void testIndexEndpoint() {

        // Test the index endpoint
        given()
                .when().get("/bookings")
                .then()
                .statusCode(200);
    }

    @Test
    @Transactional
    @TestSecurity(user = "test@example.com", roles = "User")
    public void testCreateBooking() {

        // Insert test data
        var user = new ApplicationUser();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setNickname("JohnDoe");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setUserRole(UserRole.USER);

        entityManager.persist(user);

        var booking = new Booking();
        booking.setApplicationUser(user);
        booking.setBookingDate(LocalDate.now());
        booking.setStartTime(LocalTime.now());
        booking.setDurationInHours(2);

        // Test the create endpoint
        given()
                .contentType("application/json")
                .body(booking)
                .when().post("/bookings")
                .then()
                .statusCode(200);
    }

    @Test
    @Transactional
    @TestSecurity(user = "test@example.com", roles = "User")
    public void testUpdateBooking() {
        // Insert test data
        var user = new ApplicationUser();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setNickname("JohnDoe");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setUserRole(UserRole.USER);

        entityManager.persist(user);

        var booking = new Booking();
        booking.setApplicationUser(user);
        booking.setBookingDate(LocalDate.now());
        booking.setStartTime(LocalTime.now());
        booking.setDurationInHours(2);

        entityManager.persist(booking);

        // Test the update endpoint
        given()
                .contentType("application/json")
                .body(booking)
                .when().put("/bookings/{id}", booking.getId())
                .then()
                .statusCode(200);
    }

    @Test
    @Transactional
    @TestSecurity(user = "test@example.com", roles = "User")
    public void testDeleteBooking() {
        // Insert test data
        var user = new ApplicationUser();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setNickname("JohnDoe");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setUserRole(UserRole.USER);

        entityManager.persist(user);

        var booking = new Booking();
        booking.setApplicationUser(user);
        booking.setBookingDate(LocalDate.now());
        booking.setStartTime(LocalTime.now());
        booking.setDurationInHours(2);

        entityManager.persist(booking);

        // Test the delete endpoint
        given()
                .when().delete("/bookings/{id}", booking.getId())
                .then()
                .statusCode(204);
    }
}
