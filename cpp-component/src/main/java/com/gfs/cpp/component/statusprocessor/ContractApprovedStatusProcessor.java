package com.gfs.cpp.component.statusprocessor;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.util.ContractPriceProfileStatus;

@Component
public class ContractApprovedStatusProcessor implements CppStatusChangeProcessor {

    static final String UPDATE_USER_ID = "Contract Approved Status Processor";

    @Override
    public boolean shouldUpdateStatus(ContractPriceProfileStatus cppStatus) {
        return (cppStatus == ContractPriceProfileStatus.WAITING_FOR_APPROVAL || cppStatus == ContractPriceProfileStatus.DRAFT
                || cppStatus == ContractPriceProfileStatus.HOLD || cppStatus == ContractPriceProfileStatus.CANCELLED);
    }

    @Override
    public ContractPriceProfileStatus getUpdateToStatus() {
        return ContractPriceProfileStatus.CONTRACT_APPROVED;
    }

    @Override
    public String getUpdateUserId() {
        return UPDATE_USER_ID;
    }

    @Override
    public boolean expireRequired(ContractPriceProfileStatus cppStatus) {
        return false;
    }

}
