package com.gfs.cpp.component.costmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.costmodel.CostModelDTO;
import com.gfs.cpp.common.dto.costmodel.CostModelResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusValidator;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.gfs.cpp.proxy.ItemServiceProxy;
import com.gfs.cpp.proxy.ModeledCostQueryServiceProxy;

@Service("costModelService")
public class CostModelService {

    @Autowired
    private ModeledCostQueryServiceProxy modeledCostQueryServiceProxy;

    @Autowired
    private PrcProfNonBrktCstMdlRepository prcProfNonBrktCstMdlRepository;

    @Autowired
    private Environment environment;

    @Autowired
    private ContractPriceProfileStatusValidator contractPriceProfileStatusValidator;

    @Autowired
    private ItemServiceProxy itemServiceProxy;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Cacheable(value = "costModelCache")
    public List<CostModelResponseDTO> fetchAllActiveCostModels() {

        logger.info("ModeledCostQueryServiceProxy is called to fetch all active cost models for caching");
        List<CostModelResponseDTO> costModelResponseDTOList = new ArrayList<>();
        List<CostModelDTO> costModelDTOList = modeledCostQueryServiceProxy.lookupAllActiveCostModels();
        for (CostModelDTO costModelDTO : costModelDTOList) {
            CostModelResponseDTO costModelResponseDTO = new CostModelResponseDTO();
            costModelResponseDTO.setCostModelId(costModelDTO.getModelId());
            costModelResponseDTO.setCostModelTypeValue(costModelDTO.getName());
            costModelResponseDTOList.add(costModelResponseDTO);
        }

        return costModelResponseDTOList;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public void updateCostModel(List<PrcProfNonBrktCstMdlDTO> costModelTypeDTOList, String userId) {
        int contractPriceProfileSeq = costModelTypeDTOList.get(0).getContractPriceProfileSeq();
        contractPriceProfileStatusValidator.validateIfCostModelEditableStatus(contractPriceProfileSeq);
        List<PrcProfNonBrktCstMdlDTO> savedPrcProfNonBrktCstMdlDTO = prcProfNonBrktCstMdlRepository
                .fetchPrcProfNonBrktCstMdlForCMGForCPPSeq(contractPriceProfileSeq);

        for (PrcProfNonBrktCstMdlDTO dto : costModelTypeDTOList) {
            dto.setGroupType(null);
            dto.setCostModelTypeValue(null);
        }

        Collection<PrcProfNonBrktCstMdlDTO> updatedCostModelList = CollectionUtils.subtract(costModelTypeDTOList, savedPrcProfNonBrktCstMdlDTO);

        if (CollectionUtils.isNotEmpty(updatedCostModelList)) {
            prcProfNonBrktCstMdlRepository.updateCostModel(updatedCostModelList, contractPriceProfileSeq, userId);
        }

    }

    public List<PrcProfNonBrktCstMdlDTO> fetchProductTypeAndCostModel(int contractPriceProfileSeq) {

        List<PrcProfNonBrktCstMdlDTO> prcProfNonBrktCstMdlDTO = prcProfNonBrktCstMdlRepository
                .fetchPrcProfNonBrktCstMdlForCMGForCPPSeq(contractPriceProfileSeq);

        Map<Integer, String> offeringMap = itemServiceProxy.getAllProductTypesById();
        List<PrcProfNonBrktCstMdlDTO> costModelTypeDTOList = new ArrayList<>();

        Map<Integer, String> costModelMap = new HashMap<>();
        List<CostModelResponseDTO> activeCostModelList = fetchAllActiveCostModels();

        for (CostModelResponseDTO costModelType : activeCostModelList) {
            costModelMap.put(costModelType.getCostModelId(), costModelType.getCostModelTypeValue());
        }

        String productsToExclude = environment.getProperty(CPPConstants.PRODUCTS_TO_EXCLUDE);
        List<String> productCodes = Arrays.asList(productsToExclude.split(CPPConstants.DELIMITER_COMMA));
        for (PrcProfNonBrktCstMdlDTO selectedCostModel : prcProfNonBrktCstMdlDTO) {
            if (productCodes.contains(String.valueOf(selectedCostModel.getItemPriceId()))) {
                continue;
            }
            PrcProfNonBrktCstMdlDTO costModelTypeDTO = new PrcProfNonBrktCstMdlDTO();
            if (ItemPriceLevel.PRODUCT_TYPE.getCode() == selectedCostModel.getItemPriceLevelCode()) {
                costModelTypeDTO.setGroupType(offeringMap.get(Integer.valueOf(selectedCostModel.getItemPriceId())));
            } else if (ItemPriceLevel.ITEM.getCode() == selectedCostModel.getItemPriceLevelCode()) {
                costModelTypeDTO.setGroupType(WordUtils.capitalizeFully(ItemPriceLevel.ITEM.name()));
            } else if (ItemPriceLevel.SUBGROUP.getCode() == selectedCostModel.getItemPriceLevelCode()) {
                costModelTypeDTO.setGroupType(WordUtils.capitalizeFully(ItemPriceLevel.SUBGROUP.name()));
            }
            costModelTypeDTO.setCostModelId(selectedCostModel.getCostModelId());
            costModelTypeDTO.setContractPriceProfileSeq(selectedCostModel.getContractPriceProfileSeq());
            costModelTypeDTO.setItemPriceId(selectedCostModel.getItemPriceId());
            costModelTypeDTO.setItemPriceLevelCode(selectedCostModel.getItemPriceLevelCode());
            costModelTypeDTO.setGfsCustomerId(selectedCostModel.getGfsCustomerId());
            costModelTypeDTO.setGfsCustomerTypeCode(selectedCostModel.getGfsCustomerTypeCode());
            costModelTypeDTO.setCostModelTypeValue(costModelMap.get(selectedCostModel.getCostModelId()));

            costModelTypeDTOList.add(costModelTypeDTO);
        }

        return costModelTypeDTOList;
    }

    public Set<Integer> fetchIncludedListOfProductTypeIDs() {
        HashMap<Integer, String> offeringMap = (HashMap) itemServiceProxy.getAllProductTypesById();
        String productsToExclude = environment.getProperty(CPPConstants.PRODUCTS_TO_EXCLUDE);
        List<String> productCodes = Arrays.asList(productsToExclude.split(CPPConstants.DELIMITER_COMMA));
        List<Integer> excludedProductCodes = new ArrayList();
        for (String prodCode : productCodes) {
            excludedProductCodes.add(Integer.valueOf(prodCode));
        }
        offeringMap.keySet().removeAll(excludedProductCodes);
        return offeringMap.keySet();
    }
    
}
