package com.gfs.cpp.component.statusprocessor;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.util.ContractPriceProfileStatus;

@Component
public class WaitingForApprovalStatusProcessor implements CppStatusChangeProcessor {

    static final String UPDATE_USER_ID = "Waiting For Approval Status Processor";

    @Override
    public boolean shouldUpdateStatus(ContractPriceProfileStatus cppStatus) {
        return (cppStatus == ContractPriceProfileStatus.CONTRACT_APPROVED || cppStatus == ContractPriceProfileStatus.DRAFT
                || cppStatus == ContractPriceProfileStatus.HOLD || cppStatus == ContractPriceProfileStatus.CANCELLED);
    }

    @Override
    public ContractPriceProfileStatus getUpdateToStatus() {
        return ContractPriceProfileStatus.WAITING_FOR_APPROVAL;
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
