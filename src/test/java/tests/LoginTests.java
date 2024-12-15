package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class LoginTests {

    // 1. POST https://reqres.in/api/login
    // body = { "email": "eve.holt@reqres.in", "password": "cityslicka" }
    // 2. check response 200
    // body = { "token": "QpwL5tke4Pnpja7X4" }
    @Test
    void successLoginTest() {
        String authData = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(authData)
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void noContentTypeLoginTest() {
        String authData = "";

        given()
                .log().uri()
                .body(authData)
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }

    @Test
    void invalidPasswordLoginTest() {
        String authData = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"iNvAliDpAsSwOrD\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(authData)
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }

    @Test
    void withoutPasswordLoginTest() {
        String authData = "{ \"email\": \"eve.holt@reqres.in\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(authData)
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void withoutEmailLoginTest() {
        String authData = "{ \"password\": \"cityslicka\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(authData)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    void invalidUserLoginTest() {
        String authData = "{ \"email\": \"bad.user@reqres.in\", \"password\": \"cityslicka\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(authData)
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("user not found"));
    }

    @Test
    void emptyBodyLoginTest() {
        String authData = "{ }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(authData)
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }

    @Test
    void wrongBodyLoginTest() {
        String authData = "& }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(authData)
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }
}
