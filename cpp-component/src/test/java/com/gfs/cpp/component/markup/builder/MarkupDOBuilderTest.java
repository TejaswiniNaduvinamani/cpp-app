package com.gfs.cpp.component.markup.builder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.corp.component.price.common.types.PriceLockinReason;
import com.gfs.corp.component.price.common.types.PriceLockinType;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.model.markup.FutureItemDO;
import com.gfs.cpp.common.model.markup.MarkupWrapperDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.markup.builder.MarkupDOBuilder;

@RunWith(MockitoJUnitRunner.class)
public class MarkupDOBuilderTest {

    private static final Date pricingEffectiveDate = new LocalDate(2010, 01, 01).toDate();
    private static final Date pricingExpirationDate = new LocalDate(2012, 01, 01).toDate();

    @InjectMocks
    private MarkupDOBuilder target;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void buildMarkupWrapperDOTest() throws ParseException {
        ProductTypeMarkupDTO markup = new ProductTypeMarkupDTO();
        List<ProductTypeMarkupDTO> markupList = new ArrayList<>();
        markup.setMarkup("10.43");
        markup.setUnit("$");
        markup.setEffectiveDate(pricingEffectiveDate);
        markup.setExpirationDate(pricingExpirationDate);
        markup.setItemPriceId(1);
        markup.setProductType("2");
        markup.setMarkupType(2);
        markupList.add(markup);

        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        markupWrapper.setIsMarkupSaved(false);
        markupWrapper.setGfsCustomerId("2");
        markupWrapper.setMarkupName("Test");
        markupWrapper.setProductMarkupList(markupList);
        markupWrapper.setItemLevelMarkupList(Collections.<ItemLevelMarkupDTO> emptyList());
        markupWrapper.setSubgroupMarkupList(Collections.<SubgroupMarkupDTO> emptyList());

        MarkupWrapperDO markupWrapperDO = target.buildMarkupWrapperDO(markupWrapper, 124, "v8cq5");

        assertThat(markupWrapperDO.getContractPriceProfileSeq(), equalTo(124));
        assertThat(markupWrapperDO.getMarkupList().get(0).getPriceLockedInTypeCode(), equalTo(PriceLockinType.COST_PLUS.getCode()));
        assertThat(markupWrapperDO.getMarkupList().get(0).getPriceLockinReasonCode(), equalTo(PriceLockinReason.COMPETITIVE.code));
        assertThat(markupWrapperDO.getUserName(), equalTo("v8cq5"));
        assertThat(markupWrapperDO.getMarkupList().size(), equalTo(1));
        assertThat(markupWrapperDO.getMarkupList().get(0).getItemPriceId(), is(markup.getItemPriceId()));
        assertThat(markupWrapperDO.getMarkupList().get(0).getProductType(), is(markup.getProductType()));
    }

