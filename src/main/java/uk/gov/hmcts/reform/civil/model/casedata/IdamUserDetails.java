package uk.gov.hmcts.reform.civil.model.casedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class IdamUserDetails {

    private String email;
    private String id;
}
