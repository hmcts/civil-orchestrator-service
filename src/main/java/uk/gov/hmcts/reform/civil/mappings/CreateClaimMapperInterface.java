package uk.gov.hmcts.reform.civil.mappings;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import uk.gov.hmcts.reform.civil.requestbody.CreateClaimRequest;

@Mapper
public interface CreateClaimMapperInterface {

    CreateClaimMapperInterface INSTANCE = Mappers.getMapper(CreateClaimMapperInterface.class);

    @Mapping(target = "sdtRequestIdFromSdt", source = "sdtRequestId")
    @Mapping(target = "solicitorReferences.applicantSolicitor1Reference",  source = "claimantReference")

    @Mapping(target = "applicant1.type", expression = "java(CreateClaimMappingUtils.setClaimantType())")
    @Mapping(target = "applicant1.organisationName", source = "claimant.name")
    @Mapping(target = "applicant1.bulkClaimPartyName", expression = "java(null)")
    @Mapping(target = "applicant1.primaryAddress.addressLine1", source = "claimant.address.addressLine1")
    @Mapping(target = "applicant1.primaryAddress.addressLine2", source = "claimant.address.addressLine2")
    @Mapping(target = "applicant1.primaryAddress.addressLine3", source = "claimant.address.addressLine3")
    @Mapping(target = "applicant1.primaryAddress.postTown", source = "claimant.address.addressLine4")
    @Mapping(target = "applicant1.primaryAddress.county", expression = "java(null)")
    @Mapping(target = "applicant1.primaryAddress.country", expression = "java(null)")
    @Mapping(target = "applicant1.primaryAddress.postCode", source = "claimant.address.postcode")

    @Mapping(target = "applicantSolicitor1CheckEmail", expression = "java(CreateClaimMappingUtils.checkCorrectEmail())")
    @Mapping(target = "applicant1OrganisationPolicy", expression = "java(null)")

    @Mapping(target = "respondent1.type", expression = "java(CreateClaimMappingUtils.setDefendantType())")
    @Mapping(target = "respondent1.organisationName", expression = "java(null)")
    @Mapping(target = "respondent1.bulkClaimPartyName", source = "defendant1.name")
    @Mapping(target = "respondent1.primaryAddress.addressLine1", source = "defendant1.address.addressLine1")
    @Mapping(target = "respondent1.primaryAddress.addressLine2", source = "defendant1.address.addressLine2")
    @Mapping(target = "respondent1.primaryAddress.addressLine3", source = "defendant1.address.addressLine3")
    @Mapping(target = "respondent1.primaryAddress.postTown", source = "defendant1.address.addressLine4")
    @Mapping(target = "respondent1.primaryAddress.county", expression = "java(null)")
    @Mapping(target = "respondent1.primaryAddress.country", expression = "java(null)")
    @Mapping(target = "respondent1.primaryAddress.postCode", source = "defendant1.address.postcode")

    @Mapping(target = "adddRespondent2", expression = "java(CreateClaimMappingUtils.checkRespondent2(createClaimRequest))")
    @Mapping(target = "respondent2.type", expression = "java(CreateClaimMappingUtils.setDefendantType())")
    @Mapping(target = "respondent2.organisationName", expression = "java(null)")
    @Mapping(target = "respondent2.bulkClaimPartyName", source = "defendant2.name")
    @Mapping(target = "respondent2.primaryAddress.addressLine1", source = "defendant2.address.addressLine1")
    @Mapping(target = "respondent2.primaryAddress.addressLine2", source = "defendant2.address.addressLine2")
    @Mapping(target = "respondent2.primaryAddress.addressLine3", source = "defendant2.address.addressLine3")
    @Mapping(target = "respondent2.primaryAddress.postTown", source = "defendant2.address.addressLine4")
    @Mapping(target = "respondent2.primaryAddress.county", expression = "java(null)")
    @Mapping(target = "respondent2.primaryAddress.country", expression = "java(null)")
    @Mapping(target = "respondent2.primaryAddress.postCode", source = "defendant2.address.postcode")

    @Mapping(target = "detailsOfClaim", source = "particulars")
    @Mapping(target = "totalClaimAmount", expression = "java(CreateClaimMappingUtils.claimAmount(createClaimRequest))")
    @Mapping(target = "claimInterest", expression = "java(CreateClaimMappingUtils.claimInterest(createClaimRequest))")
    @Mapping(target = "interestFromSpecificDate", source = "interest.interestOwedDate")
    @Mapping(target = "interestFromSpecificDateDescription", expression = "java(null)")
    @Mapping(target = "uiStatementOfTruth.name", source = "sotSignature")
    @Mapping(target = "uiStatementOfTruth.role", source = "sotSignatureRole")
    @Mapping(target = "applicantSolicitor1UserDetails", expression = "java(null)")

    CreateClaimCCD claimToDto(CreateClaimRequest createClaimRequest);

}
