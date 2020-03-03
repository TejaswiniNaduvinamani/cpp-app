package com.gfs.cpp.component.statusprocessor;

import com.gfs.cpp.common.util.ContractPriceProfileStatus;

public interface CppStatusChangeProcessor {

    boolean shouldUpdateStatus(ContractPriceProfileStatus cppStatus);

    ContractPriceProfileStatus getUpdateToStatus();

    String getUpdateUserId();
    
    boolean expireRequired(ContractPriceProfileStatus cppStatus);

}
