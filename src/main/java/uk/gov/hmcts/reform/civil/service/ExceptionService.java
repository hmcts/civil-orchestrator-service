package uk.gov.hmcts.reform.civil.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.civil.exceptions.ApplicationException;
import uk.gov.hmcts.reform.civil.model.CreateSDTResponse;

import static uk.gov.hmcts.reform.civil.exceptions.ErrorDetails.INVALID_DATA;
import static uk.gov.hmcts.reform.civil.exceptions.ErrorDetails.INVALID_DATA_CUSTOM;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExceptionService {

    public ResponseEntity<CreateSDTResponse> buildException(final int testId) {
        //throw own error
        if (testId == 1) {
            throw new ApplicationException(INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
        if (testId == 2) {
            throw new ApplicationException(INVALID_DATA_CUSTOM, HttpStatus.BAD_REQUEST, "2333");
        }
        return new ResponseEntity<>(
                CreateSDTResponse.builder().build(),
                HttpStatus.CREATED);
    }
}
