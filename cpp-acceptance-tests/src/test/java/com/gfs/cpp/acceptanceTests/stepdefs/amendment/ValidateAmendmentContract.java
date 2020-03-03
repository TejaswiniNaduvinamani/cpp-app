package com.gfs.cpp.acceptanceTests.stepdefs.amendment;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.PriceLockinReason;
import com.gfs.corp.component.price.common.types.PriceLockinType;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;
import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.clm.ClmContractDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfCostSchedulePkgScheduledGroupDTO;
import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;
import com.gfs.cpp.common.dto.markup.PrcProfPricingRuleOvrdDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDetailDO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgDO;
import com.gfs.cpp.common.model.splitcase.SplitCaseDO;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.data.auditauthority.PrcProfAuditAuthorityRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostRunSchedGroupRepository;
import com.gfs.cpp.data.contractpricing.PrcProfCostSchedulePkgRepository;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.gfs.cpp.data.contractpricing.PrcProfVerificationPrivlgRepository;
import com.gfs.cpp.data.distributioncenter.ContractPriceProfShipDcRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRepository;

@Component(value = "validateAmendmentContract")
public class ValidateAmendmentContract {

    @Autowired
    @Qualifier("cppJdbcTemplate")
    private NamedParameterJdbcTemplate cppJdbcTemplate;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Autowired
    private CppConceptMappingRepository cppConceptMappingRepository;

    @Autowired
    private ContractPriceProfShipDcRepository contractPriceProfShipDcRepository;

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Autowired
    private CppItemMappingRepository cppItemMappingRepository;

    @Autowired
    private PrcProfCostRunSchedGroupRepository prcProfCostRunSchedGroupRepository;

    @Autowired
    private PrcProfCostSchedulePkgRepository prcProfCostSchedulePkgRepository;

    @Autowired
    private PrcProfVerificationPrivlgRepository prcProfVerificationPrivlgRepository;

    @Autowired
    private PrcProfAuditAuthorityRepository prcProfAuditAuthorityRepository;

    @Autowired
    private PrcProfLessCaseRuleRepository prcProfLessCaseRuleRepository;

    @Autowired
    private PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    @Autowired
    private PrcProfNonBrktCstMdlRepository prcProfNonBrktCstMdlRepository;

    public void validateAmendmentContractEntries(ClmContractDTO clmContractDTO) {
        int contractPriceProfileSeq = clmContractDTO.getCppInformationDto().getContractPriceProfileSeq();
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId(CukesConstants.DEFAULT_CMG_CUSTOMER_ID);
        cmgCustomerResponseDTO.setTypeCode(CukesConstants.CMG_CUSTOMER_TYPE_ID);

        validateContractPriceProfile(contractPriceProfileSeq);
        int cppCustomerSeq = validateContractPriceProfCustomer(contractPriceProfileSeq);
        validateCPPConceptMapping(cppCustomerSeq);
        validateDistributionCenterDetails(contractPriceProfileSeq);
        validateProductAndItemMarkup(contractPriceProfileSeq);
        int customerFutureItemDescSeq = validateFutureItem(contractPriceProfileSeq);
        validateCPPItemMapping(Collections.singletonList(customerFutureItemDescSeq));

        int prcProfCostScheduledPkgSeq = validatePrcProfCostSchedulePkg(contractPriceProfileSeq, cmgCustomerResponseDTO);
        validatePrcProfCostSchedulePkgScheduledGroup(contractPriceProfileSeq, prcProfCostScheduledPkgSeq);
        validatePrcProfVarificationPrivlg(contractPriceProfileSeq);
        validatePrcProfAuditAuthority(contractPriceProfileSeq, CukesConstants.DEFAULT_CMG_CUSTOMER_ID, CukesConstants.CMG_CUSTOMER_TYPE_ID);
        validateSplitCase(contractPriceProfileSeq);
        validatePrcProfPricingRuleOvrd(contractPriceProfileSeq, cmgCustomerResponseDTO);
        validatePrcProfBrktCostModel(contractPriceProfileSeq, cmgCustomerResponseDTO);

    }

    private void validateContractPriceProfile(int contractPriceProfileSeq) {
        ContractPricingResponseDTO contractPriceProfile = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        assertThat(contractPriceProfile.getContractPriceProfStatusCode(), equalTo(ContractPriceProfileStatus.DRAFT.code));
        assertThat(contractPriceProfile.getVersionNumber(), is(2));
        assertThat(contractPriceProfile.getClmAgreementId(), equalTo(CukesConstants.AMENDMENT_AGREEMENT_ID));
        assertThat(contractPriceProfile.getClmParentAgreementId(), equalTo(CukesConstants.AGREEMENT_ID));
        assertThat(contractPriceProfile.getClmContractStartDate(), equalTo(CukesConstants.AMENDMENT_EFFECTIVE_DATE));
    }

