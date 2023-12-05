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
import io.quarkus.test.h2.H2DatabaseTestResource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestMethodOrder(OrderAnnotation.class)
public class ApplicationUserControllerTest {

    @Inject
    EntityManager entityManager;

    @Test
    @Transactional
    @TestSecurity(user = "test@example.com", roles = "Admin")
    public void testIndexEndpoint() {

        // Test the index endpoint
        given()
                .when().get("/users")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @Transactional
    @TestSecurity(user = "test@example.com", roles = "Admin")
    public void testCreateUser() {

        // Insert test data
        var userVisitor = new ApplicationUser();
        userVisitor.setEmail("test@example.com1");
        userVisitor.setPassword("password1231");
        userVisitor.setNickname("Visitor");
        userVisitor.setLastName("Doe");
        userVisitor.setFirstName("John");
        userVisitor.setUserRole(UserRole.ADMIN);

        // Test the create endpoint
        given()
                .contentType("application/json")
                .body(userVisitor)
                .when().post("/users")
                .then()
                .statusCode(200)
                .body("email", is("test@example.com1"));
    }

    @Test
    @Transactional
    @TestSecurity(user = "test@example.com", roles = "Admin")
    public void testUpdateUser() {
        // Insert test data
        var userVisitor = new ApplicationUser();
        userVisitor.setEmail("updateduser@example.com");
        userVisitor.setPassword("password123");
        userVisitor.setNickname("Visitor");
        userVisitor.setLastName("Doe");
        userVisitor.setFirstName("John");
        userVisitor.setUserRole(UserRole.ADMIN);

        // Test the update endpoint
        given()
                .contentType("application/json")
                .body(userVisitor)
                .when().put("/users/{id}", 1)
                .then()
                .statusCode(200)
                .body("email", is("updateduser@example.com"));
    }

    @Test
    @Transactional
    @TestSecurity(user = "test@example.com", roles = "Admin")
    public void testDeleteUser() {

        // Test the delete endpoint
        given()
                .when().delete("/users/{id}", 1)
                .then()
                .statusCode(204);

        given()
                .when().get("/users")
                .then()
                .statusCode(200)
                .body("size()", is(0));
    }
}
