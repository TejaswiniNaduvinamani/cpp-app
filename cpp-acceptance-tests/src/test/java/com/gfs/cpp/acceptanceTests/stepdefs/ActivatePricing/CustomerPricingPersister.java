package com.gfs.cpp.acceptanceTests.stepdefs.ActivatePricing;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getContractPriceProfileSeq;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getExistingContractPriceProfileSeq;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.corp.component.price.common.types.PriceLockinReason;
import com.gfs.cpp.acceptanceTests.common.data.AcceptableRealCustomer;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.model.auditauthority.PrcProfAuditAuthorityDO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgDO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgScheduledGroupDO;
import com.gfs.cpp.common.model.markup.PrcProfNonBrktCstMdlDO;
import com.gfs.cpp.common.model.markup.PrcProfPricingRuleOvrdDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.common.model.splitcase.PrcProfLessCaseRuleDO;
import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostRunSchedGroupRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgRepository;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;

@Component(value = "customerPricingPersister")
public class CustomerPricingPersister {

    @Autowired
    private PrcProfCostRunSchedGroupRepository prcProfCostRunSchedGroupRepository;

    @Autowired
    private PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    @Autowired
    private PrcProfAuditAuthorityRepository prcProfAuditAuthorityRepository;

    @Autowired
    private PrcProfNonBrktCstMdlRepository prcProfNonBrktCstMdlRepository;

    @Autowired
    private PrcProfLessCaseRuleRepository prcProfLessCaseRuleRepository;

    @Autowired
    private PrcProfCostSchedulePkgRepository prcProfCostSchedulePkgRepository;

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    public void savePricingForExistingContract(Date effectiveDate, Date expirationDate) throws Exception {

        savePricingInPrcProfForExistingContract(effectiveDate, expirationDate);
        saveCIPEntriesForRealCustomer(effectiveDate, expirationDate, PriceLockinReason.COMPETITIVE.getCode(), ItemPriceLevel.PRODUCT_TYPE.getCode(),
                getExistingContractPriceProfileSeq(),CukesConstants.PRODUCT_PRICE_ID);
    }

    public void savePricingForExistingContractWithBidReasonCode(Date effectiveDate, Date expirationDate, int itemId) throws Exception {

        saveCIPEntriesForRealCustomer(effectiveDate, expirationDate, PriceLockinReason.AWARDED_BID.getCode(), ItemPriceLevel.ITEM.getCode(),
                getExistingContractPriceProfileSeq(),itemId);
    }

    public void savePricingForItemMarkupForExistingContractWithoutBidReasonCode(Date effectiveDate, Date expirationDate, int itemId) throws Exception {

        saveCIPEntriesForRealCustomer(effectiveDate, expirationDate, PriceLockinReason.COMPETITIVE.getCode(), ItemPriceLevel.ITEM.getCode(),
                getExistingContractPriceProfileSeq(),itemId);
    }

    public void savePricingForCustomerForAnItemMarkupForExistingContractWithoutBidReasonCode(Date effectiveDate, Date expirationDate, int itemId) throws Exception {

        saveCIPEntriesForRealCustomer(effectiveDate, expirationDate, PriceLockinReason.COMPETITIVE.getCode(), ItemPriceLevel.ITEM.getCode(),
                getExistingContractPriceProfileSeq(),itemId);
    }
    
    public void savePricingForRealUnitTypeCustomerForAnItemMarkupForExistingContractWithBidReasonCode(Date effectiveDate, Date expirationDate, int itemId) throws Exception {

        saveCIPEntriesForRealCustomerUnit(effectiveDate, expirationDate, PriceLockinReason.AWARDED_BID.getCode(), ItemPriceLevel.ITEM.getCode(),
                getExistingContractPriceProfileSeq(),itemId);
    }
    
    private void savePricingInPrcProfForExistingContract(Date effectiveDate, Date expirationDate) {
        PrcProfAuditAuthorityDO prcProfAuditAuthorityDO = savePrcProfAuditAuthority(effectiveDate, expirationDate);
        saveCostSchedulePkgAndPkgGroup(effectiveDate, expirationDate, prcProfAuditAuthorityDO);
        savePrcProfNonBrktCstMdl(effectiveDate, expirationDate);
        savePrcProfPricingRuleOvrd(effectiveDate, expirationDate);
        savePrcProfLessCaseRule(effectiveDate, expirationDate);
    }

