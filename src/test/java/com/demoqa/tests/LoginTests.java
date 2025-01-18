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

public class LoginTests extends TestBase {

    @Test
    void successLoginWithUiTest() {
        open("/login");
        $("#userName").setValue(TestData.login);
        $("#password").setValue(TestData.password).pressEnter();

        $("#userName-value").shouldHave(text(TestData.login));
    }

    @Test
    void successLoginWithApiTest() {
        String authData = "{ \"userName\": \"null\", \"password\": \"!23Edcxzaq\"}";

        Response authResponse = given()
            .log().uri()
            .contentType(JSON)
            .body(authData)
        .when()
            .post("/Account/v1/Login")
        .then()
            .log().status()
            .log().body()
            .statusCode(200)
            .extract().response();

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", authResponse.path("userId")));
        getWebDriver().manage().addCookie(new Cookie("expires", authResponse.path("expires")));
        getWebDriver().manage().addCookie(new Cookie("token", authResponse.path("token")));

        open("/profile");

        $("#userName-value").shouldHave(text(TestData.login));
    }
}
