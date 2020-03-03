package com.gfs.cpp.common.constants;

public class CPPConstants {

    private CPPConstants() {
    }

    /***********
     * Constants for SOAP service call to fetch distribution centers *
     **************/

    public static final String STATUS_CODE = "AC";
    public static final int COMPANY_NUMBER = 1;
    public static final String LANGUAGE_CODE = "en";
    public static final String DCCODES_TO_EXCLUDE = "distributionCenter.dc.exclude";

    /***********
     * Constants for database call to save the first page data *
     *************/

    public static final String CONTRACT_PRICE_PROFILE_ID = "contractPriceProfileId";
    public static final String VERSION_NUMBER = "versionNumber";
    public static final String CONTRACT_PRICE_PROFILE_SEQ = "contractPriceProfileSeq";
    public static final String ROWS_UPDATED = "noOfRowsUpdated";
    public static final String SCHEDULE_FOR_COST_GREGORIAN = "gregorian";
    public static final String DATE_FORMAT = "MM/dd/yyyy";
    public static final int INDICATOR_ZERO = 0;
    public static final int INDICATOR_ONE = 1;
    public static final int SCHEDULE_GROUP_SEQ_GREGORIAN_W = 100;
    public static final int SCHEDULE_GROUP_SEQ_GREGORIAN_M = 105;
    public static final int SCHEDULE_GROUP_SEQ_GFS_FISCAL_W = 100;
    public static final int SCHEDULE_GROUP_SEQ_GFS_FISCAL_M = 101;
    public static final String RESPONSE_RECEIVED = "responseCode";
    public static final String SCHEDULE_FOR_COST_GFS_FISCAL = "fiscal";
    public static final String VALIDATION_MESSAGE = "validationMessage";
    public static final String VALIDATION_CODE = "validationCode";
    public static final String DUPLICATE_CODE = "112";
    public static final String INVALID_CODE = "111";

    /***********
     * Constants for service call to get customer id *
     *************/

    public static final String CUSTOMER_GROUP_CMG = "CMG";
    public static final String HYPHEN = "-";
    public static final String DUMMY_USER = "cpp_user";
    public static final String SLASH = "/";

    /*********
     * Constants for PA System service call to get product type *
     ***********/

    public static final String PRODUCTS_TO_EXCLUDE = "productCodes.exclude";

    /***********
     * Constants for markups *
     *************/
    public static final int INDICATOR_TWO = 2;
    public static final int INDICATOR_THREE = 3;
    public static final String MARKUP_FORMAT = "%.2f";
    public static final int MARKUP_BASED_ON_SELL_OVERRIDE_ID = 7;
    public static final String MARKUP_ON_SELL = "markupOnSell";
    public static final String EXPIRE_LOWER = "expireLower";

    /*********
     * Constants for System service call to get SplitCase Table *
     ***********/

    public static final String SPLIT_CASE_FEE = "35.00";
    public static final int CMG_CUSTOMER_TYPE_CODE = 31;
    public static final String SPLIT_CASE_ITEM_PRICE_LEVEL_CODE = "2";
    public static final String LESSCASE_PRICE_RULE_ID = "2";
    public static final String SPLIT_CASE_LAST_UPDATE_USER_ID = "CPP_USER";
    public static final double EMPTY_SPLIT_CASE_FEE = 0.00;
    public static final String AMOUNT_FORMAT = "0.00##";
    public static final String DELIMITER_COMMA = ",";
    public static final String SPLIT_CASE_CONTRACT_LANGUAGE_COLUMN_DESC = "Split Case Fee Type";

    /*********
     * Constants for System service call to get Review *
     ***********/
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String REVIEW_DEFAULT = "DEFAULT";
    public static final String GFS_FISCAL_CALNEDAR = "GFS Fiscal Calendar";
    public static final String GREGORIAN_CALENDAR = "Gregorian Calendar";
    public static final String ZERO_INDICATOR = "0";
    public static final String ONE_INDICATOR = "1";
    public static final String TWO_INDICATOR = "2";
    public static final String THREE_INDICATOR = "3";
    public static final String COST_SCHEDULE_PACK = "Cost Schedule Package";
    public static final String FORMAL_PRICE_AUDIT_PRVG = "Formal Price Audit Privileges";
    public static final String PRICE_VERIFICATION_PRVG = "Price Verification privileges";
    public static final String COST_MODEL = "Cost Model";
    public static final int DEFAULT_SUBGROUP_COST_MODEL = 10;
    public static final int DEFAULT_PRODUCT_COST_MODEL = 10;
    public static final int VALUE_THIRTEEN = 13;
    public static final int DEFAULT_ITEM_COST_MODEL = 50;
    public static final String MARKUP_BASED_ON_SELL = "Markup based on sell?";
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String CPP_USER = "Aaron Mockridge";
    public static final String MARKUP_TYPE = "Markup Type";

