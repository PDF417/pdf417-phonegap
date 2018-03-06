//
//  USDLMapping.m
//  Pdf417Demo
//
//  Created by Jure Cular on 06/03/2018.
//

#import "USDLMapping.h"
#import <MicroBlink/MicroBlink.h>

@implementation USDLMapping

- (instancetype)init {
    self = [super init];

    if (self) {
        NSMutableDictionary *dictionary = [[NSMutableDictionary alloc] init];

        dictionary[@(DocumentType)] = @"Document Type";
        dictionary[@(StandardVersionNumber)] = @"Standard Version Number";
        dictionary[@(CustomerFamilyName)] = @"Customer Family Name";
        dictionary[@(CustomerFirstName)] = @"Customer First Name";
        dictionary[@(CustomerFullName)] = @"Customer Name";
        dictionary[@(DateOfBirth)] = @"Date of Birth";
        dictionary[@(Sex)] = @"Sex";
        dictionary[@(EyeColor)] = @"Eye Color";
        dictionary[@(AddressStreet)] = @"Address - Street 1";
        dictionary[@(AddressCity)] = @"Address - City";
        dictionary[@(AddressJurisdictionCode)] = @"Address - Jurisdiction Code";
        dictionary[@(AddressPostalCode)] = @"Address - Postal Code";
        dictionary[@(FullAddress)] = @"Full Address";
        dictionary[@(Height)] = @"Height";
        dictionary[@(HeightIn)] = @"Height in";
        dictionary[@(HeightCm)] = @"Height cm";
        dictionary[@(CustomerMiddleName)] = @"Customer Middle Name";
        dictionary[@(HairColor)] = @"Hair color";
        dictionary[@(NameSuffix)] = @"Name Suffix";
        dictionary[@(AKAFullName)] = @"Alias / AKA Name";
        dictionary[@(AKAFamilyName)] = @"Alias / AKA Family Name";
        dictionary[@(AKAGivenName)] = @"Alias / AKA Given Name";
        dictionary[@(AKASuffixName)] = @"Alias / AKA Suffix Name";
        dictionary[@(WeightRange)] = @"Weight Range";
        dictionary[@(WeightPounds)] = @"Weight (pounds)";
        dictionary[@(WeightKilograms)] = @"Weight (kilograms)";
        dictionary[@(CustomerIdNumber)] = @"Customer ID Number";
        dictionary[@(FamilyNameTruncation)] = @"Family name truncation";
        dictionary[@(FirstNameTruncation)] = @"First name truncation";
        dictionary[@(MiddleNameTruncation)] = @"Middle name truncation";
        dictionary[@(PlaceOfBirth)] = @"Place of birth";
        dictionary[@(AddressStreet2)] = @"Address - Street 2";
        dictionary[@(RaceEthnicity)] = @"Race / ethnicity";
        dictionary[@(NamePrefix)] = @"Name Prefix";
        dictionary[@(CountryIdentification)] = @"Country Identification";
        dictionary[@(ResidenceStreetAddress)] = @"Driver Residence Street Address";
        dictionary[@(ResidenceStreetAddress2)] = @"Driver Residence Street Address 2";
        dictionary[@(ResidenceCity)] = @"Driver Residence City";
        dictionary[@(ResidenceJurisdictionCode)] = @"Driver Residence Jurisdiction Code";
        dictionary[@(ResidencePostalCode)] = @"Driver Residence Postal Code";
        dictionary[@(ResidenceFullAddress)] = @"Driver Residence Full Address";
        dictionary[@(Under18)] = @"Under 18 Until";
        dictionary[@(Under19)] = @"Under 19 Until";
        dictionary[@(Under21)] = @"Under 21 Until";
        dictionary[@(SocialSecurityNumber)] = @"Social Security Number";
        dictionary[@(AKASocialSecurityNumber)] = @"Alias / AKA Social Security Number";
        dictionary[@(AKAMiddleName)] = @"Alias / AKA Middle Name";
        dictionary[@(AKAPrefixName)] = @"Alias / AKA Prefix Name";
        dictionary[@(OrganDonor)] = @"Organ Donor Indicator";
        dictionary[@(Veteran)] = @"Veteran Indicator";
        dictionary[@(AKADateOfBirth)] = @"Alias / AKA Date of Birth";
        dictionary[@(IssuerIdentificationNumber)] = @"Issuer Identification Number";
        dictionary[@(DocumentExpirationDate)] = @"Document Expiration Date";
        dictionary[@(JurisdictionVersionNumber)] = @"Jurisdiction Version Number";
        dictionary[@(JurisdictionVehicleClass)] = @"Jurisdiction-specific vehicle class";
        dictionary[@(JurisdictionRestrictionCodes)] = @"Jurisdiction-specific restriction codes";
        dictionary[@(JurisdictionEndorsementCodes)] = @"Jurisdiction-specific endorsement codes";
        dictionary[@(DocumentIssueDate)] = @"Document Issue Date";
        dictionary[@(FederalCommercialVehicleCodes)] = @"Federal Commercial Vehicle Codes";
        dictionary[@(IssuingJurisdiction)] = @"Issuing jurisdiction";
        dictionary[@(StandardVehicleClassification)] = @"Standard vehicle classification";
        dictionary[@(IssuingJurisdictionName)] = @"Issuing jurisdiction name";
        dictionary[@(StandardEndorsementCode)] = @"Standard endorsement code";
        dictionary[@(StandardRestrictionCode)] = @"Standard restriction code";
        dictionary[@(JurisdictionVehicleClassificationDescription)] = @"Jurisdiction-specific vehicle classification description";
        dictionary[@(JurisdictionEndorsmentCodeDescription)] = @"Jurisdiction-specific endorsment code description";
        dictionary[@(JurisdictionRestrictionCodeDescription)] = @"Jurisdiction-spacific restriction code description";
        dictionary[@(InventoryControlNumber)] = @"Inventory control number";
        dictionary[@(CardRevisionDate)] = @"Card Revision Date";
        dictionary[@(DocumentDiscriminator)] = @"Document Discriminator";
        dictionary[@(LimitedDurationDocument)] = @"Limited Duration Document Indicator";
        dictionary[@(AuditInformation)] = @"Audit information";
        dictionary[@(ComplianceType)] = @"Compliance Type";
        dictionary[@(IssueTimestamp)] = @"Issue Timestamp";
        dictionary[@(PermitExpirationDate)] = @"Driver Permit Expiration Date";
        dictionary[@(PermitIdentifier)] = @"Permit Identifier";
        dictionary[@(PermitIssueDate)] = @"Driver Permit Issue Date";
        dictionary[@(NumberOfDuplicates)] = @"Number of Duplicates";
        dictionary[@(HAZMATExpirationDate)] = @"HAZMAT Endorsement Expiration Date";
        dictionary[@(MedicalIndicator)] = @"Medical Indicator/Codes";
        dictionary[@(NonResident)] = @"Non-Resident Indicator";
        dictionary[@(UniqueCustomerId)] = @"Unique Customer Identifier";
        dictionary[@(DataDiscriminator)] = @"Data discriminator";
        dictionary[@(DocumentExpirationMonth)] = @"Document Expiration Month";
        dictionary[@(DocumentNonexpiring)] = @"Document Nonexpiring";
        dictionary[@(SecurityVersion)] = @"Security Version";

        _mKeyMappings = dictionary;
    }

    return self;

}

+ (USDLMapping *)sharedInstance {
    static USDLMapping *sharedInstance = nil;
    static dispatch_once_t onceToken;

    dispatch_once(&onceToken, ^{
        sharedInstance = [[USDLMapping alloc] init];
    });

    return sharedInstance;
}

@end
