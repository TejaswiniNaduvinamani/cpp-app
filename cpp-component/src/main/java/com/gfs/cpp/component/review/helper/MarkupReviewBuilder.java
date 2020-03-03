package com.gfs.cpp.component.review.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.component.markup.MarkupFetcher;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@Component
public class MarkupReviewBuilder {

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Autowired
    private MarkupFetcher markupFetcher;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    public MarkupReviewDTO buildFetchMarkupDTO(int contractPriceProfileSeq, Date pricingEffectiveDate, Date pricingExpirationDate) {

        MarkupReviewDTO markupDTO = new MarkupReviewDTO();
        List<MarkupGridDTO> markupGridDTOList = new ArrayList<>();

        Map<String, MarkupGridDTO> buildMarkupGridByCustomer = markupFetcher.buildMarkupGridByCustomer(contractPriceProfileSeq, pricingEffectiveDate,
                pricingExpirationDate);

        List<CMGCustomerResponseDTO> cmgCustomerResponseDTOList = contractPriceProfCustomerRepository.fetchGFSCustomerDetailsList(contractPriceProfileSeq);
        for (CMGCustomerResponseDTO cmgCustomer : cmgCustomerResponseDTOList) {
            MarkupGridDTO markupGridDTO = buildMarkupGridByCustomer.get(cmgCustomer.getId());
            markupGridDTOList.add(markupGridDTO);
        }

        sortMarkupGridByGFSCustomerId(markupGridDTOList);
        ContractPricingResponseDTO contractPriceProfileDetails = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        markupGridDTOList.get(0).setMarkupName(contractPriceProfileDetails.getClmContractName());

        markupDTO.setMarkupGridDTOs(markupGridDTOList);
        return markupDTO;
    }

    private void sortMarkupGridByGFSCustomerId(List<MarkupGridDTO> markupGridDTOList) {
        Collections.sort(markupGridDTOList, new Comparator<MarkupGridDTO>() {
            public int compare(MarkupGridDTO o1, MarkupGridDTO o2) {
                Integer customerId1 = Integer.parseInt(o1.getGfsCustomerId());
                Integer customerId2 = Integer.parseInt(o2.getGfsCustomerId());
                return (customerId1).compareTo(customerId2);
            }
        });
    }
}
