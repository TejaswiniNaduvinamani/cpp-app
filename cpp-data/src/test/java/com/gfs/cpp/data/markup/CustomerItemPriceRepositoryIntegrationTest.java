package com.gfs.cpp.data.markup;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.corp.component.price.common.types.PriceLockinReason;
import com.gfs.corp.component.price.common.types.PriceLockinType;
import com.gfs.corp.customer.common.dto.CustomerPK;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.furtherance.FurtheranceAction;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class CustomerItemPriceRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    private static final String USER_NAME = "test user";

    @Autowired
    private CustomerItemPriceRepository target;

    @ExpectedDatabase(value = "CustomerItemPriceRepositoryIntegrationTest.shouldDeleteExistingItem-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.shouldDeleteExistingItem.xml")
    @Test
    public void shouldDeleteExistingItem() throws Exception {

        int contractPriceProfileSeq = -2001;
        String gfsCustomerId = "-10001234";
        String itemId = "-100129";
        int gfsCustomerType = 36;
        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(itemId);

        target.deleteExistingItemOrSubgroupMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemIdList, ItemPriceLevel.ITEM.getCode());

    }

    @ExpectedDatabase(value = "CustomerItemPriceRepositoryIntegrationTest.shouldDeleteExistingItem-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.shouldDeleteExistingSubgroup.xml")
    @Test
    public void shouldDeleteExistingSubgroup() throws Exception {

        int contractPriceProfileSeq = -2001;
        String gfsCustomerId = "-10001234";
        String subgroupId = "-100129";
        int gfsCustomerType = 36;
        List<String> subgroupIdList = new ArrayList<>();
        subgroupIdList.add(subgroupId);

        target.deleteExistingItemOrSubgroupMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, subgroupIdList,
                ItemPriceLevel.SUBGROUP.getCode());

    }

    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.shouldFetchItemTypeMarkups.xml")
    @Test
    public void shouldFetchItemTypeMarkups() throws Exception {
        int contractPriceProfileSeq = -3001;
        String gfsCustomerId = "-10001234";

        List<ItemLevelMarkupDTO> fetchItemTypeMarkups = target.fetchItemTypeMarkups(contractPriceProfileSeq, gfsCustomerId);

        assertThat(fetchItemTypeMarkups.size(), equalTo(1));

        ItemLevelMarkupDTO actual = fetchItemTypeMarkups.get(0);
        assertThat(actual.getItemId(), equalTo("-100129"));
        assertThat(actual.getMarkup(), equalTo("10.00"));
        assertThat(actual.getMarkupType(), equalTo(1));
        assertThat(actual.getUnit(), equalTo("%"));

        Date expectedExpirationDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999");
        assertThat(actual.getExpirationDate(), equalTo(expectedExpirationDate));

        Date expectedEffectiveDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999");
        assertThat(actual.getEffectiveDate(), equalTo(expectedEffectiveDate));
    }

    @ExpectedDatabase(value = "CustomerItemPriceRepositoryIntegrationTest.shouldInsertCustomerItemPrice-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldInsertCustomerItemPrice() throws Exception {

        int contractPriceProfileSeq = -2001;
        ProductTypeMarkupDO productTypeMarkup = new ProductTypeMarkupDO();
        productTypeMarkup.setEffectiveDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        productTypeMarkup.setExpirationDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        productTypeMarkup.setItemPriceId(-100129);
        productTypeMarkup.setMarkup(new BigDecimal(10));
        productTypeMarkup.setMarkupType(1);
        productTypeMarkup.setProductType("0");
        productTypeMarkup.setUnit("%");
        productTypeMarkup.setGfsCustomerId("-10001234");
        productTypeMarkup.setCustomerTypeCode(36);
        productTypeMarkup.setPriceLockedInTypeCode(PriceLockinType.COST_PLUS.getCode());
        productTypeMarkup.setPriceLockinReasonCode(PriceLockinReason.COMPETITIVE.code);
        List<ProductTypeMarkupDO> productList = new ArrayList<>();
        productList.add(productTypeMarkup);

        target.saveMarkup(productList, USER_NAME, contractPriceProfileSeq);
    }

    @ExpectedDatabase(value = "CustomerItemPriceRepositoryIntegrationTest.shouldUpdateCustomerItemPrice-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.shouldUpdateCustomerItemPrice.xml")
    @Test
    public void shouldUpdateCustomerItemPrice() throws Exception {
        int contractPriceProfileSeq = -2001;
        ProductTypeMarkupDO productTypeMarkup = new ProductTypeMarkupDO();
        productTypeMarkup.setEffectiveDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        productTypeMarkup.setExpirationDate(new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE));
        productTypeMarkup.setItemPriceId(-100129);
        productTypeMarkup.setMarkup(new BigDecimal(12.12));
        productTypeMarkup.setMarkupType(2);
        productTypeMarkup.setProductType("2");
        productTypeMarkup.setUnit("$");
        productTypeMarkup.setGfsCustomerId("-10001234");
        productTypeMarkup.setCustomerTypeCode(36);
        List<ProductTypeMarkupDO> productList = new ArrayList<>();
        productList.add(productTypeMarkup);
        productTypeMarkup.setPriceLockedInTypeCode(PriceLockinType.COST_PLUS.getCode());
        productTypeMarkup.setPriceLockinReasonCode(PriceLockinReason.COMPETITIVE.code);
        target.updateMarkup(productList, USER_NAME, contractPriceProfileSeq);
    }

    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchMarkupCount() throws Exception {
        int contractPriceProfileSeq = -3001;

        int markupCount = target.fetchMarkupCount(contractPriceProfileSeq);

        assertThat(markupCount, equalTo(2));
    }

    @ExpectedDatabase(value = "CustomerItemPriceRepositoryIntegrationTest.shouldDeleteExistingItem-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.shouldDeleteExistingItem.xml")
    @Test
    public void shouldDeleteExceptionData() throws Exception {
        int contractPriceProfileSeq = -2001;
        String gfsCustomerId = "-10001234";

        target.deleteExceptionData(contractPriceProfileSeq, gfsCustomerId);
    }

    @ExpectedDatabase(value = "CustomerItemPriceRepositoryIntegrationTest.shouldExpireCustomerItemPriceForContract-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.shouldExpireCustomerItemPriceForContract.xml")
    @Test
    public void shouldExpireCustomerItemPriceForContract() throws Exception {

        target.expireNonCmgPriceForContract(-4001, new LocalDate(2018, 04, 20).toDate(), "updated user");
    }

    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchProductTypeMarkups() throws Exception {
        int contractPriceProfileSeq = -3001;

        List<ProductTypeMarkupDTO> productTypeMarkupList = target.fetchProductTypeMarkups(contractPriceProfileSeq);

        assertThat(productTypeMarkupList.size(), equalTo(1));

        ProductTypeMarkupDTO actual = productTypeMarkupList.get(0);
        assertThat(actual.getItemPriceId(), equalTo(-100129));
        assertThat(actual.getMarkup(), equalTo("10.00"));
        assertThat(actual.getMarkupType(), equalTo(1));
        assertThat(actual.getUnit(), equalTo("%"));
        assertThat(actual.getGfsCustomerId(), equalTo("-10001234"));

        Date expectedExpirationDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999");
        assertThat(actual.getExpirationDate(), equalTo(expectedExpirationDate));

        Date expectedEffectiveDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999");
        assertThat(actual.getEffectiveDate(), equalTo(expectedEffectiveDate));
    }

    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchSellPriceIndCount() throws Exception {
        int contractPriceProfileSeq = -3001;

        int actualCount = target.fetchSellPriceIndCount(contractPriceProfileSeq);

        assertThat(actualCount, equalTo(2));
    }

    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.shouldFetchSellPriceIndCountZero.xml")
    @Test
    public void shouldFetchSellPriceIndCountZero() throws Exception {
        int contractPriceProfileSeq = -3001;

        int actualCount = target.fetchSellPriceIndCount(contractPriceProfileSeq);

        assertThat(actualCount, equalTo(0));
    }

    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchAllMarkups() throws Exception {
        int contractPriceProfileSeq = -3001;
        String gfsCustomerId = "-10001234";

        List<ProductTypeMarkupDTO> productTypeMarkupList = target.fetchAllMarkup(contractPriceProfileSeq, gfsCustomerId);

        assertThat(productTypeMarkupList.size(), equalTo(2));

        ProductTypeMarkupDTO actual = productTypeMarkupList.get(0);
        assertThat(actual.getItemPriceId(), equalTo(-100129));
        assertThat(actual.getMarkup(), equalTo("10.00"));
        assertThat(actual.getMarkupType(), equalTo(1));
        assertThat(actual.getUnit(), equalTo("%"));
        assertThat(actual.getGfsCustomerId(), equalTo("-10001234"));

        Date expectedExpirationDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999");
        assertThat(actual.getExpirationDate(), equalTo(expectedExpirationDate));

        Date expectedEffectiveDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999");
        assertThat(actual.getEffectiveDate(), equalTo(expectedEffectiveDate));
    }

    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.ShouldFetchPricingForRealCustomer.xml")
    @Test
    public void ShouldFetchPricingForRealCustomer() throws Exception {

        List<ProductTypeMarkupDTO> productTypeMarkupList = target.fetchPricingForRealCustomer(-3001);
        assertThat(productTypeMarkupList.size(), equalTo(6));
    }

    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchItemCount() throws Exception {
        String itemId = "-100129";
        String gfsCustomerId = "-10001234";
        int gfsCustomerTypeCode = 36;
        int contractPriceProfileSeq = -3001;
        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(itemId);

        List<String> existingItemList = target.fetchAlreadyExistingItemsOrSubgroups(itemIdList, gfsCustomerId, gfsCustomerTypeCode,
                contractPriceProfileSeq, ItemPriceLevel.ITEM.getCode());

        assertThat(existingItemList.size(), equalTo(1));
        assertThat(existingItemList.get(0), equalTo(itemId));
    }

    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.shouldNotFetchExistingBidLockinEntriesForCustomer.xml")
    @Test
    public void shouldNotFetchExistingBidLockinEntriesForCustomer() throws Exception {

        List<String> itemPriceIdList = new ArrayList<String>();
        String itemPriceId_1 = "-100129";
        String itemPriceId_2 = "-100130";

        itemPriceIdList.add(itemPriceId_1);
        itemPriceIdList.add(itemPriceId_2);

        String customerId = "-10001234";
        int customerTypeCode = 36;

        Date effectiveDate = new LocalDate(2018, 12, 01).toDate();
        Date expirationDate = new LocalDate(2019, 02, 01).toDate();

        List<CustomerPK> customerList = new ArrayList<CustomerPK>();

        CustomerPK customerPk = new CustomerPK();
        customerPk.setId(customerId);
        customerPk.setTypeCode(customerTypeCode);

        CustomerPK customerPk1 = new CustomerPK();
        customerPk1.setId("-10001235");
        customerPk1.setTypeCode(39);

        CustomerPK customerPk2 = new CustomerPK();
        customerPk2.setId("-10001236");
        customerPk2.setTypeCode(38);

        customerList.add(customerPk);
        customerList.add(customerPk1);
        customerList.add(customerPk2);

        List<ProductTypeMarkupDTO> prodList = target.fetchExistingBidLockinEntriesForCustomer(customerList, itemPriceIdList, effectiveDate,
                expirationDate);
        assertThat(prodList.size(), equalTo(0));
    }

    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.shouldFetchExistingBidLockinEntriesForCustomer.xml")
    @Test
    public void shouldFetchExistingBidLockinEntriesForCustomer() throws Exception {

        List<String> itemPriceIdList = new ArrayList<String>();
        String itemPriceId_1 = "-100129";
        String itemPriceId_2 = "-100130";

        itemPriceIdList.add(itemPriceId_1);
        itemPriceIdList.add(itemPriceId_2);

        String customerId = "-10001234";
        int customerTypeCode = 36;

        Date effectiveDate = new LocalDate(2018, 12, 01).toDate();
        Date expirationDate = new LocalDate(2019, 02, 01).toDate();

        List<CustomerPK> customerList = new ArrayList<CustomerPK>();

        CustomerPK customerPk = new CustomerPK();
        customerPk.setId(customerId);
        customerPk.setTypeCode(customerTypeCode);

        CustomerPK customerPk1 = new CustomerPK();
        customerPk1.setId("-10001235");
        customerPk1.setTypeCode(39);

        CustomerPK customerPk2 = new CustomerPK();
        customerPk2.setId("-10001236");
        customerPk2.setTypeCode(38);

        customerList.add(customerPk);
        customerList.add(customerPk1);
        customerList.add(customerPk2);

        List<ProductTypeMarkupDTO> prodList = target.fetchExistingBidLockinEntriesForCustomer(customerList, itemPriceIdList, effectiveDate,
                expirationDate);
        assertThat(prodList.size(), equalTo(3));

        for (ProductTypeMarkupDTO makupDTO : prodList) {
            assertThat(makupDTO.getPriceLockinReasonCode(), equalTo(4));
            assertThat(makupDTO.getItemPriceId(), CoreMatchers.anyOf(CoreMatchers.is(-100129), CoreMatchers.is(-100130)));
            assertThat(makupDTO.getGfsCustomerId(),
                    CoreMatchers.anyOf(CoreMatchers.is("-10001235"), CoreMatchers.is("-10001236"), CoreMatchers.is("-10001234")));
            assertThat(makupDTO.getGfsCustomerTypeCode(), CoreMatchers.anyOf(CoreMatchers.is(36), CoreMatchers.is(38), CoreMatchers.is(39)));
        }
    }

    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.shouldFetchLatestContractVersionMarkups.xml")
    @Test
    public void shouldFetchLatestContractVersionMarkups() throws Exception {
        int contractPriceProfileSeq = -3001;

        List<ProductTypeMarkupDTO> productTypeMarkupList = target.fetchMarkupsForCMGs(contractPriceProfileSeq);
        assertThat(productTypeMarkupList.size(), equalTo(5));
        assertThat(productTypeMarkupList.get(0).getGfsCustomerId(),
                CoreMatchers.anyOf(CoreMatchers.is("-1000"), CoreMatchers.is("-1001"), CoreMatchers.is("-1002")));

    }

    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchAllGfsCustomerIds() throws Exception {
        String gfsCustomerId = "-10001234";
        int contractPriceProfileSeq = -3001;

        List<String> gfsCustomerIdsList = target.fetchAllGfsCustomerIdsInCustomerItemPrice(contractPriceProfileSeq);
        assertThat(gfsCustomerIdsList.size(), equalTo(1));
        assertThat(gfsCustomerIdsList.get(0), equalTo(gfsCustomerId));

    }

    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.shouldFetchSubgroup.xml")
    @Test
    public void shouldFetchSubgroup() throws Exception {
        String subgroupId = "-11";
        String gfsCustomerId = "-10001234";
        int gfsCustomerTypeCode = 36;
        int contractPriceProfileSeq = -3001;
        List<String> subgroupIdList = new ArrayList<>();
        subgroupIdList.add(subgroupId);

        List<String> existingSubgroup = target.fetchAlreadyExistingItemsOrSubgroups(subgroupIdList, gfsCustomerId, gfsCustomerTypeCode,
                contractPriceProfileSeq, ItemPriceLevel.SUBGROUP.getCode());

        assertThat(existingSubgroup.get(0), equalTo(subgroupId));
    }

    @ExpectedDatabase(value = "CustomerItemPriceRepositoryIntegrationTest.shouldExpireItemInCustomerItemPrice-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.shouldExpireItemInCustomerItemPrice.xml")
    @Test
    public void shouldExpireItemPricing() throws Exception {
        String cmgCustomerId = "-10001234";
        int cmgCustomerTypeCode = -36;
        String itemPriceId = "-100129";
        int contractPriceProfileSeq = -4001;
        int itemPriceLevelCode = -7;
        String userName = "expire user";

        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);

        Date expirationDate = new LocalDate(2018, 12, 01).toDate();

        target.expireItemPricing(contractPriceProfileSeq, cmgCustomerId, cmgCustomerTypeCode, expireItemList, itemPriceLevelCode, expirationDate,
                userName);

    }

    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.shouldFetchMarkupsForRealCustomersForFurtherance.xml")
    @Test
    public void shouldFetchMarkupsForRealCustomersForFurtherance() throws Exception {
        int cppFurtheranceSeq = -1;

        Map<Integer, List<ProductTypeMarkupDTO>> fetchMarkups = target.fetchMarkupsForRealCustomersForFurtherance(cppFurtheranceSeq);
        assertThat(fetchMarkups.size(), equalTo(1));

        List<ProductTypeMarkupDTO> addedItems = fetchMarkups.get(FurtheranceAction.ADDED.getCode());
        List<ProductTypeMarkupDTO> deleteddItems = fetchMarkups.get(FurtheranceAction.DELETED.getCode());
        List<ProductTypeMarkupDTO> updatedItems = fetchMarkups.get(FurtheranceAction.UPDATED.getCode());

        assertThat(addedItems, equalTo(null));
        assertThat(updatedItems.size(), equalTo(2));
        assertThat(deleteddItems, equalTo(null));

        for (ProductTypeMarkupDTO productTypeMarkupDTO : updatedItems) {
            assertThat(productTypeMarkupDTO.getGfsCustomerId(), CoreMatchers.anyOf(CoreMatchers.is("-1212"), CoreMatchers.is("-1213")));
            assertThat(productTypeMarkupDTO.getGfsCustomerTypeCode(), equalTo(36));
            assertThat(productTypeMarkupDTO.getItemPriceId(), equalTo(1));
            assertThat(productTypeMarkupDTO.getProductType(), equalTo("2"));
        }

    }

    @ExpectedDatabase(value = "CustomerItemPriceRepositoryIntegrationTest.shouldExpireItemPricingForDeletedItemsDuringFurtherance-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CustomerItemPriceRepositoryIntegrationTest.shouldExpireItemPricingForDeletedItemsDuringFurtherance.xml")
    @Test
    public void shouldExpireItemPricingForDeletedItemsDuringFurtherance() throws Exception {
        String cmgCustomerId = "-10001234";
        int cmgCustomerTypeCode = -36;
        String itemPriceId = "-100130";
        int contractPriceProfileSeq = -4001;
        int itemPriceLevelCode = -7;
        String userName = "expire user";

        Date expirationDate = new LocalDate(2018, 12, 01).toDate();

        List<ProductTypeMarkupDO> productTypeMarkupDOList = new ArrayList<>();

        ProductTypeMarkupDO productTypeMarkupDO = new ProductTypeMarkupDO();

        productTypeMarkupDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        productTypeMarkupDO.setCustomerTypeCode(cmgCustomerTypeCode);
        productTypeMarkupDO.setGfsCustomerId(cmgCustomerId);
        productTypeMarkupDO.setItemPriceId(Integer.parseInt(itemPriceId));
        productTypeMarkupDO.setProductType(String.valueOf(itemPriceLevelCode));

        productTypeMarkupDOList.add(productTypeMarkupDO);

        target.expireItemPricingForDeletedItemsDuringFurtherance(expirationDate, userName, productTypeMarkupDOList);

    }

}