    /***********
     * Constants for assignment *
     *************/
    public static final String ENABLE_ACTIVATE_PRICING_BUTTON = "enableActivatePricingButton";

    /*********
     * Constants for System service call to get Docx *
     ***********/
    public static final String DOCX = ".docx";
    public static final int DOCX_BORDER_SIZE = 4;
    public static final int DOCX_FOOTER_SIZE = 9;
    public static final int DOCX_FOOTER_TAB_ONE_LENGTH = 4500;
    public static final int DOCX_FOOTER_TAB_TWO_LENGTH = 9000;
    public static final int DOCX_HEADER_FONT_SIZE = 11;
    public static final String DEFAULT_DOCX_NAME = "CPP-Contract";
    public static final String UNDERSCORE = "_";
    public static final String DOCX_FONT = "Times New Roman";
    public static final int DOCX_FONT_SIZE = 10;
    public static final String DOCX_FOOTER_VERSION = "Version ";
    public static final String DOCX_PSWD = "Docx.password";
    public static final String DOCX_CENTER = "center";
    public static final String DOCX_RIGHT = "right";
    public static final String DOCX_SELL_PRICE = ".            Sell Price.";
    public static final String DOCX_MARKUPS = ".            Mark-Ups.";
    public static final String DOCX_ITEM = " Item ";
    public static final String DOCX_MARKUP = " Mark-Up ";
    public static final String DOCX_MARKUP_TYPE = " Mark-Up Type ";
    public static final String DOCX_SPLITCASE = " Split Case Fee ";
    public static final String DOCX_COST = ".            Cost of Products.";
    public static final String DOCX_AUDIT = ".            Price Audit.";
    public static final String DOCX_DURATION = ".            Duration of GFS Cost.";
    public static final String DOCX_PRICING = "PRICING SCHEDULE";
    public static final String DOCX_DIST = ".            Distribution Centers.";
    public static final String DOCX_CATEGORY = " Category";
    public static final String DOCX_SELL_UNIT = " Sell Unit";
    public static final String DOCX_PER_CASE = " Per Case";
    public static final String DOCX_PER_WEIGHT = " Per Weight";
    public static final String DOCX_SPLITCASE_FEE = ".            Split Case Fee";

    /***********
     * Constants for Future Effective and Expiration Date *
     *************/
    public static final String FUTURE_DATE = "01/01/9999";

    /***********
     * Constants for JUnit *
     *************/
    public static final String MESSAGE = "msg";
    public static final String JUNIT_STATUS_CODE = "statusCd";
    public static final int RESULT_FOUND_CODE = 200;
    public static final int INTERNAL_ERROR_CODE = 500;
    public static final String RESPONSE_200 = "RESULT FOUND";
    public static final String RESPONSE_500 = "SOME INTERNAL ERROR OCCURRED";

    /***********
     * Constants for Item query Service *
     *************/
    public static final String ITEM_QUERY_HOST_URL = "item.service.url";
    public static final String CUSTOMER_NAME = "customerName";

    /***********
     * Constants for CLM Integration *
     *************/
    public static final String CREATE_ACTION = "Created";
    public static final String CLM_URL_KEY = "clmUrlKey";
    public static final String RETURN_CLM_URL = "clm.url";

    /********
     * Constants for Furtherance
     */
    public static final String SPLIT_CASE_TABLE_NAMES = "PRC_PROF_LESSCASE_RULE";
    public static final String CIP_TABLE_NAMES = "CUSTOMER_ITEM_PRICE";
    public static final String DOCX_FURTHERANCE = "Furtherance Document";
    public static final String DOCX_FURTHERANCE_EFFECTIVE_DATE = "Furtherance Effective Date (mm/dd/yyyy): ";
    public static final String DOCX_FURTHERANCE_REASON_FOR_CHANGE = "Reason for Change";
    public static final String DOCX_FURTHERANCE_CONTACT_REFERENCE = "Contract Reference";
    public static final String DOCX_FURTHERANCE_MARK_UP = "Mark-ups";
    public static final String DOCX_FURTHERANCE_SPLITCASE_FEE = "Split Case Fee";
    public static final String DOCX_FURTHERANCE_NAME = "Furtherance-";

    public static final int MAX_CONTRACT_NAME_LENGTH = 245;
    public static final int MAX_CONTRACT_NAME_LENGTH_FOR_CMG = 30;

    public static final String SEARCH_LINK_FOR_PRICING = "/pricinginformation?agreementId=";
    public static final String SEARCH_LINK_FOR_FURTHERANCE = "/furtheranceinformation?agreementId=";
    public static final String SEARCH_LINK_PARAM = "&contractType=";
}
