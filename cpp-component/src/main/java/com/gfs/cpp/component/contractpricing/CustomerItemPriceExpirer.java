package com.gfs.cpp.component.contractpricing;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@Component
public class CustomerItemPriceExpirer {


    private static final Logger logger = LoggerFactory.getLogger(CustomerItemPriceExpirer.class);

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    public void expireNonCMGCustomerItemPrice(int contractPriceProfileSeq, Date expirationDate, String updatedUserId) {

        logger.info("Expring customer item price with date {} for contract {}", expirationDate, contractPriceProfileSeq);

        customerItemPriceRepository.expireNonCmgPriceForContract(contractPriceProfileSeq, expirationDate, updatedUserId);

    }

    public void expireItemPricingForRealCustomer(Date expirationDate, String updatedUserId, List<ProductTypeMarkupDO> productTypeMarkupDOList, Date newEffectiveDate, Date newExpirationDate) {
        customerItemPriceRepository.expireItemPricingForRealCustomer(expirationDate, updatedUserId, productTypeMarkupDOList, newEffectiveDate,newExpirationDate);
    }
}
