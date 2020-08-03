package org.openapi.generator;

import java.io.IOException;
import java.util.List;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.api.DefaultApi;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@ExtendWith(MockitoExtension.class)
public class WebClientListStringTest {

  private final DefaultApi defaultApi = new DefaultApi();


  @Test
  public void defaultApi_shouldReturnFluxOfStrings_whenServerReturnsListOfStringJson() throws IOException{
    try (MockWebServer stringListServer = new MockWebServer()) {

      // given
      final MockResponse mockResponse = new MockResponse();
      mockResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
      mockResponse.setBody("[\"one\",\"two\", \"three\"]");
      stringListServer.enqueue(mockResponse);
      stringListServer.start();
      defaultApi.getApiClient().setBasePath(stringListServer.url("/").toString());

      // when
      List<String> testGet = defaultApi.testGet().collectList().block();

      // then
      BDDAssertions.then(testGet).hasSize(3);
      BDDAssertions.then(testGet).containsExactly("one", "two", "three");
      
    } 
  }
  
}