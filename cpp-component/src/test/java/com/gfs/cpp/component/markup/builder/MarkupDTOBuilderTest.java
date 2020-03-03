package com.gfs.cpp.component.markup.builder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.component.price.common.types.AmountType;
import com.gfs.corp.component.price.common.types.PricingUnitType;
import com.gfs.cpp.common.dto.markup.ItemInformationDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.component.markup.MarkupFetcher;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;
import com.gfs.cpp.proxy.ItemServiceProxy;

@RunWith(MockitoJUnitRunner.class)
public class MarkupDTOBuilderTest {

    private static final String GFS_CUSTOMER_ID = "1";
    private static final Date effectiveDate = new LocalDate(2018, 01, 01).toDate();
    private static final Date expirationDate = new LocalDate(9999, 01, 01).toDate();

    @InjectMocks
    private MarkupDTOBuilder target;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Mock
    private PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    @Mock
    private ItemServiceProxy itemServiceProxy;

    @Mock
    private MarkupFetcher markupFetcher;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Test
    public void shouldBuildDefaultProductMarkupDTOList() {

        Map<Integer, String> productMap = new HashMap<>();
        productMap.put(1, "Test");

        when(itemServiceProxy.getAllProductTypesById()).thenReturn(productMap);

        List<ProductTypeMarkupDTO> result = target.buildDefaultProductMarkupDTOList(GFS_CUSTOMER_ID, effectiveDate, expirationDate);

        verify(itemServiceProxy).getAllProductTypesById();

        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getGfsCustomerId(), equalTo(GFS_CUSTOMER_ID));
        assertThat(result.get(0).getProductType(), equalTo("Test"));
        assertThat(result.get(0).getItemPriceId(), equalTo(1));
        assertThat(result.get(0).getEffectiveDate(), equalTo(effectiveDate));
        assertThat(result.get(0).getExpirationDate(), equalTo(expirationDate));

    }

    @Test
    public void shouldFormatAmount() {

        String markupAmount = "2";
        String result = target.formatAmount(markupAmount);
        assertThat(result, equalTo("2.00"));
    }

    @Test
    public void shouldBuildDefaultItemLevelMarkupDTO() {

        ItemLevelMarkupDTO result = target.buildDefaultItemLevelMarkupDTO(expirationDate, effectiveDate);

        assertThat(result.getUnit(), equalTo("$"));
        assertThat(result.getStockingCode(), equalTo(0));
        assertThat(result.getMarkupType(), equalTo(2));
        assertThat(result.getIsItemSaved(), equalTo(false));
        assertThat(result.getEffectiveDate(), equalTo(effectiveDate));
        assertThat(result.getExpirationDate(), equalTo(expirationDate));
    }

    @Test
    public void shoudlBuildItemLevelMarkupDTOForProductTypeMarkup() {

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setMarkup("2");
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setEffectiveDate(effectiveDate);
        productTypeMarkupDTO.setExpirationDate(expirationDate);
        productTypeMarkupDTO.setUnit("$");
        productTypeMarkupDTO.setMarkupType(1);
        ItemInformationDTO itemInformationDTO = new ItemInformationDTO();
        itemInformationDTO.setItemDescription("Test");
        itemInformationDTO.setStockingCode("0");

        ItemLevelMarkupDTO result = target.buildItemLevelMarkupDTO(itemInformationDTO, false, productTypeMarkupDTO);

        assertThat(result.getUnit(), equalTo("$"));
        assertThat(result.getStockingCode(), equalTo(0));
        assertThat(result.getMarkupType(), equalTo(1));
        assertThat(result.getMarkup(), equalTo("2.00"));
        assertThat(result.getItemId(), equalTo("1"));
        assertThat(result.isNoItemId(), equalTo(false));
        assertThat(result.getIsItemSaved(), equalTo(true));
        assertThat(result.getEffectiveDate(), equalTo(effectiveDate));
        assertThat(result.getExpirationDate(), equalTo(expirationDate));

    }

    @Test
    public void shoudlBuildItemLevelMarkupDTOForItemLevelMarkup() {

        int stockingCode = 0;
        String itemId = "1";
        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setMarkup("2");
        itemLevelMarkupDTO.setUnit("$");
        itemLevelMarkupDTO.setMarkupType(1);
        itemLevelMarkupDTO.setStockingCode(stockingCode);

        ItemLevelMarkupDTO result = target.buildItemLevelMarkupDTO(itemLevelMarkupDTO, itemId, true, effectiveDate, expirationDate);

        assertThat(result.getUnit(), equalTo("$"));
        assertThat(result.getStockingCode(), equalTo(0));
        assertThat(result.getMarkupType(), equalTo(1));
        assertThat(result.getMarkup(), equalTo("2.00"));
        assertThat(result.getItemId(), equalTo("1"));
        assertThat(result.isNoItemId(), equalTo(true));
        assertThat(result.getIsItemSaved(), equalTo(true));
        assertThat(result.getStockingCode(), equalTo(stockingCode));
        assertThat(result.getEffectiveDate(), equalTo(effectiveDate));
        assertThat(result.getExpirationDate(), equalTo(expirationDate));

    }

    @Test
    public void shouldBuildDefaultSubgroupMarkupResult() {
        Date pricingEffectiveDate = effectiveDate;
        Date pricingExpirationDate = expirationDate;

        SubgroupMarkupDTO subgroupMarkupDTO = target.buildDefaultSubgroupMarkupDTO(pricingExpirationDate, pricingEffectiveDate);

        assertThat(subgroupMarkupDTO.getEffectiveDate(), equalTo(pricingEffectiveDate));
        assertThat(subgroupMarkupDTO.getExpirationDate(), equalTo(pricingExpirationDate));
        assertThat(subgroupMarkupDTO.getIsSubgroupSaved(), equalTo(false));
        assertThat(subgroupMarkupDTO.getMarkup(), equalTo(StringUtils.EMPTY));
        assertThat(subgroupMarkupDTO.getMarkupType(), equalTo(PricingUnitType.CASE.getCode()));
        assertThat(subgroupMarkupDTO.getSubgroupDesc(), equalTo(StringUtils.EMPTY));
        assertThat(subgroupMarkupDTO.getSubgroupId(), equalTo(StringUtils.EMPTY));
        assertThat(subgroupMarkupDTO.getUnit(), equalTo(AmountType.DOLLAR.getCode()));
    }

    @Test
    public void shouldBuildSubgroupMarkupDTO() {
        String subgroupDesc = "Test";
        String subgroupId = "1";
        String markup = "12.00";
        int markupType = 2;
        String unit = "$";

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setEffectiveDate(effectiveDate);
        productTypeMarkupDTO.setExpirationDate(expirationDate);
        productTypeMarkupDTO.setGfsCustomerId(GFS_CUSTOMER_ID);
        productTypeMarkupDTO.setItemPriceId(1);
        productTypeMarkupDTO.setProductType("1");
        productTypeMarkupDTO.setUnit(unit);
        productTypeMarkupDTO.setMarkupType(markupType);
        productTypeMarkupDTO.setMarkup(markup);

        SubgroupMarkupDTO result = target.buildSubgroupMarkupDTO(productTypeMarkupDTO, subgroupDesc, effectiveDate, expirationDate);

        assertThat(result.getMarkup(), equalTo(markup));
        assertThat(result.getSubgroupDesc(), equalTo(subgroupDesc));
        assertThat(result.getSubgroupId(), equalTo(subgroupId));
        assertThat(result.getMarkupType(), equalTo(markupType));
    }

}
