package com.gfs.cpp.component.statusprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.clm.ClmContractChangeEventDTO;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@Component
@Profile("!ignoreUpdate")
public class UpdateContractUrlProcessor implements PricingContractCreateProcessor {

    @Autowired
    private ClmApiProxy clmApiProxy;

    @Override
    public void process(ClmContractChangeEventDTO contractChangeEvent) {
        clmApiProxy.updateCppUrlForPricingContract(contractChangeEvent.getAgreementId(), contractChangeEvent.getContractType());
    }
}
