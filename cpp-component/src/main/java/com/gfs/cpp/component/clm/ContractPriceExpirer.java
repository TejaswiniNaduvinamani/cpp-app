package com.gfs.cpp.component.clm;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.contractpricing.ContractPriceProfileExpirer;
import com.gfs.cpp.component.contractpricing.CustomerItemPriceExpirer;

@Component
public class ContractPriceExpirer {

    private static final Logger logger = LoggerFactory.getLogger(ContractPriceExpirer.class);

    @Autowired
    private ContractPriceProfileExpirer contractPriceProfileExpirer;
    @Autowired
    private CustomerItemPriceExpirer customerItemPriceExpirer;
    @Autowired
    private CPPDateUtils cppDateUtils;

    public void expirePriceForContract(int contractPriceProfileSeq, String lastUpdateUserId) {

        logger.info("Expring pricing for {}", contractPriceProfileSeq);
        Date expirationDate = cppDateUtils.oneDayPreviousCurrentDate();
        expirePriceProfileForCPPSequence(contractPriceProfileSeq, expirationDate, lastUpdateUserId);
        expireCustomerItemPricingForCPPSequence(contractPriceProfileSeq, expirationDate, lastUpdateUserId);
    }

    public void expirePriceProfileForCPPSequence(int latestActivatedPricingCPPSeq, Date expirationDate, String lastUpdateUserId) {
        contractPriceProfileExpirer.expireAllNonCmgPriceProfileDataForContract(latestActivatedPricingCPPSeq, expirationDate, lastUpdateUserId);
    }

    public void expireCustomerItemPricingForCPPSequence(int latestActivatedPricingCPPSeq, Date expirationDate, String lastUpdateUserId) {
        customerItemPriceExpirer.expireNonCMGCustomerItemPrice(latestActivatedPricingCPPSeq, expirationDate, lastUpdateUserId);
    }

}
