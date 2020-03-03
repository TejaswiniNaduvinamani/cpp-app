package com.gfs.cpp.component.markup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.component.markup.builder.MarkupGridBuilder;
import com.gfs.cpp.component.markup.builder.MarkupWrapperBuilder;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;
import com.gfs.cpp.proxy.ItemQueryProxy;

@Service
public class MarkupFetcher {

    @Autowired
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Autowired
    private CustomerServiceProxy customerServiceProxy;

    @Autowired
    private MarkupWrapperBuilder markupWrapperBuilder;

    @Autowired
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Autowired
    private ItemQueryProxy itemQueryProxy;

    @Autowired
    private MarkupGridBuilder markupGridBuilder;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    public List<MarkupWrapperDTO> fetchAllMarkups(int contractPriceProfileSeq, Date pricingExpirationDate, Date pricingEffectiveDate) {

        Map<String, MarkupGridDTO> buildMarkupGridByCustomer = buildMarkupGridByCustomer(contractPriceProfileSeq, pricingEffectiveDate,
                pricingExpirationDate);

        List<MarkupWrapperDTO> markupWrapperList = new ArrayList<>();

        List<CMGCustomerResponseDTO> fetchGFSCustomerDetailsList = contractPriceProfCustomerRepository.fetchGFSCustomerDetailsList(contractPriceProfileSeq);
        for (CMGCustomerResponseDTO cmgCustomerResponseDTO : fetchGFSCustomerDetailsList) {
            MarkupGridDTO markupGridDTO = buildMarkupGridByCustomer.get(cmgCustomerResponseDTO.getId());

            if (markupGridDTO == null) {
                String markupName = customerServiceProxy.fetchGroupName(cmgCustomerResponseDTO.getId(), cmgCustomerResponseDTO.getTypeCode());
                markupWrapperList.add(markupWrapperBuilder.buildDefaultMarkupWrapper(contractPriceProfileSeq, cmgCustomerResponseDTO.getId(),
                        markupName, pricingEffectiveDate, pricingExpirationDate));
            } else {
                markupWrapperList.add(markupWrapperBuilder.buildMarkupWrapperFromGrid(contractPriceProfileSeq, markupGridDTO));
            }
        }

        sortMarkupWrapperByGFSCustomerId(markupWrapperList);

        ContractPricingResponseDTO contractPriceProfileDetails = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        markupWrapperList.get(0).setMarkupName(contractPriceProfileDetails.getClmContractName());
        return markupWrapperList;
    }

    public Map<String, MarkupGridDTO> buildMarkupGridByCustomer(int contractPriceProfileSeq, Date pricingEffectiveDate, Date pricingExpirationDate) {
        List<ProductTypeMarkupDTO> allMarkupsForContractCmgs = customerItemPriceRepository.fetchMarkupsForCMGs(contractPriceProfileSeq);
        return buildMarkupGridForAllCustomer(allMarkupsForContractCmgs, contractPriceProfileSeq, pricingEffectiveDate, pricingExpirationDate);
    }

    Map<String, MarkupGridDTO> buildMarkupGridForAllCustomer(List<ProductTypeMarkupDTO> allMarkupsForContract, int contractPriceProfileSeq,
            Date pricingEffectiveDate, Date pricingExpirationDate) {

        Map<String, MarkupGridDTO> markupGridByCmgs = new HashMap<>();

        if (CollectionUtils.isNotEmpty(allMarkupsForContract)) {

            Map<String, List<ProductTypeMarkupDTO>> groupMarkupsByCustomer = groupMarkupsByCustomer(allMarkupsForContract, pricingEffectiveDate,
                    pricingExpirationDate);
            Map<String, ItemInformationDTO> allItemsByItemId = getItemInformationForAllItemLevelItems(allMarkupsForContract);

            for (Entry<String, List<ProductTypeMarkupDTO>> productTypeMarkupDTOEntry : groupMarkupsByCustomer.entrySet()) {
                String cmgCustomerId = productTypeMarkupDTOEntry.getKey();
                List<ProductTypeMarkupDTO> allMarkupsForCmg = productTypeMarkupDTOEntry.getValue();

                MarkupGridDTO markupGridForCmg = markupGridBuilder.buildMarkupGrid(cmgCustomerId, allMarkupsForCmg, allItemsByItemId,
                        contractPriceProfileSeq, pricingEffectiveDate, pricingExpirationDate);
                markupGridByCmgs.put(cmgCustomerId, markupGridForCmg);
            }
        }
        return markupGridByCmgs;

    }

    private Map<String, ItemInformationDTO> getItemInformationForAllItemLevelItems(List<ProductTypeMarkupDTO> allMarkupsForContract) {

        List<String> allItemLevelItemids = extractAllItemLevelItemIds(allMarkupsForContract);
        return itemQueryProxy.getItemInformationByItemId(allItemLevelItemids);

    }

    private List<String> extractAllItemLevelItemIds(List<ProductTypeMarkupDTO> allMarkupsForContract) {
        List<String> allItemIds = new ArrayList<>();
        for (ProductTypeMarkupDTO markup : allMarkupsForContract) {
            if (ItemPriceLevel.ITEM.getCode() == Integer.parseInt(markup.getProductType())) {
                allItemIds.add(String.valueOf(markup.getItemPriceId()));
            }
        }
        return allItemIds;
    }

    private Map<String, List<ProductTypeMarkupDTO>> groupMarkupsByCustomer(List<ProductTypeMarkupDTO> allMarkupsForContract,
            Date pricingEffectiveDate, Date pricingExpirationDate) {

        Map<String, List<ProductTypeMarkupDTO>> allMarkupsByCustomer = new HashMap<>();

        for (ProductTypeMarkupDTO productTypeMarkupDTO : allMarkupsForContract) {

            String customerId = productTypeMarkupDTO.getGfsCustomerId();
            productTypeMarkupDTO.setEffectiveDate(pricingEffectiveDate);
            productTypeMarkupDTO.setExpirationDate(pricingExpirationDate);
            List<ProductTypeMarkupDTO> markupForCustomer = allMarkupsByCustomer.get(customerId);

            if (markupForCustomer == null) {
                markupForCustomer = new ArrayList<>();
                allMarkupsByCustomer.put(customerId, markupForCustomer);
            }
            markupForCustomer.add(productTypeMarkupDTO);
        }
        return allMarkupsByCustomer;
    }

    private void sortMarkupWrapperByGFSCustomerId(List<MarkupWrapperDTO> markupWrapperList) {
        Collections.sort(markupWrapperList, new Comparator<MarkupWrapperDTO>() {
            public int compare(MarkupWrapperDTO o1, MarkupWrapperDTO o2) {
                Integer customerId1 = Integer.parseInt(o1.getGfsCustomerId());
                Integer customerId2 = Integer.parseInt(o2.getGfsCustomerId());
                return (customerId1).compareTo(customerId2);
            }
        });
    }
}