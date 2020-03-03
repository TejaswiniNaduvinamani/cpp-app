package com.gfs.cpp.common.dto.clm;

public enum ClmContractStatus {

    // @formatter:off

    DRAFT("Draft"),
    WAITING_FOR_APPROVAL("Waiting For Approval"),
    APPROVED("Approved"),
    WAITING_FOR_INTERNAL_SIGNATURE("Waiting for Internal Signature"),
    WAITING_FOR_EXTERNAL_SIGNATURE("Waiting for External Signature"),
    REVIEW_PENDING("Review Pending"),
    EXECUTED("Executed"),
    SUPERSEDED("Superseded"),
    TERMINATED("Terminated"),
    EXPIRED("Expired"),
    DELETED("Deleted"),
    CANCELLED("Cancelled"),
    ON_HOLD("On Hold");
    
    // @formatter:on

    public final String value;

    ClmContractStatus(String value) {
        this.value = value;
    }

}
