package com.gfs.cpp.common.model.customerinfo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.customerinfo.CPPInformationDO;

@RunWith(MockitoJUnitRunner.class)
public class CPPInformationDOTest {

    @InjectMocks
    private CPPInformationDO cppInformationDO;
    
    @Test
    public void testcppInformationDO() {

        cppInformationDO.setClmContractTypeCode(1);
        cppInformationDO.setContractPriceProfileId(1);
        cppInformationDO.setContractPriceProfileSeq(1);
        cppInformationDO.setLastUpdateUserId("");
        cppInformationDO.setPriceEffectiveDate(new Date());
        cppInformationDO.setPriceExpirationDate(new Date());
        
        final CPPInformationDO actual = SerializationUtils.clone(cppInformationDO);

        assertThat(cppInformationDO.equals(actual), is(true));
        assertThat(cppInformationDO.hashCode(), is(actual.hashCode()));
        assertThat(cppInformationDO.toString() != null, is(true));

        assertThat(actual.getClmContractTypeCode(), is(cppInformationDO.getClmContractTypeCode()));
        assertThat(actual.getContractPriceProfileId(), is(cppInformationDO.getContractPriceProfileId()));
        assertThat(actual.getContractPriceProfileSeq(), is(cppInformationDO.getContractPriceProfileSeq()));
        assertThat(actual.getLastUpdateUserId(), is(cppInformationDO.getLastUpdateUserId()));
        assertThat(actual.getPriceEffectiveDate(), is(cppInformationDO.getPriceEffectiveDate()));
        assertThat(actual.getPriceExpirationDate(), is(cppInformationDO.getPriceExpirationDate()));

    }
}