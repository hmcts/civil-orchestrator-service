package uk.gov.hmcts.reform.civil.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.civilcommonsmock.ccd.model.PreviousOrganisationCollectionItem;

import java.util.List;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class MockOrgPolicy {

    @JsonProperty("Organisation")
    private MockOrg organisation;

    @JsonProperty("OrgPolicyReference")
    private String orgPolicyReference;

    @JsonProperty("OrgPolicyCaseAssignedRole")
    private String orgPolicyCaseAssignedRole;

    @JsonProperty("PreviousOrganisations")
    private List<PreviousOrganisationCollectionItem> previousOrganisations;

}
