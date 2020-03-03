package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.PrcProfAuditAuthorityDTO;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfAuditAuthorityDTOTest {
	
	@InjectMocks
	private PrcProfAuditAuthorityDTO dto;
	
	@Test
    public void testPrcProfAuditAuthorityDTO() {
        dto.setContractPriceProfileSeq(1);
        dto.setGfsCustomerId("1");
        dto.setGfsCustomerType(22);
        dto.setCreateUserId("test user");
        dto.setEffectiveDate(new LocalDate(2018, 05, 05).toDate());
        dto.setExpirationDate(new LocalDate(2019, 05, 05).toDate());
        dto.setLastUpdateUserId("test user");
        dto.setPrcProfAuditAuthorityInd(1);

        final PrcProfAuditAuthorityDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        
        assertThat(dto.getContractPriceProfileSeq(), is(actual.getContractPriceProfileSeq()));
        assertThat(dto.getGfsCustomerId(), is(actual.getGfsCustomerId()));
        assertThat(dto.getGfsCustomerType(), is(actual.getGfsCustomerType()));
        assertThat(dto.getCreateUserId(), is(actual.getCreateUserId()));
        assertThat(dto.getEffectiveDate(), is(actual.getEffectiveDate()));
        assertThat(dto.getExpirationDate(), is(actual.getExpirationDate()));
        assertThat(dto.getLastUpdateUserId(), is(actual.getLastUpdateUserId()));
        assertThat(dto.getPrcProfAuditAuthorityInd(), is(actual.getPrcProfAuditAuthorityInd()));
        }

}
