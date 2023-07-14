package uk.gov.hmcts.reform.civil.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import uk.gov.hmcts.reform.civil.Application;
import uk.gov.hmcts.reform.civil.exceptions.ApiError;
import uk.gov.hmcts.reform.civil.exceptions.Payload;
import uk.gov.hmcts.reform.civil.model.CreateSDTResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExceptionControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void welcome_ok() throws Exception {
        Payload payload = new Payload();
        payload.setName("abc");
        payload.setAge(19);

        ResponseEntity<CreateSDTResponse> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/exception/3",
                        payload, CreateSDTResponse.class);
        assertEquals(201, responseEntity.getStatusCodeValue());
    }

    @Test
    public void welcome_validate_fail() throws Exception {
        Payload payload = new Payload();
        payload.setName("abc");
        payload.setAge(1);

        ResponseEntity<ApiError> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/exception/3",
                        payload, ApiError.class);
        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("1001", responseEntity.getBody().getErrorId());
    }

    @Test
    public void welcome_exception_1() throws Exception {
        Payload payload = new Payload();
        payload.setName("abc");
        payload.setAge(19);

        ResponseEntity<ApiError> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/exception/1",
                        payload, ApiError.class);
        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("1001", responseEntity.getBody().getErrorId());
    }

    @Test
    public void welcome_exception_2() throws Exception {
        Payload payload = new Payload();
        payload.setName("abc");
        payload.setAge(19);

        ResponseEntity<ApiError> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/exception/2",
                        payload, ApiError.class);
        assertEquals(400, responseEntity.getStatusCodeValue());
        assertEquals("1003", responseEntity.getBody().getErrorId());
    }
}
