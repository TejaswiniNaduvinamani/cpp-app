package com.gfs.cpp.component.costmodel;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.costmodel.CostModelDTO;
import com.gfs.cpp.common.dto.costmodel.CostModelResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;
import com.gfs.cpp.component.statusprocessor.ContractPriceProfileStatusValidator;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.gfs.cpp.proxy.ItemServiceProxy;
import com.gfs.cpp.proxy.ModeledCostQueryServiceProxy;

@RunWith(MockitoJUnitRunner.class)
public class CostModelServiceTest {

    @InjectMocks
    private CostModelService target;

    @Mock
    private ModeledCostQueryServiceProxy modeledCostQueryService;

    @Mock
    private PrcProfNonBrktCstMdlRepository prcProfNonBrktCstMdlRepository;

    @Mock
    private ContractPriceProfileStatusValidator contractPriceProfileStatusValidator;

    @Mock
    private ItemServiceProxy itemServiceProxy;

    @Mock
    private Environment environment;

    @Before
    public void before() throws IOException {
        when(environment.getProperty(CPPConstants.PRODUCTS_TO_EXCLUDE)).thenReturn("12");
    }
    
    
    @Test
    public void testfetchIncludedListOfProductTypeIDs() {
        Map<Integer, String> offeringMap = new HashMap<Integer, String>();
        offeringMap.put(10, "10-Last");
        offeringMap.put(12, "20-Last");
        when(itemServiceProxy.getAllProductTypesById()).thenReturn(offeringMap);
        final Set<Integer> result = target.fetchIncludedListOfProductTypeIDs();
        assertThat(result.size(), equalTo(1));
        assertThat(result.iterator().next().intValue(), equalTo(10));
        verify(itemServiceProxy).getAllProductTypesById();
    }

    @Test
    public void testFetchAllActiveCostModels() {

        List<CostModelDTO> costModelDTOList = new ArrayList<>();
        CostModelDTO costModelDTO = new CostModelDTO();
        costModelDTO.setModelId(71);
        costModelDTO.setName("Default Cost Model Name");
        costModelDTO.setProfileSelectionCriteria("Default Cost Model Profile");

        costModelDTOList.add(costModelDTO);

        when(modeledCostQueryService.lookupAllActiveCostModels()).thenReturn(costModelDTOList);

        final List<CostModelResponseDTO> result = target.fetchAllActiveCostModels();

        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getCostModelId(), equalTo(71));
        assertThat(result.get(0).getCostModelTypeValue(), equalTo("Default Cost Model Name"));

