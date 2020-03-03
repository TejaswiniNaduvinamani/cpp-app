package com.gfs.cpp.component.activatepricing;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.component.activatepricing.CustomerPriceProfileProcessor;
import com.gfs.cpp.component.activatepricing.CustomerPricingCopier;
import com.gfs.cpp.component.clm.ContractPriceExpirer;
import com.gfs.cpp.component.contractpricing.ContractPriceProfileExpirer;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;

@RunWith(MockitoJUnitRunner.class)
public class CustomerPriceProfileProcessorTest {

    @InjectMocks
    private CustomerPriceProfileProcessor target;

    @Mock
    private ContractPriceProfileExpirer contractPriceProfileExpirer;

    @Mock
    private CustomerPricingCopier customerPricingCopier;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Mock
    private ContractPriceExpirer contractPriceExpirer;

    @Test
    public void shouldExpireAndSavePriceProfile() {

        int latestActivatedPricingCPPSeq = 1;
        Date newEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date newExpirationDate = new LocalDate(2019, 01, 01).toDate();
        Date expirationDateForExistingPricing = new LocalDate(2017, 12, 31).toDate();
        ContractPricingResponseDTO contractDetails = getContractPricingResponseDTO();
        List<ContractCustomerMappingDTO> contractCustomerMappingDTOList = getContractCustomerMappingDTOList();
        int contractPriceProfileSeq = 1;
        String userId = "Test";
        String customerId = "Customer ID";
        int cmgCustomerTypeCode = 31;

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId(customerId);
        cmgCustomerResponseDTO.setTypeCode(cmgCustomerTypeCode);
        cmgCustomerResponseDTO.setDefaultCustomerInd(1);

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(buildCMGCustomerResponseDTO());

        target.expireAndSavePriceProfile(latestActivatedPricingCPPSeq, userId, expirationDateForExistingPricing, contractDetails,
                contractCustomerMappingDTOList);

        verify(contractPriceExpirer).expirePriceProfileForCPPSequence(latestActivatedPricingCPPSeq, expirationDateForExistingPricing, userId);

        verify(contractPriceProfileExpirer).expireAllPriceProfileDataForRealCust(expirationDateForExistingPricing, userId,
                contractCustomerMappingDTOList.get(0).getGfsCustomerId(), contractCustomerMappingDTOList.get(0).getGfsCustomerTypeCode(),
                newEffectiveDate, newExpirationDate);
        verify(customerPricingCopier).savePrcProfAuditAuthority(contractPriceProfileSeq, userId, getContractCustomerMappingDTOList().get(0),
                getContractPricingResponseDTO(), cmgCustomerResponseDTO);
        verify(customerPricingCopier).saveCostSchedulePkgAndPkgGroup(contractPriceProfileSeq, userId, getContractCustomerMappingDTOList().get(0),
                getContractPricingResponseDTO(), cmgCustomerResponseDTO);
        verify(customerPricingCopier).savePrcProfNonBrktCstMdl(contractPriceProfileSeq, userId, getContractCustomerMappingDTOList().get(0),
                getContractPricingResponseDTO(), cmgCustomerResponseDTO);
        verify(customerPricingCopier).savePrcProfPricingRuleOvrd(contractPriceProfileSeq, userId, getContractCustomerMappingDTOList().get(0),
                getContractPricingResponseDTO(), cmgCustomerResponseDTO);
        verify(customerPricingCopier).savePrcProfLessCaseRule(contractPriceProfileSeq, userId, getContractCustomerMappingDTOList().get(0),
                getContractPricingResponseDTO(), cmgCustomerResponseDTO);

    }

    @Test
    public void shouldExpirePriceProfileDetails() {
        String userId = "Test";
        Date expirationDateForExistingPricing = new LocalDate(2017, 12, 31).toDate();
        Date newEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date newExpirationDate = new LocalDate(2019, 01, 01).toDate();

        List<ContractCustomerMappingDTO> contractCustomerMappingDTOList = getContractCustomerMappingDTOList();

        target.expirePriceProfileDetails(expirationDateForExistingPricing, userId, newEffectiveDate, newExpirationDate,
                getContractCustomerMappingDTOList().get(0));
        verify(contractPriceProfileExpirer).expireAllPriceProfileDataForRealCust(expirationDateForExistingPricing, userId,
                contractCustomerMappingDTOList.get(0).getGfsCustomerId(), contractCustomerMappingDTOList.get(0).getGfsCustomerTypeCode(),
                newEffectiveDate, newExpirationDate);
    }

