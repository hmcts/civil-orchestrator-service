package uk.gov.hmcts.reform.civil.mappings;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import uk.gov.hmcts.reform.civil.modelsdt.CreateClaimSDT;
import uk.gov.hmcts.reform.civilcommonsmock.civil.enums.YesOrNo;
import uk.gov.hmcts.reform.civilcommonsmock.civil.model.ClaimAmountBreakup;
import uk.gov.hmcts.reform.civilcommonsmock.civil.model.ClaimAmountBreakupDetails;
import uk.gov.hmcts.reform.civilcommonsmock.civil.model.CorrectEmail;
import uk.gov.hmcts.reform.civilcommonsmock.civil.model.OrganisationId;
import uk.gov.hmcts.reform.civilcommonsmock.civil.model.OrganisationPolicy;
import uk.gov.hmcts.reform.civilcommonsmock.civil.model.Party;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface CreateClaimMapperInterface {

    CreateClaimMapperInterface INSTANCE = Mappers.getMapper(CreateClaimMapperInterface.class);

    @Mapping(target = "solicitorReferences.applicantSolicitor1Reference",  source = "claimantReference")
    @Mapping(target = "applicant1.type", expression = "java(setClaimantType())")
    @Mapping(target = "applicant1.partyID", expression = "java(null)")
    @Mapping(target = "applicant1.individualTitle", expression = "java(null)")
    @Mapping(target = "applicant1.individualFirstName", expression = "java(null)")
    @Mapping(target = "applicant1.individualLastName", expression = "java(null)")
    @Mapping(target = "applicant1.individualDateOfBirth", expression = "java(null)")
    @Mapping(target = "applicant1.organisationName", source = "claimant.name")
    @Mapping(target = "applicant1.companyName", expression = "java(null)")
    @Mapping(target = "applicant1.soleTraderTitle", expression = "java(null)")
    @Mapping(target = "applicant1.soleTraderFirstName", expression = "java(null)")
    @Mapping(target = "applicant1.soleTraderLastName", expression = "java(null)")
    @Mapping(target = "applicant1.soleTraderDateOfBirth", expression = "java(null)")
    @Mapping(target = "applicant1.soleTraderTradingAs", expression = "java(null)")
    @Mapping(target = "applicant1.partyName", expression = "java(null)")
    @Mapping(target = "applicant1.partyTypeDisplayValue", expression = "java(null)")
    @Mapping(target = "applicant1.partyEmail", expression = "java(null)")
    @Mapping(target = "applicant1.partyPhone", expression = "java(null)")
    @Mapping(target = "applicant1.unavailableDates", expression = "java(null)")
    @Mapping(target = "applicant1.flags", expression = "java(null)")
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
    @Mapping(target = "respondent1.partyID", expression = "java(null)")
    @Mapping(target = "respondent1.individualTitle", source = "defendant1.name")
    @Mapping(target = "respondent1.individualFirstName", source = "defendant1.name")
    @Mapping(target = "respondent1.individualLastName", source = "defendant1.name")
    @Mapping(target = "respondent1.individualDateOfBirth", expression = "java(null)")
    @Mapping(target = "respondent1.organisationName", expression = "java(null)")
    @Mapping(target = "respondent1.companyName", expression = "java(null)")
    @Mapping(target = "respondent1.soleTraderTitle", expression = "java(null)")
    @Mapping(target = "respondent1.soleTraderFirstName", expression = "java(null)")
    @Mapping(target = "respondent1.soleTraderLastName", expression = "java(null)")
    @Mapping(target = "respondent1.soleTraderDateOfBirth", expression = "java(null)")
    @Mapping(target = "respondent1.soleTraderTradingAs", expression = "java(null)")
    @Mapping(target = "respondent1.partyName", expression = "java(null)")
    @Mapping(target = "respondent1.partyTypeDisplayValue", expression = "java(null)")
    @Mapping(target = "respondent1.partyEmail", expression = "java(null)")
    @Mapping(target = "respondent1.partyPhone", expression = "java(null)")
    @Mapping(target = "respondent1.unavailableDates", expression = "java(null)")
    @Mapping(target = "respondent1.flags", expression = "java(null)")
    @Mapping(target = "respondent1.primaryAddress.addressLine1", source = "defendant1.address.line1")
    @Mapping(target = "respondent1.primaryAddress.addressLine2", source = "defendant1.address.line2")
    @Mapping(target = "respondent1.primaryAddress.addressLine3", source = "defendant1.address.line3")
    @Mapping(target = "respondent1.primaryAddress.postTown", source = "defendant1.address.line4")
    @Mapping(target = "respondent1.primaryAddress.county", expression = "java(null)")
    @Mapping(target = "respondent1.primaryAddress.country", expression = "java(null)")
    @Mapping(target = "respondent1.primaryAddress.postCode", source = "defendant1.address.postcode")

    @Mapping(target = "AddRespondent2", expression = "java(checkRespondent2(createClaimSDT))")

    @Mapping(target = "respondent2.type", expression = "java(setDefendantType())")
    @Mapping(target = "respondent2.partyID", expression = "java(null)")
    @Mapping(target = "respondent2.individualTitle", source = "defendant2.name")
    @Mapping(target = "respondent2.individualFirstName", source = "defendant2.name")
    @Mapping(target = "respondent2.individualLastName", source = "defendant2.name")
    @Mapping(target = "respondent2.individualDateOfBirth", expression = "java(null)")
    @Mapping(target = "respondent2.organisationName", expression = "java(null)")
    @Mapping(target = "respondent2.companyName", expression = "java(null)")
    @Mapping(target = "respondent2.soleTraderTitle", expression = "java(null)")
    @Mapping(target = "respondent2.soleTraderFirstName", expression = "java(null)")
    @Mapping(target = "respondent2.soleTraderLastName", expression = "java(null)")
    @Mapping(target = "respondent2.soleTraderDateOfBirth", expression = "java(null)")
    @Mapping(target = "respondent2.soleTraderTradingAs", expression = "java(null)")
    @Mapping(target = "respondent2.partyName", expression = "java(null)")
    @Mapping(target = "respondent2.partyTypeDisplayValue", expression = "java(null)")
    @Mapping(target = "respondent2.partyEmail", expression = "java(null)")
    @Mapping(target = "respondent2.partyPhone", expression = "java(null)")
    @Mapping(target = "respondent2.unavailableDates", expression = "java(null)")
    @Mapping(target = "respondent2.flags", expression = "java(null)")
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
    @Mapping(target = "interestFromSpecificDate", source = "interest.owedDate")
    @Mapping(target = "interestFromSpecificDateDescription", expression = "java(null)")
    @Mapping(target = "uiStatementOfTruth.name", source = "sotSignature")
    @Mapping(target = "uiStatementOfTruth.role", source = "sotSignatureRole")

    CreateClaimCCD claimToDto(CreateClaimSDT createClaimSDT);

    default Party.Type setClaimantType() {
        return Party.Type.ORGANISATION;
    }

    default Party.Type setDefendantType() {
        return Party.Type.INDIVIDUAL;
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
