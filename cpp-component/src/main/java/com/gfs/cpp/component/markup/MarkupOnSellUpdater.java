package com.gfs.cpp.component.markup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.markup.MarkupRequestDTO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;

@Component
public class MarkupOnSellUpdater {

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    private PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    public void saveOrUpdateMarkupOnSellIndicator(MarkupRequestDTO markupRequest, String userName, String gfsCustomerId, int gfsCustomerTypeCode) {
        if (prcProfPricingRuleOvrdRepository.fetchSellPriceCount(markupRequest.getContractPriceProfileSeq()) > 0) {
            prcProfPricingRuleOvrdRepository.updateMarkupOnSellIndicator(markupRequest, gfsCustomerId, gfsCustomerTypeCode, userName,
                    cppDateUtils.getFutureDate(), cppDateUtils.getFutureDate());
        } else {
            prcProfPricingRuleOvrdRepository.saveMarkupOnSellIndicator(markupRequest, gfsCustomerId, gfsCustomerTypeCode, userName,
                    cppDateUtils.getFutureDate(), cppDateUtils.getFutureDate());
        }
    }

    public void updateMarkupOnSell(int contractPriceProfileSeq, String userName) {
        CMGCustomerResponseDTO cmgCustomerResponseDTO = contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq);
        MarkupRequestDTO markupRequestDTO = new MarkupRequestDTO();
        markupRequestDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        int sellPriceCount = customerItemPriceRepository.fetchSellPriceIndCount(contractPriceProfileSeq);
        int markupAmountTypeCount = customerItemDescPriceRepository.fetchMarkupAmountTypeCodeCount(contractPriceProfileSeq);
        if (sellPriceCount > 0 || markupAmountTypeCount > 0) {
            markupRequestDTO.setMarkupOnSell(true);
        }
        saveOrUpdateMarkupOnSellIndicator(markupRequestDTO, userName, cmgCustomerResponseDTO.getId(), cmgCustomerResponseDTO.getTypeCode());
    }

}