    public static void copyProperties(Object src, Object trg) {

        BeanWrapper scrObject = new BeanWrapperImpl(src);
        BeanWrapper trgObject = new BeanWrapperImpl(trg);

        PropertyDescriptor[] pdList = scrObject.getPropertyDescriptors();
        for (PropertyDescriptor pd : pdList) {
            if (scrObject.isWritableProperty(pd.getName())) {
                if (trgObject.isReadableProperty(pd.getName())) {
                    trgObject.setPropertyValue(pd.getName(), scrObject.getPropertyValue(pd.getName()));
                }
            }
        }
    }

    public PrcProfAuditAuthorityDO savePrcProfAuditAuthority(Date effectiveDate, Date expirationDate) {
        List<PrcProfAuditAuthorityDO> prcProfAuditAuthorityDOList = new ArrayList<>();
        PrcProfAuditAuthorityDO prcProfAuditAuthorityDO = new PrcProfAuditAuthorityDO();

        prcProfAuditAuthorityDO.setContractPriceProfileSeq(getExistingContractPriceProfileSeq());
        prcProfAuditAuthorityDO.setCreateUserId(CPPConstants.DUMMY_USER);
        prcProfAuditAuthorityDO.setEffectiveDate(effectiveDate);
        prcProfAuditAuthorityDO.setExpirationDate(expirationDate);
        prcProfAuditAuthorityDO.setGfsCustomerId(CukesConstants.REAL_CUSTOMER_ID);
        prcProfAuditAuthorityDO.setGfsCustomerTypeCode(CukesConstants.REAL_CUSTOMER_TYPE_ID);
        prcProfAuditAuthorityDO.setLastUpdateUserId(CPPConstants.DUMMY_USER);
        prcProfAuditAuthorityDO.setPrcProfAuditAuthorityInd(1);

        prcProfAuditAuthorityDOList.add(prcProfAuditAuthorityDO);

        prcProfAuditAuthorityRepository.savePriceProfileAuditForCustomer(prcProfAuditAuthorityDOList);

        return prcProfAuditAuthorityDO;
    }

    public void saveCostSchedulePkgAndPkgGroup(Date effectiveDate, Date expirationDate, PrcProfAuditAuthorityDO prcProfAuditAuthorityDO) {

        int pkgcostScehdeleSeqNumerForCustomer = prcProfCostSchedulePkgRepository.fetchPrcProfileCostSchedulePkgNextSeq();
        List<PrcProfCostSchedulePkgDO> prcProfCostSchedulePkgDOList = new ArrayList<>();
        PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDO = new PrcProfCostSchedulePkgDO();

        copyProperties(prcProfAuditAuthorityDO, prcProfCostSchedulePkgDO);

        prcProfCostSchedulePkgDO.setPrcProfCostSchedulePkgSeq(pkgcostScehdeleSeqNumerForCustomer);

        prcProfCostSchedulePkgDOList.add(prcProfCostSchedulePkgDO);
        prcProfCostSchedulePkgRepository.savePrcProfCostScheduleForCustomer(prcProfCostSchedulePkgDOList);

        saveCostScheduleGroupPkg(pkgcostScehdeleSeqNumerForCustomer);
    }

    private void saveCostScheduleGroupPkg(int pkgcostScehdeleSeqNumerForCustomer) {
        List<PrcProfCostSchedulePkgScheduledGroupDO> prcProfCostSchedulePkgScheduledGroupDOList = new ArrayList<>();
        PrcProfCostSchedulePkgScheduledGroupDO prcProfCostSchedulePkgScheduledGroupDO = new PrcProfCostSchedulePkgScheduledGroupDO();

        prcProfCostSchedulePkgScheduledGroupDO.setContractPriceSeq(getExistingContractPriceProfileSeq());
        prcProfCostSchedulePkgScheduledGroupDO.setCostRunFrequencyCode("M");
        prcProfCostSchedulePkgScheduledGroupDO.setPrcProfCostSchedulePkgSeq(pkgcostScehdeleSeqNumerForCustomer);
        prcProfCostSchedulePkgScheduledGroupDO.setScheduleGroup(105);

        prcProfCostSchedulePkgScheduledGroupDOList.add(prcProfCostSchedulePkgScheduledGroupDO);
        prcProfCostRunSchedGroupRepository.savePrcProfCostRunSchedGroupForCustomer(prcProfCostSchedulePkgScheduledGroupDOList);
    }

