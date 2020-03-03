package com.gfs.cpp.common.model.splitcase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.splitcase.SplitCaseDO;

@RunWith(MockitoJUnitRunner.class)
public class SplitCaseDOTest {

    @InjectMocks
    private SplitCaseDO splitCaseDO;

    @Test
    public void testSplitCaseDO() {
        splitCaseDO.setContractPriceProfileSeq(0);
        splitCaseDO.setEffectiveDate(new Date());
        splitCaseDO.setExpirationDate(new Date());
        splitCaseDO.setItemPriceId("2");
        splitCaseDO.setProductType("Test");
        splitCaseDO.setSplitCaseFee(0);
        splitCaseDO.setUnit("%");
        splitCaseDO.setLessCaseRuleId(1);
        splitCaseDO.setGfsCustomerTypeCode(22);

        final SplitCaseDO actual = SerializationUtils.clone(splitCaseDO);

        assertThat(splitCaseDO.equals(actual), is(true));
        assertThat(splitCaseDO.hashCode(), is(actual.hashCode()));
        assertThat(splitCaseDO.toString() != null, is(true));
        assertThat(actual.getContractPriceProfileSeq(), is(splitCaseDO.getContractPriceProfileSeq()));
        assertThat(actual.getEffectiveDate(), is(splitCaseDO.getEffectiveDate()));
        assertThat(actual.getExpirationDate(), is(splitCaseDO.getExpirationDate()));
        assertThat(actual.getItemPriceId(), is(splitCaseDO.getItemPriceId()));
        assertThat(actual.getProductType(), is(splitCaseDO.getProductType()));
        assertThat(actual.getSplitCaseFee(), is(splitCaseDO.getSplitCaseFee()));
        assertThat(actual.getUnit(), is(splitCaseDO.getUnit()));
        assertThat(actual.getLessCaseRuleId(), is(splitCaseDO.getLessCaseRuleId()));
        assertThat(actual.getGfsCustomerTypeCode(), is(splitCaseDO.getGfsCustomerTypeCode()));
    }
}
