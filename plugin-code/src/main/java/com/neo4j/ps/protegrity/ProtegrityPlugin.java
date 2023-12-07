package com.neo4j.ps.protegrity;

import java.util.*;
import java.util.stream.*;

import org.neo4j.graphdb.*;
import org.neo4j.procedure.*;
import org.neo4j.logging.*;

import com.protegrity.ap.java.*;

public class ProtegrityPlugin
{

        @Context public GraphDatabaseService db;
        @Context public Log log;
        @Context public Transaction curTxn;


        @UserFunction("Protegrity.protectData")
        @Description("protectData - this function calls Protegrity to protect data according to the specified protection data element by name")
        public String protectData(
                        // @Name("UserName") String _username,
                        @Name("DataElement") String _dataElement,
                        @Name("DataValue") String _dataValue,
                        @Name(value = "doDebug", defaultValue = "true") Boolean doDebug
                        ) 
	{
		// initialize variables for Protegrity
		String[] inputStringArray = new String[1];
        String[] ProtectStringArray = new String[1];
		String protectedString = null;

		// instantiate call to protegrity protector
                Protector protector = null;
		try {
			protector = Protector.getProtector();
			if (doDebug) log.info("Protegrity Protector Instantiated");
		}
		catch (Exception e) {
			log.info("Couldn't create instantiate Protegrity Protector: %s",e);
		}

		// create a protegrity session
        SessionObject session = null;
		try {
			// get a protegrity session...hard coding the username to "neo4j" since that is the OS username
            // that the database (and protegrity application protector elements) is running under
            session = protector.createSession("neo4j");
			if (doDebug) log.info("Protegrity session created");
		}
		catch (Exception e) {
			log.info("Couldn't create create session with Protegrity: %s",e);
        }


		// call Protegrity to encrypt/protect data
                inputStringArray[0] = _dataValue;
		try {
			if(!protector.protect(session,_dataElement,inputStringArray,ProtectStringArray))
				log.info("Call to Protegrity protect API failed for element '%s' and value '%s'!!! \nError :%s",_dataElement,inputStringArray[0],protector.getLastError(session));
			protectedString = ProtectStringArray[0];
			if (doDebug) log.info("Call to Protegrity protect API succeeded for element '%s' and value '%s' --> protected value: '%s'!!!",_dataElement,inputStringArray[0],protectedString);
		}
                catch (Exception e) {
                        log.info("Couldn't invoke data proection using element '%s' and value '%s' -> error: %s",_dataElement,inputStringArray[0],e);
                }

		return protectedString;
	}





        @UserFunction("Protegrity.unprotectData")
        @Description("unprotectData - this function calls Protegrity to unprotect data according to the specified protection data element by name")
        public String unprotectData(
                        // @Name("UserName") String _username,
                        @Name("DataElement") String _dataElement,
                        @Name("ProtectedValue") String _protectedValue,
                        @Name(value = "doDebug", defaultValue = "true") Boolean doDebug
                        )
        {
                // initialize variables for Protegrity
                String[] inputStringArray = new String[1];
                String[] UnProtectStringArray = new String[1];
                String unprotectedString = null;

                // instantiate call to protegrity protector
                Protector protector = null;
                try {
                        protector = Protector.getProtector();
                        if (doDebug) log.info("Protegrity Protector Instantiated");
                }
                catch (Exception e) {
                        log.info("Couldn't create instantiate Protegrity Protector: %s",e);
                }

                // create a protegrity session
                SessionObject session = null;
                try {
                        // get a protegrity session...hard coding the username to "neo4j" since that is the OS username
                        // that the database (and protegrity application protector elements) is running under
                        session = protector.createSession("neo4j");
                        if (doDebug) log.info("Protegrity session created");
                }
                catch (Exception e) {
                        log.info("Couldn't create create session with Protegrity: %s",e);
                }


                // call Protegrity to encrypt/protect data
                inputStringArray[0] = _protectedValue;
                try {
                        if(!protector.unprotect(session,_dataElement,inputStringArray,UnProtectStringArray))
                                log.info("Call to Protegrity protect API failed for element '%s' and value '%s'!!! \nError :%s",_dataElement,inputStringArray[0],protector.getLastError(session));
                        unprotectedString = UnProtectStringArray[0];
                        if (doDebug) log.info("Call to Protegrity protect API succeeded for element '%s' and value '%s' --> protected value: '%s'!!!",_dataElement,inputStringArray[0],unprotectedString);
                }
                catch (Exception e) {
                        log.info("Couldn't invoke data proection using element '%s' and value '%s' -> error: %s",_dataElement,inputStringArray[0],e);
                }

                return unprotectedString;
        }


