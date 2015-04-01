//
//  usdl_keys.js
//  MicroBlink BlinkID library
//
//  Created by Jurica Cerovec, Marko Mihovilic on 10/01/13.
//  Copyright (c) 2015 MicroBlink. All rights reserved.
//


//pragma mark - Keys for obtaining data on driver's licenses

// ****** 1. Determining AAMVA Version

//==============================================================/
//=============== 1. DETERMINING AAMVA VERSION =================/
//==============================================================/

/**
 Mandatory on all AAMVA driver's license versions.

 Possible values "0", "1", "2", "3", "4", "5", "6", "7", "8", "Compact" and
 specifies the version level of the PDF417 bar code format.

 AAMVA Version Number: This is a value between "00" and "99" that
 specifies the version level of the PDF417 bar code format. Version "0" and "00"
 is reserved for bar codes printed to the specification of the American Association
 of Motor Vehicle Administrators (AAMVA) prior to the adoption of the AAMVA DL/ID-2000
 standard. All bar codes compliant with the AAMVA DL/ID-2000 standard are designated
 Version "01." All barcodes compliant with AAMVA Card Design Specification version
 1.0, dated 09-2003 shall be designated Version "02." All barcodes compliant with
 AAMVA Card Design Specification version 2.0, dated 03-2005 shall be designated
 Version "03." All barcodes compliant with AAMVA Card Design Standard version 1.0,
 dated 07-2009 shall be designated Version "04." All barcodes compliant with AAMVA
 Card Design Standard version 1.0, dated 07-2010 shall be designated Version "05."
 All barcodes compliant with AAMVA Card Design Standard version 1.0, dated 07- 2011
 shall be designated Version "06". All barcodes compliant with AAMVA Card Design
 Standard version 1.0, dated 06-2012 shall be designated Version "07". All barcodes
 compliant with this current AAMVA standard shall be designated "08". Should a need
 arise requiring major revision to the format, this field provides the means to
 accommodate additional revision. "Compact" when Compact encoding is used.
 */
var kPPAamvaVersionNumber = "AAMVA Version Number";

// ****** 2. Keys Existing on all barcode versions

//==============================================================/
//========= 2. KEYS EXISTING ON ALL BARCODE VERSIONS ===========/
//==============================================================/

// ****** 2.1. Mandatory keys

/***************************************************************/
/*********************** MANDATORY KEYS ************************/
/***************************************************************/

// ****** 2.1.1. Personal data

//======================= PERSONAL DATA ========================/

/**
 Mandatory on all barcode versions, including compact encoding.

 Family name of the cardholder. (Family name is sometimes also called "last name" or "surname.")
 Collect full name for record, print as many characters as possible on portrait side of DL/ID.
 */
var kPPCustomerFamilyName = "Customer Family Name";

/**
 Mandatory on all barcode versions, including compact encoding.

 First name of the cardholder.
 */
var kPPCustomerFirstName = "Customer First Name";

/**
 Mandatory on all barcode versions, including compact encoding.

 Date on which the cardholder was born. MMDDCCYY format.
 */
var kPPDateOfBirth = "Date of Birth";

/**
 Mandatory on all barcode versions, including compact encoding.

 Gender of the cardholder. 1 = male, 2 = female.
 */
var kPPSex = "Sex";

/**
 Mandatory on AAMVA 01, 02, 03, 04, 05, 06, 07, 08 and Compact encoding

 Mandatory on all barcode versions, including compact encoding. (ANSI D-20 codes)

 Code   Description
 BLK    Black
 BLU    Blue
 BRO    Brown
 GRY    Gray
 GRN    Green
 HAZ    Hazel
 MAR    Maroon
 PNK    Pink
 DIC    Dichromatic
 UNK    Unknown
 */
var kPPEyeColor = "Eye Color";

/**
 Mandatory on AAMVA 02, 03, 04, 05, 06, 07, 08 and Compact encoding. Optional on 01.

 See also kPPHeightIn, kPPHeightCm

 Height of cardholder.

 Inches (in): number of inches followed by " in" or " IN". Example. 6'1'' = "073 in"

 Centimeters (cm): number of centimeters followed by " cm" or " CM". Example. 181 centimeters = "181 cm"
 */
var kPPHeight = "Height";

