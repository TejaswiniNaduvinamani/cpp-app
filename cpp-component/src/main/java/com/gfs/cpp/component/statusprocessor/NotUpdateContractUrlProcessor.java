package com.gfs.cpp.component.statusprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.clm.ClmContractChangeEventDTO;

@Component
@Profile("ignoreUpdate")
public class NotUpdateContractUrlProcessor implements PricingContractCreateProcessor {

    private static final Logger logger = LoggerFactory.getLogger(NotUpdateContractUrlProcessor.class);

    @Override
    public void process(ClmContractChangeEventDTO contractChangeEvent) {
        logger.info("Ignoring url update for {}:", contractChangeEvent.getAgreementId());
    }

}
