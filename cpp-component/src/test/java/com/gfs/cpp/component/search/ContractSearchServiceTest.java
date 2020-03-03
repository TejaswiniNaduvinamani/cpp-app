package com.gfs.cpp.component.search;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.search.ContractSearchResultDTO;
import com.gfs.cpp.common.util.ContractTypeForSearchEnum;
import com.gfs.cpp.component.search.ContractSearchService;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;

@RunWith(MockitoJUnitRunner.class)
public class ContractSearchServiceTest {

    private static final Date pricingEffectiveDate = new LocalDate(9999, 01, 01).toDate();

    @InjectMocks
    private ContractSearchService target;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Mock
    private CustomerServiceProxy customerServiceProxy;

    @Test
    public void shouldSearchContractByContractNameWhenNoFurtherance() {
        String contractName = "test";
        String customerName = "test-customer-name";

        List<ContractSearchResultDTO> contractSearchResultList = buildContractSearchResultDTO(CPPConstants.NO);
        when(contractPriceProfileRepository.fetchContractDetailsByContractName(contractName)).thenReturn(contractSearchResultList);
        when(customerServiceProxy.findCustomerName(contractSearchResultList.get(0).getGfsCustomerId(),
                contractSearchResultList.get(0).getGfsCustomerTypeCode())).thenReturn(customerName);

        List<ContractSearchResultDTO> actual = target.searchContractsByContractName(contractName);

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getContractName(), equalTo(contractSearchResultList.get(0).getContractName()));
        assertThat(actual.get(0).getCppLink(), equalTo(CPPConstants.SEARCH_LINK_FOR_PRICING + contractSearchResultList.get(0).getAgreementId()
                + CPPConstants.SEARCH_LINK_PARAM + ContractTypeForSearchEnum.getTypeByDesc(contractSearchResultList.get(0).getContractType())));
        assertThat(actual.get(0).getCustomerName(), equalTo(customerName));
        assertThat(actual.get(0).getAgreementId(), equalTo(contractSearchResultList.get(0).getAgreementId()));
        assertThat(actual.get(0).getContractName(), equalTo(contractSearchResultList.get(0).getContractName()));
        assertThat(actual.get(0).getContractPriceProfileId(), equalTo(contractSearchResultList.get(0).getContractPriceProfileId()));
        assertThat(actual.get(0).getIsFurtheranceExist(), equalTo(contractSearchResultList.get(0).getIsFurtheranceExist()));
        assertThat(actual.get(0).getStatus(), equalTo(contractSearchResultList.get(0).getStatus()));

