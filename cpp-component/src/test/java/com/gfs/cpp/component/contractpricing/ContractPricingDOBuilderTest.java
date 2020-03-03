package com.gfs.cpp.component.contractpricing;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.contractpricing.ContractPricingDOBuilder;

@RunWith(MockitoJUnitRunner.class)
public class ContractPricingDOBuilderTest {

    private static final String AGREEMENT_ID = "agreementId";
    private static final String PARENT_AGREEMENT_ID = "parent agreement id";
    private static final Date CONTRACT_END_DATE = new LocalDate(2019, 1, 1).toDate();
    private static final Date CONTRACT_START_DATE = new LocalDate(2018, 1, 1).toDate();
    @InjectMocks
    private ContractPricingDOBuilder target;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void shouldReturnContractPricingDOWhenNoPriviligesSelected() throws ParseException {

        Date date = new LocalDate(2018, 01, 05).toDate();

        ContractPricingDTO contractPricingDTO = buildContractPricingDTO();

        CMGCustomerResponseDTO cMGCustomerResponseDTO = buildCMGCustomerResponseDTO();

        when(cppDateUtils.getFutureDate()).thenReturn(date);

        ContractPricingDO actual = target.buildContractPricingDO(contractPricingDTO, cMGCustomerResponseDTO);

        assertThat(actual.getAgreementId(), is(contractPricingDTO.getAgreementId()));
        assertThat(actual.getContractName(), is(contractPricingDTO.getContractName()));
        assertThat(actual.getContractPriceProfileId(), is(contractPricingDTO.getContractPriceProfileId()));
        assertThat(actual.getContractPriceProfileSeq(), is(contractPricingDTO.getContractPriceProfileSeq()));
        assertThat(actual.getCustomerTypeCode(), is(cMGCustomerResponseDTO.getTypeCode()));
        assertThat(actual.getCostModelGFSAssesFee(), is(contractPricingDTO.getAssessmentFeeFlag()));
        assertThat(actual.getCostModelTransferFee(), is(contractPricingDTO.getTransferFeeFlag()));
        assertThat(actual.getContractTypeCode(), is(contractPricingDTO.getContractType()));
        assertThat(actual.getEffectiveDateFuture(), is(date));
        assertThat(actual.getExpirationDateFuture(), is(date));
        assertThat(actual.getGfsCustomerId(), is(cMGCustomerResponseDTO.getId()));
        assertThat(actual.getLabelAssesmentInd(), is(0));
        assertThat(actual.getPriceAuditInd(), is(0));
        assertThat(actual.getPriceAuditPrivileges(), is(contractPricingDTO.getPriceAuditFlag()));
        assertThat(actual.getPriceVerifInd(), is(0));
        assertThat(actual.getPriceVerifyPrivileges(), is(contractPricingDTO.getPriceVerificationFlag()));
        assertThat(actual.getPricingExpirationDate(), is(date));
        assertThat(actual.getPricingEffectiveDate(), is(date));
        assertThat(actual.getScheduleForCostChange(), is(contractPricingDTO.getScheduleForCostChange()));
        assertThat(actual.getTransferFeeInd(), is(0));
        assertThat(actual.getAgreementId(), is(AGREEMENT_ID));
        assertThat(actual.getParentAgreementId(), is(PARENT_AGREEMENT_ID));
        assertThat(actual.getClmContractStartDate(), is(CONTRACT_START_DATE));
        assertThat(actual.getClmContractEndDate(), is(CONTRACT_END_DATE));
        verify(cppDateUtils, atLeast(1)).getFutureDate();

    }

