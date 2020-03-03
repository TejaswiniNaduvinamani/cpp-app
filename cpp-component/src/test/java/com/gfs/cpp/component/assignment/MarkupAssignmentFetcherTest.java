
package com.gfs.cpp.component.assignment;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.MarkupAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGContractDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.component.assignment.MarkupAssignmentFetcher;
import com.gfs.cpp.component.markup.MarkupFetcher;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.proxy.CustomerServiceProxy;

@RunWith(MockitoJUnitRunner.class)
public class MarkupAssignmentFetcherTest {

    @InjectMocks
    private MarkupAssignmentFetcher target;

    @Mock
    private MarkupFetcher markupFetcher;

    @Mock
    private CustomerServiceProxy customerServiceProxy;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Mock
    private CppConceptMappingRepository cppConceptMappingRepository;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Test
    public void shouldReturnMarkupsForCustomer() {
        int contractPriceProfileSeq = 1;

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1");
        cmgCustomerResponseDTO.setTypeCode(31);
        List<CMGCustomerResponseDTO> cmgCustomerResponseDTOList = new ArrayList<>();
        cmgCustomerResponseDTOList.add(cmgCustomerResponseDTO);

        CMGContractDTO value = new CMGContractDTO();
        value.setContractName("TEST");
        value.setContractPriceProfileId("1");

        Map<String, MarkupGridDTO> markupGridByCustomer = buildMarkupGridByCustomer();

        List<String> gfsCustomerIdList = new ArrayList<>();
        gfsCustomerIdList.add("1");

        when(markupFetcher.buildMarkupGridByCustomer(contractPriceProfileSeq, null, null)).thenReturn(markupGridByCustomer);
        when(contractPriceProfCustomerRepository.fetchGFSCustomerDetailsList(eq(contractPriceProfileSeq))).thenReturn(cmgCustomerResponseDTOList);
        when(contractPriceProfCustomerRepository.fetchCPPCustomerSeq(eq(cmgCustomerResponseDTOList.get(0).getId()),
                eq(cmgCustomerResponseDTOList.get(0).getTypeCode()), eq(contractPriceProfileSeq))).thenReturn(1);
        when(customerItemPriceRepository.fetchAllGfsCustomerIdsInCustomerItemPrice(contractPriceProfileSeq)).thenReturn(gfsCustomerIdList);
        when(contractPriceProfileRepository.fetchExpireLowerIndicator(contractPriceProfileSeq)).thenReturn(-1);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(buildContractPriceProfileDetails());

        List<MarkupAssignmentDTO> result = target.fetchMarkupAssignment(contractPriceProfileSeq);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getGfsCustomerId(), is("1"));
        assertThat(result.get(0).getExpireLowerInd(), is(-1));
        assertThat(result.get(0).getMarkupList().size(), is(1));
        assertThat(result.get(0).getMarkupList().get(0).getItemPriceId(), is(1));
        assertThat(result.get(0).getMarkupList().get(0).getProductType(), is("TEST"));
        assertThat(result.get(0).getMarkupList().get(0).getMarkup(), is("TEST"));
        assertThat(result.get(0).getMarkupList().get(0).getMarkupType(), is(1));
        assertThat(result.get(0).getMarkupList().get(0).getUnit(), is("$"));
        assertThat(result.get(0).getMarkupName(), is(buildContractPriceProfileDetails().getClmContractName()));
        assertThat(result.get(0).getGfsCustomerType(), is("CMG"));
        assertThat(result.get(0).getIsAssignmentSaved(), is(false));

