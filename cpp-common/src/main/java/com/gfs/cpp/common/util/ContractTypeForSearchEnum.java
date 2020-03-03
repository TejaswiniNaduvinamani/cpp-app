package com.gfs.cpp.common.util;

public enum ContractTypeForSearchEnum {

 // @formatter:off
    REGIONAL(1, "Regional-Original", "ICMDistributionAgreementRegional"),
    GPO(2, "GPOMaster-Original", "ICMGPOMasterAgreement"),
    STREET(3, "Street-Original", "ICMDistributionAgreementStreet"),
    NATIONAL(4, "National-Original", "ICMDistributionAgreementNational"),
    STREET_AMENDMENT(5, "Street-Amendment", "ICMDistributionAgreementStreetAmendment"),
    REGIONAL_AMENDMENT(6, "Regional-Amendment", "ICMDistributionAgreementRegionalAmendment"),
    GPO_AMENDMENT(7, "GPOMaster-Amendment", "ICMGPOMasterAgreementAmendment"),
    NATIONAL_AMENDMENT(8, "National-Amendment", "ICMDistributionAgreementNationalAmendment");
    
    
 // @formatter:on

    public final int code;
    public final String desc;
    public final String type;

    ContractTypeForSearchEnum(int code, String desc, String type) {
        this.code = code;
        this.desc = desc;
        this.type = type;
    }

    public final int getCode() {
        return code;
    }

    public final String getDesc() {
        return desc;
    }

    public final String getType() {
        return type;
    }

    public static String getDescByCode(int code) {
        for (ContractTypeForSearchEnum statusType : values()) {
            if (statusType.code == code) {
                return statusType.getDesc();
            }
        }
        return null;
    }

    public static String getTypeByDesc(String desc) {
        for (ContractTypeForSearchEnum statusType : values()) {
            if (statusType.desc == desc) {
                return statusType.getType();
            }
        }
        return null;
    }

    public static String getTypeByCode(int code) {
        for (ContractTypeForSearchEnum statusType : values()) {
            if (statusType.code == code) {
                return statusType.getType();
            }
        }
        return null;
    }
}
