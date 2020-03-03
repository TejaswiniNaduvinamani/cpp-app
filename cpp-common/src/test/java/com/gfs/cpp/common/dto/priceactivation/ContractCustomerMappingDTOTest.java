package com.gfs.cpp.common.dto.priceactivation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;

@RunWith(MockitoJUnitRunner.class)
public class ContractCustomerMappingDTOTest {

    @InjectMocks
    private ContractCustomerMappingDTO dto;

    @Test
    public void testMethods() {
        dto.setCppConceptMappingSeq(1);
        dto.setCppCustomerSeq(1);
        dto.setGfsCustomerId("1");
        dto.setGfsCustomerTypeCode(22);
        dto.setDefaultCustomerInd(1);

        final ContractCustomerMappingDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.getCppConceptMappingSeq(), is(actual.getCppConceptMappingSeq()));
        assertThat(dto.getCppCustomerSeq(), is(actual.getCppCustomerSeq()));
        assertThat(dto.getGfsCustomerId(), is(actual.getGfsCustomerId()));
        assertThat(dto.getGfsCustomerTypeCode(), is(actual.getGfsCustomerTypeCode()));
        assertThat(dto.getDefaultCustomerInd(), is(actual.getDefaultCustomerInd()));
    }

}
