package com.gfs.cpp.common.dto.customerinfo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;

@RunWith(MockitoJUnitRunner.class)
public class CPPInformationDTOTest {

    @InjectMocks
    private CPPInformationDTO target;

    @Test
    public void validateCPPInformationDTO() {

        target.setContractPriceProfileId(1);
        target.setContractPriceProfileSeq(2);
        target.setVersionNumber(1);

        CPPInformationDTO actual = SerializationUtils.clone(target);

        assertThat(actual, equalTo(target));
        assertThat(actual.hashCode(), equalTo(target.hashCode()));
        assertThat(actual.toString(), notNullValue());

        assertThat(actual.getContractPriceProfileId(), is(target.getContractPriceProfileId()));
        assertThat(actual.getContractPriceProfileSeq(), is(target.getContractPriceProfileSeq()));
        assertThat(actual.getVersionNumber(), is(target.getVersionNumber()));
    }

}
