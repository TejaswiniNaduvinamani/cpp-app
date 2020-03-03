package com.gfs.cpp.common.model.assignments;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.assignments.ItemAssignmentDO;

@RunWith(MockitoJUnitRunner.class)
public class ItemAssignmentDOTest {

    @InjectMocks
    private ItemAssignmentDO itemAssignmentDO;

    @Test
    public void testItemAssignmentDO() {
        itemAssignmentDO.setCustomerItemDescSeq(1);
        itemAssignmentDO.setItemPriceLevelCode(1);
        itemAssignmentDO.setItemPriceId("1");
        itemAssignmentDO.setEffectiveDate(new Date());
        itemAssignmentDO.setExpirationDate(new Date());

        final ItemAssignmentDO actual = SerializationUtils.clone(itemAssignmentDO);

        assertThat(itemAssignmentDO.equals(actual), is(true));
        assertThat(itemAssignmentDO.hashCode(), is(actual.hashCode()));
        assertThat(itemAssignmentDO.toString() != null, is(true));
        assertThat(itemAssignmentDO.getCustomerItemDescSeq(), is(actual.getCustomerItemDescSeq()));
        assertThat(itemAssignmentDO.getItemPriceLevelCode(), is(actual.getItemPriceLevelCode()));
        assertThat(itemAssignmentDO.getItemPriceId(), is(actual.getItemPriceId()));
        assertThat(itemAssignmentDO.getEffectiveDate(), is(actual.getEffectiveDate()));
        assertThat(itemAssignmentDO.getExpirationDate(), is(actual.getExpirationDate()));
    }
}
