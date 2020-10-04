package steps;

import bindings.Numbers;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import org.apache.http.HttpStatus;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class SortSteps {
    private Response response;

    private static final String REQUEST_BODY = "{ \"numbers\": [ 3, 1, 2 ] }";
    private static final String RESPONSE_BODY = "{ \"numbers\": [ 1, 2, 3 ] }";
    private static final String HOST = "http://localhost";
    private static final Integer PORT = 8880;
    private static final String PATH = "/api/sort";

    @Step
    public void sendListOfNumbers(String body) {
        response = SerenityRest.
                given().body(body).
                when().post(PATH);
    }

    @Step
    public void sendPostRequestWithoutBody() {
        // body("empty") was added for ability to find needed mocked response
        response = SerenityRest.
                given().body("empty").
                when().post(PATH);
    }

    @Step
    public void sendGetRequest() {
        response = SerenityRest.
                when().get(PATH);
    }

    @Step
    public void sendPutRequest(String body) {
        response = SerenityRest.
                given().body(body).
                when().put(PATH);
    }

    @Step
    public void sendDeleteRequest() {
        response = SerenityRest.
                when().delete(PATH);
    }

    @Step
    public void isExecutedSuccessfulStatus() {
        response.then().statusCode(HttpStatus.SC_OK);
    }

    @Step
    public void isExecutedBadRequestStatus() {
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Step
    public void isExecutedNotFoundStatus() {
        response.then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Step
    public void isExecutedMethodNotAllowedStatus() {
        response.then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Step
    public void verifyBody(Integer[] expectedValues) {
        Numbers actualBody = response.body().as(Numbers.of().getClass());
        assertThat(Arrays.deepEquals(expectedValues, actualBody.getNumbers())).
                as("Sequences of numbers are not identical").
                isTrue();
    }
}
