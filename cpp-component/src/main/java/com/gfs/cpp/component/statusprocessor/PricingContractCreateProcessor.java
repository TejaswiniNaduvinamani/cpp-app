package com.gfs.cpp.component.statusprocessor;

import com.gfs.cpp.common.dto.clm.ClmContractChangeEventDTO;

public interface PricingContractCreateProcessor {

    public void process(ClmContractChangeEventDTO contractChangeEvent);

}
