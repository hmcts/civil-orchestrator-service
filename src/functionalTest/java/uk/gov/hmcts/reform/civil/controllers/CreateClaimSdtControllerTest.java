package uk.gov.hmcts.reform.civil.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXB;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.reform.civil.modelsdt.AddressType;
import uk.gov.hmcts.reform.civil.modelsdt.CreateClaimSDT;
import uk.gov.hmcts.reform.civil.service.CreateClaimFromSdtService;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(controllers = CreateClaimSdtController.class)
class CreateClaimSdtControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private CreateClaimFromSdtService createClaimFromSdtService;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

/*    @BeforeEach
    public void initMocks() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .build();
    }*/


    @Test
    public void validRequestWithJsonContent() throws Exception {
        CreateClaimSDT createClaimSDT = CreateClaimSDT.builder().bulkCustomerId("123123").build();
        String jsonBody = objectMapper.writeValueAsString(createClaimSDT);
        System.out.println(jsonBody);

        String uri = "/createSDTClaim";

        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post(uri)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(jsonBody))
            .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void methodNotAllowedGet() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/createSDTClaim";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                                              .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(405, status);;
    }

    @Test
    public void methodNotAllowedPut() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/createSDTClaim";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                                              .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(405, status);;
    }

    @Test
    public void methodNotAllowedDelete() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/createSDTClaim";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                                              .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(405, status);;
    }

}