/**
 Mandatory on all standard barcode versions. Not defined on Compact encoding, where you must use `kPPFullAddress`.

 Street portion of the cardholder address.
 The place where the registered driver of a vehicle (individual or corporation) may be contacted such as a house number, street address etc.
 */
var kPPAddressStreet = "Address - Street 1";

/**
 Mandatory on all standard barcode versions. Not defined on Compact encoding, where you must use `kPPFullAddress`.

 City portion of the cardholder address.
 */
var kPPAddressCity = "Address - City";

/**
 Mandatory on all standard barcode versions. Not defined on Compact encoding, where you must use `kPPFullAddress`.

 State portion of the cardholder address.
 */
var kPPAddressJurisdictionCode = "Address - Jurisdiction Code";

/**
 Mandatory on all standard barcode versions. Not defined on Compact encoding, where you must use `kPPFullAddress`.

 Postal code portion of the cardholder address in the U.S. and Canada. If the
 trailing portion of the postal code in the U.S. is not known, zeros will be used
 to fill the trailing set of numbers up to nine (9) digits.
 */
var kPPAddressPostalCode = "Address - Postal Code";

// ****** 2.1.2. License data

//======================== LICENSE DATA ========================/

/**
 Mandatory on all barcode versions, including compact encoding.

 Date on which the document was issued. (MMDDCCYY format).
 */
var kPPDocumentIssueDate = "Document Issue Date";

/**
 Mandatory on all barcode versions, including compact encoding.

 Date on which the driving and identification privileges granted by the document are
 no longer valid. (MMDDCCYY format).
 */
var kPPDocumentExpirationDate = "Document Expiration Date";

/**
 Mandatory on all standard barcode formats, optional on compact encoding.

 This number uniquely identifies the issuing jurisdiction and can
 be obtained by contacting the ISO Issuing Authority (AAMVA)
 */
var kPPIssuerIdentificationNumber = "Issuer Identification Number";


/**
 Mandatory on all barcode versions, including compact encoding.

 Jurisdiction Version Number: This is a decimal value between 00 and 99 that
 specifies the jurisdiction version level of the PDF417 bar code format.
 Notwithstanding iterations of this standard, jurisdictions implement incremental
 changes to their bar codes, including new jurisdiction-specific data, compression
 algorithms for digitized images, digital signatures, or new truncation
 conventions used for names and addresses. Each change to the bar code format
 within each AAMVA version (above) must be noted, beginning with Jurisdiction
 Version 00.
 */
var kPPJurisdictionVersionNumber = "Jurisdiction Version Number";

/**
 Mandatory on all standard barcode versions. Not defined on Compact encoding, which has no compatible field.

 Jurisdiction-specific vehicle class / group code, designating the type
 of vehicle the cardholder has privilege to drive.
 */
var kPPJurisdictionVehicleClass = "Jurisdiction-specific vehicle class";

/**
 Mandatory on all standard barcode versions. Not defined on Compact encoding, which has no compatible field.

 Jurisdiction-specific codes that represent restrictions to driving
 privileges (such as airbrakes, automatic transmission, daylight only, etc.).
 */
var kPPJurisdictionRestrictionCodes = "Jurisdiction-specific restriction codes";

/**
 Mandatory on all standard barcode versions. Not defined on Compact encoding, which has no compatible field.

 Jurisdiction-specific codes that represent additional privileges
 granted to the cardholder beyond the vehicle class (such as transportation of
 passengers, hazardous materials, operation of motorcycles, etc.).
 */
var kPPJurisdictionEndorsementCodes = "Jurisdiction-specific endorsement codes";

/**
 Mandatory on all barcode versions, including compact encoding.

 The number assigned or calculated by the issuing authority.
 */
var kPPCustomerIdNumber = "Customer ID Number";

// ****** 2.2. Optional keys

/***************************************************************/
/*********************** OPTIONAL KEYS *************************/
/***************************************************************/

// ****** 2.2.1. Personal data

//======================= PERSONAL DATA ========================/

/**
 Optional on all barcode versions, including compact encoding.

 Bald, black, blonde, brown, gray, red/auburn, sandy, white, unknown. If the issuing
 jurisdiction wishes to abbreviate colors, the three-character codes provided in ANSI D20 must be
 used.

 Code   Description
 BAL    Bald
 BLK    Black
 BLN    Blond
 BRO    Brown
 GRY    Grey
 RED    Red/Auburn
 SDY    Sandy
 WHI    White
 UNK    Unknown
 */
