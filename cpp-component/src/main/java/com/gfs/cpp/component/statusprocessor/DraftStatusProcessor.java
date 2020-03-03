package com.gfs.cpp.component.statusprocessor;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.util.ContractPriceProfileStatus;

@Component
public class DraftStatusProcessor implements CppStatusChangeProcessor {

    static final String UPDATE_USER_ID = "Draft Status Processor";

    @Override
    public ContractPriceProfileStatus getUpdateToStatus() {
        return ContractPriceProfileStatus.DRAFT;
    }

    @Override
    public boolean shouldUpdateStatus(ContractPriceProfileStatus cppStatus) {
        return cppStatus == ContractPriceProfileStatus.WAITING_FOR_APPROVAL || cppStatus == ContractPriceProfileStatus.CONTRACT_APPROVED
                || cppStatus == ContractPriceProfileStatus.HOLD || cppStatus == ContractPriceProfileStatus.CANCELLED;
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
