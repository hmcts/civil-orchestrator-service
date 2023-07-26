package uk.gov.hmcts.reform.civil.model.casedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CorrectEmail {

    private String email;
    private YesOrNo correct;

    public boolean isCorrect() {
        return correct == YesOrNo.YES;
    }
}