    @Test
    public void shouldSavePriceProfileForRealCustomers() throws ParseException {

        int contractPriceProfileSeq = 1;
        String userId = "Test";
        String customerId = "Customer ID";
        int cmgCustomerTypeCode = 31;

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId(customerId);
        cmgCustomerResponseDTO.setTypeCode(cmgCustomerTypeCode);
        cmgCustomerResponseDTO.setDefaultCustomerInd(1);

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(buildCMGCustomerResponseDTO());

        target.savePriceProfileForRealCustomers(contractPriceProfileSeq, userId, getContractPricingResponseDTO(),
                getContractCustomerMappingDTOList().get(0));
        verify(customerPricingCopier).savePrcProfAuditAuthority(contractPriceProfileSeq, userId, getContractCustomerMappingDTOList().get(0),
                getContractPricingResponseDTO(), cmgCustomerResponseDTO);
        verify(customerPricingCopier).saveCostSchedulePkgAndPkgGroup(contractPriceProfileSeq, userId, getContractCustomerMappingDTOList().get(0),
                getContractPricingResponseDTO(), cmgCustomerResponseDTO);
        verify(customerPricingCopier).savePrcProfNonBrktCstMdl(contractPriceProfileSeq, userId, getContractCustomerMappingDTOList().get(0),
                getContractPricingResponseDTO(), cmgCustomerResponseDTO);
        verify(customerPricingCopier).savePrcProfPricingRuleOvrd(contractPriceProfileSeq, userId, getContractCustomerMappingDTOList().get(0),
                getContractPricingResponseDTO(), cmgCustomerResponseDTO);
        verify(customerPricingCopier).savePrcProfLessCaseRule(contractPriceProfileSeq, userId, getContractCustomerMappingDTOList().get(0),
                getContractPricingResponseDTO(), cmgCustomerResponseDTO);
    }

    @Test
    public void shouldExpirePriceProfileForCPPSequence() {
        int latestActivatedPricingCPPSeq = 1;
        String lastUpdateUserId = "test";
        Date expirationDate = new LocalDate(2017, 12, 31).toDate();
        target.expirePriceProfileForCPPSequence(latestActivatedPricingCPPSeq, lastUpdateUserId, expirationDate);

        verify(contractPriceExpirer).expirePriceProfileForCPPSequence(latestActivatedPricingCPPSeq, expirationDate, lastUpdateUserId);
    }

    @Test
    public void shouldNotExpirePriceProfileForCPPSequence() {
        int latestActivatedPricingCPPSeq = -1;
        String lastUpdateUserId = "test";
        Date expirationDate = new LocalDate(2017, 12, 31).toDate();
        target.expirePriceProfileForCPPSequence(latestActivatedPricingCPPSeq, lastUpdateUserId, expirationDate);

        verify(contractPriceExpirer, never()).expirePriceProfileForCPPSequence(latestActivatedPricingCPPSeq, expirationDate, lastUpdateUserId);
    }

    private CMGCustomerResponseDTO buildCMGCustomerResponseDTO() {
        CMGCustomerResponseDTO contractLevelConcept = new CMGCustomerResponseDTO();
        contractLevelConcept.setDefaultCustomerInd(1);
        contractLevelConcept.setId("Customer ID");
        contractLevelConcept.setTypeCode(31);
        return contractLevelConcept;
    }

    private ContractPricingResponseDTO getContractPricingResponseDTO() {
        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(1);
        contractDetails.setContractPriceProfStatusCode(1);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 01).toDate());
        contractDetails.setClmParentAgreementId("ParentAgreementId");
        return contractDetails;
    }

    private List<ContractCustomerMappingDTO> getContractCustomerMappingDTOList() {
        List<ContractCustomerMappingDTO> contractCustomerMappingDTOList = new ArrayList<ContractCustomerMappingDTO>();
        ContractCustomerMappingDTO contractCustomerMappingDTO = new ContractCustomerMappingDTO();
        contractCustomerMappingDTO.setCppConceptMappingSeq(1);
        contractCustomerMappingDTO.setCppCustomerSeq(2);
        contractCustomerMappingDTO.setGfsCustomerId("Customer ID");
        contractCustomerMappingDTO.setGfsCustomerTypeCode(31);
        contractCustomerMappingDTO.setDefaultCustomerInd(1);
        contractCustomerMappingDTOList.add(contractCustomerMappingDTO);

        return contractCustomerMappingDTOList;
    }

}