var kPPHairColor = "Hair color";

/**
 Optional on all barcode versions, including compact encoding.

 ame Suffix (If jurisdiction participates in systems requiring name suffix (PDPS, CDLIS, etc.), the suffix must be collected and displayed on the DL/ID).
 - JR (Junior)
 - SR (Senior)
 - 1ST or I (First)
 - 2ND or II (Second)
 - 3RD or III (Third)
 - 4TH or IV (Fourth)
 - 5TH or V (Fifth)
 - 6TH or VI (Sixth)
 - 7TH or VII (Seventh)
 - 8TH or VIII (Eighth)
 - 9TH or IX (Ninth).
 */
var kPPNameSuffix = "Name Suffix";

/**
 Optional on all standard barcode versions. Not defined on Compact encoding, where you must use `kPPFullAddress`.


 Second line of street portion of the cardholder address.
 */
var kPPAddressStreet2 = "Address - Street 2";

// ****** 2.2.2. License data

//======================== LICENSE DATA ========================/

/**
 Optional on all barcode versions, mandatory on Compact Encoding

 Jurisdictions may define a subfile to contain jurisdiction-specific information.
 These subfiles are designated with the first character of “Z” and the second
 character is the first letter of the jurisdiction's name. For example, "ZC" would
 be the designator for a California or Colorado jurisdiction-defined subfile; "ZQ"
 would be the designator for a Quebec jurisdiction-defined subfile. In the case of
 a jurisdiction-defined subfile that has a first letter that could be more than
 one jurisdiction (e.g. California, Colorado, Connecticut) then other data, like
 the IIN or address, must be examined to determine the jurisdiction.
 */
var kPPIssuingJurisdiction = "Issuing jurisdiction";

/**
 Optional on all barcode versions, including compact encoding.

 Standard vehicle classification code(s) for cardholder. This data element is a
 placeholder for future efforts to standardize vehicle classifications.
 */
var kPPStandardVehicleClassification = "Standard vehicle classification";

/**
 Optional on all standard barcode versions. Not defined on Compact encoding, which has no compatible field.

 Standard endorsement code(s) for cardholder. See codes in D20. This data element is a
 placeholder for future efforts to standardize endorsement codes.

 Code   Description
 H      Hazardous Material - This endorsement is required for the operation of any vehicle
 transporting hazardous materials requiring placarding, as defined by U.S.
 Department of Transportation regulations.
 L      Motorcycles – Including Mopeds/Motorized Bicycles.
 N      Tank - This endorsement is required for the operation of any vehicle transporting,
 as its primary cargo, any liquid or gaseous material within a tank attached to the vehicle.
 O      Other Jurisdiction Specific Endorsement(s) - This code indicates one or more
 additional jurisdiction assigned endorsements.
 P      Passenger - This endorsement is required for the operation of any vehicle used for
 transportation of sixteen or more occupants, including the driver.
 S      School Bus - This endorsement is required for the operation of a school bus. School bus means a
 CMV used to transport pre-primary, primary, or secondary school students from home to school,
 from school to home, or to and from school sponsored events. School bus does not include a
 bus used as common carrier (49 CRF 383.5).
 T      Doubles/Triples - This endorsement is required for the operation of any vehicle that would be
 referred to as a double or triple.
 X      Combined Tank/HAZ-MAT - This endorsement may be issued to any driver who qualifies for
 both the N and H endorsements.
 */
var kPPStandardEndorsementCode = "Standard endorsement code";

/**
 Optional on all standard barcode versions. Not defined on Compact encoding, which has no compatible field.

 Standard restriction code(s) for cardholder. See codes in D20. This data element is a placeholder
 for future efforts to standardize restriction codes.

 Code   Description
 B      Corrective Lenses
 C      Mechanical Devices (Special Brakes, Hand Controls, or Other Adaptive Devices)
 D      Prosthetic Aid
 E      Automatic Transmission
 F      Outside Mirror
 G      Limit to Daylight Only
 H      Limit to Employment
 I      Limited Other
 J      Other
 K      CDL Intrastate Only
 L      Vehicles without air brakes
 M      Except Class A bus
 N      Except Class A and Class B bus
 O      Except Tractor-Trailer
 V      Medical Variance Documentation Required
 W      Farm Waiver
 */
