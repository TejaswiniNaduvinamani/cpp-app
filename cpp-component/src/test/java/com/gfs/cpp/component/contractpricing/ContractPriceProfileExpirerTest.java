package com.gfs.cpp.component.contractpricing;

import static org.mockito.Mockito.verify;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.component.contractpricing.ContractPriceProfileExpirer;
import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgRepository;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;

@RunWith(MockitoJUnitRunner.class)
public class ContractPriceProfileExpirerTest {

    @InjectMocks
    private ContractPriceProfileExpirer target;

    @Mock
    private PrcProfAuditAuthorityRepository prcProfAuditAuthorityRepository;
    @Mock
    private PrcProfCostSchedulePkgRepository prcProfCostSchedulePkgRepository;
    @Mock
    private PrcProfLessCaseRuleRepository prcProfLessCaseRuleRepository;
    @Mock
    private PrcProfNonBrktCstMdlRepository prcProfNonBrktCstMdlRepository;
    @Mock
    PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    @Test
    public void shouldExpireAllPriceProfileDataForContract() throws Exception {

        int contractPriceProfileSeq = -101;
        Date expirationDate = new Date();
        String updatedUserId = "update user";

        target.expireAllNonCmgPriceProfileDataForContract(contractPriceProfileSeq, expirationDate, updatedUserId);

        verify(prcProfAuditAuthorityRepository).expireNonCmgPriceProfAuditForContract(contractPriceProfileSeq, expirationDate, updatedUserId);
        verify(prcProfCostSchedulePkgRepository).expireNonCmgCostSchedulePackageForContract(contractPriceProfileSeq, expirationDate, updatedUserId);
        verify(prcProfLessCaseRuleRepository).expireNonCmgLessCaseRuleForContract(contractPriceProfileSeq, expirationDate, updatedUserId);
        verify(prcProfNonBrktCstMdlRepository).expireNonCmgPriceProfileCostModelForContract(contractPriceProfileSeq, expirationDate, updatedUserId);
        verify(prcProfPricingRuleOvrdRepository).expireNonCmgPriceProfRuleOverdForContract(contractPriceProfileSeq, expirationDate, updatedUserId);

    }

    @Test
    public void shouldExpireAllPriceProfileDataForRealCust() throws Exception {

        int customerTypeCode = 2;
        Date expirationDate = new Date();
        String updatedUserId = "update user";
        String customerId = "1";
        Date newPricingEffectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date newPricingExpiryDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);

        target.expireAllPriceProfileDataForRealCust(expirationDate, updatedUserId, customerId, customerTypeCode, newPricingEffectiveDate,
                newPricingExpiryDate);

        verify(prcProfAuditAuthorityRepository).expirePriceProfAuditForRealCust(expirationDate, updatedUserId, customerId, customerTypeCode,
                newPricingEffectiveDate, newPricingExpiryDate);
        verify(prcProfCostSchedulePkgRepository).expireCostSchedulePackageForRealCust(expirationDate, updatedUserId, customerId, customerTypeCode,
                newPricingEffectiveDate, newPricingExpiryDate);
        verify(prcProfLessCaseRuleRepository).expireLessCaseRuleForRealCust(expirationDate, updatedUserId, customerId, customerTypeCode,
                newPricingEffectiveDate, newPricingExpiryDate);
        verify(prcProfNonBrktCstMdlRepository).expirePriceProfileCostModelForRealCust(expirationDate, updatedUserId, customerId, customerTypeCode,
                newPricingEffectiveDate, newPricingExpiryDate);
        verify(prcProfPricingRuleOvrdRepository).expirePriceProfRuleOverdForRealCust(expirationDate, updatedUserId, customerId, customerTypeCode,
                newPricingEffectiveDate, newPricingExpiryDate);
    }

}
