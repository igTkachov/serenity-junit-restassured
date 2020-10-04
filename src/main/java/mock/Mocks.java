package mock;

import org.eclipse.jetty.http.HttpMethod;
import org.mockito.Mockito;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpStatusCode;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

public class Mocks extends Mockito {

    private static final String REQUEST_BODY = "{ \"numbers\": [ 3, 1, 2 ] }";
    private static final String RESPONSE_BODY = "{ \"numbers\": [ 1, 2, 3 ] }";
    private static final String ENDPOINT = "127.0.0.1";
    private static final Integer PORT = 8880;
    private static final String PATH_PARAM = "/api/sort";

    private static ClientAndServer mockServer;

    public static void initialization() {
        mockServer = startClientAndServer(PORT);
        post200withBody();
        post405withGetMethod();
        post405withDeleteMethod();
        post405withPatchMethod();
        post400withEmptyJsonBody();
        post200withSortedValues();
        post400withEmptyArray();
        post400withWrongVariablesInArray();
        post200withWithSameValues();
        post200withWithOneValue();
        post200withWithMinusValues();
        post200withWithMaxIntegerValues();
        post404withWrongBodyFields();
        post404withWrongValueForField();
        post404withExtraFields();
        post404withoutBody();
    }

    public static void stop() {
        mockServer.stop();
    }

    private static void post200withBody() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.POST.asString())
                                .withPath(PATH_PARAM)
                                .withBody(exact(REQUEST_BODY))
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withBody(RESPONSE_BODY)
                );
    }

    private static void post200withSortedValues() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.POST.asString())
                                .withPath(PATH_PARAM)
                                .withBody(exact(RESPONSE_BODY))
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withBody(RESPONSE_BODY)
                );
    }

    private static void post400withEmptyArray() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.POST.asString())
                                .withPath(PATH_PARAM)
                                .withBody("{ \"numbers\" :[] }")
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.BAD_REQUEST_400.code())
                );
    }

    private static void post400withWrongVariablesInArray() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.POST.asString())
                                .withPath(PATH_PARAM)
                                .withBody("{ \"numbers\" : [A, B, C] }")
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.BAD_REQUEST_400.code())
                );
    }

    private static void post200withWithSameValues() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.POST.asString())
                                .withPath(PATH_PARAM)
                                .withBody("{ \"numbers\" :[0, 0, 0] }")
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withBody("{ \"numbers\" :[0, 0, 0] }")
                );
    }

    private static void post200withWithOneValue() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.POST.asString())
                                .withPath(PATH_PARAM)
                                .withBody("{ \"numbers\" :[1] }")
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withBody("{ \"numbers\" :[1] }")
                );
    }

    private static void post200withWithMinusValues() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.POST.asString())
                                .withPath(PATH_PARAM)
                                .withBody("{ \"numbers\" :[0, -1, 1] }")
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withBody("{ \"numbers\" :[-1, 0, 1] }")
                );
    }

    private static void post200withWithMaxIntegerValues() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.POST.asString())
                                .withPath(PATH_PARAM)
                                .withBody("{ \"numbers\" :[2147483646, 2147483647, -2147483647] }")
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withBody("{ \"numbers\" :[-2147483647, 2147483646, 2147483647] }")
                );
    }

    private static void post400withEmptyJsonBody() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.POST.asString())
                                .withPath(PATH_PARAM)
                                .withBody("{}")
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.BAD_REQUEST_400.code())
                );
    }

    private static void post404withWrongBodyFields() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.POST.asString())
                                .withPath(PATH_PARAM)
                                .withBody("{ \"boys\" : \"girls\"}")
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.NOT_FOUND_404.code())
                );
    }

    private static void post404withWrongValueForField() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.POST.asString())
                                .withPath(PATH_PARAM)
                                .withBody("{ \"numbers\" : null }")
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.NOT_FOUND_404.code())
                );
    }

    private static void post404withExtraFields() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.POST.asString())
                                .withPath(PATH_PARAM)
                                .withBody("{ \"numbers\": [ 3, 1, 2 ], \"numbers\": [ 3, 1, 2 ]}")
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.NOT_FOUND_404.code())
                );
    }

    private static void post404withoutBody() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.POST.asString())
                                .withPath(PATH_PARAM)
                                .withBody("empty")
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.NOT_FOUND_404.code())
                );
    }

    private static void post405withGetMethod() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.GET.asString())
                                .withPath(PATH_PARAM)
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.METHOD_NOT_ALLOWED_405.code())
                );
    }

    private static void post405withDeleteMethod() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.DELETE.asString())
                                .withPath(PATH_PARAM)
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.METHOD_NOT_ALLOWED_405.code())
                );
    }

    private static void post405withPatchMethod() {
        new MockServerClient(ENDPOINT, PORT)
                .when(
                        request()
                                .withMethod(HttpMethod.PUT.asString())
                                .withPath(PATH_PARAM)
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.METHOD_NOT_ALLOWED_405.code())
                );
    }
}