var kPPStandardRestrictionCode = "Standard restriction code";

// ****** 3. Keys existing on some barcode versions

//==============================================================/
//========== 3. KEYS EXISTING ON SOME BARCODE VERSIONS =========/
//==============================================================/

// ****** 3.1. Mandatory keys

/***************************************************************/
/*********************** MANDATORY KEYS ************************/
/***************************************************************/

// ****** 3.1.1. Personal data

//======================= PERSONAL DATA ========================/

/**
 Mandatory on AAMVA version 04, 05, 06, 07, 08, optional on 01. On other standards middle
 name is included into value of kPPFirstName.

 Middle name(s) of the cardholder. In the case of multiple middle names they
 shall be separated by a comma ",".
 */
var kPPCustomerMiddleName = "Customer Middle Name";

/**
 Optional on 01.

 See also kPPHeight, kPPHeightCm

 Height of cardholder. (FT/IN)

 FEET (1st char); Inches (2nd and 3rd char).
 Ex. 509 = 5 ft., 9 in.
 */
var kPPHeightIn = "Height in";

/**
 Optional on 01.

 See also kPPHeight, kPPHeightIn

 Height of cardholder in CENTIMETERS
 */
var kPPHeightCm = "Height cm";

/**
 Mandatory on AAMVA 01

 NAME of the individual holding the Driver License or ID as defined in
 ANSI D20 Data Dictionary. (Lastname,Firstname,MI, suffix if any)

 This field contains four portions, separated with the "," delimiter: Last Name (required)
 , (required)
 First Name (required)
 , (required if other name portions follow, otherwise optional)
 Middle Name(s) (optional)
 , (required if other name portions follow, otherwise optional)
 Suffix Code (optional)
 , (optional)
 */
var kPPCustomerFullName = "Customer Name";

/**
 Mandatory on compact encoding.

 Cardholder address.
 */
var kPPFullAddress = "Full Address";

/**
 Mandatory on AAMVA 04, 05, 06, 07, 08 and Compact Encoding

 A code that indicates whether a field has been truncated (T), has not been
 truncated (N), or – unknown whether truncated (U).
 */
var kPPFamilyNameTruncation = "Family name truncation";

/**
 Mandatory on AAMVA 04, 05, 06, 07, 08 and Compact Encoding

 A code that indicates whether a field has been truncated (T), has not been
 truncated (N), or – unknown whether truncated (U).
 */
var kPPFirstNameTruncation = "First name truncation";

/**
 Mandatory on AAMVA 04, 05, 06, 07, 08

 A code that indicates whether a field has been truncated (T), has not been
 truncated (N), or – unknown whether truncated (U).
 */
var kPPMiddleNameTruncation = "Middle name truncation";

// ****** 3.1.2. License data

//======================== LICENSE DATA ========================/

/**
 Mandatory on AAMVA 02, 03, 04, 05, 06, 07, 08 and Compact Encoding

 Country in which DL/ID is issued. U.S. = USA, Canada = CAN.
 */
var kPPCountryIdentification = "Country Identification";

/**
 Mandatory on AAMVA 02, 03, 04, 05, 06, 07, 08 and Compact Encoding

 Number must uniquely identify a particular document issued to that customer
 from others that may have been issued in the past. This number may serve multiple
 purposes of document discrimination, audit information number, and/or inventory control.
 */
var kPPDocumentDiscriminator = "Document Discriminator";

/**
 Mandatory on AAMVA versions 02 and 03.

 Federally established codes for vehicle categories, endorsements, and restrictions
 that are generally applicable to commercial motor vehicles. If the vehicle is not a
 commercial vehicle, "NONE" is to be entered.
 */
var kPPFederalCommercialVehicleCodes = "Federal Commercial Vehicle Codes";

// ****** 3.2. Optional keys

/***************************************************************/
/*********************** OPTIONAL KEYS *************************/
/***************************************************************/

// ****** 3.2.1. Personal data

//======================= PERSONAL DATA ========================/

