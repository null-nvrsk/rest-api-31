package com.demoqa.tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class BookCollectionTests extends TestBase {

    @Test
    void addBookToCollectionTest() {
        String authData = "{ \"userName\": \"null\", \"password\": \"!23Edcxzaq\"}";

        Response authResponse = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(authData)
                .when()
                .post("/Account/v1/Login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .queryParams("UserId", authResponse.path("userId"))
            .when()
                .delete("/BookStore/v1/Books")
            .then()
                .log().status()
                .log().body()
                .statusCode(204);

        String isbn = "9781449337711";
        String bookData = "{\"userId\": \"" + authResponse.path("userId") + "\", \"collectionOfIsbns\": [ "
                + "{\"isbn\": \"" + isbn + "\" }] }";

        Response addBookResponse = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(bookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().response();

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.path("userId")));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.path("token")));

        open("/profile");

        String bookTitle = "Designing Evolvable Web APIs with ASP.NET";
        $(".ReactTable").shouldHave(text(bookTitle));
    }

    @Test
    void addBookToCollectionWithDelete1BookTest() {
        String authData = "{ \"userName\": \"null\", \"password\": \"!23Edcxzaq\"}";

        Response authResponse = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(authData)
                .when()
                .post("/Account/v1/Login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        String deleteIsbn = "9781449337711";
        String deleteBookData = "{\"isbn\": \"" + deleteIsbn + "\",\"userId\": \"" + authResponse.path("userId") + "\" }";

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(deleteBookData)
            .when()
                .delete("/BookStore/v1/Book")
            .then()
                .log().status()
                .log().body()
                .statusCode(204);

        String isbn = "9781449337711";
        String bookData = "{\"userId\": \"" + authResponse.path("userId") + "\", \"collectionOfIsbns\": [ "
                + "{\"isbn\": \"" + isbn + "\" }] }";

        Response addBookResponse = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(bookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().response();

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.path("userId")));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.path("token")));

        open("/profile");

        String bookTitle = "Designing Evolvable Web APIs with ASP.NET";
        $(".ReactTable").shouldHave(text(bookTitle));
    }


    @Test
    void negative400addExistBookToCollectionTest() {
        String authData = "{ \"userName\": \"null\", \"password\": \"!23Edcxzaq\"}";

        Response authResponse = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(authData)
                .when()
                .post("/Account/v1/Login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        String isbn = "9781449337711";
        String bookData = "{\"userId\": \"" + authResponse.path("userId") + "\", \"collectionOfIsbns\": [ "
                + "{\"isbn\": \"" + isbn + "\" }] }";

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .header("Authorization", "Bearer " + authResponse.path("token"))
                .body(bookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("code", is("1210"))
                .body("message", is("ISBN already present in the User's Collection!"));
    }

    @Test
    void negative401addBookToCollectionTest() {

        String userId = "2700be58-3d40-4a67-a148-631e781f936e";
        String isbn = "9781449337711";
        String bookData = "{\"userId\": \"" + userId + "\", \"collectionOfIsbns\": [{\"isbn\": \"" + isbn + "\" }]}";

        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(bookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .log().status()
                .log().body()
                .statusCode(401)
                .body("code", is("1200"))
                .body("message", is("User not authorized!"));
    }
}
