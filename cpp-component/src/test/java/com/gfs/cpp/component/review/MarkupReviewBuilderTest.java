package com.gfs.cpp.component.review;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.component.markup.MarkupFetcher;
import com.gfs.cpp.component.review.helper.MarkupReviewBuilder;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.proxy.ItemServiceProxy;

@RunWith(MockitoJUnitRunner.class)
public class MarkupReviewBuilderTest {

    private static final String CUSTOMER_ONE = "1";
    private static final String CUSTOMER_TWO = "1";
    private static final Date effectiveDate = new LocalDate(2018, 01, 01).toDate();
    private static final Date expirationDate = new LocalDate(9999, 01, 01).toDate();

    @InjectMocks
    private MarkupReviewBuilder target;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Mock
    private ItemServiceProxy itemServiceProxy;

    @Mock
    private MarkupFetcher markupFetcher;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Test
    public void shouldBuildMarkupResult() {

        int contractPriceProfileSeq = 1;
        String contractName = "Test";
        int cppId = 123;
        String clmContractName = "contract-Name";

        Map<String, MarkupGridDTO> markupGridByCustomer = new HashMap<>();
        List<CMGCustomerResponseDTO> cmgCustomerList = new ArrayList<>();
        CMGCustomerResponseDTO cMGCustomerResponseDTOOne = new CMGCustomerResponseDTO();
        CMGCustomerResponseDTO cMGCustomerResponseDTOTwo = new CMGCustomerResponseDTO();

        MarkupGridDTO markupGridDTOOne = new MarkupGridDTO();
        markupGridDTOOne.setGfsCustomerId(CUSTOMER_ONE);
        markupGridDTOOne.setProductTypeMarkups(buildProductMarkupList(CUSTOMER_ONE));
        markupGridDTOOne.setMarkupName(contractName);
        MarkupGridDTO markupGridDTOTwo = new MarkupGridDTO();
        markupGridDTOTwo.setGfsCustomerId(CUSTOMER_TWO);
        markupGridDTOTwo.setProductTypeMarkups(buildProductMarkupList(CUSTOMER_TWO));
        markupGridDTOTwo.setMarkupName(contractName);
        markupGridByCustomer.put(CUSTOMER_TWO, markupGridDTOTwo);
        markupGridByCustomer.put(CUSTOMER_ONE, markupGridDTOOne);

        cMGCustomerResponseDTOOne.setId(CUSTOMER_ONE);
        cMGCustomerResponseDTOOne.setTypeCode(31);
        cMGCustomerResponseDTOOne.setDefaultCustomerInd(1);
        cMGCustomerResponseDTOTwo.setId(CUSTOMER_TWO);
        cMGCustomerResponseDTOTwo.setTypeCode(31);
        cMGCustomerResponseDTOTwo.setDefaultCustomerInd(1);
        cmgCustomerList.add(cMGCustomerResponseDTOOne);
        cmgCustomerList.add(cMGCustomerResponseDTOTwo);

        ContractPricingResponseDTO contractPriceProfileDetails = new ContractPricingResponseDTO();
        contractPriceProfileDetails.setClmContractName(clmContractName);
        contractPriceProfileDetails.setContractPriceProfileId(cppId);

        when(contractPriceProfCustomerRepository.fetchGFSCustomerDetailsList(contractPriceProfileSeq)).thenReturn(cmgCustomerList);
        when(markupFetcher.buildMarkupGridByCustomer(contractPriceProfileSeq, effectiveDate, expirationDate)).thenReturn(markupGridByCustomer);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(contractPriceProfileDetails);

        MarkupReviewDTO markupDTO = target.buildFetchMarkupDTO(contractPriceProfileSeq, effectiveDate, expirationDate);

        verify(contractPriceProfCustomerRepository).fetchGFSCustomerDetailsList(contractPriceProfileSeq);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        assertThat(markupDTO.isPricingOverrideInd(), equalTo(false));
        assertThat(markupDTO.getMarkupGridDTOs().size(), equalTo(2));
        assertThat(markupDTO.getMarkupGridDTOs().get(0).getGfsCustomerId(), equalTo(CUSTOMER_ONE));
        assertThat(markupDTO.getMarkupGridDTOs().get(0).getMarkupName(), equalTo(clmContractName));
        assertThat(markupDTO.getMarkupGridDTOs().get(0).getProductTypeMarkups().size(), equalTo(1));
        assertThat(markupDTO.getMarkupGridDTOs().get(0).getProductTypeMarkups().get(0).getGfsCustomerId(), equalTo(CUSTOMER_ONE));
        assertThat(markupDTO.getMarkupGridDTOs().get(0).getProductTypeMarkups().get(0).getMarkup(), equalTo("5"));
        assertThat(markupDTO.getMarkupGridDTOs().get(0).getProductTypeMarkups().get(0).getUnit(), equalTo("$"));
        assertThat(markupDTO.getMarkupGridDTOs().get(0).getProductTypeMarkups().get(0).getItemPriceId(), equalTo(1));
        assertThat(markupDTO.getMarkupGridDTOs().get(1).getGfsCustomerId(), equalTo(CUSTOMER_TWO));
        assertThat(markupDTO.getMarkupGridDTOs().get(1).getMarkupName(), equalTo(clmContractName));
        assertThat(markupDTO.getMarkupGridDTOs().get(1).getProductTypeMarkups().size(), equalTo(1));
        assertThat(markupDTO.getMarkupGridDTOs().get(1).getProductTypeMarkups().get(0).getGfsCustomerId(), equalTo(CUSTOMER_TWO));
        assertThat(markupDTO.getMarkupGridDTOs().get(1).getProductTypeMarkups().get(0).getMarkup(), equalTo("5"));
        assertThat(markupDTO.getMarkupGridDTOs().get(1).getProductTypeMarkups().get(0).getUnit(), equalTo("$"));
        assertThat(markupDTO.getMarkupGridDTOs().get(1).getProductTypeMarkups().get(0).getItemPriceId(), equalTo(1));

    }

    private List<ProductTypeMarkupDTO> buildProductMarkupList(String gfsCustomerId) {
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        List<ProductTypeMarkupDTO> fetchProductTypeMarkups = new ArrayList<>();
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setGfsCustomerId(gfsCustomerId);
        productTypeMarkupDTO.setMarkup("5");
        productTypeMarkupDTO.setUnit("$");
        fetchProductTypeMarkups.add(productTypeMarkupDTO);
        return fetchProductTypeMarkups;
    }
}