        verify(markupFetcher).buildMarkupGridByCustomer(contractPriceProfileSeq, null, null);
        verify(contractPriceProfCustomerRepository).fetchGFSCustomerDetailsList(eq(contractPriceProfileSeq));
        verify(contractPriceProfCustomerRepository).fetchCPPCustomerSeq(eq(cmgCustomerResponseDTOList.get(0).getId()),
                eq(cmgCustomerResponseDTOList.get(0).getTypeCode()), eq(contractPriceProfileSeq));
        verify(contractPriceProfileRepository).fetchExpireLowerIndicator(contractPriceProfileSeq);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
    }

    @Test
    public void shouldReturnSavedAssignment() {
        int contractPriceProfileSeq = 1;
        String customerName = "TEST";

        List<RealCustomerDTO> realCustomerDTOList = new ArrayList<>();
        RealCustomerDTO realCustomerDTO = new RealCustomerDTO();
        realCustomerDTO.setRealCustomerId("1");
        realCustomerDTO.setRealCustomerName("TEST");
        realCustomerDTO.setRealCustomerType(31);
        realCustomerDTO.setRealCustomerGroupType("CMG");
        realCustomerDTOList.add(realCustomerDTO);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("1");
        cmgCustomerResponseDTO.setTypeCode(31);
        List<CMGCustomerResponseDTO> cmgCustomerResponseDTOList = new ArrayList<>();
        cmgCustomerResponseDTOList.add(cmgCustomerResponseDTO);
        List<String> gfsCustomerIdList = new ArrayList<>();
        gfsCustomerIdList.add("1");

        Map<String, MarkupGridDTO> markupWrapperDTOList = buildMarkupGridByCustomer();

        when(markupFetcher.buildMarkupGridByCustomer(contractPriceProfileSeq, null, null)).thenReturn(markupWrapperDTOList);
        when(contractPriceProfCustomerRepository.fetchGFSCustomerDetailsList(eq(contractPriceProfileSeq))).thenReturn(cmgCustomerResponseDTOList);
        when(customerServiceProxy.findCustomerName(eq(cmgCustomerResponseDTOList.get(0).getId()),
                eq(cmgCustomerResponseDTOList.get(0).getTypeCode()))).thenReturn(customerName);
        when(contractPriceProfCustomerRepository.fetchCPPCustomerSeq(eq("1"), eq(31), eq(1))).thenReturn(1);
        when(cppConceptMappingRepository.fetchRealCustomersMapped(eq(1))).thenReturn(realCustomerDTOList);
        when(contractPriceProfileRepository.fetchExpireLowerIndicator(contractPriceProfileSeq)).thenReturn(1);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(buildContractPriceProfileDetails());

        List<MarkupAssignmentDTO> result = target.fetchMarkupAssignment(contractPriceProfileSeq);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getGfsCustomerId(), is("1"));
        assertThat(result.get(0).getExpireLowerInd(), is(1));
        assertThat(result.get(0).getMarkupList().size(), is(1));
        assertThat(result.get(0).getMarkupList().get(0).getItemPriceId(), is(1));
        assertThat(result.get(0).getMarkupList().get(0).getProductType(), is("TEST"));
        assertThat(result.get(0).getMarkupList().get(0).getMarkup(), is("TEST"));
        assertThat(result.get(0).getMarkupList().get(0).getMarkupType(), is(1));
        assertThat(result.get(0).getMarkupList().get(0).getUnit(), is("$"));
        assertThat(result.get(0).getMarkupName(), is(buildContractPriceProfileDetails().getClmContractName()));
        assertThat(result.get(0).getGfsCustomerType(), is("CMG"));
        assertThat(result.get(0).getIsAssignmentSaved(), is(true));
        assertThat(result.get(0).getRealCustomerDTOList().get(0).getRealCustomerType(), is(31));
        assertThat(result.get(0).getRealCustomerDTOList().get(0).getRealCustomerGroupType(), is("CMG"));
        assertThat(result.get(0).getRealCustomerDTOList().get(0).getIsCustomerSaved(), is(true));

        verify(markupFetcher).buildMarkupGridByCustomer(contractPriceProfileSeq, null, null);
        verify(contractPriceProfCustomerRepository).fetchGFSCustomerDetailsList(eq(contractPriceProfileSeq));
        verify(customerServiceProxy).findCustomerName(eq(cmgCustomerResponseDTOList.get(0).getId()),
                eq(cmgCustomerResponseDTOList.get(0).getTypeCode()));
        verify(cppConceptMappingRepository).fetchRealCustomersMapped(1);
        verify(contractPriceProfileRepository).fetchExpireLowerIndicator(contractPriceProfileSeq);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
    }

    private Map<String, MarkupGridDTO> buildMarkupGridByCustomer() {
        Map<String, MarkupGridDTO> markupGridByCustomer = new HashMap<>();
        List<ProductTypeMarkupDTO> markup = new ArrayList<>();
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setEffectiveDate(new LocalDate(2010, 01, 01).toDate());
        productTypeMarkupDTO.setExpirationDate(new LocalDate(2012, 01, 01).toDate());
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setMarkup("TEST");
        productTypeMarkupDTO.setMarkupType(1);
        productTypeMarkupDTO.setProductType("TEST");
        productTypeMarkupDTO.setUnit("$");

        markup.add(productTypeMarkupDTO);
        List<ItemLevelMarkupDTO> item = new ArrayList<>();
        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setMarkupType(1);

        MarkupGridDTO markupGridDTO = new MarkupGridDTO();
        markupGridDTO.setGfsCustomerId("1");
        markupGridDTO.setProductTypeMarkups(markup);
        markupGridDTO.setItemMarkups(item);
        markupGridDTO.setMarkupName("TEST");

        markupGridByCustomer.put("1", markupGridDTO);
        return markupGridByCustomer;
    }

    private ContractPricingResponseDTO buildContractPriceProfileDetails() {
        String clmContractName = "clmContractName";
        int cppId = 123;
        ContractPricingResponseDTO contractPriceProfileDetails = new ContractPricingResponseDTO();

        contractPriceProfileDetails.setContractPriceProfileId(cppId);
        contractPriceProfileDetails.setClmContractName(clmContractName);
        return contractPriceProfileDetails;
    }

}