    private int validateContractPriceProfCustomer(int contractPriceProfileSeq) {
        List<CMGCustomerResponseDTO> CMGCustomerResponseDTOList = contractPriceProfCustomerRepository.fetchGFSCustomerDetailsList(contractPriceProfileSeq);

        assertThat(CMGCustomerResponseDTOList.get(0).getId(), equalTo(CukesConstants.DEFAULT_CMG_CUSTOMER_ID));
        assertThat(CMGCustomerResponseDTOList.get(0).getTypeCode(), equalTo(CukesConstants.CMG_CUSTOMER_TYPE_ID));
        assertThat(CMGCustomerResponseDTOList.get(0).getDefaultCustomerInd(), equalTo(1));

        return CMGCustomerResponseDTOList.get(0).getCppCustomerSeq();
    }

    private void validateCPPConceptMapping(int cppCustomerSeq) {
        List<RealCustomerDTO> cmgCustomers = cppConceptMappingRepository.fetchRealCustomersMappedForCppSeq(cppCustomerSeq);

        assertThat(cmgCustomers.get(0).getRealCustomerId(), equalTo(CukesConstants.REAL_CUSTOMER_ID));
        assertThat(cmgCustomers.get(0).getRealCustomerType(), equalTo(CukesConstants.REAL_CUSTOMER_TYPE_ID));
    }

    private void validateDistributionCenterDetails(int contractPriceProfileSeq) {
        List<DistributionCenterDetailDO> distributionCenterDetailDOList = contractPriceProfShipDcRepository
                .fetchAllDistributionCenter(contractPriceProfileSeq);
        assertThat(distributionCenterDetailDOList.size(), equalTo(2));
        assertThat(Integer.valueOf(distributionCenterDetailDOList.get(0).getDcCode()), equalTo(CukesConstants.DC_NUMBER));
        assertThat(Integer.valueOf(distributionCenterDetailDOList.get(0).getDcCode()),
                CoreMatchers.anyOf(CoreMatchers.is(CukesConstants.DC_NUMBER), CoreMatchers.is(99999)));
    }

    private void validateProductAndItemMarkup(int contractPriceProfileSeq) {
        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = customerItemPriceRepository.fetchMarkupsForCMGs(contractPriceProfileSeq);
        assertThat(productTypeMarkupDTOList.size(), equalTo(3));
        for (ProductTypeMarkupDTO productTypeMarkupDTO : productTypeMarkupDTOList) {
            if (productTypeMarkupDTO.getProductType().equals("0")) {
                assertThat(productTypeMarkupDTO.getItemPriceId(), equalTo(Integer.valueOf(CukesConstants.ITEM_PRICE_ID)));
            } else if (productTypeMarkupDTO.getProductType().equals("1")) {
                assertThat(productTypeMarkupDTO.getItemPriceId(), equalTo(Integer.valueOf(CukesConstants.SUBGROUP_ID_VALID)));
            }
            assertThat(productTypeMarkupDTO.getGfsCustomerId(), equalTo(CukesConstants.DEFAULT_CMG_CUSTOMER_ID));
            assertThat(productTypeMarkupDTO.getGfsCustomerTypeCode(), equalTo(CukesConstants.CMG_CUSTOMER_TYPE_ID));
            assertThat(productTypeMarkupDTO.getPriceLockinReasonCode(), equalTo(PriceLockinReason.COMPETITIVE.getCode()));
            assertThat(productTypeMarkupDTO.getPriceLockedInTypeCode(), equalTo(PriceLockinType.COST_PLUS.getCode()));
        }
    }

    private int validateFutureItem(int contractPriceProfileSeq) {
        List<FutureItemDescriptionDTO> futureItemDescriptionDTOList = customerItemDescPriceRepository.fetchAllFutureItems(contractPriceProfileSeq);
        assertThat(futureItemDescriptionDTOList.size(), equalTo(1));
        assertThat(futureItemDescriptionDTOList.get(0).getGfsCustomerId(), equalTo(CukesConstants.DEFAULT_CMG_CUSTOMER_ID));
        assertThat(futureItemDescriptionDTOList.get(0).getGfsCustomerTypeCode(), equalTo(CukesConstants.CMG_CUSTOMER_TYPE_ID));
        assertThat(futureItemDescriptionDTOList.get(0).getFutureItemDesc(), equalTo(CukesConstants.FUTURE_ITEM_DESC));
        assertThat(futureItemDescriptionDTOList.get(0).getMarkupAmountTypeCode(), equalTo(CukesConstants.DEFAULT_UNIT));
        return futureItemDescriptionDTOList.get(0).getCustomerItemDescSeq();
    }

    private void validateCPPItemMapping(List<Integer> customerItemDescSeqList) {
        List<ItemAssignmentDTO> ItemAssignmentDTOList = cppItemMappingRepository.fetchAssignedItemsForAConcept(customerItemDescSeqList);
        assertThat(ItemAssignmentDTOList.size(), equalTo(0));
    }