    public void savePrcProfNonBrktCstMdl(Date effectiveDate, Date expirationDate) {
        List<PrcProfNonBrktCstMdlDO> prcProfNonBrktCstMdlDOList = new ArrayList<>();
        PrcProfNonBrktCstMdlDO prcProfNonBrktCstMdlDO = new PrcProfNonBrktCstMdlDO();
        prcProfNonBrktCstMdlDO.setContractPriceProfileSeq(getExistingContractPriceProfileSeq());
        prcProfNonBrktCstMdlDO.setCostModelId(1);
        prcProfNonBrktCstMdlDO.setCreateUserId(CPPConstants.DUMMY_USER);
        prcProfNonBrktCstMdlDO.setEffectiveDate(effectiveDate);
        prcProfNonBrktCstMdlDO.setExpirationDate(expirationDate);
        prcProfNonBrktCstMdlDO.setGfsCustomerId(CukesConstants.REAL_CUSTOMER_ID);
        prcProfNonBrktCstMdlDO.setGfsCustomerTypeCode(CukesConstants.REAL_CUSTOMER_TYPE_ID);
        prcProfNonBrktCstMdlDO.setItemPriceId("I");
        prcProfNonBrktCstMdlDO.setItemPriceLevelCode(3);
        prcProfNonBrktCstMdlDO.setLastUpdateUserId(CPPConstants.DUMMY_USER);

        prcProfNonBrktCstMdlDOList.add(prcProfNonBrktCstMdlDO);

        prcProfNonBrktCstMdlRepository.savePrcProfNonBrktCstMdlForCustomer(prcProfNonBrktCstMdlDOList);

    }

    public void savePrcProfPricingRuleOvrd(Date effectiveDate, Date expirationDate) {
        List<PrcProfPricingRuleOvrdDO> prcProfNonBrktCstMdlDOList = new ArrayList<>();
        PrcProfPricingRuleOvrdDO prcProfPricingRuleOvrdDO = new PrcProfPricingRuleOvrdDO();
        prcProfPricingRuleOvrdDO.setContractPriceProfileSeq(getExistingContractPriceProfileSeq());
        prcProfPricingRuleOvrdDO.setCreateUserId(CPPConstants.DUMMY_USER);
        prcProfPricingRuleOvrdDO.setEffectiveDate(effectiveDate);
        prcProfPricingRuleOvrdDO.setExpirationDate(expirationDate);
        prcProfPricingRuleOvrdDO.setGfsCustomerId(CukesConstants.REAL_CUSTOMER_ID);
        prcProfPricingRuleOvrdDO.setGfsCustomerTypeCode(CukesConstants.REAL_CUSTOMER_TYPE_ID);
        prcProfPricingRuleOvrdDO.setLastUpdateUserId(CPPConstants.DUMMY_USER);
        prcProfPricingRuleOvrdDO.setPricingOverrideId(7);
        prcProfPricingRuleOvrdDO.setPricingOverrideInd(1);

        prcProfNonBrktCstMdlDOList.add(prcProfPricingRuleOvrdDO);
        prcProfPricingRuleOvrdRepository.savePrcProfPricingRuleOvrdForCustomer(prcProfNonBrktCstMdlDOList);
    }

    public void savePrcProfLessCaseRule(Date effectiveDate, Date expirationDate) {
        List<PrcProfLessCaseRuleDO> prcProfLessCaseRuleDOList = new ArrayList<>();
        PrcProfLessCaseRuleDO prcProfLessCaseRuleDO = new PrcProfLessCaseRuleDO();
        prcProfLessCaseRuleDO.setContractPriceProfileSeq(getExistingContractPriceProfileSeq());
        prcProfLessCaseRuleDO.setCreateUserId(CPPConstants.DUMMY_USER);
        prcProfLessCaseRuleDO.setCwMarkupAmnt(1);
        prcProfLessCaseRuleDO.setCwMarkupAmountTypeCode("$");
        prcProfLessCaseRuleDO.setEffectiveDate(effectiveDate);
        prcProfLessCaseRuleDO.setExpirationDate(expirationDate);
        prcProfLessCaseRuleDO.setGfsCustomerId(CukesConstants.REAL_CUSTOMER_ID);
        prcProfLessCaseRuleDO.setGfsCustomerTypeCode(CukesConstants.REAL_CUSTOMER_TYPE_ID);
        prcProfLessCaseRuleDO.setItemPriceId("I");
        prcProfLessCaseRuleDO.setItemPriceLevelCode(2);
        prcProfLessCaseRuleDO.setLastUpdateUserId(CPPConstants.DUMMY_USER);
        prcProfLessCaseRuleDO.setLesscaseRuleId(1);
        prcProfLessCaseRuleDO.setMarkupAppliedBeforeDivInd(1);
        prcProfLessCaseRuleDO.setNonCwMarkupAmnt(3);
        prcProfLessCaseRuleDO.setNonCwMarkupAmntTypeCode("$");

        prcProfLessCaseRuleDOList.add(prcProfLessCaseRuleDO);

        prcProfLessCaseRuleRepository.savePrcProfLessCaseRuleForCustomer(prcProfLessCaseRuleDOList);
    }
    
