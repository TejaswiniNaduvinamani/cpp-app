package com.gfs.cpp.component.document.documentgenerator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.component.markup.builder.MarkupDTOBuilder;
import com.gfs.cpp.component.markup.builder.MarkupWrapperBuilder;

@RunWith(MockitoJUnitRunner.class)
public class MarkupWrapperBuilderTest {

    private static final String GFS_CUSTOMER_ID = "1";
    private static final int contractPriceProfileSeq = 1;
    private static final String ExceptionName = "Exception";
    private static final Date effectiveDate = new LocalDate(2018, 01, 01).toDate();
    private static final Date expirationDate = new LocalDate(9999, 01, 01).toDate();

    @InjectMocks
    private MarkupWrapperBuilder target;

    @Mock
    private MarkupDTOBuilder markupDTOBuilder;

    @Test
    public void shouldBuildDefaultMarkupWrapper() {

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(GFS_CUSTOMER_ID);
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setProductType("1");
        productTypeMarkupDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDTO.setGfsCustomerTypeCode(31);

        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = Collections.singletonList(productTypeMarkupDTO);
        when(markupDTOBuilder.buildDefaultProductMarkupDTOList(GFS_CUSTOMER_ID, effectiveDate, expirationDate)).thenReturn(productTypeMarkupDTOList);

        MarkupWrapperDTO result = target.buildDefaultMarkupWrapper(contractPriceProfileSeq, GFS_CUSTOMER_ID, ExceptionName, effectiveDate,
                expirationDate);

        verify(markupDTOBuilder).buildDefaultProductMarkupDTOList(GFS_CUSTOMER_ID, effectiveDate, expirationDate);

        assertThat(result.getProductMarkupList(), equalTo(productTypeMarkupDTOList));
        assertThat(result.getItemLevelMarkupList(), equalTo(Collections.<ItemLevelMarkupDTO> emptyList()));
        assertThat(result.getSubgroupMarkupList(), equalTo(Collections.<SubgroupMarkupDTO> emptyList()));
        assertThat(result.getGfsCustomerId(), equalTo(GFS_CUSTOMER_ID));
        assertThat(result.getContractPriceProfileSeq(), equalTo(contractPriceProfileSeq));
    }

    @Test
    public void shouldBuildMarkupWrapperFromGrid() {

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId(GFS_CUSTOMER_ID);
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setProductType("1");
        productTypeMarkupDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDTO.setGfsCustomerTypeCode(31);

        List<ProductTypeMarkupDTO> productTypeMarkupDTOList = Collections.singletonList(productTypeMarkupDTO);

        MarkupGridDTO markupGridDTO = new MarkupGridDTO();
        markupGridDTO.setGfsCustomerId(GFS_CUSTOMER_ID);
        markupGridDTO.setMarkupName(ExceptionName);
        markupGridDTO.setProductTypeMarkups(productTypeMarkupDTOList);
        markupGridDTO.setItemMarkups(Collections.<ItemLevelMarkupDTO> emptyList());
        markupGridDTO.setSubgroupMarkups(Collections.<SubgroupMarkupDTO> emptyList());

        MarkupWrapperDTO result = target.buildMarkupWrapperFromGrid(contractPriceProfileSeq, markupGridDTO);

        assertThat(result.getGfsCustomerId(), equalTo(GFS_CUSTOMER_ID));
        assertThat(result.getContractPriceProfileSeq(), equalTo(contractPriceProfileSeq));
        assertThat(result.getProductMarkupList(), equalTo(productTypeMarkupDTOList));
        assertThat(result.getItemLevelMarkupList(), equalTo(Collections.<ItemLevelMarkupDTO> emptyList()));
        assertThat(result.getSubgroupMarkupList(), equalTo(Collections.<SubgroupMarkupDTO> emptyList()));

    }
}
