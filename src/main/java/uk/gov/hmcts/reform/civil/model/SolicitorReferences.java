package uk.gov.hmcts.reform.civil.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitorReferences {

    private String applicantSolicitor1Reference;
    private String respondentSolicitor1Reference;
    private String respondentSolicitor2Reference;

}