    public List<ProductTypeMarkupDO> populateProductTypeMarkupDO(Date effectiveDate, Date expirationDate, int bidReasonCode, int itemPriceLevelCode, int cppSequence, int itemId){
        List<ProductTypeMarkupDO> productTypeMarkupDOList = new ArrayList<>();
        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setContractPriceProfileSeq(cppSequence);
        productTypeMarkupDO.setCreateUserId(CPPConstants.DUMMY_USER);
        productTypeMarkupDO.setCustomerTypeCode(CukesConstants.REAL_CUSTOMER_TYPE_ID);
        productTypeMarkupDO.setEffectiveDate(effectiveDate);
        productTypeMarkupDO.setExpirationDate(expirationDate);
        productTypeMarkupDO.setGfsCustomerId(CukesConstants.REAL_CUSTOMER_ID);
        productTypeMarkupDO.setHoldCostFirmInd(1);
        productTypeMarkupDO.setItemPriceId(itemId);
        productTypeMarkupDO.setProductType(String.valueOf(itemPriceLevelCode));
        productTypeMarkupDO.setLastUpdateUserId(CPPConstants.DUMMY_USER);
        productTypeMarkupDO.setMarkup(new BigDecimal(1));
        productTypeMarkupDO.setMarkupType(1);
        productTypeMarkupDO.setPriceLockedInTypeCode(1);
        productTypeMarkupDO.setPriceLockinReasonCode(bidReasonCode);
        productTypeMarkupDO.setPriceMaintenanceSourceCode(2);
        productTypeMarkupDO.setUnit("$");

        productTypeMarkupDOList.add(productTypeMarkupDO);
        return productTypeMarkupDOList;
    }

    public void saveCIPEntriesForRealCustomerUnit(Date effectiveDate, Date expirationDate, int bidReasonCode, int itemPriceLevelCode, int cppSequence, int itemId) {
        List<ProductTypeMarkupDO> productTypeMarkupDOList = populateProductTypeMarkupDO(effectiveDate,expirationDate,bidReasonCode,itemPriceLevelCode,cppSequence,itemId);
        productTypeMarkupDOList.get(0).setCustomerTypeCode(AcceptableRealCustomer.REAL_CUSTOMER_UNIT.getCustomerTypeCode());
        productTypeMarkupDOList.get(0).setGfsCustomerId(AcceptableRealCustomer.REAL_CUSTOMER_UNIT.getCustomerId());
        customerItemPriceRepository.saveMarkup(productTypeMarkupDOList, CPPConstants.DUMMY_USER, cppSequence);
    }
    
    public void saveCIPEntriesForRealCustomer(Date effectiveDate, Date expirationDate, int bidReasonCode, int itemPriceLevelCode, int cppSequence, int itemId) {
        List<ProductTypeMarkupDO> productTypeMarkupDOList = populateProductTypeMarkupDO(effectiveDate,expirationDate,bidReasonCode,itemPriceLevelCode,cppSequence,itemId);
        productTypeMarkupDOList.get(0).setCustomerTypeCode(CukesConstants.REAL_CUSTOMER_TYPE_ID);
        productTypeMarkupDOList.get(0).setGfsCustomerId(CukesConstants.REAL_CUSTOMER_ID);
        customerItemPriceRepository.saveMarkup(productTypeMarkupDOList, CPPConstants.DUMMY_USER, cppSequence);
    }

    public void saveCIPEntriesForRealCustomerForItemMarkup(Date effectiveDate, Date expirationDate) {
        saveCIPEntriesForRealCustomer(effectiveDate, expirationDate, PriceLockinReason.COMPETITIVE.getCode(), ItemPriceLevel.ITEM.getCode(),
                getContractPriceProfileSeq(),Integer.parseInt(CukesConstants.ITEM_ID));
    }

}