    private int validatePrcProfCostSchedulePkg(int contractPriceProfileSeq, CMGCustomerResponseDTO cmgCustomerResponseDTO) {
        List<PrcProfCostSchedulePkgDO> prcProfCostSchedulePkgDOList = prcProfCostSchedulePkgRepository
                .fetchPrcProfCostScheduleForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);
        assertThat(prcProfCostSchedulePkgDOList.size(), equalTo(1));
        assertThat(prcProfCostSchedulePkgDOList.get(0).getGfsCustomerId(), equalTo(CukesConstants.DEFAULT_CMG_CUSTOMER_ID));
        assertThat(prcProfCostSchedulePkgDOList.get(0).getGfsCustomerTypeCode(), equalTo(CukesConstants.CMG_CUSTOMER_TYPE_ID));
        return prcProfCostSchedulePkgDOList.get(0).getPrcProfCostSchedulePkgSeq();
    }

    private void validatePrcProfCostSchedulePkgScheduledGroup(int contractPriceProfileSeq, int prcProfCostScheduledPkgSeq) {
        List<PrcProfCostSchedulePkgScheduledGroupDTO> prcProfCostSchedulePkgScheduledGroupDTOList = prcProfCostRunSchedGroupRepository
                .fetchPrcProfCostRunSchedGroupForCPPSeq(contractPriceProfileSeq, prcProfCostScheduledPkgSeq);
        assertThat(prcProfCostSchedulePkgScheduledGroupDTOList.size(), equalTo(2));
        for (PrcProfCostSchedulePkgScheduledGroupDTO prcProfCostSchedulePkgScheduledGroupDTO : prcProfCostSchedulePkgScheduledGroupDTOList) {
            assertThat(prcProfCostSchedulePkgScheduledGroupDTO.getScheduleGroup(), CoreMatchers.anyOf(CoreMatchers.is(100), CoreMatchers.is(105)));
            assertThat(prcProfCostSchedulePkgScheduledGroupDTO.getCostRunFrequencyCode(),
                    CoreMatchers.anyOf(CoreMatchers.is("M"), CoreMatchers.is("W")));
        }
    }

    private void validatePrcProfVarificationPrivlg(int contractPriceProfileSeq) {
        int privilageIndicator = prcProfVerificationPrivlgRepository.fetchPricePriviligeIndicator(contractPriceProfileSeq);
        assertThat(privilageIndicator, equalTo(1));
    }

    private void validatePrcProfAuditAuthority(int contractPriceProfileSeq, String customerId, int customerTypeCode) {
        int auditIndicator = prcProfAuditAuthorityRepository.fetchPriceAuditIndicator(contractPriceProfileSeq, customerId, customerTypeCode);
        assertThat(auditIndicator, equalTo(1));
    }

    private void validateSplitCase(int contractPriceProfileSeq) {
        List<SplitCaseDO> splitCaseDOList = prcProfLessCaseRuleRepository.fetchSplitCaseGridForCMG(contractPriceProfileSeq);
        assertThat(splitCaseDOList.size(), equalTo(1));
        assertThat(splitCaseDOList.get(0).getSplitCaseFee(), equalTo(Double.valueOf(CukesConstants.DEFAULT_SPLITCASE_FEE)));
        assertThat(splitCaseDOList.get(0).getUnit(), equalTo(CukesConstants.DEFAULT_UNIT));
    }

    private void validatePrcProfPricingRuleOvrd(int contractPriceProfileSeq, CMGCustomerResponseDTO cmgCustomerResponseDTO) {
        List<PrcProfPricingRuleOvrdDTO> prcProfPricingRuleOvrdDTOList = prcProfPricingRuleOvrdRepository
                .fetchPrcProfPricingRuleOvrdForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);
        assertThat(prcProfPricingRuleOvrdDTOList.size(), equalTo(1));
        assertThat(prcProfPricingRuleOvrdDTOList.get(0).getPricingOverrideInd(), equalTo(1));
        assertThat(prcProfPricingRuleOvrdDTOList.get(0).getGfsCustomerId(), equalTo(CukesConstants.DEFAULT_CMG_CUSTOMER_ID));
        assertThat(prcProfPricingRuleOvrdDTOList.get(0).getGfsCustomerTypeCode(), equalTo(CukesConstants.CMG_CUSTOMER_TYPE_ID));
    }

    private void validatePrcProfBrktCostModel(int contractPriceProfileSeq, CMGCustomerResponseDTO cmgCustomerResponseDTO) {
        List<PrcProfNonBrktCstMdlDTO> prcProfNonBrktCstMdlDTOList = prcProfNonBrktCstMdlRepository
                .fetchPrcProfNonBrktCstMdlForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);
        assertThat(prcProfNonBrktCstMdlDTOList.size(), equalTo(6));
        assertThat(prcProfNonBrktCstMdlDTOList.get(0).getGfsCustomerId(), equalTo(CukesConstants.DEFAULT_CMG_CUSTOMER_ID));
        assertThat(prcProfNonBrktCstMdlDTOList.get(0).getGfsCustomerTypeCode(), equalTo(CukesConstants.CMG_CUSTOMER_TYPE_ID));
    }
}