    @Test
    public void shouldReturnContractPricingDOWhenAllPriviligesSelected() throws ParseException {

        Date date = new LocalDate(2018, 01, 05).toDate();

        ContractPricingDTO contractPricingDTO = buildContractPricingDTO();
        contractPricingDTO.setAssessmentFeeFlag(true);
        contractPricingDTO.setPriceAuditFlag(true);
        contractPricingDTO.setPriceVerificationFlag(true);
        contractPricingDTO.setScheduleForCostChange("gregorian");
        contractPricingDTO.setTransferFeeFlag(true);

        when(cppDateUtils.getFutureDate()).thenReturn(date);

        CMGCustomerResponseDTO cMGCustomerResponseDTO = buildCMGCustomerResponseDTO();

        ContractPricingDO actual = target.buildContractPricingDO(contractPricingDTO, cMGCustomerResponseDTO);

        assertThat(actual.getAgreementId(), is(contractPricingDTO.getAgreementId()));
        assertThat(actual.getContractName(), is(contractPricingDTO.getContractName()));
        assertThat(actual.getContractPriceProfileId(), is(contractPricingDTO.getContractPriceProfileId()));
        assertThat(actual.getContractPriceProfileSeq(), is(contractPricingDTO.getContractPriceProfileSeq()));
        assertThat(actual.getCustomerTypeCode(), is(cMGCustomerResponseDTO.getTypeCode()));
        assertThat(actual.getCostModelGFSAssesFee(), is(contractPricingDTO.getAssessmentFeeFlag()));
        assertThat(actual.getCostModelTransferFee(), is(contractPricingDTO.getTransferFeeFlag()));
        assertThat(actual.getContractTypeCode(), is(contractPricingDTO.getContractType()));
        assertThat(actual.getEffectiveDateFuture(), is(date));
        assertThat(actual.getExpirationDateFuture(), is(date));
        assertThat(actual.getGfsCustomerId(), is(cMGCustomerResponseDTO.getId()));
        assertThat(actual.getLabelAssesmentInd(), is(1));
        assertThat(actual.getPriceAuditInd(), is(1));
        assertThat(actual.getPriceAuditPrivileges(), is(contractPricingDTO.getPriceAuditFlag()));
        assertThat(actual.getPriceVerifInd(), is(1));
        assertThat(actual.getPriceVerifyPrivileges(), is(contractPricingDTO.getPriceVerificationFlag()));
        assertThat(actual.getPricingExpirationDate(), is(date));
        assertThat(actual.getPricingEffectiveDate(), is(date));
        assertThat(actual.getScheduleForCostChange(), is(contractPricingDTO.getScheduleForCostChange()));
        assertThat(actual.getTransferFeeInd(), is(1));

        verify(cppDateUtils, atLeast(1)).getFutureDate();
    }

    private ContractPricingDTO buildContractPricingDTO() {
        ContractPricingDTO contractPricingDTO = new ContractPricingDTO();
        contractPricingDTO.setAssessmentFeeFlag(false);
        contractPricingDTO.setContractName("Test");
        contractPricingDTO.setContractPriceProfileId(1);
        contractPricingDTO.setContractPriceProfileSeq(0);
        contractPricingDTO.setContractType("DAN");
        contractPricingDTO.setPriceAuditFlag(false);
        contractPricingDTO.setPriceVerificationFlag(false);
        contractPricingDTO.setPricingEffectiveDate(new LocalDate(2018, 01, 05).toDate());
        contractPricingDTO.setPricingExpirationDate(new LocalDate(2018, 01, 05).toDate());
        contractPricingDTO.setScheduleForCostChange("fiscal");
        contractPricingDTO.setTransferFeeFlag(false);
        contractPricingDTO.setAgreementId(AGREEMENT_ID);
        contractPricingDTO.setParentAgreementId(PARENT_AGREEMENT_ID);
        contractPricingDTO.setClmContractStartDate(CONTRACT_START_DATE);
        contractPricingDTO.setClmContractEndDate(CONTRACT_END_DATE);

        return contractPricingDTO;
    }

    private CMGCustomerResponseDTO buildCMGCustomerResponseDTO() {
        CMGCustomerResponseDTO cMGCustomerResponseDTO = new CMGCustomerResponseDTO();
        cMGCustomerResponseDTO.setId("1");
        cMGCustomerResponseDTO.setTypeCode(31);

        return cMGCustomerResponseDTO;

    }

}
