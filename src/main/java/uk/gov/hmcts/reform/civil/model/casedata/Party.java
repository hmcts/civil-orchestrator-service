package uk.gov.hmcts.reform.civil.model.casedata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class Party {

    private Type type;
    private String organisationName;
    private Address primaryAddress;
    private String bulkClaimPartyName;

    public enum Type {
        INDIVIDUAL,
        COMPANY,
        ORGANISATION,
        SOLE_TRADER,
        BULK;

        public String getDisplayValue() {
            return StringUtils.capitalize(this.name().toLowerCase().replace('_', ' '));
        }
    }

}