    @Test
    public void buildMarkupWrapperDOForItemAndFutureItem() throws ParseException {

        ItemLevelMarkupDTO itemLevelMarkup = new ItemLevelMarkupDTO();
        List<ItemLevelMarkupDTO> itemLevelMarkupList = new ArrayList<>();
        itemLevelMarkup.setMarkup("10.43");
        itemLevelMarkup.setUnit("$");
        itemLevelMarkup.setEffectiveDate(pricingEffectiveDate);
        itemLevelMarkup.setExpirationDate(pricingExpirationDate);
        itemLevelMarkup.setItemDesc("Test Future Item");
        itemLevelMarkup.setItemId("");
        itemLevelMarkup.setMarkupType(2);
        itemLevelMarkup.setNoItemId(true);
        itemLevelMarkupList.add(itemLevelMarkup);
        itemLevelMarkup = new ItemLevelMarkupDTO();
        itemLevelMarkup.setMarkup("10.43");
        itemLevelMarkup.setUnit("$");
        itemLevelMarkup.setEffectiveDate(pricingEffectiveDate);
        itemLevelMarkup.setExpirationDate(pricingExpirationDate);
        itemLevelMarkup.setItemDesc("Test Item");
        itemLevelMarkup.setItemId("12");
        itemLevelMarkup.setMarkupType(2);
        itemLevelMarkup.setNoItemId(false);
        itemLevelMarkupList.add(itemLevelMarkup);

        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        markupWrapper.setIsMarkupSaved(false);
        markupWrapper.setGfsCustomerId("2");
        markupWrapper.setMarkupName("Test");
        markupWrapper.setProductMarkupList(Collections.<ProductTypeMarkupDTO> emptyList());
        markupWrapper.setItemLevelMarkupList(itemLevelMarkupList);
        markupWrapper.setSubgroupMarkupList(Collections.<SubgroupMarkupDTO> emptyList());

        MarkupWrapperDO markupWrapperDO = target.buildMarkupWrapperDO(markupWrapper, 124, "v8cq5");

        assertThat(markupWrapperDO.getContractPriceProfileSeq(), equalTo(124));
        assertThat(markupWrapperDO.getUserName(), equalTo("v8cq5"));
        assertThat(markupWrapperDO.getMarkupList().size(), equalTo(1));
        assertThat(markupWrapperDO.getFutureItemList().size(), equalTo(1));
        assertThat(markupWrapperDO.getMarkupList().get(0).getPriceLockedInTypeCode(), equalTo(PriceLockinType.COST_PLUS.getCode()));
        assertThat(markupWrapperDO.getMarkupList().get(0).getPriceLockinReasonCode(), equalTo(PriceLockinReason.COMPETITIVE.code));
        assertThat(String.valueOf(markupWrapperDO.getMarkupList().get(0).getItemPriceId()), equalTo(itemLevelMarkupList.get(1).getItemId()));
        assertThat(markupWrapperDO.getFutureItemList().get(0).getItemDesc(), equalTo(itemLevelMarkupList.get(0).getItemDesc()));

    }

    @Test
    public void shouldBuildProductTypeDOForSubgroups() {
        int contractPriceProfileSeq = 124;
        String userName = "v8cq5";
        List<SubgroupMarkupDTO> subgroupMarkupList = new ArrayList<>();
        SubgroupMarkupDTO subgroupMarkupDTO = new SubgroupMarkupDTO();
        subgroupMarkupDTO.setEffectiveDate(pricingEffectiveDate);
        subgroupMarkupDTO.setExpirationDate(pricingExpirationDate);
        subgroupMarkupDTO.setIsSubgroupSaved(true);
        subgroupMarkupDTO.setMarkup("12.00");
        subgroupMarkupDTO.setMarkupType(2);
        subgroupMarkupDTO.setSubgroupDesc("FLOOR MATS");
        subgroupMarkupDTO.setSubgroupId("20213");
        subgroupMarkupDTO.setUnit("$");
        subgroupMarkupList.add(subgroupMarkupDTO);

        MarkupWrapperDTO markupWrapper = new MarkupWrapperDTO();
        markupWrapper.setIsMarkupSaved(false);
        markupWrapper.setGfsCustomerId("2");
        markupWrapper.setMarkupName("Test");
        markupWrapper.setProductMarkupList(Collections.<ProductTypeMarkupDTO> emptyList());
        markupWrapper.setItemLevelMarkupList(Collections.<ItemLevelMarkupDTO> emptyList());
        markupWrapper.setSubgroupMarkupList(subgroupMarkupList);

        MarkupWrapperDO result = target.buildMarkupWrapperDO(markupWrapper, contractPriceProfileSeq, userName);

        assertThat(result.getMarkupList().get(0).getProductType(), equalTo("1"));
        assertThat((result.getMarkupList().get(0).getItemPriceId()), is(new Integer(subgroupMarkupDTO.getSubgroupId())));
        assertThat(result.getMarkupList().get(0).getMarkup(), equalTo(new BigDecimal(subgroupMarkupDTO.getMarkup())));

    }

