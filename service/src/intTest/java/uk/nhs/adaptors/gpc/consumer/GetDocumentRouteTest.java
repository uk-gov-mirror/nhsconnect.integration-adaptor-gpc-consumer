package uk.nhs.adaptors.gpc.consumer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

public class GetDocumentRouteTest extends CloudGatewayRouteBaseTest {
    private static final String NOT_FOUND_GET_DOCUMENT_URI =
        "/GP0001/STU3/1/gpconnect/documents/Binary/00000000-732b-461e-86b6-edb665c45510";

    @Test
    public void When_MakingRequestForSpecificDocument_Expect_OkResponse() {
        WIRE_MOCK_SERVER.stubFor(get(urlPathEqualTo(GET_DOCUMENT_URI))
            .willReturn(aResponse()
                .withStatus(HttpStatus.SC_OK)
                .withBody(EXPECTED_DOCUMENT_BODY)));
        WIRE_MOCK_SERVER.stubFor(get(urlPathEqualTo(ENDPOINT))
            .willReturn(aResponse()
                .withStatus(HttpStatus.SC_OK)
                .withBody(String.format(EXAMPLE_SDS_BODY, WIRE_MOCK_SERVER.baseUrl()))));

        getWebTestClient().get()
            .uri(GET_DOCUMENT_URI)
            .header(SSP_FROM_HEADER, ANY_STRING)
            .header(SSP_TO_HEADER, ANY_STRING)
            .header(SSP_INTERACTION_ID_HEADER, DOCUMENT_INTERACTION_ID)
            .header(SSP_TRACE_ID_HEADER, RANDOM_UUID)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .json(EXPECTED_DOCUMENT_BODY);
    }

    @Test
    public void When_MakingRequestForSpecificDocument_Expect_ErrorResponse() {
        WIRE_MOCK_SERVER.stubFor(get(urlPathEqualTo(NOT_FOUND_GET_DOCUMENT_URI))
            .willReturn(aResponse()
                .withStatus(HttpStatus.SC_NOT_FOUND)
                .withBody(EXPECTED_NOT_FOUND_BODY)));
        WIRE_MOCK_SERVER.stubFor(get(urlPathEqualTo(ENDPOINT))
            .willReturn(aResponse()
                .withStatus(HttpStatus.SC_OK)
                .withBody(String.format(EXAMPLE_SDS_BODY, WIRE_MOCK_SERVER.baseUrl()))));

        getWebTestClient().get()
            .uri(NOT_FOUND_GET_DOCUMENT_URI)
            .header(SSP_FROM_HEADER, ANY_STRING)
            .header(SSP_TO_HEADER, ANY_STRING)
            .header(SSP_INTERACTION_ID_HEADER, DOCUMENT_INTERACTION_ID)
            .header(SSP_TRACE_ID_HEADER, RANDOM_UUID)
            .exchange()
            .expectStatus()
            .isNotFound()
            .expectBody()
            .json(EXPECTED_NOT_FOUND_BODY);
    }
}
