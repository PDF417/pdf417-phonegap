package com.phonegap.plugins.pdf417;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.microblink.entities.recognizers.blinkbarcode.usdl.USDLKeys;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by igrce on 14/02/2018.
 */

public final class USDLMapping {

    private Map<USDLKeys, String> mKeyMappings;

    private static volatile USDLMapping mInstance;

    private USDLMapping() {
        mKeyMappings = new HashMap<USDLKeys, String>();

        mKeyMappings.put(USDLKeys.DocumentType, "Document Type");
        mKeyMappings.put(USDLKeys.StandardVersionNumber, "Standard Version Number");
        mKeyMappings.put(USDLKeys.CustomerFamilyName, "Customer Family Name");
        mKeyMappings.put(USDLKeys.CustomerFirstName, "Customer First Name");
        mKeyMappings.put(USDLKeys.CustomerFullName, "Customer Name");
        mKeyMappings.put(USDLKeys.DateOfBirth, "Date of Birth");
        mKeyMappings.put(USDLKeys.Sex, "Sex");
        mKeyMappings.put(USDLKeys.EyeColor, "Eye Color");
        mKeyMappings.put(USDLKeys.AddressStreet, "Address - Street 1");
        mKeyMappings.put(USDLKeys.AddressCity, "Address - City");
        mKeyMappings.put(USDLKeys.AddressJurisdictionCode, "Address - Jurisdiction Code");
        mKeyMappings.put(USDLKeys.AddressPostalCode, "Address - Postal Code");
        mKeyMappings.put(USDLKeys.FullAddress, "Full Address");
        mKeyMappings.put(USDLKeys.Height, "Height");
        mKeyMappings.put(USDLKeys.HeightIn, "Height in");
        mKeyMappings.put(USDLKeys.HeightCm, "Height cm");
        mKeyMappings.put(USDLKeys.CustomerMiddleName, "Customer Middle Name");
        mKeyMappings.put(USDLKeys.HairColor, "Hair color");
        mKeyMappings.put(USDLKeys.NameSuffix, "Name Suffix");
        mKeyMappings.put(USDLKeys.AKAFullName, "Alias / AKA Name");
        mKeyMappings.put(USDLKeys.AKAFamilyName, "Alias / AKA Family Name");
        mKeyMappings.put(USDLKeys.AKAGivenName, "Alias / AKA Given Name");
        mKeyMappings.put(USDLKeys.AKASuffixName, "Alias / AKA Suffix Name");
        mKeyMappings.put(USDLKeys.WeightRange, "Weight Range");
        mKeyMappings.put(USDLKeys.WeightPounds, "Weight (pounds)");
        mKeyMappings.put(USDLKeys.WeightKilograms, "Weight (kilograms)");
        mKeyMappings.put(USDLKeys.CustomerIdNumber, "Customer ID Number");
        mKeyMappings.put(USDLKeys.FamilyNameTruncation, "Family name truncation");
        mKeyMappings.put(USDLKeys.FirstNameTruncation, "First name truncation");
        mKeyMappings.put(USDLKeys.MiddleNameTruncation, "Middle name truncation");
        mKeyMappings.put(USDLKeys.PlaceOfBirth, "Place of birth");
        mKeyMappings.put(USDLKeys.AddressStreet2, "Address - Street 2");
        mKeyMappings.put(USDLKeys.RaceEthnicity, "Race / ethnicity");
        mKeyMappings.put(USDLKeys.NamePrefix, "Name Prefix");
        mKeyMappings.put(USDLKeys.CountryIdentification, "Country Identification");
        mKeyMappings.put(USDLKeys.ResidenceStreetAddress, "Driver Residence Street Address");
        mKeyMappings.put(USDLKeys.ResidenceStreetAddress2, "Driver Residence Street Address 2");
        mKeyMappings.put(USDLKeys.ResidenceCity, "Driver Residence City");
        mKeyMappings.put(USDLKeys.ResidenceJurisdictionCode, "Driver Residence Jurisdiction Code");
        mKeyMappings.put(USDLKeys.ResidencePostalCode, "Driver Residence Postal Code");
        mKeyMappings.put(USDLKeys.ResidenceFullAddress, "Driver Residence Full Address");
        mKeyMappings.put(USDLKeys.Under18, "Under 18 Until");
        mKeyMappings.put(USDLKeys.Under19, "Under 19 Until");
        mKeyMappings.put(USDLKeys.Under21, "Under 21 Until");
        mKeyMappings.put(USDLKeys.SocialSecurityNumber, "Social Security Number");
        mKeyMappings.put(USDLKeys.AKASocialSecurityNumber, "Alias / AKA Social Security Number");
        mKeyMappings.put(USDLKeys.AKAMiddleName, "Alias / AKA Middle Name");
        mKeyMappings.put(USDLKeys.AKAPrefixName, "Alias / AKA Prefix Name");
        mKeyMappings.put(USDLKeys.OrganDonor, "Organ Donor Indicator");
        mKeyMappings.put(USDLKeys.Veteran, "Veteran Indicator");
        mKeyMappings.put(USDLKeys.AKADateOfBirth, "Alias / AKA Date of Birth");
        mKeyMappings.put(USDLKeys.IssuerIdentificationNumber, "Issuer Identification Number");
        mKeyMappings.put(USDLKeys.DocumentExpirationDate, "Document Expiration Date");
        mKeyMappings.put(USDLKeys.JurisdictionVersionNumber, "Jurisdiction Version Number");
        mKeyMappings.put(USDLKeys.JurisdictionVehicleClass, "Jurisdiction-specific vehicle class");
        mKeyMappings.put(USDLKeys.JurisdictionRestrictionCodes, "Jurisdiction-specific restriction codes");
        mKeyMappings.put(USDLKeys.JurisdictionEndorsementCodes, "Jurisdiction-specific endorsement codes");
        mKeyMappings.put(USDLKeys.DocumentIssueDate, "Document Issue Date");
        mKeyMappings.put(USDLKeys.FederalCommercialVehicleCodes, "Federal Commercial Vehicle Codes");
        mKeyMappings.put(USDLKeys.IssuingJurisdiction, "Issuing jurisdiction");
        mKeyMappings.put(USDLKeys.StandardVehicleClassification, "Standard vehicle classification");
        mKeyMappings.put(USDLKeys.IssuingJurisdictionName, "Issuing jurisdiction name");
        mKeyMappings.put(USDLKeys.StandardEndorsementCode, "Standard endorsement code");
        mKeyMappings.put(USDLKeys.StandardRestrictionCode, "Standard restriction code");
        mKeyMappings.put(USDLKeys.JurisdictionVehicleClassificationDescription, "Jurisdiction-specific vehicle classification description");
        mKeyMappings.put(USDLKeys.JurisdictionEndorsmentCodeDescription, "Jurisdiction-specific endorsment code description");
        mKeyMappings.put(USDLKeys.JurisdictionRestrictionCodeDescription, "Jurisdiction-spacific restriction code description");
        mKeyMappings.put(USDLKeys.InventoryControlNumber, "Inventory control number");
        mKeyMappings.put(USDLKeys.CardRevisionDate, "Card Revision Date");
        mKeyMappings.put(USDLKeys.DocumentDiscriminator, "Document Discriminator");
        mKeyMappings.put(USDLKeys.LimitedDurationDocument, "Limited Duration Document Indicator");
        mKeyMappings.put(USDLKeys.AuditInformation, "Audit information");
        mKeyMappings.put(USDLKeys.ComplianceType, "Compliance Type");
        mKeyMappings.put(USDLKeys.IssueTimestamp, "Issue Timestamp");
        mKeyMappings.put(USDLKeys.PermitExpirationDate, "Driver Permit Expiration Date");
        mKeyMappings.put(USDLKeys.PermitIdentifier, "Permit Identifier");
        mKeyMappings.put(USDLKeys.PermitIssueDate, "Driver Permit Issue Date");
        mKeyMappings.put(USDLKeys.NumberOfDuplicates, "Number of Duplicates");
        mKeyMappings.put(USDLKeys.HAZMATExpirationDate, "HAZMAT Endorsement Expiration Date");
        mKeyMappings.put(USDLKeys.MedicalIndicator, "Medical Indicator/Codes");
        mKeyMappings.put(USDLKeys.NonResident, "Non-Resident Indicator");
        mKeyMappings.put(USDLKeys.UniqueCustomerId, "Unique Customer Identifier");
        mKeyMappings.put(USDLKeys.DataDiscriminator, "Data discriminator");
        mKeyMappings.put(USDLKeys.DocumentExpirationMonth, "Document Expiration Month");
        mKeyMappings.put(USDLKeys.DocumentNonexpiring, "Document Nonexpiring");
        mKeyMappings.put(USDLKeys.SecurityVersion, "Security Version");

    }

    public static USDLMapping getInstance() {
        if (mInstance == null) {
            synchronized (USDLMapping.class) {
                if (mInstance == null) {
                    mInstance = new USDLMapping();
                }
            }
        }
        return mInstance;
    }

    @Nullable
    public String mapUSDLKey(@NonNull USDLKeys key) {
        return mKeyMappings.get(key);
    }
}
