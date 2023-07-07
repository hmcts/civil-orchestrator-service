package uk.gov.hmcts.reform.civil.controllers;

import jakarta.xml.bind.JAXB;
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
import uk.gov.hmcts.reform.civil.requestbody.AddressType;
import uk.gov.hmcts.reform.civil.service.CreateClaimFromSdtService;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest
class CreateClaimSdtControllerTest {

    @MockBean
    private MockMvc mvc;
    @MockBean
    private CreateClaimFromSdtService createClaimFromSdtService;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Test
    public void validRequestWithXmlContent() throws Exception {
        AddressType addressType = new AddressType("test", "test", "test", "test", "test");
        StringWriter sw = new StringWriter();
        JAXB.marshal(addressType, sw);
        String xmlString = sw.toString();

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/createSDTClaim";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                                              .contentType(MediaType.APPLICATION_XML_VALUE)
                                              .content(xmlString))
            .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void methodNotAllowedGet() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/createSDTClaim";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                                              .accept(MediaType.APPLICATION_XML_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(405, status);;
    }

    @Test
    public void methodNotAllowedPut() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/createSDTClaim";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                                              .accept(MediaType.APPLICATION_XML_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(405, status);;
    }

    @Test
    public void methodNotAllowedDelete() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String uri = "/createSDTClaim";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                                              .accept(MediaType.APPLICATION_XML_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(405, status);;
    }

}