/**
 Optional on AAMVA 02, 03, 04, 05, 06, 07, 08 and Compact Encoding

 Codes for race or ethnicity of the cardholder, as defined in ANSI D20.

 Race:
 Code   Description
 AI     Alaskan or American Indian (Having Origins in Any of The Original Peoples of
 North America, and Maintaining Cultural Identification Through Tribal
 Affiliation of Community Recognition)
 AP     Asian or Pacific Islander (Having Origins in Any of the Original Peoples of
 the Far East, Southeast Asia, or Pacific Islands. This Includes China, India,
 Japan, Korea, the Philippines Islands, and Samoa)
 BK     Black (Having Origins in Any of the Black Racial Groups of Africa)
 W      White (Having Origins in Any of The Original Peoples of Europe, North Africa,
 or the Middle East)

 Ethnicity:
 Code   Description
 H      Hispanic Origin (A Person of Mexican, Puerto Rican, Cuban, Central or South
 American or Other Spanish Culture or Origin, Regardless of Race)
 O      Not of Hispanic Origin (Any Person Other Than Hispanic)
 U      Unknown
 */
var kPPRaceEthnicity = "Race / ethnicity";

/**
 Optional on AAMVA 02, 03, 04, 05, 06, 07, 08 and Compact Encoding

 Country and municipality and/or state/province
 */
var kPPPlaceOfBirth = "Place of birth";

/**
 Optional on AAMVA 02, 03, 04, 05, 06, 07, 08

 Indicates the approximate weight range of the cardholder:
 0 = up to 31 kg (up to 70 lbs)
 1 = 32 – 45 kg (71 – 100 lbs)
 2 = 46 - 59 kg (101 – 130 lbs)
 3 = 60 - 70 kg (131 – 160 lbs)
 4 = 71 - 86 kg (161 – 190 lbs)
 5 = 87 - 100 kg (191 – 220 lbs)
 6 = 101 - 113 kg (221 – 250 lbs)
 7 = 114 - 127 kg (251 – 280 lbs)
 8 = 128 – 145 kg (281 – 320 lbs)
 9 = 146+ kg (321+ lbs)
 */
var kPPWeightRange = "Weight Range";

/**
 Optional on AAMVA 01, 04, 05, 06, 07, 08

 Cardholder weight in pounds Ex. 185 lb = "185"
 */
var kPPWeightPounds = "Weight (pounds)";

/**
 Optional on AAMVA 01, 04, 05, 06, 07, 08 and Compact Encoding

 Cardholder weight in kilograms Ex. 84 kg = "084"
 */
var kPPWeightKilograms = "Weight (kilograms)";

/**
 Optional on AAMVA 01

 PREFIX to Driver Name. Freeform as defined by issuing jurisdiction.
 */
var kPPNamePrefix = "Name Prefix";

/**
 Optional on AAMVA version 01.

 Driver Residence Street Address 1.
 */
var kPPResidenceStreetAddress = "Driver Residence Street Address";

/**
 Optional on AAMVA version 01.

 Driver Residence Street Address 2.
 */
var kPPResidenceStreetAddress2 = "Driver Residence Street Address 2";

/**
 Optional on AAMVA version 01.

 Driver Residence City
 */
var kPPResidenceCity = "Driver Residence City";

/**
 Optional on AAMVA version 01.

 Driver Residence Jurisdiction Code.
 */
var kPPResidenceJurisdictionCode = "Driver Residence Jurisdiction Code";

/**
 Optional on AAMVA version 01.

 Driver Residence Postal Code.
 */
var kPPResidencePostalCode = "Driver Residence Postal Code";

/**
 Optional on AAMVA 05, 06, 07, 08

 Date on which the cardholder turns 18 years old. (MMDDCCYY format).
 */
var kPPUnder18 = "Under 18 Until";

/**
 Optional on AAMVA 05, 06, 07, 08

 Date on which the cardholder turns 19 years old. (MMDDCCYY format).
 */
var kPPUnder19 = "Under 19 Until";

/**
 Optional on AAMVA 05, 06, 07, 08

 Date on which the cardholder turns 21 years old. (MMDDCCYY format).
 */
var kPPUnder21 = "Under 21 Until";

/**
 Optional on AAMVA version 01.

 The number assigned to an individual by the Social Security Administration.
 */
var kPPSocialSecurityNumber = "Social Security Number";

/**
 Optional on AAMVA version 01.

 Driver "AKA" Social Security Number. FORMAT SAME AS DRIVER SOC SEC NUM. ALTERNATIVE NUMBERS(S) used as SS NUM.
 */
