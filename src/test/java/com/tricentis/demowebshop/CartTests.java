package com.tricentis.demowebshop;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class CartTests extends TestBase {

    @Test
    void addToCartAsAuthorizedUserTest() {
        // TODO: move to API auth class
        String authCookieKey = "NOPCOMMERCE.AUTH";
        String authCookieValue = given()
            .contentType("application/x-www-form-urlencoded")
            .formParam("Email", login)
            .formParam("Password", password)
        .when()
            .post("/login")
        .then()
            .log().all()
            .statusCode(302)
            .extract()
            .cookie(authCookieKey);

        // TODO: get actual cart size

        String bodyData =
                "product_attribute_72_5_18=52&" +
                "product_attribute_72_6_19=54&" +
                "product_attribute_72_3_20=58&" +
                "product_attribute_72_8_30=93&" +
                "product_attribute_72_8_30=94&" +
                "addtocart_72.EnteredQuantity=2";

        given()
            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
            .cookie(authCookieKey, authCookieValue)
            .body(bodyData)
        .when()
            .post("/addproducttocart/details/72/1")
        .then()
            .log().all()
            .statusCode(200)
            .body("success", is(true))
            .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"));

        // TODO: check new cart size
    }

    @Test
    void addToCartAsGuestTest() {
        String bodyData =
                "product_attribute_72_5_18=52&" +
                "product_attribute_72_6_19=54&" +
                "product_attribute_72_3_20=58&" +
                "product_attribute_72_8_30=93&" +
                "product_attribute_72_8_30=94&" +
                "addtocart_72.EnteredQuantity=2";

        given()
            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
            .body(bodyData)
        .when()
            .post("/addproducttocart/details/72/1")
        .then()
            .log().all()
            .statusCode(200)
            .body("success", is(true))
            .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
            .body("updatetopcartsectionhtml", is("(2)"));
    }
}
