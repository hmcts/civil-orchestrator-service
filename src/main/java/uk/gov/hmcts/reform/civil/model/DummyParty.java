package uk.gov.hmcts.reform.civil.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.civil.enums.Type;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DummyParty {

    private Type type;
    private String orgName;
    private Address primaryAddress;
    private String individualTitle;
    private String individualFirstName;
    private String individualLastName;

}
