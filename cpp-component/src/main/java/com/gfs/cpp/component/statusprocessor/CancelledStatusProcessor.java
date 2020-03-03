package com.gfs.cpp.component.statusprocessor;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.util.ContractPriceProfileStatus;

@Component
public class CancelledStatusProcessor implements CppStatusChangeProcessor {

    static final String UPDATE_USER_ID = "Cancelled Status Processor";

    @Override
    public boolean shouldUpdateStatus(ContractPriceProfileStatus cppStatus) {
        return cppStatus != ContractPriceProfileStatus.CANCELLED;
    }

    @Override
    public ContractPriceProfileStatus getUpdateToStatus() {
        return ContractPriceProfileStatus.CANCELLED;
    }

    @Override
    public String getUpdateUserId() {
        return UPDATE_USER_ID;
    }

    @Override
    public boolean expireRequired(ContractPriceProfileStatus cppStatus) {
        return cppStatus == ContractPriceProfileStatus.PRICING_ACTIVATED;
    }
}
