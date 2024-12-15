package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import models.lombok.LoginBodyLombokModel;
import models.lombok.LoginResponseLombokModel;
import models.lombok.LoginWithoutPasswordBodyLombokModel;
import models.lombok.MissingPasswordResponseModel;
import models.pojo.LoginBodyPojoModel;
import models.pojo.LoginResponsePojoModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static specs.LoginSpec.*;

public class LoginExtendedTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    // 1. POST https://reqres.in/api/login
    // body = { "email": "eve.holt@reqres.in", "password": "cityslicka" }
    // 2. check response 200
    // body = { "token": "QpwL5tke4Pnpja7X4" }
    @Test
    void successLoginBadPracticeTest() {
        String authData = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";

        given()
            .log().uri()
            .log().headers()
            .log().body()
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
    void successLoginWithPojoModelTest() {
        LoginBodyPojoModel loginBody = new LoginBodyPojoModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponsePojoModel loginResponse = given()
                .log().uri()
                .log().headers()
                .log().body()
                .contentType(JSON)
                .body(loginBody)
            .when()
                .post("https://reqres.in/api/login")
            .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponsePojoModel.class);

        // JUnit5 assert
        // assertEquals("QpwL5tke4Pnpja7X4", loginResponse.getToken());

        // // AssertJ assert
        assertThat(loginResponse.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    public void loginWithLombokModelSuccessTest() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponseLombokModel loginResponse = given()
                .log().uri()
                .log().headers()
                .log().body()
                .contentType(JSON)
                .body(loginBody)
            .when()
                .post("https://reqres.in/api/login")
            .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseLombokModel.class);

        // JUnit5 assert
        // assertEquals("QpwL5tke4Pnpja7X4", loginResponse.getToken());

        // // AssertJ assert
        assertThat(loginResponse.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    public void loginWithAllureSuccessTest() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponseLombokModel loginResponse = given()
                .filter(new AllureRestAssured())
                .log().uri()
                .log().headers()
                .log().body()
                .contentType(JSON)
                .body(loginBody)
            .when()
                .post("https://reqres.in/api/login")
            .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseLombokModel.class);

        assertThat(loginResponse.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    public void loginWithCustomAllureSuccessTest() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponseLombokModel loginResponse = given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().headers()
                .log().body()
                .contentType(JSON)
                .body(loginBody)
            .when()
                .post("https://reqres.in/api/login")
            .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseLombokModel.class);

        assertThat(loginResponse.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    public void loginWithAllureStepsSuccessTest() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponseLombokModel loginResponse =
                step("Get authorization data", () -> given()
                        .filter(withCustomTemplates())
                        .log().uri()
                        .log().headers()
                        .log().body()
                        .contentType(JSON)
                        .body(loginBody)
                    .when()
                        .post("https://reqres.in/api/login")
                    .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(LoginResponseLombokModel.class));

        step("Verify authorization response", () ->
            assertThat(loginResponse.getToken()).isEqualTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void loginWithSpecsSuccessTest() {
        LoginBodyLombokModel loginBody = new LoginBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");
        loginBody.setPassword("cityslicka");

        LoginResponseLombokModel loginResponse =
                step("Get authorization data", () ->
                    given(loginRequestSpec)
                        .body(loginBody)
                    .when()
                        .post("/login")
                    .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseLombokModel.class));

        step("Verify authorization response", () -> {
            assertThat(loginResponse.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
        });
    }

    @Test
    public void missingPasswordTest() {
        LoginWithoutPasswordBodyLombokModel loginBody = new LoginWithoutPasswordBodyLombokModel();
        loginBody.setEmail("eve.holt@reqres.in");

        MissingPasswordResponseModel loginResponse =
                step("Get authorization data", () ->
                    given(loginRequestSpec)
                        .body(loginBody)
                    .when()
                        .post("/login")
                    .then()
                        .spec(missingPasswordResponseSpec)
                        .extract().as(MissingPasswordResponseModel.class));

        step("Verify authorization response", () -> {
            assertThat(loginResponse.getError()).isEqualTo("Missing password");
        });
    }
}
