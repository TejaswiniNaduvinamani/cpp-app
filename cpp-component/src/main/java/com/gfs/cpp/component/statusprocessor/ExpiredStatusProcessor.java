package com.gfs.cpp.component.statusprocessor;

import org.springframework.stereotype.Component;

import com.gfs.cpp.common.util.ContractPriceProfileStatus;

@Component
public class ExpiredStatusProcessor implements CppStatusChangeProcessor {

    static final String UPDATE_USER_ID = "Expired Status Processor";

    @Override
    public boolean shouldUpdateStatus(ContractPriceProfileStatus cppStatus) {
        return cppStatus != ContractPriceProfileStatus.EXPIRED;
    }

    @Override
    public ContractPriceProfileStatus getUpdateToStatus() {
        return ContractPriceProfileStatus.EXPIRED;
    }

    @Override
    public String getUpdateUserId() {
        return UPDATE_USER_ID;
    }

    @Override
    public boolean expireRequired(ContractPriceProfileStatus cppStatus) {
        return true;
    }

}
