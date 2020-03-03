package com.gfs.cpp.component.assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.assignments.MarkupAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.util.CustomerTypeCodeEnum;
import com.gfs.cpp.component.markup.MarkupFetcher;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;

@Component
public class MarkupAssignmentFetcher {

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Autowired
    private CppConceptMappingRepository cppConceptMappingRepository;

    @Autowired
    private CustomerServiceProxy customerServiceProxy;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private MarkupFetcher markupFetcher;

    public List<MarkupAssignmentDTO> fetchMarkupAssignment(int contractPriceProfileSeq) {

        List<MarkupAssignmentDTO> markupAssignmentDTOList = new ArrayList<>();

        List<CMGCustomerResponseDTO> cmgCustomerResponseList = contractPriceProfCustomerRepository.fetchGFSCustomerDetailsList(contractPriceProfileSeq);
        Map<String, MarkupGridDTO> markupGridByCustomer = markupFetcher.buildMarkupGridByCustomer(contractPriceProfileSeq, null, null);

        for (CMGCustomerResponseDTO cmgCustomerResponseDTO : cmgCustomerResponseList) {
            MarkupGridDTO markupGridDTO = markupGridByCustomer.get(cmgCustomerResponseDTO.getId());
            if (markupGridDTO != null) {
                MarkupAssignmentDTO markupAssignmentDTO = buildMarkupAssignmentDTO(cmgCustomerResponseDTO, markupGridDTO, contractPriceProfileSeq);
                int cppCustomerSeq = contractPriceProfCustomerRepository.fetchCPPCustomerSeq(cmgCustomerResponseDTO.getId(),
                        cmgCustomerResponseDTO.getTypeCode(), contractPriceProfileSeq);
                fetchSavedRealCustomerList(markupAssignmentDTO, cppCustomerSeq);
                markupAssignmentDTOList.add(markupAssignmentDTO);
            }
        }

        if (CollectionUtils.isNotEmpty(markupAssignmentDTOList)) {
            sortMarkupGridByCustomerId(markupAssignmentDTOList);

            ContractPricingResponseDTO contractPriceProfileDetails = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);
            markupAssignmentDTOList.get(0).setMarkupName(contractPriceProfileDetails.getClmContractName());
        }
        return markupAssignmentDTOList;
    }

    private String findCustomer(String gfsCustomerId, int gfsCustomerType) {
        return customerServiceProxy.findCustomerName(gfsCustomerId, gfsCustomerType);
    }

    private void fetchSavedRealCustomerList(MarkupAssignmentDTO markupAssignmentDTO, int cppCustomerSeq) {
        List<RealCustomerDTO> realCustomerDTOList = cppConceptMappingRepository.fetchRealCustomersMapped(cppCustomerSeq);
        if (!realCustomerDTOList.isEmpty()) {
            for (RealCustomerDTO realCustomerDTO : realCustomerDTOList) {
                String realCustomerName = findCustomer(realCustomerDTO.getRealCustomerId(), realCustomerDTO.getRealCustomerType());
                realCustomerDTO.setRealCustomerName(realCustomerName);
                realCustomerDTO.setIsCustomerSaved(true);
                realCustomerDTO.setRealCustomerGroupType(CustomerTypeCodeEnum.getNameByCode(realCustomerDTO.getRealCustomerType()));
            }
            markupAssignmentDTO.setIsAssignmentSaved(true);
            markupAssignmentDTO.setRealCustomerDTOList(realCustomerDTOList);
        }
    }

    private MarkupAssignmentDTO buildMarkupAssignmentDTO(CMGCustomerResponseDTO cmgCustomerForCustomer, MarkupGridDTO markupGridDTO,
            int contractPriceProfileSeq) {
        MarkupAssignmentDTO markupAssignmentDTO = new MarkupAssignmentDTO();
        markupAssignmentDTO.setIsDefault(cmgCustomerForCustomer.getDefaultCustomerInd() == 0 ? false : true);
        markupAssignmentDTO.setMarkupList(markupGridDTO.getProductTypeMarkups());
        markupAssignmentDTO.setItemList(markupGridDTO.getItemMarkups());
        markupAssignmentDTO.setSubgroupList(markupGridDTO.getSubgroupMarkups());
        markupAssignmentDTO.setMarkupName(markupGridDTO.getMarkupName());
        markupAssignmentDTO.setGfsCustomerId(markupGridDTO.getGfsCustomerId());
        markupAssignmentDTO.setGfsCustomerTypeId(cmgCustomerForCustomer.getTypeCode());
        markupAssignmentDTO.setIsAssignmentSaved(false);
        markupAssignmentDTO.setGfsCustomerType(CustomerTypeCodeEnum.getNameByCode(cmgCustomerForCustomer.getTypeCode()));
        markupAssignmentDTO.setRealCustomerDTOList(buildDefaultRealCustomerDTOList());
        markupAssignmentDTO.setExpireLowerInd(contractPriceProfileRepository.fetchExpireLowerIndicator(contractPriceProfileSeq));
        return markupAssignmentDTO;
    }

    private void sortMarkupGridByCustomerId(List<MarkupAssignmentDTO> markupAssignmentDTOList) {
        Collections.sort(markupAssignmentDTOList, new Comparator<MarkupAssignmentDTO>() {
            public int compare(MarkupAssignmentDTO markupAssignment1, MarkupAssignmentDTO markupAssignment2) {
                return markupAssignment1.getGfsCustomerId().compareTo(markupAssignment2.getGfsCustomerId());
            }
        });
    }

    private List<RealCustomerDTO> buildDefaultRealCustomerDTOList() {
        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<>();
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        realCustomerDTO.setRealCustomerGroupType(StringUtils.EMPTY);
        realCustomerDTO.setRealCustomerId(StringUtils.EMPTY);
        realCustomerDTO.setRealCustomerName(StringUtils.EMPTY);
        realCustomerDTO.setIsCustomerSaved(false);
        realCustomerDTO.setRealCustomerType(-1);
        realCustomerDTOList.add(realCustomerDTO);
        return realCustomerDTOList;
    }

}
