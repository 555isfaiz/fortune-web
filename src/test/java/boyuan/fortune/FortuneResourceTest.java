package boyuan.fortune;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class FortuneResourceTest {

    @Test
    void fortuneReturnsMessage() {
        given()
                .when().get("/api/fortune")
                .then()
                .statusCode(200)
                .body("message", not(emptyString()));
    }

    @Test
    void addFortunePersists() {
        long before = count();

        given()
                .contentType(ContentType.JSON)
                .body("{\"message\":\"May your tests pass\",\"author\":\"CI\"}")
                .when().post("/api/fortune")
                .then()
                .statusCode(204);

        assertEquals(before + 1, count());
    }

    @Test
    void addFortuneWithoutAuthor() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"message\":\"Anonymous wisdom\"}")
                .when().post("/api/fortune")
                .then()
                .statusCode(204);
    }

    @Test
    void addNullBodyIsRejected() {
        given()
                .contentType(ContentType.JSON)
                .when().post("/api/fortune")
                .then()
                .statusCode(400);
    }

    @Test
    void addBlankMessageIsRejected() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"author\":\"CI\"}")
                .when().post("/api/fortune")
                .then()
                .statusCode(400);
    }

    private long count() {
        return QuarkusTransaction.requiringNew().call(FortuneEntity::count);
    }
}
