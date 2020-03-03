package com.gfs.cpp.data.markup;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.model.markup.FutureItemDO;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class CustomerItemDescPriceRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    private static final String USER_NAME = "test user";
    @Autowired
    private CustomerItemDescPriceRepository target;

    @ExpectedDatabase(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldDeleteFutureItem-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldDeleteFutureItem.xml")
    @Test
    public void shouldDeleteFutureItem() throws Exception {

        int contractPriceProfileSeq = -2001;
        String gfsCustomerId = "-10001234";
        String itemDesc = "chicken";
        target.deleteFutureItem(contractPriceProfileSeq, gfsCustomerId, itemDesc);
    }

    @ExpectedDatabase(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldDeleteFutureItem-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldDeleteAllFutureItem.xml")
    @Test
    public void shouldDeleteAllFutureItem() throws Exception {

        int contractPriceProfileSeq = -2001;
        String gfsCustomerId = "-10001234";

        target.deleteAllFutureItem(contractPriceProfileSeq, gfsCustomerId);
    }

    @DatabaseSetup(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldFetchFutureItemTypeMarkups.xml")
    @Test
    public void shouldFetchFutureItemTypeMarkups() throws Exception {
        int contractPriceProfileSeq = -4001;
        String gfsCustomerId = "-10001234";
        int gfsCustomerTypeCode = 36;

        List<ItemLevelMarkupDTO> fetchFutureItemTypeMarkups = target.fetchFutureItemTypeMarkups(contractPriceProfileSeq, gfsCustomerId,
                gfsCustomerTypeCode);
        ItemLevelMarkupDTO actualItemLevelMarkup = fetchFutureItemTypeMarkups.get(0);

        assertThat(actualItemLevelMarkup.getItemDesc(), equalTo("chicken"));
        assertThat(actualItemLevelMarkup.getMarkup(), equalTo("20.00"));
        assertThat(actualItemLevelMarkup.getMarkupType(), equalTo(1));
        assertThat(actualItemLevelMarkup.getUnit(), equalTo("$"));
    }

    @ExpectedDatabase(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldInsertFutureItemTypeMarkups-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldInsertFutureItemTypeMarkups() throws Exception {

        int contractPriceProfileSeq = -4001;
        List<FutureItemDO> futureItemList = new ArrayList<>();
        FutureItemDO futureItemDO = new FutureItemDO();
        futureItemDO.setContractPriceProfileSeq(124);
        futureItemDO.setCustomerTypeCode(36);
        futureItemDO.setGfsCustomerId("-10001234");
        futureItemDO.setItemDesc("chicken");
        futureItemDO.setMarkup(new BigDecimal("20"));
        futureItemDO.setUnit("$");
        futureItemDO.setMarkupType(1);
        futureItemDO.setEffectiveDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        futureItemDO.setExpirationDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        futureItemList.add(futureItemDO);

        target.saveFutureItems(futureItemList, USER_NAME, contractPriceProfileSeq);
    }

    @ExpectedDatabase(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldUpdateFutureItemTypeMarkups-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldUpdateFutureItemTypeMarkups.xml")
    @Test
    public void shouldUpdateFutureItemTypeMarkups() throws Exception {
        int contractPriceProfileSeq = -4001;
        List<FutureItemDO> futureItemList = new ArrayList<>();
        FutureItemDO futureItemDO = new FutureItemDO();
        futureItemDO.setContractPriceProfileSeq(124);
        futureItemDO.setCustomerTypeCode(36);
        futureItemDO.setGfsCustomerId("-10001234");
        futureItemDO.setItemDesc("chicken");
        futureItemDO.setMarkup(new BigDecimal("15"));
        futureItemDO.setUnit("%");
        futureItemDO.setMarkupType(1);
        futureItemDO.setEffectiveDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        futureItemDO.setExpirationDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        futureItemList.add(futureItemDO);

        target.updateFutureItems(futureItemList, USER_NAME, contractPriceProfileSeq);
    }

    @DatabaseSetup(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldFetchAllSavedFutureItems.xml")
    @Test
    public void shouldFetchAllFutureItems() throws Exception {
        int contractPriceProfileSeq = -4001;

        List<FutureItemDescriptionDTO> fetchFutureItemDescList = target.fetchAllFutureItems(contractPriceProfileSeq);
        FutureItemDescriptionDTO actualFutureItemDescDTO = fetchFutureItemDescList.get(0);

        assertThat(actualFutureItemDescDTO.getGfsCustomerId(), equalTo("-10001234"));
        assertThat(actualFutureItemDescDTO.getGfsCustomerTypeCode(), equalTo(36));
        assertThat(actualFutureItemDescDTO.getFutureItemDesc(), equalTo("chicken"));
    }

    @DatabaseSetup(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldFetchAllSavedFutureItems.xml")
    @Test
    public void shouldFetchFutureItemForAssignment() throws Exception {
        int contractPriceProfileSeq = -4001;
        String gfsCustomerId = "-10001234";
        String itemDesc = "chicken";
        int gfsCustomerTypeCode = 36;

        ItemLevelMarkupDTO itemLevelMarkupDTO = target.fetchFutureItemForAssignment(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemDesc);

        assertThat(itemLevelMarkupDTO.getCustomerItemDescSeq(), equalTo(-301));
        assertThat(itemLevelMarkupDTO.getMarkupType(), equalTo(1));
        assertThat(itemLevelMarkupDTO.getUnit(), equalTo("$"));
    }

    @DatabaseSetup(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldFetchAllSavedFutureItems.xml")
    @Test
    public void shouldFetchFutureItemForAssignmentEmptyResultException() throws Exception {
        int contractPriceProfileSeq = -4001;
        String gfsCustomerId = "-10001234";
        String itemDesc = "";
        int gfsCustomerTypeCode = 36;

        ItemLevelMarkupDTO itemLevelMarkupDTO = target.fetchFutureItemForAssignment(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemDesc);

        assertThat(itemLevelMarkupDTO, equalTo(null));
    }

    @DatabaseSetup(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldFetchMarkupAmountTypeCodeCount.xml")
    @Test
    public void shouldFetchMarkupAmountTypeCodeCount() throws Exception {
        int contractPriceProfileSeq = -4001;

        int actualCount = target.fetchMarkupAmountTypeCodeCount(contractPriceProfileSeq);

        assertThat(actualCount, equalTo(1));
    }

    @DatabaseSetup(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldFetchMarkupAmountTypeCodeCountZero.xml")
    @Test
    public void shouldFetchMarkupAmountTypeCodeCountZero() throws Exception {
        int contractPriceProfileSeq = -4001;

        int actualCount = target.fetchMarkupAmountTypeCodeCount(contractPriceProfileSeq);

        assertThat(actualCount, equalTo(0));
    }

    @Test
    public void shouldFetchCPPCustomerItemDescPriceNextSequence() throws Exception {
        int cppCustomerItemDesPriceSeq = target.fetchCPPCustomerItemDescPriceNextSequence();
        assertThat(cppCustomerItemDesPriceSeq, equalTo(1001));

    }

    @ExpectedDatabase(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldSaveFutureItemForCPPSeq.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldSaveFutureItemForCPPSeq() throws Exception {
        String gfsCustomerId = "-10001234";
        int gfsCustomerTypeCode = 36;
        String futureItemDesc = "chicken";
        int costMarkupAmt = 20;
        String markupAmountTypeCode = "$";
        int markupUnitTypeCode = 1;

        FutureItemDescriptionDTO futureItemDTO = new FutureItemDescriptionDTO();
        futureItemDTO.setGfsCustomerId(gfsCustomerId);
        futureItemDTO.setGfsCustomerTypeCode(gfsCustomerTypeCode);
        futureItemDTO.setFutureItemDesc(futureItemDesc);
        futureItemDTO.setCostMarkupAmt(costMarkupAmt);
        futureItemDTO.setMarkupAmountTypeCode(markupAmountTypeCode);
        futureItemDTO.setMarkupUnitTypeCode(markupUnitTypeCode);

        int contractPriceProfileSeq = -4001;
        int cppCustomerItemDescPriceSeq = -301;

        target.saveFutureItemForCPPSeq(futureItemDTO, USER_NAME, contractPriceProfileSeq, cppCustomerItemDescPriceSeq);
    }

    @DatabaseSetup(value = "CustomerItemDescPriceRepositoryIntegrationTest.shouldFetchFutureItemTypeMarkups.xml")
    @Test
    public void shouldFetchFutureItemForFurtherance() throws Exception {
        int contractPriceProfileSeq = -4001;
        String gfsCustomerId = "-10001234";
        int gfsCustomerTypeCode = 36;
        String itemDesc = "chicken";

        FutureItemDescriptionDTO futureItemDescriptionDTO = target.fetchFutureItemForFurtherance(contractPriceProfileSeq, gfsCustomerId,
                gfsCustomerTypeCode, itemDesc);

        assertThat(futureItemDescriptionDTO.getFutureItemDesc(), equalTo("chicken"));
        assertThat(futureItemDescriptionDTO.getCustomerItemDescSeq(), equalTo(-301));
        assertThat(futureItemDescriptionDTO.getCostMarkupAmt(), equalTo(20));
        assertThat(futureItemDescriptionDTO.getMarkupAmountTypeCode(), equalTo("$"));
    }
}
