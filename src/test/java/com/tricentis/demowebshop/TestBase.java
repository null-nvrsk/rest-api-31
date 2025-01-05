package com.tricentis.demowebshop;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    String login = "mxlamer@mail.ru";
    String password = "123456";

    @BeforeAll
    public static void setup() {
        Configuration.baseUrl = "https://demowebshop.tricentis.com";
        RestAssured.baseURI = "https://demowebshop.tricentis.com";

        SelenideLogger.addListener("allure", new AllureSelenide());
    }
}
