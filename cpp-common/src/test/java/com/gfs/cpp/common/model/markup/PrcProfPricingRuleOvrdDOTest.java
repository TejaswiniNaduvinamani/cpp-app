package com.gfs.cpp.common.model.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.markup.PrcProfPricingRuleOvrdDO;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfPricingRuleOvrdDOTest {
	
	@InjectMocks
	private PrcProfPricingRuleOvrdDO prcProfPricingRuleOvrdDO;
	
	@Test
    public void testPrcProfPricingRuleOvrdDO() {
        prcProfPricingRuleOvrdDO.setContractPriceProfileSeq(1);
        prcProfPricingRuleOvrdDO.setCreateUserId("test user");
        prcProfPricingRuleOvrdDO.setEffectiveDate(null);
        prcProfPricingRuleOvrdDO.setExpirationDate(null);
        prcProfPricingRuleOvrdDO.setGfsCustomerId("1");
        prcProfPricingRuleOvrdDO.setGfsCustomerTypeCode(1);
        prcProfPricingRuleOvrdDO.setLastUpdateUserId("test user");
        prcProfPricingRuleOvrdDO.setPricingOverrideId(1);
        prcProfPricingRuleOvrdDO.setPricingOverrideInd(2);
        
        final PrcProfPricingRuleOvrdDO actual = SerializationUtils.clone(prcProfPricingRuleOvrdDO);

        assertThat(prcProfPricingRuleOvrdDO.equals(actual), is(true));
        assertThat(prcProfPricingRuleOvrdDO.hashCode(), is(actual.hashCode()));
        assertThat(prcProfPricingRuleOvrdDO.toString() != null, is(true));
        
        assertThat(prcProfPricingRuleOvrdDO.getContractPriceProfileSeq(), is(actual.getContractPriceProfileSeq()));
        assertThat(prcProfPricingRuleOvrdDO.getCreateUserId(), is(actual.getCreateUserId()));
        assertThat(prcProfPricingRuleOvrdDO.getEffectiveDate(), is(actual.getEffectiveDate()));
        assertThat(prcProfPricingRuleOvrdDO.getExpirationDate(), is(actual.getExpirationDate()));
        assertThat(prcProfPricingRuleOvrdDO.getGfsCustomerTypeCode(), is(actual.getGfsCustomerTypeCode()));
        assertThat(prcProfPricingRuleOvrdDO.getPricingOverrideId(), is(actual.getPricingOverrideId()));
        assertThat(prcProfPricingRuleOvrdDO.getPricingOverrideInd(), is(actual.getPricingOverrideInd()));
        assertThat(prcProfPricingRuleOvrdDO.getGfsCustomerId(), is(actual.getGfsCustomerId()));
        assertThat(prcProfPricingRuleOvrdDO.getLastUpdateUserId(), is(actual.getLastUpdateUserId()));
	}

}