        //##################################################################################################################


        @UserFunction("Protegrity.PROTECT_ACCOUNT_NUM")
        @Description("PROTECT_ACCOUNT_NUM - this function calls Protegrity to protect data for account numbers")
        public String PROTECT_ACCOUNT_NUM(
                @Name("AccountNumber") String accountNumber
        ) {
                // The DataElement is hardcoded based on the mapping table
                String dataElement = "dtStrNPL"; // The element used for account numbers
                // Call the protectData function with the hardcoded element and the passed account number
                return this.protectData(dataElement, accountNumber, true);
        }

        @UserFunction("Protegrity.UNPROTECT_ACCOUNT_NUM")
        @Description("UNPROTECT_ACCOUNT_NUM - this function calls Protegrity to unprotect data for account numbers")
        public String UNPROTECT_ACCOUNT_NUM(
                @Name("ProtectedAccountNumber") String protectedAccountNumber
        ) {
                // The DataElement is hardcoded based on the mapping table
                String dataElement = "dtStrNPL"; // The element used for account numbers
                // Call the unprotectData function with the hardcoded element and the passed account number
                return this.unprotectData(dataElement, protectedAccountNumber, true);
        }

        @UserFunction("Protegrity.PROTECT_NAME")
        @Description("PROTECT_NAME - this function calls Protegrity to protect data for names")
        public String PROTECT_NAME(
                @Name("Name") String name
        ) {
                // The DataElement is hardcoded based on the mapping table
                String dataElement = "dtStrNPL"; // The element used for names
                // Call the protectData function with the hardcoded element and the passed name
                return protectData(dataElement, name, true);
        }

        @UserFunction("Protegrity.UNPROTECT_NAME")
        @Description("UNPROTECT_NAME - this function calls Protegrity to unprotect data for names")
        public String UNPROTECT_NAME(
                @Name("ProtectedName") String protectedName
        ) {
                // The DataElement is hardcoded based on the mapping table
                String dataElement = "dtStrNPL"; // The element used for names
                // Call the unprotectData function with the hardcoded element and the passed name
                return unprotectData(dataElement, protectedName, true);
        }

        @UserFunction("Protegrity.PROTECT_FIRST_NAME")
        @Description("PROTECT_FIRST_NAME - Protects first names")
        public String PROTECT_FIRST_NAME(@Name("FirstName") String firstName) {
        String dataElement = "dtStrNPL"; // Element for first names
        return protectData(dataElement, firstName, true);
        }

        @UserFunction("Protegrity.UNPROTECT_FIRST_NAME")
        @Description("UNPROTECT_FIRST_NAME - Unprotects first names")
        public String UNPROTECT_FIRST_NAME(@Name("ProtectedFirstName") String protectedFirstName) {
        String dataElement = "dtStrNPL"; // Element for first names
        return unprotectData(dataElement, protectedFirstName, true);
        }

        @UserFunction("Protegrity.PROTECT_LAST_NAME")
        @Description("PROTECT_LAST_NAME - Protects last names")
        public String PROTECT_LAST_NAME(@Name("LastName") String lastName) {
        String dataElement = "dtStrNPL"; // Element for last names
        return protectData(dataElement, lastName, true);
        }

        @UserFunction("Protegrity.UNPROTECT_LAST_NAME")
        @Description("UNPROTECT_LAST_NAME - Unprotects last names")
        public String UNPROTECT_LAST_NAME(@Name("ProtectedLastName") String protectedLastName) {
        String dataElement = "dtStrNPL"; // Element for last names
        return unprotectData(dataElement, protectedLastName, true);
        }

        @UserFunction("Protegrity.PROTECT_MAIDEN_NAME")
        @Description("PROTECT_MAIDEN_NAME - Protects maiden names")
        public String PROTECT_MAIDEN_NAME(@Name("MaidenName") String maidenName) {
        String dataElement = "dtStrNPL"; // Element for maiden names
        return protectData(dataElement, maidenName, true);
        }

