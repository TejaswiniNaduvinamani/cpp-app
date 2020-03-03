package com.gfs.cpp.common.model.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgDO;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfCostSchedulePkgDOTest {

    @InjectMocks
    private PrcProfCostSchedulePkgDO prcProfCostSchedulePkgDO;

    @Test
    public void testPrcProfCostSchedulePkgDO() {
        prcProfCostSchedulePkgDO.setContractPriceProfileSeq(1);
        prcProfCostSchedulePkgDO.setGfsCustomerTypeCode(1);
        prcProfCostSchedulePkgDO.setPrcProfCostSchedulePkgSeq(2);
        prcProfCostSchedulePkgDO.setCreateUserId("test user");
        prcProfCostSchedulePkgDO.setEffectiveDate(null);
        prcProfCostSchedulePkgDO.setExpirationDate(null);
        prcProfCostSchedulePkgDO.setGfsCustomerId("1");
        prcProfCostSchedulePkgDO.setLastUpdateUserId("test user");

        final PrcProfCostSchedulePkgDO actual = SerializationUtils.clone(prcProfCostSchedulePkgDO);

        assertThat(prcProfCostSchedulePkgDO.equals(actual), is(true));
        assertThat(prcProfCostSchedulePkgDO.hashCode(), is(actual.hashCode()));
        assertThat(prcProfCostSchedulePkgDO.toString() != null, is(true));

        assertThat(prcProfCostSchedulePkgDO.getContractPriceProfileSeq(), is(actual.getContractPriceProfileSeq()));
        assertThat(prcProfCostSchedulePkgDO.getCreateUserId(), is(actual.getCreateUserId()));
        assertThat(prcProfCostSchedulePkgDO.getEffectiveDate(), is(actual.getEffectiveDate()));
        assertThat(prcProfCostSchedulePkgDO.getExpirationDate(), is(actual.getExpirationDate()));
        assertThat(prcProfCostSchedulePkgDO.getGfsCustomerTypeCode(), is(actual.getGfsCustomerTypeCode()));
        assertThat(prcProfCostSchedulePkgDO.getPrcProfCostSchedulePkgSeq(), is(actual.getPrcProfCostSchedulePkgSeq()));
        assertThat(prcProfCostSchedulePkgDO.getGfsCustomerId(), is(actual.getGfsCustomerId()));
        assertThat(prcProfCostSchedulePkgDO.getLastUpdateUserId(), is(actual.getLastUpdateUserId()));
    }

}
