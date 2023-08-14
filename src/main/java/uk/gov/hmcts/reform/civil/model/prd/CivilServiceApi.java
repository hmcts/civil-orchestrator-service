package uk.gov.hmcts.reform.civil.model.prd;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.reform.civil.responsebody.CreateClaimResponseBody;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@FeignClient(name = "civil-service-api", url = "${civil_service.api.url}")
public interface CivilServiceApi {

    @GetMapping("/cases/caseworker/searchCaseForSDT/{userId}")
    Boolean searchCaseForCaseworker(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @PathVariable String userId,
        @RequestParam("searchParam") String searchParam);

    @PostMapping("/cases/caseworkers/create-case/{userId}")
    ResponseEntity<String> caseworkerSubmitEvent(
        @PathVariable("userId") String userId,
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
        @RequestBody CreateClaimResponseBody submitEventDto
    );

    @GetMapping("/cases/caseworker/validatePin")
    List<String> validatePostCode(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
        @RequestParam("postCode") String postCodeToValidate);

}
