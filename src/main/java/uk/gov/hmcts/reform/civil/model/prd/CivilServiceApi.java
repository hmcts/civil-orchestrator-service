package uk.gov.hmcts.reform.civil.model.prd;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@FeignClient(name = "civil-service-api", url = "${civil_service.api.url}")
public interface CivilServiceApi {

    @GetMapping("/cases/caseworker/searchCaseForSDT/{userId}")
    Boolean searchCaseForCaseworker(
        @RequestHeader(AUTHORIZATION) String authorisation,
        @PathVariable String userId,
        @RequestParam("searchParam") String searchParam);

}
