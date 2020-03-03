package com.gfs.cpp.common.util;

public enum ContractPriceProfileStatus {

    // @formatter:off
    
    DRAFT(10,"Draft"),
    HOLD(20,"On Hold"),
    WAITING_FOR_APPROVAL(30,"Waiting For Approval"),
    CONTRACT_APPROVED(40,"Contract Approved"),
    PRICING_ACTIVATED(50,"Pricing Activated"),
    EXPIRED(60,"Expired"),
    TERMINATED(70,"Terminated"),
    CANCELLED(80,"Cancelled"),
    DELETED(90,"Deleted");

    // @formatter:on

    public final int code;
    public final String desc;

    ContractPriceProfileStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public final int getCode() {
        return code;
    }

    public final String getDesc() {
        return desc;
    }

    public static ContractPriceProfileStatus getStatusByCode(int code) {
        for (ContractPriceProfileStatus statusType : values()) {
            if (statusType.code == code) {
                return statusType;
            }
        }
        return null;
    }

}
