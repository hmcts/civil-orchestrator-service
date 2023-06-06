package uk.gov.hmcts.reform.civil.mappings;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import uk.gov.hmcts.reform.civil.enums.SpecClaimTimelineList;
import uk.gov.hmcts.reform.civil.enums.Type;
import uk.gov.hmcts.reform.civil.enums.YesOrNo;
import uk.gov.hmcts.reform.civil.model.*;
import uk.gov.hmcts.reform.civil.model.interestcalc.InterestClaimFromType;
import uk.gov.hmcts.reform.civil.model.interestcalc.InterestClaimOptions;
import uk.gov.hmcts.reform.civil.model.interestcalc.SameRateInterestSelection;
import uk.gov.hmcts.reform.civil.model.interestcalc.SameRateInterestType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface CreateClaimMapperInterface {

    CreateClaimMapperInterface INSTANCE = Mappers.getMapper(CreateClaimMapperInterface.class);

    @Mapping(target = "solicitorReferences.applicantSolicitor1Reference",  source = "claimantReference")
    @Mapping(target = "applicant1.type", expression = "java(setClaimantType())")
    @Mapping(target = "applicant1.orgName", source = "claimant.name")
    @Mapping(target = "applicant1.individualTitle", expression = "java(null)")
    @Mapping(target = "applicant1.individualFirstName", expression = "java(null)")
    @Mapping(target = "applicant1.individualLastName", expression = "java(null)")
    @Mapping(target = "applicant1.primaryAddress.addressLine1", source = "claimant.address.line1")
    @Mapping(target = "applicant1.primaryAddress.addressLine2", source = "claimant.address.line2")
    @Mapping(target = "applicant1.primaryAddress.addressLine3", source = "claimant.address.line3")
    @Mapping(target = "applicant1.primaryAddress.postTown", source = "claimant.address.line4")
    @Mapping(target = "applicant1.primaryAddress.county", expression = "java(null)")
    @Mapping(target = "applicant1.primaryAddress.country", expression = "java(null)")
    @Mapping(target = "applicant1.primaryAddress.postCode", source = "claimant.address.postcode")

    @Mapping(target = "applicantSolicitor1CheckEmail", expression = "java(checkCorrectEmail())")
    @Mapping(target = "applicant1OrganisationPolicy", expression = "java(applicantOrganisation())")

    @Mapping(target = "respondent1.type", expression = "java(setDefendantType())")
    @Mapping(target = "respondent1.orgName", expression = "java(null)")
    @Mapping(target = "respondent1.individualTitle", source = "defendant1.name")
    @Mapping(target = "respondent1.individualFirstName", source = "defendant1.name") // requires logic to split a fullname, into name attributes
    @Mapping(target = "respondent1.individualLastName", source = "defendant1.name")
    @Mapping(target = "respondent1.primaryAddress.addressLine1", source = "defendant1.address.line1")
    @Mapping(target = "respondent1.primaryAddress.addressLine2", source = "defendant1.address.line2")
    @Mapping(target = "respondent1.primaryAddress.addressLine3", source = "defendant1.address.line3")
    @Mapping(target = "respondent1.primaryAddress.postTown", source = "defendant1.address.line4")
    @Mapping(target = "respondent1.primaryAddress.county", expression = "java(null)")
    @Mapping(target = "respondent1.primaryAddress.country", expression = "java(null)")
    @Mapping(target = "respondent1.primaryAddress.postCode", source = "defendant1.address.postcode")

    @Mapping(target = "AddRespondent2", expression = "java(checkRespondent2(createClaimSDT))")

    @Mapping(target = "respondent2.type", expression = "java(setDefendantType())")
    @Mapping(target = "respondent2.orgName", expression = "java(null)")
    @Mapping(target = "respondent2.individualTitle", source = "defendant2.name")
    @Mapping(target = "respondent2.individualFirstName", source = "defendant2.name")
    @Mapping(target = "respondent2.individualLastName", source = "defendant2.name")
    @Mapping(target = "respondent2.primaryAddress.addressLine1", source = "defendant2.address.line1")
    @Mapping(target = "respondent2.primaryAddress.addressLine2", source = "defendant2.address.line2")
    @Mapping(target = "respondent2.primaryAddress.addressLine3", source = "defendant2.address.line3")
    @Mapping(target = "respondent2.primaryAddress.postTown", source = "defendant2.address.line4")
    @Mapping(target = "respondent2.primaryAddress.county", expression = "java(null)")
    @Mapping(target = "respondent2.primaryAddress.country", expression = "java(null)")
    @Mapping(target = "respondent2.primaryAddress.postCode", source = "defendant2.address.postcode")

    @Mapping(target = "detailsOfClaim", source = "particulars")
    @Mapping(target = "claimAmountBreakup", expression = "java(claimAmount(createClaimSDT))")
    @Mapping(target = "claimInterest", expression = "java(claimInterest(createClaimSDT))")
    @Mapping(target = "interestFromSpecificDate", source = "claimDate")
    @Mapping(target = "uiStatementOfTruth.name", source = "sotSignature")

    CreateClaimCCD claimToDto(CreateClaimSDT createClaimSDT);

    default Type setClaimantType() {
        return Type.ORGANISATION;
    }

    default Type setDefendantType() {
        return Type.INDIVIDUAL;
    }

    default CorrectEmail checkCorrectEmail(){
       CorrectEmail correctEmail = new CorrectEmail();
       correctEmail.setCorrect(YesOrNo.YES);

       return correctEmail;
    }

    default OrganisationPolicy applicantOrganisation(){
        OrganisationPolicy organisationPolicy = new OrganisationPolicy();
        organisationPolicy.setOrganisation(OrganisationId.builder().build());
        organisationPolicy.setOrgPolicyCaseAssignedRole("testrole");
        organisationPolicy.setOrgPolicyReference("test");

        return organisationPolicy;
    }

    default YesOrNo checkRespondent2(CreateClaimSDT createClaimSDT) {
        if (createClaimSDT.getDefendant2() != null) {
            return YesOrNo.YES;
        } else {
            return YesOrNo.NO;
        }
    }

    default List<ClaimAmountBreakup> claimAmount(CreateClaimSDT createClaimSDT){
       BigDecimal bigDecimal = new BigDecimal(createClaimSDT.getClaimAmount());
       List<ClaimAmountBreakup> claimAmountBreakup = new ArrayList<>();
       claimAmountBreakup.add(ClaimAmountBreakup.builder()
                                   .value(ClaimAmountBreakupDetails.builder()
                                              .claimAmount(bigDecimal)
                                              .claimReason("Test reason1")
                                              .build()).build());

       return claimAmountBreakup;
    }

    default YesOrNo claimInterest(CreateClaimSDT createClaimSDT) {
        if (createClaimSDT.getReserveRightToClaimInterest().equals(true)) {
            return YesOrNo.YES;
        } else {
            return YesOrNo.NO;
        }
    }

}
