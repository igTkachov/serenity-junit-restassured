import bindings.Numbers;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import mock.Mocks;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;


@RunWith(SerenityRunner.class)
@TestMethodOrder(OrderAnnotation.class)
public class SortPostIT {

    private static final String REQUEST_BODY = "{ \"numbers\": [ 3, 1, 2 ] }";
    private static final Integer[] EXPECTED_VALUES = {1, 2, 3};
    private static final String HOST = "http://localhost";
    private static final Integer PORT = 8880;
    private static final String PATH = "/api/sort";

    @BeforeClass
    public static void before() {
        Mocks.initialization();
        RestAssured.baseURI = HOST.concat(":").concat(String.valueOf(PORT));
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterClass
    public static void after() {
        Mocks.stop();
    }

    @Test
    @Order(1)
    public void status200ForRequestWithValidBody() {
        // 1st option
        SerenityRest.
                given().body(REQUEST_BODY).
                when().post(PATH).
                then().statusCode(HttpStatus.SC_OK).
                body("numbers", contains(equalTo(1),equalTo(2), equalTo(3)));

        // 2d option
        Numbers responseBody = SerenityRest.
                given().body(REQUEST_BODY).
                when().post(PATH).
                then().statusCode(HttpStatus.SC_OK).
                extract().
                body().
                as(Numbers.of().getClass());

        assertThat(Arrays.deepEquals(EXPECTED_VALUES, responseBody.getNumbers())).
                as("Sequences of numbers are not identical").
                isTrue();
    }

    @Test
    @Order(3)
    public void status200ForRequestWithSortedValues() {
        String requestBody ="{ \"numbers\": [ 1, 2, 3 ] }";
        SerenityRest.
                given().body(requestBody).
                when().post(PATH).
                then().statusCode(HttpStatus.SC_OK).
                body("numbers", contains(equalTo(1),equalTo(2), equalTo(3)));
    }

    @Test
    @Order(2)
    public void status200ForRequestWithOneValue() {
        String requestBody = "{ \"numbers\" :[1] }";
        SerenityRest.
                given().body(requestBody).
                when().post(PATH).
                then().statusCode(HttpStatus.SC_OK).
                body("numbers", contains(equalTo(1)));
    }

    @Test
    @Order(5)
    public void status200ForRequestWithSameValues() {
        String requestBody = "{ \"numbers\" :[0, 0, 0] }";
        SerenityRest.
                given().body(requestBody).
                when().post(PATH).
                then().statusCode(HttpStatus.SC_OK).
                body("numbers", contains(equalTo(0),equalTo(0), equalTo(0)));
    }

    @Test
    @Order(4)
    public void status200ForRequestWithMinusValues() {
        String requestBody = "{ \"numbers\" :[0, -1, 1] }";
        SerenityRest.
                given().body(requestBody).
                when().post(PATH).
                then().statusCode(HttpStatus.SC_OK).
                body("numbers", contains(equalTo(-1),equalTo(0), equalTo(1)));
    }

    @Test
    @Order(7)
    public void status200ForRequestWithMaxIntegerValues() {
        String requestBody = "{ \"numbers\" :[2147483646, 2147483647, -2147483647] }";
        SerenityRest.
                given().body(requestBody).
                when().post(PATH).
                then().statusCode(HttpStatus.SC_OK).
                body("numbers", contains(equalTo(-2147483647),equalTo(2147483646), equalTo(2147483647)));
    }

    @Test
    @Order(6)
    public void status400ForRequestWithEmptyJson() {
        SerenityRest.
                given().body("{}").
                when().post(PATH).
                then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @Order(8)
    public void status404ForRequestWithEmptyBody() {
        SerenityRest.
                given().body("").
                when().post(PATH).
                then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @Order(9)
    public void status404ForRequestWithWrongBodyFields() {
        SerenityRest.
                given().body("{ \"boys\" : \"girls\"}").
                when().post(PATH).
                then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @Order(10)
    public void status404ForRequestWithWrongValueForField() {
        SerenityRest.
                given().body("{ \"numbers\" : null }").
                when().post(PATH).
                then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @Order(11)
    public void status400ForRequestWithEmptyArray() {
        SerenityRest.
                given().body("{ \"numbers\" :[] }").
                when().post(PATH).
                then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @Order(12)
    public void status400ForRequestWithWrongVariablesInArray() {
        SerenityRest.
                given().body("{ \"numbers\" : [A, B, C] }").
                when().post(PATH).
                then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @Order(13)
    public void status404ForRequestWithExtraFields() {
        SerenityRest.
                given().body("{ \"numbers\": [ 3, 1, 2 ], \"numbers\": [ 3, 1, 2 ]}").
                when().post(PATH).
                then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @Order(14)
    public void status404ForRequestWithoutBody() {
        // body("empty") was added for ability to find needed mocked response
        SerenityRest.
                given().body("empty").
                when().post(PATH).
                then().statusCode(HttpStatus.SC_NOT_FOUND).log().all();
    }

    @Test
    @Order(15)
    public void status405ForGetMethod() {
        SerenityRest.
                when().get(PATH).
                then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    @Order(16)
    public void status405ForPutMethod() {
        SerenityRest.
                given().body(REQUEST_BODY).
                when().put(PATH).
                then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    @Order(17)
    public void status405ForDeleteMethod() {
        SerenityRest.
                when().delete(PATH).
                then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }
}