        verify(contractPriceProfileRepository).fetchContractDetailsByContractName(contractName);
        verify(customerServiceProxy).findCustomerName(contractSearchResultList.get(0).getGfsCustomerId(),
                contractSearchResultList.get(0).getGfsCustomerTypeCode());
    }

    @Test
    public void shouldSearchContractByContractNameWhenFurtheranceExist() {
        String contractName = "test";
        List<ContractSearchResultDTO> contractSearchResultList = buildContractSearchResultDTO(CPPConstants.YES);
        contractSearchResultList.get(0).setGfsCustomerId(null);
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractTypeSeq(6);

        when(contractPriceProfileRepository.fetchContractDetailsByContractName(contractName)).thenReturn(contractSearchResultList);
        when(contractPriceProfileRepository.fetchContractDetailsByAgreementId(contractSearchResultList.get(0).getParentAgreementId()))
                .thenReturn(contractPricingResponseDTO);

        List<ContractSearchResultDTO> actual = target.searchContractsByContractName(contractName);

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getContractName(), equalTo(contractSearchResultList.get(0).getContractName()));
        assertThat(actual.get(0).getCppLink(),
                equalTo(CPPConstants.SEARCH_LINK_FOR_FURTHERANCE + contractSearchResultList.get(0).getParentAgreementId()
                        + CPPConstants.SEARCH_LINK_PARAM
                        + ContractTypeForSearchEnum.getTypeByCode(contractPricingResponseDTO.getClmContractTypeSeq())));
        assertThat(actual.get(0).getCustomerName(), equalTo(null));
        assertThat(actual.get(0).getAgreementId(), equalTo(contractSearchResultList.get(0).getAgreementId()));
        assertThat(actual.get(0).getContractName(), equalTo(contractSearchResultList.get(0).getContractName()));
        assertThat(actual.get(0).getContractPriceProfileId(), equalTo(contractSearchResultList.get(0).getContractPriceProfileId()));
        assertThat(actual.get(0).getIsFurtheranceExist(), equalTo(contractSearchResultList.get(0).getIsFurtheranceExist()));
        assertThat(actual.get(0).getStatus(), equalTo(contractSearchResultList.get(0).getStatus()));

        verify(contractPriceProfileRepository).fetchContractDetailsByContractName(contractName);
        verify(contractPriceProfileRepository).fetchContractDetailsByAgreementId(contractSearchResultList.get(0).getParentAgreementId());
    }

    @Test
    public void shouldSearchContractByCustomerNameWhenNoFurtherance() {
        String customerName = "test-customer-name";
        String gfsCustomerId = "12345";
        int gfsCustomerTypeCode = 12;

        List<ContractSearchResultDTO> contractSearchResultList = buildContractSearchResultDTO(CPPConstants.NO);

        when(contractPriceProfileRepository.fetchContractDetailsByCustomer(gfsCustomerId, gfsCustomerTypeCode)).thenReturn(contractSearchResultList);
        when(customerServiceProxy.findCustomerName(contractSearchResultList.get(0).getGfsCustomerId(),
                contractSearchResultList.get(0).getGfsCustomerTypeCode())).thenReturn(customerName);

        List<ContractSearchResultDTO> actual = target.searchContractsByCustomer(gfsCustomerId, gfsCustomerTypeCode);

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getContractName(), equalTo(contractSearchResultList.get(0).getContractName()));
        assertThat(actual.get(0).getCppLink(), equalTo(CPPConstants.SEARCH_LINK_FOR_PRICING + contractSearchResultList.get(0).getAgreementId()
                + CPPConstants.SEARCH_LINK_PARAM + ContractTypeForSearchEnum.getTypeByDesc(contractSearchResultList.get(0).getContractType())));
        assertThat(actual.get(0).getCustomerName(), equalTo(customerName));
        assertThat(actual.get(0).getAgreementId(), equalTo(contractSearchResultList.get(0).getAgreementId()));
        assertThat(actual.get(0).getContractName(), equalTo(contractSearchResultList.get(0).getContractName()));
        assertThat(actual.get(0).getContractPriceProfileId(), equalTo(contractSearchResultList.get(0).getContractPriceProfileId()));
        assertThat(actual.get(0).getIsFurtheranceExist(), equalTo(contractSearchResultList.get(0).getIsFurtheranceExist()));
        assertThat(actual.get(0).getStatus(), equalTo(contractSearchResultList.get(0).getStatus()));

        verify(contractPriceProfileRepository).fetchContractDetailsByCustomer(gfsCustomerId, gfsCustomerTypeCode);
        verify(customerServiceProxy).findCustomerName(contractSearchResultList.get(0).getGfsCustomerId(),
                contractSearchResultList.get(0).getGfsCustomerTypeCode());
    }

    @Test
    public void shouldSearchContractByCustomerWhenFurtheranceExist() {
        String gfsCustomerId = "12345";
        int gfsCustomerTypeCode = 12;

        List<ContractSearchResultDTO> contractSearchResultList = buildContractSearchResultDTO(CPPConstants.YES);
        contractSearchResultList.get(0).setGfsCustomerId(null);
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractTypeSeq(6);

        when(contractPriceProfileRepository.fetchContractDetailsByCustomer(gfsCustomerId, gfsCustomerTypeCode)).thenReturn(contractSearchResultList);
        when(contractPriceProfileRepository.fetchContractDetailsByAgreementId(contractSearchResultList.get(0).getParentAgreementId()))
                .thenReturn(contractPricingResponseDTO);

        List<ContractSearchResultDTO> actual = target.searchContractsByCustomer(gfsCustomerId, gfsCustomerTypeCode);

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getContractName(), equalTo(contractSearchResultList.get(0).getContractName()));
        assertThat(actual.get(0).getCppLink(),
                equalTo(CPPConstants.SEARCH_LINK_FOR_FURTHERANCE + contractSearchResultList.get(0).getParentAgreementId()
                        + CPPConstants.SEARCH_LINK_PARAM
                        + ContractTypeForSearchEnum.getTypeByCode(contractPricingResponseDTO.getClmContractTypeSeq())));
        assertThat(actual.get(0).getCustomerName(), equalTo(null));
        assertThat(actual.get(0).getAgreementId(), equalTo(contractSearchResultList.get(0).getAgreementId()));
        assertThat(actual.get(0).getContractName(), equalTo(contractSearchResultList.get(0).getContractName()));
        assertThat(actual.get(0).getContractPriceProfileId(), equalTo(contractSearchResultList.get(0).getContractPriceProfileId()));
        assertThat(actual.get(0).getIsFurtheranceExist(), equalTo(contractSearchResultList.get(0).getIsFurtheranceExist()));
        assertThat(actual.get(0).getStatus(), equalTo(contractSearchResultList.get(0).getStatus()));

        verify(contractPriceProfileRepository).fetchContractDetailsByCustomer(gfsCustomerId, gfsCustomerTypeCode);
        verify(contractPriceProfileRepository).fetchContractDetailsByAgreementId(contractSearchResultList.get(0).getParentAgreementId());
    }

    @Test
    public void shouldSearchContractByCPPIdWhenNoFurtherance() {
        String customerName = "test-customer-name";
        long contractPriceProfileId = 100;

        List<ContractSearchResultDTO> contractSearchResultList = buildContractSearchResultDTO(CPPConstants.NO);

        when(contractPriceProfileRepository.fetchContractDetailsbyCPPId(contractPriceProfileId)).thenReturn(contractSearchResultList);
        when(customerServiceProxy.findCustomerName(contractSearchResultList.get(0).getGfsCustomerId(),
                contractSearchResultList.get(0).getGfsCustomerTypeCode())).thenReturn(customerName);

        List<ContractSearchResultDTO> actual = target.searchContractsByCPPId(contractPriceProfileId);

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getContractName(), equalTo(contractSearchResultList.get(0).getContractName()));
        assertThat(actual.get(0).getCppLink(), equalTo(CPPConstants.SEARCH_LINK_FOR_PRICING + contractSearchResultList.get(0).getAgreementId()
                + CPPConstants.SEARCH_LINK_PARAM + ContractTypeForSearchEnum.getTypeByDesc(contractSearchResultList.get(0).getContractType())));
        assertThat(actual.get(0).getCustomerName(), equalTo(customerName));
        assertThat(actual.get(0).getAgreementId(), equalTo(contractSearchResultList.get(0).getAgreementId()));
        assertThat(actual.get(0).getContractName(), equalTo(contractSearchResultList.get(0).getContractName()));
        assertThat(actual.get(0).getContractPriceProfileId(), equalTo(contractSearchResultList.get(0).getContractPriceProfileId()));
        assertThat(actual.get(0).getIsFurtheranceExist(), equalTo(contractSearchResultList.get(0).getIsFurtheranceExist()));
        assertThat(actual.get(0).getStatus(), equalTo(contractSearchResultList.get(0).getStatus()));

        verify(contractPriceProfileRepository).fetchContractDetailsbyCPPId(contractPriceProfileId);
        verify(customerServiceProxy).findCustomerName(contractSearchResultList.get(0).getGfsCustomerId(),
                contractSearchResultList.get(0).getGfsCustomerTypeCode());
    }

    @Test
    public void shouldSearchContractByCPPIdWhenFurtheranceExist() {
        long contractPriceProfileId = 100;

        List<ContractSearchResultDTO> contractSearchResultList = buildContractSearchResultDTO(CPPConstants.YES);
        contractSearchResultList.get(0).setGfsCustomerId(null);
        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractTypeSeq(6);

        when(contractPriceProfileRepository.fetchContractDetailsbyCPPId(contractPriceProfileId)).thenReturn(contractSearchResultList);
        when(contractPriceProfileRepository.fetchContractDetailsByAgreementId(contractSearchResultList.get(0).getParentAgreementId()))
                .thenReturn(contractPricingResponseDTO);

        List<ContractSearchResultDTO> actual = target.searchContractsByCPPId(contractPriceProfileId);

        assertThat(actual.size(), equalTo(1));
        assertThat(actual.get(0).getContractName(), equalTo(contractSearchResultList.get(0).getContractName()));
        assertThat(actual.get(0).getCppLink(),
                equalTo(CPPConstants.SEARCH_LINK_FOR_FURTHERANCE + contractSearchResultList.get(0).getParentAgreementId()
                        + CPPConstants.SEARCH_LINK_PARAM
                        + ContractTypeForSearchEnum.getTypeByCode(contractPricingResponseDTO.getClmContractTypeSeq())));
        assertThat(actual.get(0).getCustomerName(), equalTo(null));
        assertThat(actual.get(0).getAgreementId(), equalTo(contractSearchResultList.get(0).getAgreementId()));
        assertThat(actual.get(0).getContractName(), equalTo(contractSearchResultList.get(0).getContractName()));
        assertThat(actual.get(0).getContractPriceProfileId(), equalTo(contractSearchResultList.get(0).getContractPriceProfileId()));
        assertThat(actual.get(0).getIsFurtheranceExist(), equalTo(contractSearchResultList.get(0).getIsFurtheranceExist()));
        assertThat(actual.get(0).getStatus(), equalTo(contractSearchResultList.get(0).getStatus()));

        verify(contractPriceProfileRepository).fetchContractDetailsbyCPPId(contractPriceProfileId);
        verify(contractPriceProfileRepository).fetchContractDetailsByAgreementId(contractSearchResultList.get(0).getParentAgreementId());
    }

    private List<ContractSearchResultDTO> buildContractSearchResultDTO(String isFurtheranceExist) {
        List<ContractSearchResultDTO> contractSearchResultList = new ArrayList<>();
        ContractSearchResultDTO contractSearchResultDTO = new ContractSearchResultDTO();
        contractSearchResultDTO.setAgreementId("test-agreement-id");
        contractSearchResultDTO.setParentAgreementId("parent-agreement-id");
        contractSearchResultDTO.setGfsCustomerId("12345");
        contractSearchResultDTO.setGfsCustomerTypeCode(12);
        contractSearchResultDTO.setContractPriceProfileId(1);
        contractSearchResultDTO.setEffectiveDate(pricingEffectiveDate);
        contractSearchResultDTO.setIsFurtheranceExist(isFurtheranceExist);
        contractSearchResultDTO.setStatus("Contract Approved");
        contractSearchResultDTO.setVersion(1);
        contractSearchResultDTO.setContractType(ContractTypeForSearchEnum.getDescByCode(1));
        contractSearchResultList.add(contractSearchResultDTO);
        return contractSearchResultList;
    }
}
