import { environment } from './../../../environments/environment';

let imagePath = '../../../';
if (environment.production) {
              imagePath = './';
}

export const IMAGE_PATH = imagePath;

export const DATE_FORMAT_MMDDYYYY = 'MM/DD/YYYY';

export const DATE_FORMAT_YYYYMMDD = 'YYYY-MM-DD HH:mm:ss';

export const DATE_FORMAT_PIPE = 'MM/dd/yyyy';

export const MARKUP_PATTERN = /^([0-9]{0,})(([.]{1})([0-9]{1,}))?$/;

export const ITEM_ID_PATTERN = /[0-9]/;

export const SUBGROUP_ID_PATTERN = /[0-9]/;

export const BACKSPACE_KEY = 8;

export const ENTER_KEY = 13;

export const ZERO = 0;

export const ONE = 1;

export const YES = 'Yes';

export const NO = 'No';

export const ZERO_VALUE = '0.00';

export const OK = 200;

export const DUPLICATE_ITEM_ERROR_CODE = 150;

export const EXECUTED = 'Executed';

export const CONTRACT_APPROVED = 'Contract Approved';

export const FURTHERANCE_STEPPER_CODE = 1;

export enum DUPLICATE_VALIDATION_CODE {
  DUPLICATE_CODE = 112,
  CUSTOMER_ALREADY_MAPPED = 114,
  SUBGROUP_ALREADY_EXIST = 123
}

export enum VALIDATION_CODE {
  INVALID_CUSTOMER_FOUND = 117,
  INACTIVE_CUSTOMER_FOUND = 119,
  HAS_MULTIPLE_CUSTOMER_MAPPING = 113,
  CUSTOMER_NOT_MAPPED_TO_DEFAULT_CONCEPT = 121,
  CUSTOMER_UNIT_MAPPED_TO_DEFAULT_CONCEPT = 122,
  NOT_A_MEMBER_OF_DEFAULT_CUSTOMER = 116,
  INVALID_SUBGROUP = 124
}

export enum ITEM_ID_STATUS {
  VALID= 'valid',
  INVALID= 'invalid',
  INACTIVE= 'inactive',
  DUPLICATE= 'duplicate'
};

export enum STEPPER_NUMBERS {
  PRICING_INFORMATION= 1,
  DISTRIBUTION_CENTERS= 2,
  MARKUP= 3,
  SPLIT_CASE= 4,
  REVIEW= 5
};

export enum STEPPER_URL {
  PRICING_INFORMATION_URL= '/pricinginformation',
  DISTRIBUTION_CENTERS_URL= '/distributioncenters',
  MARKUP_URL= '/markup',
  SPLIT_CASE_URL= '/splitcasefee',
  REVIEW_URL= '/review'
};

export enum STEPPER_LABEL  {
  PRICING_INFORMATION= 'Contract Pricing Information',
  DISTRIBUTION_CENTERS= 'Distribution Centers',
  MARKUP= 'Markup',
  SPLIT_CASE= 'Split Case Fee',
  REVIEW= 'Review'
};

export enum CONTRACT_TYPES  {
  IFS= 'ICMDistributionAgreementStreet',
  DAN= 'ICMDistributionAgreementNational',
  DAR= 'ICMDistributionAgreementRegional',
  GPO= 'ICMGPOMasterAgreement',
  IFS_AMENDMENT= 'ICMDistributionAgreementStreetAmendment',
  DAN_AMENDMENT= 'ICMDistributionAgreementNationalAmendment',
  DAR_AMENDMENT= 'ICMDistributionAgreementRegionalAmendment',
  GPO_AMENDMENT= 'ICMGPOMasterAgreementAmendment'
};

export enum CONTRACT_TYPE_DISPLAY_NAMES {
  IFS= 'Independent Food Service (IFS)',
  DAN= 'Distribution Agreement National',
  DAR= 'Distribution Agreement Regional',
  GPO= 'GPO Master Agreement'
};

export enum CALENDAR_TYPES  {
  FISCAL_CALENDAR= 'fiscal',
  GREGORIAN_CALENDAR= 'gregorian'
}

export enum MARKUP_TYPES  {
  SELL_UNIT= 1,
  PER_CASE= 2
}

export enum UNIT_TYPES  {
  DOLLAR= '$',
  PERCENT= '%'
}

export enum TOGGLE_QUESTION  {
  YES= 'yes',
  NO= 'no'
}

export enum RETURN_TO_CLM {
  HTTP_LINK = 'https://gfsdev.icertis.com/Agreement/Details?entityName=',
  AND_ID = '&id='
}

export enum ERROR_TYPES  {
  ACCESS_DENIED = 'ACCESS_DENIED',
  CLM_STATUS_NOT_DRAFT = 'CLM_STATUS_NOT_DRAFT',
  NEW_CONTRACT_INVALID_STATUS = 'NEW_CONTRACT_INVALID_STATUS',
  EXPIRED_CONTRACT = 'EXPIRED_CONTRACT',
  CANNOT_CREATE_FURTHERANCE = 'CANNOT_CREATE_FURTHERANCE',
  IN_PROGRESS_FURTHERANCE_FOUND = 'IN_PROGRESS_FURTHERANCE_FOUND',
  INVALID_AMENDMENT_EFFECTIVE_DATE = 'INVALID_AMENDMENT_EFFECTIVE_DATE',
  RETURN_TO_CLM = 'RETURN_TO_CLM'
}
