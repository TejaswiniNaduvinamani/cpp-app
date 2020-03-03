package com.gfs.cpp.component.search;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.search.ContractSearchResultDTO;
import com.gfs.cpp.common.util.ContractTypeForSearchEnum;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;

@Service
public class ContractSearchService {

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private CustomerServiceProxy customerServiceProxy;

    public List<ContractSearchResultDTO> searchContractsByContractName(String contractName) {
        List<ContractSearchResultDTO> contractSearchResultList = contractPriceProfileRepository.fetchContractDetailsByContractName(contractName);
        populateCustomerNameAndCPPLink(contractSearchResultList);
        return contractSearchResultList;
    }

    public List<ContractSearchResultDTO> searchContractsByCustomer(String gfsCustomerId, int gfsCustomerTypeCode) {

        List<ContractSearchResultDTO> contractSearchResultList = contractPriceProfileRepository.fetchContractDetailsByCustomer(gfsCustomerId,
                gfsCustomerTypeCode);
        populateCustomerNameAndCPPLink(contractSearchResultList);
        return contractSearchResultList;
    }

    public List<ContractSearchResultDTO> searchContractsByCPPId(long contractPriceProfileId) {

        List<ContractSearchResultDTO> contractSearchResultList = contractPriceProfileRepository.fetchContractDetailsbyCPPId(contractPriceProfileId);
        populateCustomerNameAndCPPLink(contractSearchResultList);
        return contractSearchResultList;
    }

    private void populateCustomerNameAndCPPLink(List<ContractSearchResultDTO> contractSearchResultList) {
        for (ContractSearchResultDTO contractSearchResultDTO : contractSearchResultList) {
            createCppLink(contractSearchResultDTO);
            if (StringUtils.isNotBlank(contractSearchResultDTO.getGfsCustomerId())) {
                contractSearchResultDTO.setCustomerName(customerServiceProxy.findCustomerName(contractSearchResultDTO.getGfsCustomerId(),
                        contractSearchResultDTO.getGfsCustomerTypeCode()));
            }
        }
    }

    private void createCppLink(ContractSearchResultDTO contractSearchResultDTO) {
        if (contractSearchResultDTO.getIsFurtheranceExist().equals(CPPConstants.YES)) {
            ContractPricingResponseDTO contractPricingResponseDTO = contractPriceProfileRepository
                    .fetchContractDetailsByAgreementId(contractSearchResultDTO.getParentAgreementId());
            if (contractPricingResponseDTO != null) {
                contractSearchResultDTO.setCppLink(
                        CPPConstants.SEARCH_LINK_FOR_FURTHERANCE + contractSearchResultDTO.getParentAgreementId() + CPPConstants.SEARCH_LINK_PARAM
                                + ContractTypeForSearchEnum.getTypeByCode(contractPricingResponseDTO.getClmContractTypeSeq()));
            }
        } else {
            contractSearchResultDTO.setCppLink(CPPConstants.SEARCH_LINK_FOR_PRICING + contractSearchResultDTO.getAgreementId()
                    + CPPConstants.SEARCH_LINK_PARAM + ContractTypeForSearchEnum.getTypeByDesc(contractSearchResultDTO.getContractType()));
        }
    }

}
