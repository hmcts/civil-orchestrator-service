package uk.gov.hmcts.reform.civil.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CreateClaimFromSdtServiceTest {

    @Autowired
    private CreateClaimFromSdtService createClaimFromSdtService;

    @Test
    void whenInputIsInvalid_thenThrowException() {


    }
}
