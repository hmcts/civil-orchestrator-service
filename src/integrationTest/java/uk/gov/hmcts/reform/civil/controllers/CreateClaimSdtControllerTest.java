package uk.gov.hmcts.reform.civil.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.reform.civil.exceptions.RestExceptionHandler;
import uk.gov.hmcts.reform.civil.requestbody.AddressType;
import uk.gov.hmcts.reform.civil.requestbody.ClaimantType;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;
import uk.gov.hmcts.reform.civil.requestbody.DefendantType;
import uk.gov.hmcts.reform.civil.requestbody.Interest;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimErrorResponse;
import uk.gov.hmcts.reform.civil.service.CreateClaimFromSdtService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@WebMvcTest(controllers = {CreateClaimSdtController.class, RestExceptionHandler.class})
class CreateClaimSdtControllerTest {

    @MockBean
    private MockMvc mvc;
    @MockBean
    private CreateClaimFromSdtService createClaimFromSdtService;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;
    private String uri = "/createSDTClaim";
    public static final String SDTREQUEST_ID = "SdtRequestId";

    @BeforeEach
    public void initMocks() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .build();
    }

    @Test
    public void validRequestWithJsonContent() throws Exception {

        when(createClaimFromSdtService.getSdtRequestId()).thenReturn("SdtRequestIdFromccd");
        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("15678908")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .claimant(ClaimantType.builder().name("claimant").address(AddressType.builder().postcode("BR11LS").build())
                          .build())
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
            .sotSignature("sotSignatureExample")
            .interest(Interest.builder().interestOwedDate(LocalDate.now()).build())
            .build();
        String jsonBody = objectMapper.writeValueAsString(createClaimSDT);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post(uri)
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .header(AUTHORIZATION, "Bearer user1")
                                                   .header(SDTREQUEST_ID, "unique")
                                                   .content(jsonBody))
            .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void inValidHeaderWithJsonContent() throws Exception {

        when(createClaimFromSdtService.getSdtRequestId()).thenReturn("unique");
        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("15678908")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .claimant(ClaimantType.builder().name("claimant").address(AddressType.builder().postcode("BR11LS").build())
                          .build())
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant2").build())
            .sotSignature("sotSignatureExample")
            .interest(Interest.builder().interestOwedDate(LocalDate.now()).build())
            .build();
        String jsonBody = objectMapper.writeValueAsString(createClaimSDT);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post(uri)
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .header(AUTHORIZATION, "Bearer user1")
                                                   .header(SDTREQUEST_ID, "unique")
                                                   .content(jsonBody))
            .andReturn();

        int status = mvcResult.getResponse().getStatus();
        CreateClaimErrorResponse sdtErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreateClaimErrorResponse.class);
        assertEquals(400, status);
        assertEquals(sdtErrorResponse.getErrorCode(), "000");
        Assertions.assertThat(sdtErrorResponse.getErrorText().contains("Request already processed"));
    }


    @Test
    public void inValidRequestWithJsonContent() throws Exception {
        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder()
            .bulkCustomerId("123123")
            .defendant1(DefendantType.builder().name("Defendant1").build())
            .sotSignature("roleAs").build();
        String jsonBody = objectMapper.writeValueAsString(createClaimSDT);

        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post(uri)
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .header(AUTHORIZATION, "Bearer user1")
                                                   .header(SDTREQUEST_ID, "unique")
                                                   .content(jsonBody))
            .andReturn();

        int status = mvcResult.getResponse().getStatus();
        // we need to get the error code and error message which should highlight the field errors.
        CreateClaimErrorResponse sdtErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreateClaimErrorResponse.class);
        assertEquals(400, status);
        assertEquals(sdtErrorResponse.getErrorCode(), "000");
        assertEquals(sdtErrorResponse.getErrorText(), "Bad data");
    }

    @Test
    public void inValidRequestBodyWithSameNameOfDefendant1AndDefendant2() throws Exception {
        CreateClaimRequest createClaimSDT = CreateClaimRequest.builder().bulkCustomerId("15678908")
            .claimAmount(Long.valueOf(9999))
            .particulars("particulars")
            .claimantReference("1568h8992334")
            .claimant(ClaimantType.builder().name("claimant").build())
            .defendant1(DefendantType.builder().name("defendant1").build())
            .defendant2(DefendantType.builder().name("defendant1").build())
            .sotSignature("bulk issuer role")
            .build();

        String jsonBody = objectMapper.writeValueAsString(createClaimSDT);
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post(uri)
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .header(AUTHORIZATION, "Bearer user1")
                                                   .header(SDTREQUEST_ID, "unique")
                                                   .content(jsonBody))
            .andReturn();

        int status = mvcResult.getResponse().getStatus();
        // we need to get the error code and error message which should highlight the field errors.
        CreateClaimErrorResponse sdtErrorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreateClaimErrorResponse.class);
        assertEquals(400, status);
        assertEquals("000", sdtErrorResponse.getErrorCode());
        assertEquals("Bad data", sdtErrorResponse.getErrorText());
    }

    @Test
    public void methodNotAllowedGet() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                                              .accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(405, status);
    }

    @Test
    public void methodNotAllowedPut() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                                              .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(405, status);
    }

    @Test
    public void methodNotAllowedDelete() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                                              .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(405, status);
    }

}