        @UserFunction("Protegrity.UNPROTECT_MAIDEN_NAME")
        @Description("UNPROTECT_MAIDEN_NAME - Unprotects maiden names")
        public String UNPROTECT_MAIDEN_NAME(@Name("ProtectedMaidenName") String protectedMaidenName) {
        String dataElement = "dtStrNPL"; // Element for maiden names
        return unprotectData(dataElement, protectedMaidenName, true);
        }

        // PROTECT_MOTHER_MAIDEN_NAME and UNPROTECT_MOTHER_MAIDEN_NAME
        @UserFunction("Protegrity.PROTECT_MOTHER_MAIDEN_NAME")
        @Description("PROTECT_MOTHER_MAIDEN_NAME - Protects mother's maiden names")
        public String PROTECT_MOTHER_MAIDEN_NAME(@Name("MotherMaidenName") String motherMaidenName) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, motherMaidenName, true);
        }

        @UserFunction("Protegrity.UNPROTECT_MOTHER_MAIDEN_NAME")
        @Description("UNPROTECT_MOTHER_MAIDEN_NAME - Unprotects mother's maiden names")
        public String UNPROTECT_MOTHER_MAIDEN_NAME(@Name("ProtectedMotherMaidenName") String protectedMotherMaidenName) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedMotherMaidenName, true);
        }

        // PROTECT_CARD_EXPIRY_DATE and UNPROTECT_CARD_EXPIRY_DATE
        @UserFunction("Protegrity.PROTECT_CARD_EXPIRY_DATE")
        @Description("PROTECT_CARD_EXPIRY_DATE - Protects card expiry dates")
        public String PROTECT_CARD_EXPIRY_DATE(@Name("CardExpiryDate") String cardExpiryDate) {
        String dataElement = "dtDateYYYYMMDD";
        return protectData(dataElement, cardExpiryDate, true);
        }

        @UserFunction("Protegrity.UNPROTECT_CARD_EXPIRY_DATE")
        @Description("UNPROTECT_CARD_EXPIRY_DATE - Unprotects card expiry dates")
        public String UNPROTECT_CARD_EXPIRY_DATE(@Name("ProtectedCardExpiryDate") String protectedCardExpiryDate) {
        String dataElement = "dtDateYYYYMMDD";
        return unprotectData(dataElement, protectedCardExpiryDate, true);
        }

        // PROTECT_CARD_EXPIRY_DATE_YYMM and UNPROTECT_CARD_EXPIRY_DATE_YYMM
        @UserFunction("Protegrity.PROTECT_CARD_EXPIRY_DATE_YYMM")
        @Description("PROTECT_CARD_EXPIRY_DATE_YYMM - Protects card expiry dates in YYMM format")
        public String PROTECT_CARD_EXPIRY_DATE_YYMM(@Name("CardExpiryDateYYMM") String cardExpiryDateYYMM) {
        String dataElement = "dtNumNPL";
        return protectData(dataElement, cardExpiryDateYYMM, true);
        }

        @UserFunction("Protegrity.UNPROTECT_CARD_EXPIRY_DATE_YYMM")
        @Description("UNPROTECT_CARD_EXPIRY_DATE_YYMM - Unprotects card expiry dates in YYMM format")
        public String UNPROTECT_CARD_EXPIRY_DATE_YYMM(@Name("ProtectedCardExpiryDateYYMM") String protectedCardExpiryDateYYMM) {
        String dataElement = "dtNumNPL";
        return unprotectData(dataElement, protectedCardExpiryDateYYMM, true);
        }

        // PROTECT_DOB and UNPROTECT_DOB
        @UserFunction("Protegrity.PROTECT_DOB")
        @Description("PROTECT_DOB - Protects dates of birth")
        public String PROTECT_DOB(@Name("DOB") String dob) {
        String dataElement = "dtDoBYYYY-MM-DD";
        return protectData(dataElement, dob, true);
        }

        @UserFunction("Protegrity.UNPROTECT_DOB")
        @Description("UNPROTECT_DOB - Unprotects dates of birth")
        public String UNPROTECT_DOB(@Name("ProtectedDOB") String protectedDob) {
        String dataElement = "dtDoBYYYY-MM-DD";
        return unprotectData(dataElement, protectedDob, true);
        }

        // PROTECT_DOB_YEAR and UNPROTECT_DOB_YEAR
        @UserFunction("Protegrity.PROTECT_DOB_YEAR")
        @Description("PROTECT_DOB_YEAR - Protects year of birth")
        public String PROTECT_DOB_YEAR(@Name("DOBYear") String dobYear) {
        String dataElement = "dtNumNPL";
        return protectData(dataElement, dobYear, true);
        }

        @UserFunction("Protegrity.UNPROTECT_DOB_YEAR")
        @Description("UNPROTECT_DOB_YEAR - Unprotects year of birth")
        public String UNPROTECT_DOB_YEAR(@Name("ProtectedDOBYear") String protectedDobYear) {
        String dataElement = "dtNumNPL";
        return unprotectData(dataElement, protectedDobYear, true);
        }

        // PROTECT_DOB_MONTH and UNPROTECT_DOB_MONTH
        @UserFunction("Protegrity.PROTECT_DOB_MONTH")
        @Description("PROTECT_DOB_MONTH - Protects month of birth")
        public String PROTECT_DOB_MONTH(@Name("DOBMonth") String dobMonth) {
        String dataElement = "dtNumNPL";
        return protectData(dataElement, dobMonth, true);
        }

        @UserFunction("Protegrity.UNPROTECT_DOB_MONTH")
        @Description("UNPROTECT_DOB_MONTH - Unprotects month of birth")
        public String UNPROTECT_DOB_MONTH(@Name("ProtectedDOBMonth") String protectedDobMonth) {
        String dataElement = "dtNumNPL";
        return unprotectData(dataElement, protectedDobMonth, true);
        }

        // PROTECT_DOB_MONTH_YEAR and UNPROTECT_DOB_MONTH_YEAR
        @UserFunction("Protegrity.PROTECT_DOB_MONTH_YEAR")
        @Description("PROTECT_DOB_MONTH_YEAR - Protects month and year of birth")
        public String PROTECT_DOB_MONTH_YEAR(@Name("DOBMonthYear") String dobMonthYear) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, dobMonthYear, true);
        }

        @UserFunction("Protegrity.UNPROTECT_DOB_MONTH_YEAR")
        @Description("UNPROTECT_DOB_MONTH_YEAR - Unprotects month and year of birth")
        public String UNPROTECT_DOB_MONTH_YEAR(@Name("ProtectedDOBMonthYear") String protectedDobMonthYear) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedDobMonthYear, true);
        }

        // PROTECT_DRIVING_LICENSE and UNPROTECT_DRIVING_LICENSE
        @UserFunction("Protegrity.PROTECT_DRIVING_LICENSE")
        @Description("PROTECT_DRIVING_LICENSE - Protects driving license numbers")
        public String PROTECT_DRIVING_LICENSE(@Name("DrivingLicense") String drivingLicense) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, drivingLicense, true);
        }

        @UserFunction("Protegrity.UNPROTECT_DRIVING_LICENSE")
        @Description("UNPROTECT_DRIVING_LICENSE - Unprotects driving license numbers")
        public String UNPROTECT_DRIVING_LICENSE(@Name("ProtectedDrivingLicense") String protectedDrivingLicense) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedDrivingLicense, true);
        }

        // PROTECT_SSN and UNPROTECT_SSN
        @UserFunction("Protegrity.PROTECT_SSN")
        @Description("PROTECT_SSN - Protects Social Security Numbers")
        public String PROTECT_SSN(@Name("SSN") String ssn) {
        String dataElement = "dtSSN123456789";
        return protectData(dataElement, ssn, true);
        }

        @UserFunction("Protegrity.UNPROTECT_SSN")
        @Description("UNPROTECT_SSN - Unprotects Social Security Numbers")
        public String UNPROTECT_SSN(@Name("ProtectedSSN") String protectedSsn) {
        String dataElement = "dtSSN123456789";
        return unprotectData(dataElement, protectedSsn, true);
        }

        // PROTECT_SSN_LAST4 and UNPROTECT_SSN_LAST4
        @UserFunction("Protegrity.PROTECT_SSN_LAST4")
        @Description("PROTECT_SSN_LAST4 - Protects last 4 digits of Social Security Numbers")
        public String PROTECT_SSN_LAST4(@Name("SSNLast4") String ssnLast4) {
        String dataElement = "dtSSN1234";
        return protectData(dataElement, ssnLast4, true);
        }

        @UserFunction("Protegrity.UNPROTECT_SSN_LAST4")
        @Description("UNPROTECT_SSN_LAST4 - Unprotects last 4 digits of Social Security Numbers")
        public String UNPROTECT_SSN_LAST4(@Name("ProtectedSSNLast4") String protectedSsnLast4) {
        String dataElement = "dtSSN1234";
        return unprotectData(dataElement, protectedSsnLast4, true);
        }

        // PROTECT_PIN and UNPROTECT_PIN
        @UserFunction("Protegrity.PROTECT_PIN")
        @Description("PROTECT_PIN - Protects Personal Identification Numbers")
        public String PROTECT_PIN(@Name("PIN") String pin) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, pin, true);
        }

        @UserFunction("Protegrity.UNPROTECT_PIN")
        @Description("UNPROTECT_PIN - Unprotects Personal Identification Numbers")
        public String UNPROTECT_PIN(@Name("ProtectedPIN") String protectedPin) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedPin, true);
        }

        // PROTECT_PASSWORD and UNPROTECT_PASSWORD
        @UserFunction("Protegrity.PROTECT_PASSWORD")
        @Description("PROTECT_PASSWORD - Protects passwords")
        public String PROTECT_PASSWORD(@Name("Password") String password) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, password, true);
        }

        @UserFunction("Protegrity.UNPROTECT_PASSWORD")
        @Description("UNPROTECT_PASSWORD - Unprotects passwords")
        public String UNPROTECT_PASSWORD(@Name("ProtectedPassword") String protectedPassword) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedPassword, true);
        }

        // PROTECT_TIN and UNPROTECT_TIN
        @UserFunction("Protegrity.PROTECT_TIN")
        @Description("PROTECT_TIN - Protects Tax Identification Numbers")
        public String PROTECT_TIN(@Name("TIN") String tin) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, tin, true);
        }

        @UserFunction("Protegrity.UNPROTECT_TIN")
        @Description("UNPROTECT_TIN - Unprotects Tax Identification Numbers")
        public String UNPROTECT_TIN(@Name("ProtectedTIN") String protectedTin) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedTin, true);
        }

        // PROTECT_PASSPORT_NUM and UNPROTECT_PASSPORT_NUM
        @UserFunction("Protegrity.PROTECT_PASSPORT_NUM")
        @Description("PROTECT_PASSPORT_NUM - Protects passport numbers")
        public String PROTECT_PASSPORT_NUM(@Name("PassportNum") String passportNum) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, passportNum, true);
        }

        @UserFunction("Protegrity.UNPROTECT_PASSPORT_NUM")
        @Description("UNPROTECT_PASSPORT_NUM - Unprotects passport numbers")
        public String UNPROTECT_PASSPORT_NUM(@Name("ProtectedPassportNum") String protectedPassportNum) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedPassportNum, true);
        }

        // PROTECT_ADDRESS and UNPROTECT_ADDRESS
        @UserFunction("Protegrity.PROTECT_ADDRESS")
        @Description("PROTECT_ADDRESS - Protects addresses")
        public String PROTECT_ADDRESS(@Name("Address") String address) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, address, true);
        }

        @UserFunction("Protegrity.UNPROTECT_ADDRESS")
        @Description("UNPROTECT_ADDRESS - Unprotects addresses")
        public String UNPROTECT_ADDRESS(@Name("ProtectedAddress") String protectedAddress) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedAddress, true);
        }

        // PROTECT_CITY and UNPROTECT_CITY
        @UserFunction("Protegrity.PROTECT_CITY")
        @Description("PROTECT_CITY - Protects city names")
        public String PROTECT_CITY(@Name("City") String city) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, city, true);
        }

        @UserFunction("Protegrity.UNPROTECT_CITY")
        @Description("UNPROTECT_CITY - Unprotects city names")
        public String UNPROTECT_CITY(@Name("ProtectedCity") String protectedCity) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedCity, true);
        }

        // PROTECT_STATE and UNPROTECT_STATE
        @UserFunction("Protegrity.PROTECT_STATE")
        @Description("PROTECT_STATE - Protects state names")
        public String PROTECT_STATE(@Name("State") String state) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, state, true);
        }

        @UserFunction("Protegrity.UNPROTECT_STATE")
        @Description("UNPROTECT_STATE - Unprotects state names")
        public String UNPROTECT_STATE(@Name("ProtectedState") String protectedState) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedState, true);
        }

        // PROTECT_COUNTRY and UNPROTECT_COUNTRY
        @UserFunction("Protegrity.PROTECT_COUNTRY")
        @Description("PROTECT_COUNTRY - Protects country names")
        public String PROTECT_COUNTRY(@Name("Country") String country) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, country, true);
        }

        @UserFunction("Protegrity.UNPROTECT_COUNTRY")
        @Description("UNPROTECT_COUNTRY - Unprotects country names")
        public String UNPROTECT_COUNTRY(@Name("ProtectedCountry") String protectedCountry) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedCountry, true);
        }

        // PROTECT_ZIP_CODE and UNPROTECT_ZIP_CODE
        @UserFunction("Protegrity.PROTECT_ZIP_CODE")
        @Description("PROTECT_ZIP_CODE - Protects zip codes")
        public String PROTECT_ZIP_CODE(@Name("ZipCode") String zipCode) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, zipCode, true);
        }

        @UserFunction("Protegrity.UNPROTECT_ZIP_CODE")
        @Description("UNPROTECT_ZIP_CODE - Unprotects zip codes")
        public String UNPROTECT_ZIP_CODE(@Name("ProtectedZipCode") String protectedZipCode) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedZipCode, true);
        }

        // Protect service date and unprotect service date
        @UserFunction("Protegrity.PROTECT_SERVICE_DATE")
        @Description("PROTECT_SERVICE_DATE - Protects service dates")
        public String PROTECT_SERVICE_DATE(@Name("ServiceDate") String serviceDate) {
        String dataElement = "dtDateYYYYMMDD";
        return protectData(dataElement, serviceDate, true);
        }

        @UserFunction("Protegrity.UNPROTECT_SERVICE_DATE")
        @Description("UNPROTECT_SERVICE_DATE - Unprotects service dates")
        public String UNPROTECT_SERVICE_DATE(@Name("ProtectedServiceDate") String protectedServiceDate) {
        String dataElement = "dtDateYYYYMMDD";
        return unprotectData(dataElement, protectedServiceDate, true);
        }

        // hire date
        @UserFunction("Protegrity.PROTECT_HIRE_DATE")
        @Description("PROTECT_HIRE_DATE - Protects hire dates")
        public String PROTECT_HIRE_DATE(@Name("HireDate") String hireDate) {
        String dataElement = "dtDateYYYYMMDD";
        return protectData(dataElement, hireDate, true);
        }

        @UserFunction("Protegrity.UNPROTECT_HIRE_DATE")
        @Description("UNPROTECT_HIRE_DATE - Unprotects hire dates")
        public String UNPROTECT_HIRE_DATE(@Name("ProtectedHireDate") String protectedHireDate) {
        String dataElement = "dtDateYYYYMMDD";
        return unprotectData(dataElement, protectedHireDate, true);
        }

        // original hire date
        @UserFunction("Protegrity.PROTECT_ORIGINAL_HIRE_DATE")
        @Description("PROTECT_ORIGINAL_HIRE_DATE - Protects original hire dates")
        public String PROTECT_ORIGINAL_HIRE_DATE(@Name("OriginalHireDate") String originalHireDate) {
        String dataElement = "dtDateYYYYMMDD";
        return protectData(dataElement, originalHireDate, true);
        }

        @UserFunction("Protegrity.UNPROTECT_ORIGINAL_HIRE_DATE")
        @Description("UNPROTECT_ORIGINAL_HIRE_DATE - Unprotects original hire dates")
        public String UNPROTECT_ORIGINAL_HIRE_DATE(@Name("ProtectedOriginalHireDate") String protectedOriginalHireDate) {
        String dataElement = "dtDateYYYYMMDD";
        return unprotectData(dataElement, protectedOriginalHireDate, true);
        }

        // termination date
        @UserFunction("Protegrity.PROTECT_TERMINATION_DATE")
        @Description("PROTECT_TERMINATION_DATE - Protects termination dates")
        public String PROTECT_TERMINATION_DATE(@Name("TerminationDate") String terminationDate) {
        String dataElement = "dtDateYYYYMMDD";
        return protectData(dataElement, terminationDate, true);
        }

        @UserFunction("Protegrity.UNPROTECT_TERMINATION_DATE")
        @Description("UNPROTECT_TERMINATION_DATE - Unprotects termination dates")
        public String UNPROTECT_TERMINATION_DATE(@Name("ProtectedTerminationDate") String protectedTerminationDate) {
        String dataElement = "dtDateYYYYMMDD";
        return unprotectData(dataElement, protectedTerminationDate, true);
        }

        // termination entry date
        @UserFunction("Protegrity.PROTECT_TERMINATION_ENTRY_DATE")
        @Description("PROTECT_TERMINATION_ENTRY_DATE - Protects termination entry dates")
        public String PROTECT_TERMINATION_ENTRY_DATE(@Name("TerminationEntryDate") String terminationEntryDate) {
        String dataElement = "dtDateYYYYMMDD";
        return protectData(dataElement, terminationEntryDate, true);
        }

        @UserFunction("Protegrity.UNPROTECT_TERMINATION_ENTRY_DATE")
        @Description("UNPROTECT_TERMINATION_ENTRY_DATE - Unprotects termination entry dates")
        public String UNPROTECT_TERMINATION_ENTRY_DATE(@Name("ProtectedTerminationEntryDate") String protectedTerminationEntryDate) {
        String dataElement = "dtDateYYYYMMDD";
        return unprotectData(dataElement, protectedTerminationEntryDate, true);
        }

        // sso
        @UserFunction("Protegrity.PROTECT_SSO")
        @Description("PROTECT_SSO - Protects SSOs")
        public String PROTECT_SSO(@Name("SSO") String sso) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, sso, true);
        }

        @UserFunction("Protegrity.UNPROTECT_SSO")
        @Description("UNPROTECT_SSO - Unprotects SSOs")
        public String UNPROTECT_SSO(@Name("ProtectedSSO") String protectedSso) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedSso, true);
        }

        // ip addr
        @UserFunction("Protegrity.PROTECT_IP_ADDR")
        @Description("PROTECT_IP_ADDR - Protects IP addresses")
        public String PROTECT_IP_ADDR(@Name("IPAddr") String ipAddr) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, ipAddr, true);
        }

        @UserFunction("Protegrity.UNPROTECT_IP_ADDR")
        @Description("UNPROTECT_IP_ADDR - Unprotects IP addresses")
        public String UNPROTECT_IP_ADDR(@Name("ProtectedIPAddr") String protectedIpAddr) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedIpAddr, true);
        }

        // user id
        @UserFunction("Protegrity.PROTECT_USER_ID")
        @Description("PROTECT_USER_ID - Protects user IDs")
        public String PROTECT_USER_ID(@Name("UserID") String userId) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, userId, true);
        }

        @UserFunction("Protegrity.UNPROTECT_USER_ID")
        @Description("UNPROTECT_USER_ID - Unprotects user IDs")
        public String UNPROTECT_USER_ID(@Name("ProtectedUserID") String protectedUserId) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedUserId, true);
        }

        // merchant id
        @UserFunction("Protegrity.PROTECT_MERCHANT_ID")
        @Description("PROTECT_MERCHANT_ID - Protects merchant IDs")
        public String PROTECT_MERCHANT_ID(@Name("MerchantID") String merchantId) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, merchantId, true);
        }

        @UserFunction("Protegrity.UNPROTECT_MERCHANT_ID")
        @Description("UNPROTECT_MERCHANT_ID - Unprotects merchant IDs")
        public String UNPROTECT_MERCHANT_ID(@Name("ProtectedMerchantID") String protectedMerchantId) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedMerchantId, true);
        }

        // card sec code
        @UserFunction("Protegrity.PROTECT_CARD_SEC_CODE")
        @Description("PROTECT_CARD_SEC_CODE - Protects card security codes")
        public String PROTECT_CARD_SEC_CODE(@Name("CardSecCode") String cardSecCode) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, cardSecCode, true);
        }

        @UserFunction("Protegrity.UNPROTECT_CARD_SEC_CODE")
        @Description("UNPROTECT_CARD_SEC_CODE - Unprotects card security codes")
        public String UNPROTECT_CARD_SEC_CODE(@Name("ProtectedCardSecCode") String protectedCardSecCode) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedCardSecCode, true);
        }

        // phone
        @UserFunction("Protegrity.PROTECT_PHONE")
        @Description("PROTECT_PHONE - Protects phone numbers")
        public String PROTECT_PHONE(@Name("Phone") String phone) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, phone, true);
        }

        @UserFunction("Protegrity.UNPROTECT_PHONE")
        @Description("UNPROTECT_PHONE - Unprotects phone numbers")
        public String UNPROTECT_PHONE(@Name("ProtectedPhone") String protectedPhone) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedPhone, true);
        }

        // email id
        @UserFunction("Protegrity.PROTECT_EMAIL_ID")
        @Description("PROTECT_EMAIL_ID - Protects email IDs")
        public String PROTECT_EMAIL_ID(@Name("EmailID") String emailId) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, emailId, true);
        }

        // misc contact info
        @UserFunction("Protegrity.PROTECT_MISC_CONTACT_INFO")
        @Description("PROTECT_MISC_CONTACT_INFO - Protects miscellaneous contact information")
        public String PROTECT_MISC_CONTACT_INFO(@Name("MiscContactInfo") String miscContactInfo) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, miscContactInfo, true);
        }

        @UserFunction("Protegrity.UNPROTECT_MISC_CONTACT_INFO")
        @Description("UNPROTECT_MISC_CONTACT_INFO - Unprotects miscellaneous contact information")
        public String UNPROTECT_MISC_CONTACT_INFO(@Name("ProtectedMiscContactInfo") String protectedMiscContactInfo) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedMiscContactInfo, true);
        }

        // misc number npl
        @UserFunction("Protegrity.PROTECT_MISC_NUMBER_NPL")
        @Description("PROTECT_MISC_NUMBER_NPL - Protects miscellaneous numbers")
        public String PROTECT_MISC_NUMBER_NPL(@Name("MiscNumberNPL") String miscNumberNPL) {
        String dataElement = "dtNumNPL";
        return protectData(dataElement, miscNumberNPL, true);
        }

        @UserFunction("Protegrity.UNPROTECT_MISC_NUMBER_NPL")
        @Description("UNPROTECT_MISC_NUMBER_NPL - Unprotects miscellaneous numbers")
        public String UNPROTECT_MISC_NUMBER_NPL(@Name("ProtectedMiscNumberNPL") String protectedMiscNumberNPL) {
        String dataElement = "dtNumNPL";
        return unprotectData(dataElement, protectedMiscNumberNPL, true);
        }

        // misc number
        @UserFunction("Protegrity.PROTECT_MISC_NUMBER")
        @Description("PROTECT_MISC_NUMBER - Protects miscellaneous numbers")
        public String PROTECT_MISC_NUMBER(@Name("MiscNumber") String miscNumber) {
        String dataElement = "dtNumNPL";
        return protectData(dataElement, miscNumber, true);
        }

        @UserFunction("Protegrity.UNPROTECT_MISC_NUMBER")
        @Description("UNPROTECT_MISC_NUMBER - Unprotects miscellaneous numbers")
        public String UNPROTECT_MISC_NUMBER(@Name("ProtectedMiscNumber") String protectedMiscNumber) {
        String dataElement = "dtNumNPL";
        return unprotectData(dataElement, protectedMiscNumber, true);
        }

        // misc string
        @UserFunction("Protegrity.PROTECT_MISC_STRING")
        @Description("PROTECT_MISC_STRING - Protects miscellaneous strings")
        public String PROTECT_MISC_STRING(@Name("MiscString") String miscString) {
        String dataElement = "dtStrNPL";
        return protectData(dataElement, miscString, true);
        }

        @UserFunction("Protegrity.UNPROTECT_MISC_STRING")
        @Description("UNPROTECT_MISC_STRING - Unprotects miscellaneous strings")
        public String UNPROTECT_MISC_STRING(@Name("ProtectedMiscString") String protectedMiscString) {
        String dataElement = "dtStrNPL";
        return unprotectData(dataElement, protectedMiscString, true);
        }

        // misc date
        @UserFunction("Protegrity.PROTECT_MISC_DATE")
        @Description("PROTECT_MISC_DATE - Protects miscellaneous dates")
        public String PROTECT_MISC_DATE(@Name("MiscDate") String miscDate) {
        String dataElement = "dtDateYYYYMMDD";
        return protectData(dataElement, miscDate, true);
        }

        @UserFunction("Protegrity.UNPROTECT_MISC_DATE")
        @Description("UNPROTECT_MISC_DATE - Unprotects miscellaneous dates")
        public String UNPROTECT_MISC_DATE(@Name("ProtectedMiscDate") String protectedMiscDate) {
        String dataElement = "dtDateYYYYMMDD";
        return unprotectData(dataElement, protectedMiscDate, true);
        }

}
