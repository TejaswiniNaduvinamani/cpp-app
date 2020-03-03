package com.gfs.cpp.common.dto.clm;

import java.util.HashMap;
import java.util.Map;

public enum ClmContractAction {

    // @formatter:off

    PUBLISHED("Published"),
    SENT_FOR_APPROVAL("SentForApproval"),
    UPDATED("Updated"),
    APPROVED("Approved"),
    REJECT("Rejected"),
    ON_HOLD("OnHold"),
    SENT_FOR_REVIEW("SentForInternalReview"),
    REVERT_ON_HOLD("RevertOnHold"),
    CANCELLED("Cancelled"),
    REVERT_CANCELLATION("RevertCancellation"),
    EXPIRED("Expired"),
    DELETED("Deleted"),
    TERMINATED("Terminated"),
    EXECUTED("Executed"),
    
    AMENDMENT_PUBLISHED("AmendmentPublished"),
    AMENDMENT_SENT_FOR_APPROVAL("AmendmentSentForApproval"),
    AMENDMENT_UPDATED("AmendmentUpdated"),
    AMENDMENT_APPROVED("AmendmentApproved"),
    AMENDMENT_REJECT("AmendmentRejected"),
    AMENDMENT_ON_HOLD("AmendmentOnHold"),
    AMENDMENT_REVERT_ON_HOLD("AmendmentRevertOnHold"),
    AMENDMENT_SENT_FOR_REVIEW("AmendmentSentForInternalReview"),
    AMENDMENT_CANCELLED("AmendmentCancelled"),
    AMENDMENT_REVERT_CANCELLATION("AmendmentRevertCancellation"),
    AMENDMENT_DELETED("AmendmentDeleted");
    
    // @formatter:on

    public final String value;

    ClmContractAction(String value) {
        this.value = value;
    }

    /** instanceMap */
    private static Map<String, ClmContractAction> valueMap = new HashMap<>();

    static {
        for (ClmContractAction inst : values()) {
            valueMap.put(inst.value, inst);
        }
    }

    public static ClmContractAction getByValue(String value) {
        return valueMap.get(value);
    }

}
