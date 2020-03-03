package com.gfs.cpp.acceptanceTests.config;

import java.util.Date;

import org.joda.time.LocalDate;

public class CukesConstants {
    public static final int CONTRACT_PRICE_PROFILE_SEQ = 999999999;
    public static final String GFS_FISCAL = "fiscal";
    public static final String SCHEDULE_FOR_COST_GREGORIAN = "gregorian";
    public static final String CLM_CONTRACT_TYPE_STREET = "ICMDistributionAgreementStreet";
    public static final String GREGORIAN_CALENDAR = "Gregorian Calendar";
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String ITEM_ID = "681204";
    public static final String ITEM_ID_TWO = "681205";
    public static final String EXCEPTION_NAME = "exception markup";
    public static final String DAYS_FROM_NOW = "days from now";
    public static final String NEW_EXCEPTION_NAME = "exception markup";
    public static final int FURTHERANCE_INITIATED_CODE = 1;

    public static final String CONTRACT_NAME = "contract name for cucumber test";
    public static final String VALID_CUSTOMER_NAME = "9999-contract name for cucumber test";
    public static final String CLM_CONTRACT_TYPE_REGIIONAL = "ICMDistributionAgreementRegional";
    public static final String MARKUP_TYPE_CMG = "CMG";
    public static final int CPP_ID = 999999999;
    public static final int EXISTING_CPP_ID = 999999998;

    public static final int CMG_CUSTOMER_TYPE_ID = 31;
    public static final String DEFAULT_CMG_CUSTOMER_ID = "2014";
    public static final int REAL_CUSTOMER_TYPE_ID = 30;
    public static final int REAL_CUSTOMER_TYPE_ID_CUSTOMER_UNIT = 0;
    public static final String REAL_CUSTOMER_ID = "9999";
    public static final int REAL_CUSTOMER_TYPE_ID_EXCEPTION = 32;
    public static final String REAL_CUSTOMER_ID_EXCEPTION = "999";
    public static final String GFS_CUSTOMER_ID_INACTIVE = "-2014";
    public static final String GFS_CUSTOMER_ID_INVALID = "-10100123";
    public static final String EXCEPTION_CUSTOMER_ID = "2096";
    public static final String CURRENT_USER_ID = "vc71u";

    public static final String MEMBER_ID_INACTIVE = "-123456";
    public static final String MEMBER_ID_ACTIVE = "123456";
    public static final String REAL_CUSTOMER_ID_WITH_INACTIVE_MEMBER = "-9999";

    public static final int DC_NUMBER = 11;
    public static final String DC_NAME = "Dallas";

    public static final int PRICE_ID = 681204;
    public static final int PRODUCT_PRICE_ID = 1;
    public static final String PRODUCT_PRICE_ID_ONE = "1";
    public static final String PRODUCT_PRICE_ID_TWO = "2";
    public static final String ITEM_PRICE_ID = "681204";
    public static final String ITEM_DESC = "Chicken Nuggets";
    public static final String ITEM_DESC_TWO = "Veg Nuggets";
    public static final String PRODUCT_TYPE = "5";
    public static final String MARKUP_VALUE = "10.43";
    public static final int PER_CASE_TYPE = 2;
    public static final int SELL_UNIT_TYPE = 1;

    public static final Date EFFECTIVE_DATE = new LocalDate(2015, 1, 1).toDate();
    public static final String DATE_FORMAT = "MM/dd/yyyy";

    public static final String DEFAULT_SPLITCASE_FEE = "5.00";

    public static final String DEFAULT_UNIT = "$";
    public static final String PERCENT_UNIT = "%";

    public static final String AGREEMENT_ID = "6faa275f-442d-4d14-aead-690756c4f25a";
    public static final String EXHIBIT_SYS_ID = "d407a41c-7c22-4468-a3af-9f69e79331e7";
    public static final String NEW_ITEM_ID_TO_ASSIGN = "999999";
    public static final String NEW_ITEM_ID_FOR_FURTHERANCE = "123456";
    public static final String FUTURE_ITEM_DESC = "Red Chilli";

    public static final String CPP_EXCEPTION_THROWN_ATTRIBUTE_NAME = "cpp_exception_thrown";
    public static final String CPP_ERROR_TYPE = "cpp_error_type";
    public static final String CPP_ERROR_CODE = "cpp_error_code";
    public static final String CPP_ERROR_MESSAGE = "cpp_error_message";

    public static final String AMENDMENT_AGREEMENT_ID = "amd6faa275f-442d-4d14-aead-690756c4f25b";
    public static final int VERSION_NBR = 1;

    public static final String CLM_CONTRACT_TYPE_REGIONAL_AMENDMENT = "ICMDistributionAgreementRegionalAmendment";
    public static final Date AMENDMENT_EFFECTIVE_DATE = new LocalDate(2026, 1, 1).toDate();
    public static final Date CLM_CONTRACT_START_DATE = new LocalDate(2017, 11, 1).toDate();
    public static final Date CLM_CONTRACT_END_DATE = new LocalDate(2099, 12, 31).toDate();
    public static final String UPDATED_CONTRACT_NAME = "updated contract name";

    public static final int PRODUCT_LEVEL_CODE = 2;
    public static final int ITEM_LEVEL_CODE = 0;
    public static final int SUBGROUP_LEVEL_CODE = 1;

    public static final String SUBGROUP_ID_VALID = "11";
    public static final String SUBGROUP_ID_TWO = "12";
    public static final String SUBGROUP_ID_INVALID = "166720";
    public static final String SUBGROUP_DESCRIPTION = "TABLETOP MISC.";
    public static final String SUBGROUP_DESCRIPTION_TWO = "FLOOR CARE";
    public static final String EMPTY_SUBGROUP_DESC = "";

    public static final String PARENT_AGREEMENT_ID = "6faa275f-442d-4d14-aead-690756c4f25a";
    public static final Date FURTHERANCE_EFFECTIVE_DATE = new LocalDate(9999, 1, 2).toDate();
    public static final String FURTHERANCE_REASON = "test reason";
    public static final String FURTHERANCE_REFERENCE_CONTACT = "test contact";
    public static final int CPP_FURTHERANCE_SEQ = 999999999;
    public static final Date UPDATED_FURTHERANCE_EFFECTIVE_DATE = new LocalDate(9999, 2, 3).toDate();
    public static final String UPDATED_FURTHERANCE_REASON = "updated test reason";
    public static final String UPDATED_FURTHERANCE_REFERENCE_CONTACT = "updated test contact";
    public static final String FURTHERANCE_ITEM_ID = "681205";
    public static final String FURTHERANCE_ITEM_DESC = "Furtehrance Item";

    public static final int COST_MODEL_ID = 71;
    public static final String COST_MODEL_NAME = "DEFAULT COST MODEL";
    public static final String COST_MODEL_PROFILE_SELECTION = "DEFAULT COST MODEL PROFILE";
    public static final int SAVED_COST_MODEL_ID = 70;
    public static final int UPDATE_COST_MODEL_ID_FOR_ITEM = 50;
    public static final int UPDATE_COST_MODEL_ID_FOR_SUBGROUP = 80;
    public static final int UPDATE_COST_MODEL_ID_FOR_PRODUCT_TYPE = 85;

}
