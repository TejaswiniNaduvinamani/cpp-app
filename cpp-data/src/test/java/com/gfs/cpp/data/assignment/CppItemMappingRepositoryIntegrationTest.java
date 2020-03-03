package com.gfs.cpp.data.assignment;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.model.assignments.ItemAssignmentDO;
import com.gfs.cpp.data.assignment.CppItemMappingRepository;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class CppItemMappingRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    private static final String USER_NAME = "test user";

    @Autowired
    private CppItemMappingRepository target;

    @ExpectedDatabase(value = "CppItemMappingRepositoryIntegrationTest.shouldSaveItems.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldAssignItems() throws Exception {

        List<ItemAssignmentDO> itemAssignmentDoList = new ArrayList<>();

        ItemAssignmentDO itemAssignmentDo1 = new ItemAssignmentDO();
        itemAssignmentDo1.setCustomerItemDescSeq(-101);
        itemAssignmentDo1.setItemPriceId("-1001");
        itemAssignmentDo1.setItemPriceLevelCode(36);

        ItemAssignmentDO itemAssignmentDo2 = new ItemAssignmentDO();
        itemAssignmentDo2.setCustomerItemDescSeq(-102);
        itemAssignmentDo2.setItemPriceId("-1002");
        itemAssignmentDo2.setItemPriceLevelCode(36);

        itemAssignmentDoList.add(itemAssignmentDo1);
        itemAssignmentDoList.add(itemAssignmentDo2);

        target.saveItems(itemAssignmentDoList, USER_NAME);
    }

    @ExpectedDatabase(value = "CppItemMappingRepositoryIntegrationTest.shouldSaveCPPItemMapping.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldSaveCPPItemMapping() throws Exception {

        Date effectiveDateForExistingRecords = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2018");
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/9999");

        List<ItemAssignmentDO> itemAssignmentDoList = new ArrayList<>();

        ItemAssignmentDO itemAssignmentDo1 = new ItemAssignmentDO();
        itemAssignmentDo1.setCustomerItemDescSeq(-101);
        itemAssignmentDo1.setItemPriceId("-1001");
        itemAssignmentDo1.setItemPriceLevelCode(36);
        itemAssignmentDo1.setEffectiveDate(effectiveDateForExistingRecords);

        ItemAssignmentDO itemAssignmentDo2 = new ItemAssignmentDO();
        itemAssignmentDo2.setCustomerItemDescSeq(-102);
        itemAssignmentDo2.setItemPriceId("-1002");
        itemAssignmentDo2.setItemPriceLevelCode(36);
        itemAssignmentDo2.setEffectiveDate(effectiveDateForExistingRecords);

        itemAssignmentDoList.add(itemAssignmentDo1);
        itemAssignmentDoList.add(itemAssignmentDo2);

        target.saveCPPItemMapping(itemAssignmentDoList, USER_NAME, expirationDate);
    }

    @DatabaseSetup(value = "CppItemMappingRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchAssignedItems() throws Exception {

        List<Integer> customerItemDescSeqList = new ArrayList<>();
        customerItemDescSeqList.add(-101);

        List<ItemAssignmentDTO> itemAssignmentDTOList = target.fetchAssignedItemsForAConcept(customerItemDescSeqList);

        assertThat(itemAssignmentDTOList.size(), equalTo(2));
        assertThat(itemAssignmentDTOList.get(0).getItemId(), equalTo("-1001"));
    }

    @ExpectedDatabase(value = "CppItemMappingRepositoryIntegrationTest.shouldUpdateItems-results.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CppItemMappingRepositoryIntegrationTest.shouldUpdateItems.xml")
    @Test
    public void shouldUpdateItems() throws Exception {
        List<ItemAssignmentDO> updateItemAssignmentDOList = new ArrayList<>();
        ItemAssignmentDO itemAssignmentDO = new ItemAssignmentDO();
        itemAssignmentDO.setCustomerItemDescSeq(-101);
        itemAssignmentDO.setItemPriceId("-1001");
        itemAssignmentDO.setItemPriceLevelCode(36);
        updateItemAssignmentDOList.add(itemAssignmentDO);

        target.expireOrUpdateItems(updateItemAssignmentDOList, new SimpleDateFormat("MM/dd/yyyy").parse("04/04/9999"), "test user");
    }

    @ExpectedDatabase(value = "CppItemMappingRepositoryIntegrationTest.shouldExpireItems-results.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CppItemMappingRepositoryIntegrationTest.xml")
    @Test
    public void shouldExpireItems() throws Exception {
        List<ItemAssignmentDO> updateItemAssignmentDOList = new ArrayList<>();
        ItemAssignmentDO itemAssignmentDO = new ItemAssignmentDO();
        itemAssignmentDO.setCustomerItemDescSeq(-101);
        itemAssignmentDO.setItemPriceId("-1001");
        itemAssignmentDO.setItemPriceLevelCode(36);
        updateItemAssignmentDOList.add(itemAssignmentDO);

        target.expireOrUpdateItems(updateItemAssignmentDOList, new LocalDate(2018, 01, 01).toDate(), "test user");
    }

    @DatabaseSetup(value = "CppItemMappingRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchAllAssignedItemsInConcept() throws Exception {

        List<Integer> customerItemDescSeqList = new ArrayList<>();
        customerItemDescSeqList.add(-101);
        customerItemDescSeqList.add(-102);

        List<ItemAssignmentDTO> assignedItemIdList = target.fetchAssignedItemsForAConcept(customerItemDescSeqList);

        assertThat(assignedItemIdList.size(), equalTo(3));
        assertThat(assignedItemIdList.get(0).getItemId(), equalTo("-1001"));
        assertThat(assignedItemIdList.get(1).getItemId(), equalTo("-1002"));
        assertThat(assignedItemIdList.get(2).getItemId(), equalTo("-1001"));
    }

}
