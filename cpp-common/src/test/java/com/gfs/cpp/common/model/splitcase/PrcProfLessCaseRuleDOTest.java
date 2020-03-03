package com.gfs.cpp.common.model.splitcase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.splitcase.PrcProfLessCaseRuleDO;


@RunWith(MockitoJUnitRunner.class)
public class PrcProfLessCaseRuleDOTest {
	
	 @InjectMocks
	    private PrcProfLessCaseRuleDO prcProfLessCaseRuleDO;
	 
	 
	 @Test
	    public void testPrcProfLessCaseRuleDO() {
		 	prcProfLessCaseRuleDO.setContractPriceProfileSeq(1);
	    	prcProfLessCaseRuleDO.setGfsCustomerId("1");
	    	prcProfLessCaseRuleDO.setCreateUserId("test user");
	    	prcProfLessCaseRuleDO.setEffectiveDate(new LocalDate(2018, 05, 05).toDate());
	    	prcProfLessCaseRuleDO.setExpirationDate(new LocalDate(2019, 05, 05).toDate());
	    	prcProfLessCaseRuleDO.setLastUpdateUserId("test user");
	    	prcProfLessCaseRuleDO.setCwMarkupAmnt(1);
	    	prcProfLessCaseRuleDO.setCwMarkupAmountTypeCode("12");
	    	prcProfLessCaseRuleDO.setGfsCustomerTypeCode(1);
	    	prcProfLessCaseRuleDO.setItemPriceId("1");
	    	prcProfLessCaseRuleDO.setItemPriceLevelCode(1);
	    	prcProfLessCaseRuleDO.setLesscaseRuleId(1);
	    	prcProfLessCaseRuleDO.setMarkupAppliedBeforeDivInd(1);
	    	prcProfLessCaseRuleDO.setNonCwMarkupAmnt(3);
	    	prcProfLessCaseRuleDO.setNonCwMarkupAmntTypeCode("12");
	    	
	        final PrcProfLessCaseRuleDO actual = SerializationUtils.clone(prcProfLessCaseRuleDO);

	        assertThat(prcProfLessCaseRuleDO.equals(actual), is(true));
	        assertThat(prcProfLessCaseRuleDO.hashCode(), is(actual.hashCode()));
	        assertThat(prcProfLessCaseRuleDO.toString() != null, is(true));
	        
	        assertThat(prcProfLessCaseRuleDO.getContractPriceProfileSeq(), is(actual.getContractPriceProfileSeq()));
	        assertThat(prcProfLessCaseRuleDO.getGfsCustomerId(), is(actual.getGfsCustomerId()));
	        assertThat(prcProfLessCaseRuleDO.getGfsCustomerTypeCode(), is(actual.getGfsCustomerTypeCode()));
	        assertThat(prcProfLessCaseRuleDO.getCreateUserId(), is(actual.getCreateUserId()));
	        assertThat(prcProfLessCaseRuleDO.getEffectiveDate(), is(actual.getEffectiveDate()));
	        assertThat(prcProfLessCaseRuleDO.getExpirationDate(), is(actual.getExpirationDate()));
	        assertThat(prcProfLessCaseRuleDO.getLastUpdateUserId(), is(actual.getLastUpdateUserId()));
	        assertThat(prcProfLessCaseRuleDO.getCwMarkupAmnt(), is(actual.getCwMarkupAmnt()));
	        assertThat(prcProfLessCaseRuleDO.getCwMarkupAmountTypeCode(), is(actual.getCwMarkupAmountTypeCode()));
	        assertThat(prcProfLessCaseRuleDO.getItemPriceId(), is(actual.getItemPriceId()));
	        assertThat(prcProfLessCaseRuleDO.getItemPriceLevelCode(), is(actual.getItemPriceLevelCode()));
	        assertThat(prcProfLessCaseRuleDO.getLesscaseRuleId(), is(actual.getLesscaseRuleId()));
	        assertThat(prcProfLessCaseRuleDO.getMarkupAppliedBeforeDivInd(), is(actual.getMarkupAppliedBeforeDivInd()));
	        assertThat(prcProfLessCaseRuleDO.getNonCwMarkupAmnt(), is(actual.getNonCwMarkupAmnt()));
	        assertThat(prcProfLessCaseRuleDO.getNonCwMarkupAmntTypeCode(), is(actual.getNonCwMarkupAmntTypeCode()));
	    }

}