        verify(modeledCostQueryService).lookupAllActiveCostModels();
    }

    @Test
    public void testFetchProductTypeAndCostModel() throws Exception {

        Integer contractPriceProfileSeq = 1;
        List<PrcProfNonBrktCstMdlDTO> PrcProfNonBrktCstMdlDTOList = new ArrayList<>();
        PrcProfNonBrktCstMdlDTOList.add(buildPrcProfNonBrktCstMdlDTO(contractPriceProfileSeq, 0));
        PrcProfNonBrktCstMdlDTOList.add(buildPrcProfNonBrktCstMdlDTO(contractPriceProfileSeq, 1));
        PrcProfNonBrktCstMdlDTOList.add(buildPrcProfNonBrktCstMdlDTO(contractPriceProfileSeq, 2));
        
        List<CostModelDTO> costModelDTOList = new ArrayList<>();
        CostModelDTO costModelDTO = new CostModelDTO();
        costModelDTO.setModelId(1);
        costModelDTO.setName("Default Cost Model Name");
        costModelDTO.setProfileSelectionCriteria("Default Cost Model Profile");
        costModelDTOList.add(costModelDTO);

        PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO = new PrcProfNonBrktCstMdlDTO();
        prcProfNonBrktCstMdlDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        prcProfNonBrktCstMdlDTO.setCostModelId(1);
        prcProfNonBrktCstMdlDTO.setCostModelTypeValue("costModelTypeValue");
        prcProfNonBrktCstMdlDTO.setGfsCustomerId("gfsCustomerId");
        prcProfNonBrktCstMdlDTO.setGfsCustomerTypeCode(1);
        prcProfNonBrktCstMdlDTO.setItemPriceId("12");
        prcProfNonBrktCstMdlDTO.setItemPriceLevelCode(0);
        PrcProfNonBrktCstMdlDTOList.add(prcProfNonBrktCstMdlDTO);

        Map<Integer, String> offeringMap = new HashMap<Integer, String>();
        offeringMap.put(70, "70-Last");

        when(prcProfNonBrktCstMdlRepository.fetchPrcProfNonBrktCstMdlForCMGForCPPSeq(contractPriceProfileSeq)).thenReturn(PrcProfNonBrktCstMdlDTOList);
        when(itemServiceProxy.getAllProductTypesById()).thenReturn(offeringMap);
        when(modeledCostQueryService.lookupAllActiveCostModels()).thenReturn(costModelDTOList);
        final List<PrcProfNonBrktCstMdlDTO> result = target.fetchProductTypeAndCostModel(contractPriceProfileSeq);

        assertThat(result.size(), equalTo(3));
        assertThat(result.get(0).getContractPriceProfileSeq(), equalTo(contractPriceProfileSeq));
        assertThat(result.get(0).getCostModelId(), equalTo(PrcProfNonBrktCstMdlDTOList.get(0).getCostModelId()));
        assertThat(result.get(0).getGfsCustomerId(), equalTo(PrcProfNonBrktCstMdlDTOList.get(0).getGfsCustomerId()));
        assertThat(result.get(0).getGfsCustomerTypeCode(), equalTo(PrcProfNonBrktCstMdlDTOList.get(0).getGfsCustomerTypeCode()));
        assertThat(result.get(0).getItemPriceId(), equalTo(PrcProfNonBrktCstMdlDTOList.get(0).getItemPriceId()));
        assertThat(result.get(0).getItemPriceLevelCode(), equalTo(PrcProfNonBrktCstMdlDTOList.get(0).getItemPriceLevelCode()));
        assertThat(result.get(0).getCostModelTypeValue(), equalTo("Default Cost Model Name"));

        assertThat(result.get(1).getContractPriceProfileSeq(), equalTo(contractPriceProfileSeq));
        assertThat(result.get(1).getCostModelId(), equalTo(PrcProfNonBrktCstMdlDTOList.get(1).getCostModelId()));
        assertThat(result.get(1).getGfsCustomerId(), equalTo(PrcProfNonBrktCstMdlDTOList.get(1).getGfsCustomerId()));
        assertThat(result.get(1).getGfsCustomerTypeCode(), equalTo(PrcProfNonBrktCstMdlDTOList.get(1).getGfsCustomerTypeCode()));
        assertThat(result.get(1).getItemPriceId(), equalTo(PrcProfNonBrktCstMdlDTOList.get(1).getItemPriceId()));
        assertThat(result.get(1).getItemPriceLevelCode(), equalTo(PrcProfNonBrktCstMdlDTOList.get(1).getItemPriceLevelCode()));
        assertThat(result.get(0).getCostModelTypeValue(), equalTo("Default Cost Model Name"));
        
        assertThat(result.get(2).getContractPriceProfileSeq(), equalTo(contractPriceProfileSeq));
        assertThat(result.get(2).getCostModelId(), equalTo(PrcProfNonBrktCstMdlDTOList.get(2).getCostModelId()));
        assertThat(result.get(2).getGfsCustomerId(), equalTo(PrcProfNonBrktCstMdlDTOList.get(2).getGfsCustomerId()));
        assertThat(result.get(2).getGfsCustomerTypeCode(), equalTo(PrcProfNonBrktCstMdlDTOList.get(2).getGfsCustomerTypeCode()));
        assertThat(result.get(2).getItemPriceId(), equalTo(PrcProfNonBrktCstMdlDTOList.get(2).getItemPriceId()));
        assertThat(result.get(2).getItemPriceLevelCode(), equalTo(PrcProfNonBrktCstMdlDTOList.get(2).getItemPriceLevelCode()));
        assertThat(result.get(0).getCostModelTypeValue(), equalTo("Default Cost Model Name"));
        
        verify(prcProfNonBrktCstMdlRepository).fetchPrcProfNonBrktCstMdlForCMGForCPPSeq(contractPriceProfileSeq);
        verify(itemServiceProxy).getAllProductTypesById();
        verify(modeledCostQueryService).lookupAllActiveCostModels();
    }

    @Test
    public void testUpdateCostModel() throws Exception {
        Integer contractPriceProfileSeq = 1;
        List<PrcProfNonBrktCstMdlDTO> PrcProfNonBrktCstMdlDTOList = new ArrayList<>();
        PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO = new PrcProfNonBrktCstMdlDTO();
        prcProfNonBrktCstMdlDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        prcProfNonBrktCstMdlDTO.setCostModelId(1);
        prcProfNonBrktCstMdlDTO.setCostModelTypeValue("costModelTypeValue");
        prcProfNonBrktCstMdlDTO.setGfsCustomerId("gfsCustomerId");
        prcProfNonBrktCstMdlDTO.setGfsCustomerTypeCode(1);
        prcProfNonBrktCstMdlDTO.setItemPriceId("70");
        prcProfNonBrktCstMdlDTO.setItemPriceLevelCode(0);
        PrcProfNonBrktCstMdlDTOList.add(prcProfNonBrktCstMdlDTO);

        List<PrcProfNonBrktCstMdlDTO> PrcProfNonBrktCstMdlDTOListSaved = new ArrayList<>();
        PrcProfNonBrktCstMdlDTOListSaved.add(buildPrcProfNonBrktCstMdlDTO(contractPriceProfileSeq, 0));

        Map<Integer, String> offeringMap = new HashMap<Integer, String>();
        offeringMap.put(70, "70-Last");

        when(prcProfNonBrktCstMdlRepository.fetchPrcProfNonBrktCstMdlForCMGForCPPSeq(contractPriceProfileSeq)).thenReturn(PrcProfNonBrktCstMdlDTOListSaved);
        target.updateCostModel(PrcProfNonBrktCstMdlDTOList, "test");

        verify(prcProfNonBrktCstMdlRepository).updateCostModel(PrcProfNonBrktCstMdlDTOList, contractPriceProfileSeq, "test");
        verify(contractPriceProfileStatusValidator).validateIfCostModelEditableStatus(contractPriceProfileSeq);

    }

    private PrcProfNonBrktCstMdlDTO buildPrcProfNonBrktCstMdlDTO(Integer contractPriceProfileSeq, int itemPriceLevelCode) {

        PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO = new PrcProfNonBrktCstMdlDTO();
        prcProfNonBrktCstMdlDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        prcProfNonBrktCstMdlDTO.setCostModelId(1);
        prcProfNonBrktCstMdlDTO.setCostModelTypeValue("costModelTypeValue");
        prcProfNonBrktCstMdlDTO.setGfsCustomerId("gfsCustomerId");
        prcProfNonBrktCstMdlDTO.setGfsCustomerTypeCode(1);
        prcProfNonBrktCstMdlDTO.setItemPriceId("70");
        prcProfNonBrktCstMdlDTO.setGroupType("group type");
        prcProfNonBrktCstMdlDTO.setItemPriceLevelCode(itemPriceLevelCode);
        return prcProfNonBrktCstMdlDTO;
    }
}
