package com.gfs.cpp.common.dto.search;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.search.ContractSearchResultDTO;

@RunWith(MockitoJUnitRunner.class)
public class ContractSearchResultDTOTest {

    @InjectMocks
    private ContractSearchResultDTO dto;

    @Test
    public void testMethods() {
        dto.setAgreementId("agreement-id");
        dto.setContractName("test");
        dto.setContractPriceProfileId(100);
        dto.setContractType("regional");
        dto.setCppLink("/test/pricing");
        dto.setCustomerName("Brinker's");
        dto.setEffectiveDate(new Date());
        dto.setGfsCustomerId("1234");
        dto.setGfsCustomerTypeCode(3);
        dto.setIsFurtheranceExist("yes");
        dto.setParentAgreementId("parent-agreement-id");
        dto.setStatus("Approved");
        dto.setVersion(1);

        final ContractSearchResultDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(actual.getAgreementId(), is(dto.getAgreementId()));
        assertThat(actual.getContractName(), is(dto.getContractName()));
        assertThat(actual.getContractPriceProfileId(), is(dto.getContractPriceProfileId()));
        assertThat(actual.getContractType(), is(dto.getContractType()));
        assertThat(actual.getCppLink(), is(dto.getCppLink()));
        assertThat(actual.getCustomerName(), is(dto.getCustomerName()));
        assertThat(actual.getEffectiveDate(), is(dto.getEffectiveDate()));
        assertThat(actual.getGfsCustomerId(), is(dto.getGfsCustomerId()));
        assertThat(actual.getIsFurtheranceExist(), is(dto.getIsFurtheranceExist()));
        assertThat(actual.getGfsCustomerTypeCode(), is(dto.getGfsCustomerTypeCode()));
        assertThat(actual.getParentAgreementId(), is(dto.getParentAgreementId()));
        assertThat(actual.getStatus(), is(dto.getStatus()));
        assertThat(actual.getVersion(), is(dto.getVersion()));
    }

}