var kPPAKASocialSecurityNumber = "Alias / AKA Social Security Number";

/*
 Optional on AAMVA version 01, 02

 Other name by which cardholder is known. ALTERNATIVE NAME(S) of the individual holding
 the Driver License or ID. FORMAT same as defined in ANSI D20 Data Dictionary.
 (Lastname,Firstname,MI, suffix if any.)

 The Name field contains four portions, separated with the "," delimiter: Last Name (required)
 , (required)
 First Name (required)
 , (required if other name portions follow, otherwise optional)
 Middle Name(s) (optional)
 , (required if other name portions follow, otherwise optional)
 Suffix Code (optional)
 , (optional)
 */
var kPPAKAFullName = "Alias / AKA Name";

/**
 Optional on AAMVA 01, 03, 04, 05, 06, 07, 08 and Compact Encoding

 Other family name by which cardholder is known.
 */
var kPPAKAFamilyName = "Alias / AKA Family Name";

/**
 Optional on AAMVA 01

 ALTERNATIVE MIDDLE NAME(s) or INITIALS of the individual holding the Driver License or ID.
 Hyphenated names acceptable, spaces between names acceptable, but no other
 use of special symbols
 */
var kPPAKAMiddleName = "Alias / AKA Middle Name";

/**
 Optional on AAMVA 01, 03, 04, 05, 06, 07, 08 and Compact Encoding

 Other given name by which cardholder is known
 */
var kPPAKAGivenName = "Alias / AKA Given Name";

/**
 Optional on AAMVA 01

 ALTERNATIVE PREFIX to Driver Name. Freeform as defined by issuing jurisdiction.
 */
var kPPAKAPrefixName = "Alias / AKA Prefix Name";

/**
 Optional on AAMVA 01, 03, 04, 05, 06, 07, 08 and Compact Encoding

 Other suffix by which cardholder is known.

 The Suffix Code Portion, if submitted, can contain only the Suffix Codes shown in the following table (e.g., Andrew Johnson, III = JOHNSON@ANDREW@@3RD):

 Suffix     Meaning or Synonym
 JR         Junior
 SR         Senior or Esquire 1ST First
 2ND        Second
 3RD        Third
 4TH        Fourth
 5TH        Fifth
 6TH        Sixth
 7TH        Seventh
 8TH        Eighth
 9TH        Ninth
 */
var kPPAKASuffixName = "Alias / AKA Suffix Name";

/**
 Optional on AAMVA 06, 07, 08

 Field that indicates that the cardholder is an organ donor = "1".
 */
var kPPOrganDonor = "Organ Donor Indicator";

/**
 Optional on AAMVA 07, 08

 Field that indicates that the cardholder is a veteran = "1"
 */
var kPPVeteran = "Veteran Indicator";

/**
 Optional on AAMVA 01

 ALTERNATIVE DATES(S) given as date of birth.
 */
var kPPAKADateOfBirth = "Alias / AKA Date of Birth";


/**
 Optional on compact encoding.

 Portrait image timestamp
 */
var kPPImageTimestamp = "Image timestamp";

/**
 Optional on compact encoding.

 Type of image
 */
var kPPImageType = "ImageType";

/**
 Optional on compact encoding.

 Portrait image
 */
var kPPPortraitImage = "Portrait Image";

/**
 Optional on compact encodings.

 BDB format owner
 */
var kPPBDBFormatOwner = "BDB format owner";

/**
 Optional on compact encodings.

 BDB format type
 */
var kPPBDBFormatType = "BDB format type";

/**
 Optional on compact encodings.

 Biometric data block length
 */
var kPPBiometricDataLength = "Biometric data length";

/**
 Optional on compact encodings.

 Biometric data block
 */
var kPPBiometricData = "Biometric data";

// ****** 3.2.2. License data

//======================== LICENSE DATA ========================/

/**
 Optional on AAMVA 02, 03, 04, 05, 06, 07, 08 and Compact Encoding

 Text that explains the jurisdiction-specific code(s) for classifications
 of vehicles cardholder is authorized to drive.
 */
var kPPJurisdictionVehicleClassificationDescription = "Jurisdiction-specific vehicle classification description";

/**
 Optional on AAMVA 02, 03, 04, 05, 06, 07, 08 and Compact Encoding

 Text that explains the jurisdiction-specific code(s) that indicates additional
 driving privileges granted to the cardholder beyond the vehicle class.
 */
