import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import mock.Mocks;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import steps.SortSteps;

@RunWith(SerenityRunner.class)
@TestMethodOrder(OrderAnnotation.class)
public class SortPostSerenityIT {

    private static final String REQUEST_BODY = "{ \"numbers\": [ 3, 1, 2 ] }";
    private static final Integer[] EXPECTED_VALUES = {1, 2, 3};
    private static final String HOST = "http://localhost";
    private static final Integer PORT = 8880;

    @Steps
    SortSteps steps;

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
        steps.sendListOfNumbers(REQUEST_BODY);
        steps.isExecutedSuccessfulStatus();
        steps.verifyBody(EXPECTED_VALUES);
    }

    @Test
    @Order(3)
    public void status200ForRequestWithSortedValues() {
        String requestBody ="{ \"numbers\": [ 1, 2, 3 ] }";

        steps.sendListOfNumbers(requestBody);
        steps.isExecutedSuccessfulStatus();
        steps.verifyBody(EXPECTED_VALUES);
    }

    @Test
    @Order(2)
    public void status200ForRequestWithOneValue() {
        String requestBody = "{ \"numbers\" :[1] }";
        Integer[] expectedValues = {1};

        steps.sendListOfNumbers(requestBody);
        steps.isExecutedSuccessfulStatus();
        steps.verifyBody(expectedValues);
    }
    @Test
    @Order(5)
    public void status200ForRequestWithSameValues() {
        String requestBody = "{ \"numbers\" :[0, 0, 0] }";
        Integer[] expectedValues = {0, 0, 0};

        steps.sendListOfNumbers(requestBody);
        steps.isExecutedSuccessfulStatus();
        steps.verifyBody(expectedValues);
    }

    @Test
    @Order(4)
    public void status200ForRequestWithMinusValues() {
        String requestBody = "{ \"numbers\" :[0, -1, 1] }";
        Integer[] expectedValues = {-1, 0, 1};

        steps.sendListOfNumbers(requestBody);
        steps.isExecutedSuccessfulStatus();
        steps.verifyBody(expectedValues);
    }

    @Test
    @Order(7)
    public void status200ForRequestWithMaxIntegerValues() {
        String requestBody = "{ \"numbers\" :[2147483646, 2147483647, -2147483647] }";
        Integer[] expectedValues = {-2147483647, 2147483646, 2147483647};

        steps.sendListOfNumbers(requestBody);
        steps.isExecutedSuccessfulStatus();
        steps.verifyBody(expectedValues);
    }

    @Test
    @Order(6)
    public void status400ForRequestWithEmptyJson() {
        String requestBody = "{}";

        steps.sendListOfNumbers(requestBody);
        steps.isExecutedBadRequestStatus();
    }

    @Test
    @Order(8)
    public void status404ForRequestWithEmptyBody() {
        String requestBody = "";

        steps.sendListOfNumbers(requestBody);
        steps.isExecutedNotFoundStatus();
    }

    @Test
    @Order(9)
    public void status404ForRequestWithWrongBodyFields() {
        String requestBody = "{ \"boys\" : \"girls\"}";

        steps.sendListOfNumbers(requestBody);
        steps.isExecutedNotFoundStatus();
    }

    @Test
    @Order(10)
    public void status404ForRequestWithWrongValueForField() {
        String requestBody = "{ \"numbers\" : null }";

        steps.sendListOfNumbers(requestBody);
        steps.isExecutedNotFoundStatus();
    }

    @Test
    @Order(11)
    public void status400ForRequestWithEmptyArray() {
        String requestBody = "{ \"numbers\" :[] }";

        steps.sendListOfNumbers(requestBody);
        steps.isExecutedBadRequestStatus();
    }

    @Test
    @Order(12)
    public void status400ForRequestWithWrongVariablesInArray() {
        String requestBody = "{ \"numbers\" : [A, B, C] }";

        steps.sendListOfNumbers(requestBody);
        steps.isExecutedBadRequestStatus();
    }

    @Test
    @Order(13)
    public void status404ForRequestWithExtraFields() {
        String requestBody = "{ \"numbers\": [ 3, 1, 2 ], \"numbers\": [ 3, 1, 2 ]}";

        steps.sendListOfNumbers(requestBody);
        steps.isExecutedNotFoundStatus();
    }

    @Test
    @Order(14)
    public void status404ForRequestWithoutBody() {
        steps.sendPostRequestWithoutBody();
        steps.isExecutedNotFoundStatus();
    }

    @Test
    @Order(15)
    public void status405ForGetMethod() {
        steps.sendGetRequest();
        steps.isExecutedMethodNotAllowedStatus();
    }

    @Test
    @Order(16)
    public void status405ForPutMethod() {
        steps.sendPutRequest(REQUEST_BODY);
        steps.isExecutedMethodNotAllowedStatus();
    }

    @Test
    @Order(17)
    public void status405ForDeleteMethod() {
        steps.sendDeleteRequest();
        steps.isExecutedMethodNotAllowedStatus();
    }
}
