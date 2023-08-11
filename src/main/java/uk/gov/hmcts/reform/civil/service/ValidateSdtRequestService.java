package uk.gov.hmcts.reform.civil.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.civil.model.prd.CivilServiceApi;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateSdtRequestService {

    private final CivilServiceApi civilServiceApi;
    private final UserService userService;

    public Boolean validateSdtRequest(String authorization, String sdtRequestIdFromSdt) {
        String userId = userService.getUserInfo(authorization).getUid();
        return civilServiceApi.searchCaseForCaseworker(
            authorization,
            userId,
            sdtRequestIdFromSdt
        );
    }
}
