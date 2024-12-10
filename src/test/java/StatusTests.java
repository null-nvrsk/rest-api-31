import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class StatusTests {
    // 1. запрос на https://selenoid.autotests.cloud/status
    // 2. получить ответ { total: 20, used: 0, queued: 0, pending: 0, browsers: { chrome: { 100.0: { }, ...
    // 3. проверить total = 20


    @Test
    void checkTotal20() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(21));
    }

    @Test
    void checkTotalWithResponseLogs() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .log().all()
                .body("total", is(20));
    }

    @Test
    void checkTotalWithLogs() {
        given()
                .log().all()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().all()
                .body("total", is(20));
    }
}