    @Test
    public void shouldBuildProductMarkupDOList() {
        String userId = "test user";
        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId("AgreementId");
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(1);
        contractDetails.setContractPriceProfStatusCode(1);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2018, 01, 01).toDate());

        List<ProductTypeMarkupDTO> productTypeMarkupList = new ArrayList<>();
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId("1");
        productTypeMarkupDTO.setEffectiveDate(new LocalDate(2018, 01, 05).toDate());
        productTypeMarkupDTO.setExpirationDate(new LocalDate(2019, 01, 05).toDate());
        productTypeMarkupDTO.setItemPriceId(2);
        productTypeMarkupDTO.setMarkup("1");
        productTypeMarkupDTO.setMarkupType(2);
        productTypeMarkupDTO.setProductType("1");
        productTypeMarkupDTO.setUnit("2");
        productTypeMarkupDTO.setGfsCustomerTypeCode(12);
        productTypeMarkupList.add(productTypeMarkupDTO);

        List<ProductTypeMarkupDO> productTypeMarkupDOList = new ArrayList<>();
        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();
        productTypeMarkupDO.setEffectiveDate(new Date());
        productTypeMarkupDO.setExpirationDate(new Date());
        productTypeMarkupDO.setItemPriceId(2);
        productTypeMarkupDO.setMarkup(new BigDecimal(0));
        productTypeMarkupDO.setMarkupType(2);
        productTypeMarkupDO.setProductType("");
        productTypeMarkupDO.setUnit("");
        productTypeMarkupDO.setGfsCustomerId("1234");
        productTypeMarkupDO.setCustomerTypeCode(3);
        productTypeMarkupDOList.add(productTypeMarkupDO);

        List<ProductTypeMarkupDO> actual = target.buildProductMarkupDOList(userId, contractDetails.getPricingEffectiveDate(),
                contractDetails.getPricingExpirationDate(), productTypeMarkupList);

        assertThat(actual.get(0).getContractPriceProfileSeq(), is(productTypeMarkupDTO.getContractPriceProfileSeq()));
        assertThat(actual.get(0).getCustomerTypeCode(), is(productTypeMarkupDTO.getGfsCustomerTypeCode()));

    }

    @Test
    public void shouldBuildProductMarkupDOListForAmendment() throws ParseException {
        String userId = "test user";
        int contractPriceProfileSeq = 1;
        Date farOutDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);

        List<ProductTypeMarkupDTO> productTypeMarkupList = new ArrayList<>();
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setGfsCustomerId("1");
        productTypeMarkupDTO.setEffectiveDate(new LocalDate(2018, 01, 05).toDate());
        productTypeMarkupDTO.setExpirationDate(new LocalDate(2019, 01, 05).toDate());
        productTypeMarkupDTO.setItemPriceId(2);
        productTypeMarkupDTO.setMarkup("1");
        productTypeMarkupDTO.setMarkupType(2);
        productTypeMarkupDTO.setProductType("1");
        productTypeMarkupDTO.setUnit("2");
        productTypeMarkupDTO.setGfsCustomerTypeCode(12);
        productTypeMarkupList.add(productTypeMarkupDTO);

        List<ProductTypeMarkupDO> actual = target.buildProductMarkupDOListForAmendment(userId, contractPriceProfileSeq, farOutDate,
                productTypeMarkupList);

        assertThat(actual.get(0).getContractPriceProfileSeq(), is(contractPriceProfileSeq));
        assertThat(actual.get(0).getItemPriceId(), is(productTypeMarkupList.get(0).getItemPriceId()));
        assertThat(actual.get(0).getMarkupType(), is(productTypeMarkupList.get(0).getMarkupType()));
        assertThat(actual.get(0).getCustomerTypeCode(), is(12));
        assertThat(actual.get(0).getEffectiveDate(), is(farOutDate));
        assertThat(actual.get(0).getExpirationDate(), is(farOutDate));

    }

    @Test
    public void shouldBuildMarkupDOListForNewAssignedItem() throws ParseException {

        int contractPriceProfileSeq = 1;
        String gfsCustomerId = "1001";
        int gfsCustomerTypeCode = 31;
        String itemId = "681204";

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemId);
        ItemLevelMarkupDTO futureItemDTO = buildFutureItemDTO();

        when(cppDateUtils.getFutureDate()).thenReturn(new LocalDate(9999, 01, 01).toDate());

        List<ProductTypeMarkupDO> result = target.buildMarkupDOListForAssignment(itemAssignmentWrapperDTO, futureItemDTO);

        verify(cppDateUtils).getFutureDate();

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getCustomerTypeCode(), is(31));
        assertThat(result.get(0).getGfsCustomerId(), is("1001"));
        assertThat(result.get(0).getMarkupType(), is(1));
        assertThat(result.get(0).getUnit(), is("%"));
        assertThat(result.get(0).getProductType(), is("0"));
        assertThat(result.get(0).getPriceLockedInTypeCode(), equalTo(PriceLockinType.COST_PLUS.getCode()));
        assertThat(result.get(0).getPriceLockinReasonCode(), equalTo(PriceLockinReason.COMPETITIVE.code));
    }

    @Test
    public void shouldBuildProductTypeMarkupDOForFutureItem() {

        FutureItemDO futureItemDO = new FutureItemDO();
        ItemAssignmentDTO itemAssignmentDTO = buildItemAssignmentDTO("1");
        int gfsCustomerTypeCode = 1;
        String gfsCustomerId = "1";

        when(cppDateUtils.getFutureDate()).thenReturn(new LocalDate(9999, 01, 01).toDate());

        ProductTypeMarkupDO productTypeMarkupDO = target.buildProductTypeMarkupDO(gfsCustomerId, gfsCustomerTypeCode, itemAssignmentDTO,
                futureItemDO);

        assertThat(productTypeMarkupDO.getGfsCustomerId(), is(gfsCustomerId));
        assertThat(productTypeMarkupDO.getCustomerTypeCode(), is(gfsCustomerTypeCode));
        assertThat(String.valueOf(productTypeMarkupDO.getItemPriceId()), is(itemAssignmentDTO.getItemId()));
        assertThat(productTypeMarkupDO.getMarkup(), is(futureItemDO.getMarkup()));
        assertThat(productTypeMarkupDO.getMarkupType(), is(futureItemDO.getMarkupType()));
        assertThat(productTypeMarkupDO.getUnit(), is(futureItemDO.getUnit()));

    }

    private ItemLevelMarkupDTO buildFutureItemDTO() {
        ItemLevelMarkupDTO futureItemDTO = new ItemLevelMarkupDTO();
        futureItemDTO.setCustomerItemDescSeq(1);
        futureItemDTO.setItemDesc("test");
        futureItemDTO.setMarkup("2.00");
        futureItemDTO.setMarkupType(1);
        futureItemDTO.setUnit("%");
        return futureItemDTO;
    }

    private ItemAssignmentWrapperDTO buildItemAssignmentWrapperDTO(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerTypeCode,
            String itemId) {
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = new ItemAssignmentWrapperDTO();
        itemAssignmentWrapperDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        itemAssignmentWrapperDTO.setExceptionName("test");
        itemAssignmentWrapperDTO.setFutureItemDesc("test");
        itemAssignmentWrapperDTO.setGfsCustomerId(gfsCustomerId);
        itemAssignmentWrapperDTO.setGfsCustomerTypeCode(gfsCustomerTypeCode);
        itemAssignmentWrapperDTO.setIsFutureItemSaved(true);
        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        itemAssignmentDTOList.add(buildItemAssignmentDTO(itemId));
        itemAssignmentWrapperDTO.setItemAssignmentList(itemAssignmentDTOList);
        return itemAssignmentWrapperDTO;
    }

    private ItemAssignmentDTO buildItemAssignmentDTO(String itemId) {
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemDescription("test");
        itemAssignmentDTO.setCustomerItemDescSeq(1);
        itemAssignmentDTO.setItemId(itemId);
        itemAssignmentDTO.setIsItemSaved(true);
        itemAssignmentDTO.setItemPriceLevelCode(ItemPriceLevel.ITEM.getCode());
        return itemAssignmentDTO;
    }

}
