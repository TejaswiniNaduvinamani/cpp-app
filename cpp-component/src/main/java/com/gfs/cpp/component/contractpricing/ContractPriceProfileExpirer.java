package com.gfs.cpp.component.contractpricing;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgRepository;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;

@Component
public class ContractPriceProfileExpirer {

    private static final Logger logger = LoggerFactory.getLogger(ContractPriceProfileExpirer.class);

    @Autowired
    private PrcProfAuditAuthorityRepository prcProfAuditAuthorityRepository;
    @Autowired
    private PrcProfCostSchedulePkgRepository prcProfCostSchedulePkgRepository;
    @Autowired
    private PrcProfLessCaseRuleRepository prcProfLessCaseRuleRepository;
    @Autowired
    private PrcProfNonBrktCstMdlRepository prcProfNonBrktCstMdlRepository;
    @Autowired
    PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    public void expireAllNonCmgPriceProfileDataForContract(int contractPriceProfileSeq, Date expirationDate, String updatedUserId) {

        logger.info("Expiring price profile details with date {} for contract {}", expirationDate, contractPriceProfileSeq);

        prcProfAuditAuthorityRepository.expireNonCmgPriceProfAuditForContract(contractPriceProfileSeq, expirationDate, updatedUserId);
        prcProfCostSchedulePkgRepository.expireNonCmgCostSchedulePackageForContract(contractPriceProfileSeq, expirationDate, updatedUserId);
        prcProfLessCaseRuleRepository.expireNonCmgLessCaseRuleForContract(contractPriceProfileSeq, expirationDate, updatedUserId);
        prcProfNonBrktCstMdlRepository.expireNonCmgPriceProfileCostModelForContract(contractPriceProfileSeq, expirationDate, updatedUserId);
        prcProfPricingRuleOvrdRepository.expireNonCmgPriceProfRuleOverdForContract(contractPriceProfileSeq, expirationDate, updatedUserId);
    }
    
    public void expireAllPriceProfileDataForRealCust(Date expirationDate, String updatedUserId, String customerId, int customerTypeCode, Date newPricingEffectiveDate, Date newPricingExpiryDate) {
        logger.info("Expiring price profile details for the customer {} for customer Type  {} with date range {} - {}", customerId, customerTypeCode, newPricingEffectiveDate, newPricingExpiryDate);

        prcProfAuditAuthorityRepository.expirePriceProfAuditForRealCust(expirationDate, updatedUserId, customerId, customerTypeCode, newPricingEffectiveDate, newPricingExpiryDate);
        prcProfCostSchedulePkgRepository.expireCostSchedulePackageForRealCust(expirationDate, updatedUserId, customerId, customerTypeCode, newPricingEffectiveDate, newPricingExpiryDate);
        prcProfLessCaseRuleRepository.expireLessCaseRuleForRealCust(expirationDate, updatedUserId, customerId, customerTypeCode, newPricingEffectiveDate, newPricingExpiryDate);
        prcProfNonBrktCstMdlRepository.expirePriceProfileCostModelForRealCust(expirationDate, updatedUserId, customerId, customerTypeCode, newPricingEffectiveDate, newPricingExpiryDate);
        prcProfPricingRuleOvrdRepository.expirePriceProfRuleOverdForRealCust(expirationDate, updatedUserId, customerId, customerTypeCode, newPricingEffectiveDate, newPricingExpiryDate);
 
    }
    

}
