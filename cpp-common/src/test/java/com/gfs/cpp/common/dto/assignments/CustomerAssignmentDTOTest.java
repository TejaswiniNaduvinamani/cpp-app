package com.gfs.cpp.common.dto.assignments;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.CustomerAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;

@RunWith(MockitoJUnitRunner.class)
public class CustomerAssignmentDTOTest {

    private static final Date pricingEffectiveDate = new LocalDate(2010, 01, 01).toDate();
    private static final Date pricingExpirationDate = new LocalDate(2012, 01, 01).toDate();

    @InjectMocks
    private CustomerAssignmentDTO dto;

    @Test
    public void testMethods() {
        dto.setCmgCustomerId("1");
        dto.setCmgCustomerType(31);
        dto.setContractPriceProfileSeq(1);
        dto.setContractPriceProfileId(2);
        dto.setClmContractStartDate(pricingEffectiveDate);
        dto.setClmContractEndDate(pricingExpirationDate);
        dto.setRealCustomerDTOList(new ArrayList<RealCustomerDTO>());

        final CustomerAssignmentDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.getCmgCustomerId(), is(actual.getCmgCustomerId()));
        assertThat(dto.getCmgCustomerType(), is(actual.getCmgCustomerType()));
        assertThat(dto.getContractPriceProfileSeq(), is(actual.getContractPriceProfileSeq()));
        assertThat(dto.getContractPriceProfileId(), is(actual.getContractPriceProfileId()));
        assertThat(dto.getClmContractStartDate(), is(actual.getClmContractStartDate()));
        assertThat(dto.getClmContractEndDate(), is(actual.getClmContractEndDate()));
        assertThat(dto.getRealCustomerDTOList(), is(actual.getRealCustomerDTOList()));
    }
}
