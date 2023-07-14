package uk.gov.hmcts.reform.civil.exceptions.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.civil.exceptions.Payload;
import uk.gov.hmcts.reform.civil.model.CreateSDTResponse;
import uk.gov.hmcts.reform.civil.service.ExceptionService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ExceptionController {

    private final ExceptionService exceptionService;

    @PostMapping(path = "/exception/{testId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateSDTResponse> welcome(@PathVariable("testId") int testId,
                                                     @RequestBody @Valid Payload payload) {
        return exceptionService.buildException(testId);
    }
}
