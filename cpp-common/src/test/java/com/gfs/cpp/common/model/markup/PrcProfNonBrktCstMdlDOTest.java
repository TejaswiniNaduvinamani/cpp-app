package com.gfs.cpp.common.model.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.markup.PrcProfNonBrktCstMdlDO;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfNonBrktCstMdlDOTest {
	
	@InjectMocks
	private PrcProfNonBrktCstMdlDO prcProfNonBrktCstMdlDO;
	
	 @Test
	    public void testPrcProfNonBrktCstMdlDO() {
	        prcProfNonBrktCstMdlDO.setContractPriceProfileSeq(1);
	        prcProfNonBrktCstMdlDO.setCostModelId(2);
	        prcProfNonBrktCstMdlDO.setCreateUserId("test user");
	        prcProfNonBrktCstMdlDO.setEffectiveDate(null);
	        prcProfNonBrktCstMdlDO.setExpirationDate(null);
	        prcProfNonBrktCstMdlDO.setGfsCustomerId("1");
	        prcProfNonBrktCstMdlDO.setGfsCustomerTypeCode(1);
	        prcProfNonBrktCstMdlDO.setItemPriceId("1");
	        prcProfNonBrktCstMdlDO.setItemPriceLevelCode(2);
	        prcProfNonBrktCstMdlDO.setLastUpdateUserId("test user");
	        
	        final PrcProfNonBrktCstMdlDO actual = SerializationUtils.clone(prcProfNonBrktCstMdlDO);

	        assertThat(prcProfNonBrktCstMdlDO.equals(actual), is(true));
	        assertThat(prcProfNonBrktCstMdlDO.hashCode(), is(actual.hashCode()));
	        assertThat(prcProfNonBrktCstMdlDO.toString() != null, is(true));
	        
	        assertThat(prcProfNonBrktCstMdlDO.getContractPriceProfileSeq(), is(actual.getContractPriceProfileSeq()));
	        assertThat(prcProfNonBrktCstMdlDO.getCostModelId(), is(actual.getCostModelId()));
	        assertThat(prcProfNonBrktCstMdlDO.getCreateUserId(), is(actual.getCreateUserId()));
	        assertThat(prcProfNonBrktCstMdlDO.getEffectiveDate(), is(actual.getEffectiveDate()));
	        assertThat(prcProfNonBrktCstMdlDO.getExpirationDate(), is(actual.getExpirationDate()));
	        assertThat(prcProfNonBrktCstMdlDO.getGfsCustomerTypeCode(), is(actual.getGfsCustomerTypeCode()));
	        assertThat(prcProfNonBrktCstMdlDO.getItemPriceId(), is(actual.getItemPriceId()));
	        assertThat(prcProfNonBrktCstMdlDO.getItemPriceLevelCode(), is(actual.getItemPriceLevelCode()));
	        assertThat(prcProfNonBrktCstMdlDO.getLastUpdateUserId(), is(actual.getLastUpdateUserId()));
	        assertThat(prcProfNonBrktCstMdlDO.getGfsCustomerId(), is(actual.getGfsCustomerId()));
	    }

}