var kPPJurisdictionEndorsmentCodeDescription = "Jurisdiction-specific endorsment code description";

/**
 Optional on AAMVA 02, 03, 04, 05, 06, 07, 08 and Compact Encoding

 Text describing the jurisdiction-specific restriction code(s) that curtail driving privileges.
 */
var kPPJurisdictionRestrictionCodeDescription = "Jurisdiction-spacific restriction code description";

/**
 Optional on AAMVA 02, 03, 04, 05, 06, 07, 08

 A string of letters and/or numbers that is affixed to the raw materials (card stock,
 laminate, etc.) used in producing driver licenses and ID cards. (DHS recommended field)
 */
var kPPInventoryControlNumber = "Inventory control number";

/**
 Optional on AAMVA 04, 05, 06, 07, 08 and Compact Encoding

 DHS required field that indicates date of the most recent version change or
 modification to the visible format of the DL/ID (MMDDCCYY format).
 */
var kPPCardRevisionDate = "Card Revision Date";

/**
 Optional on AAMVA 04, 05, 06, 07, 08 and Compact Encoding

 DHS required field that indicates that the cardholder has temporary lawful status = "1".
 */
var kPPLimitedDurationDocument = "Limited Duration Document Indicator";

/**
 Optional on AAMVA version 01.

 Issue Timestamp. A string used by some jurisdictions to validate the document against their data base.
 */
var kPPIssueTimestamp = "Issue Timestamp";

/**
 Optional on AAMVA version 01.

 Driver Permit Expiration Date. MMDDCCYY format. Date permit expires.
 */
var kPPPermitExpirationDate = "Driver Permit Expiration Date";

/**
 Optional on AAMVA version 01.

 Type of permit.
 */
var kPPPermitIdentifier = "Permit Identifier";

/**
 Optional on AAMVA version 01.

 Driver Permit Issue Date. MMDDCCYY format. Date permit was issued.
 */
var kPPPermitIssueDate = "Driver Permit Issue Date";

/**
 Optional on AAMVA version 01.

 Number of duplicate cards issued for a license or ID if any.
 */
var kPPNumberOfDuplicates = "Number of Duplicates";

/**
 Optional on AAMVA 02, 03, 04, 05, 06, 07, 08 and Compact Encoding

 A string of letters and/or numbers that identifies when, where, and by whom a driver
 license/ID card was made. If audit information is not used on the card or the MRT, it
 must be included in the driver record.
 */
var kPPAuditInformation = "Audit information";

/**
 Optional on AAMVA 04, 05, 06, 07, 08 and Compact Encoding

 DHS required field that indicates compliance: "M" = materially compliant;
 "F" = fully compliant; and, "N" = non-compliant.
 */
var kPPComplianceType = "Compliance Type";

/**
 Optional on AAMVA 04, 05, 06, 07, 08 and Compact Encoding

 Date on which the hazardous material endorsement granted by the document is
 no longer valid. MMDDCCYY format.
 */
var kPPHAZMATExpirationDate = "HAZMAT Endorsement Expiration Date";

/**
 Optional on AAMVA version 01.

 Medical Indicator/Codes.
 STATE SPECIFIC. Freeform; Standard "TBD"
 */
var kPPMedicalIndicator = "Medical Indicator/Codes";

/**
 Optional on AAMVA version 01.

 Non-Resident Indicator. "Y". Used by some jurisdictions to indicate holder of the document is a non-resident.
 */
var kPPNonResident = "Non-Resident Indicator";

/**
 Optional on AAMVA version 01.

 A number or alphanumeric string used by some jurisdictions to identify a "customer" across multiple data bases.
 */
var kPPUniqueCustomerId = "Unique Customer Identifier";

/**
 Optional on compact encoding.

 Document discriminator.
 */
var kPPDataDiscriminator = "Data discriminator";

// ****** 4. Keys for accessing raw barcode results

//==============================================================/
//====== 4. KEYS FOR ACCESSING RAW BARCODE RESULTS =============/
//==============================================================/

/**
 Raw pdf417 result
 */
var kPPPdf417 = "kPPPdf417";

/**
 Raw code128 result
 */
var kPPCode128 = "kPPCode128";

/**
 Raw code39 result
 */
var kPPCode39 = "kPPCode39";

